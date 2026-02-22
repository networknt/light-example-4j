package com.networknt.router.middleware;

import com.networknt.client.Http2Client;
import com.networknt.client.oauth.ClientCredentialsRequest;
import com.networknt.client.oauth.OauthHelper;
import com.networknt.client.oauth.TokenRequest;
import com.networknt.client.oauth.TokenResponse;
import com.networknt.common.DecryptUtil;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.exception.ClientException;
import com.networknt.handler.Handler;
import com.networknt.handler.MiddlewareHandler;
import com.networknt.router.RouterProxyClient;
import com.networknt.status.Status;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * This is a middleware handler that is responsible for getting a JWT access token from
 * OAuth 2.0 provider for the particular router client. The only token that is supported in
 * this handler is client credentials token as there is no user information available here.
 *
 * The client_id will be retrieved from client.yml and client_secret will be retrieved from
 * secret.yml
 *
 * This handler will also responsible for checking if the cached token is about to expired
 * or not. In which case, it will renew the token in another thread. When request comes and
 * the cached token is already expired, then it will block the request and go to the OAuth
 * provider to get a new token and then resume the request to the next handler in the chain.
 *
 * The logic is very similar with client module in light-4j but this is implemented in a
 * handler instead.
 *
 * This light-router is designed for standalone or client that is not implemented in Java
 * Otherwise, you should use client module instead of this one. In the future, we might
 * add Authorization Code grant type support by providing an endpoint in the light-router
 * to accept Authorization Code redirect and then get the token from OAuth 2.0 provider.
 *
 * There is no specific configuration file for this handler just to enable or disable it. If
 * you want to bypass this handler, you can comment it out from service.yml middleware
 * handler section or change the token.yml to disable it.
 *
 * Once the token is retrieved from OAuth 2.0 provider, it will be placed in the header as
 * Authorization Bearer token according to the OAuth 2.0 specification.
 *
 */
public class TokenHandler implements MiddlewareHandler {
    public static final String CONFIG_NAME = "token";
    public static final String CLIENT_CONFIG_NAME = "client";
    public static final String ENABLED = "enabled";

    public static Map<String, Object> config = Config.getInstance().getJsonMapConfigNoCache(CONFIG_NAME);
    static Logger logger = LoggerFactory.getLogger(RouterProxyClient.class);

    private volatile HttpHandler next;

    private String jwt;    // the cached jwt token for client credentials grant type
    private long expire;   // jwt expire time in millisecond so that we don't need to parse the jwt.
    private volatile boolean renewing = false;
    private volatile long expiredRetryTimeout;
    private volatile long earlyRetryTimeout;


    static final String TLS = "tls";
    static final String LOAD_TRUST_STORE = "loadTrustStore";
    static final String LOAD_KEY_STORE = "loadKeyStore";
    static final String TRUST_STORE = "trustStore";
    static final String KEY_STORE = "keyStore";
    static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";
    static final String TRUST_STORE_PASSWORD_PROPERTY = "javax.net.ssl.trustStorePassword";

    static final String OAUTH = "oauth";
    static final String TOKEN = "token";
    static final String TOKEN_RENEW_BEFORE_EXPIRED = "tokenRenewBeforeExpired";
    static final String EXPIRED_REFRESH_RETRY_DELAY = "expiredRefreshRetryDelay";
    static final String EARLY_REFRESH_RETRY_DELAY = "earlyRefreshRetryDelay";
    static final String OAUTH_HTTP2_SUPPORT = "oauthHttp2Support";

    static final String STATUS_CLIENT_CREDENTIALS_TOKEN_NOT_AVAILABLE = "ERR10009";

    static Map<String, Object> clientConfig;
    static Map<String, Object> tokenConfig;
    static Map<String, Object> secretConfig;
    static boolean oauthHttp2Support;

    private final Object lock = new Object();

    public TokenHandler() {
        clientConfig = Config.getInstance().getJsonMapConfig(CLIENT_CONFIG_NAME);
        if(clientConfig != null) {
            Map<String, Object> oauthConfig = (Map<String, Object>)clientConfig.get(OAUTH);
            if(oauthConfig != null) {
                tokenConfig = (Map<String, Object>)oauthConfig.get(TOKEN);
            }
        }
        Map<String, Object> securityConfig = Config.getInstance().getJsonMapConfig("security");
        if(securityConfig != null) {
            Boolean b = (Boolean)securityConfig.get(OAUTH_HTTP2_SUPPORT);
            oauthHttp2Support = (b == null ? false : b.booleanValue());
        }

        Map<String, Object> secretMap = Config.getInstance().getJsonMapConfig("secret");
        if(secretMap != null) {
            secretConfig = DecryptUtil.decryptMap(secretMap);
        } else {
            throw new ExceptionInInitializerError("Could not locate secret.yml");
        }
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        // check if there is a bear token in the authorization header in the request. If this
        // is one, then this must be the subject token that is linked to the original user.
        // We will keep this token in the Authorization header but create a new token with
        // client credentials grant type with scopes for the particular client. (Can we just
        // assume that the subject token has the scope already?)
        checkCCTokenExpired();
        exchange.getRequestHeaders().put(Headers.AUTHORIZATION, "Bearer " + jwt);
        Handler.next(exchange, next);
    }

    @Override
    public HttpHandler getNext() {
        return next;
    }

    @Override
    public MiddlewareHandler setNext(final HttpHandler next) {
        Handlers.handlerNotNull(next);
        this.next = next;
        return this;
    }

    @Override
    public boolean isEnabled() {
        Object object = config.get(ENABLED);
        return object != null && (Boolean) object;
    }


    private void checkCCTokenExpired() throws ClientException, ApiException {
        long tokenRenewBeforeExpired = (Integer) tokenConfig.get(TOKEN_RENEW_BEFORE_EXPIRED);
        long expiredRefreshRetryDelay = (Integer)tokenConfig.get(EXPIRED_REFRESH_RETRY_DELAY);
        long earlyRefreshRetryDelay = (Integer)tokenConfig.get(EARLY_REFRESH_RETRY_DELAY);
        boolean isInRenewWindow = expire - System.currentTimeMillis() < tokenRenewBeforeExpired;
        logger.trace("isInRenewWindow = " + isInRenewWindow);
        if(isInRenewWindow) {
            if(expire <= System.currentTimeMillis()) {
                logger.trace("In renew window and token is expired.");
                // block other request here to prevent using expired token.
                synchronized (TokenHandler.class) {
                    if(expire <= System.currentTimeMillis()) {
                        logger.trace("Within the synch block, check if the current request need to renew token");
                        if(!renewing || System.currentTimeMillis() > expiredRetryTimeout) {
                            // if there is no other request is renewing or the renewing flag is true but renewTimeout is passed
                            renewing = true;
                            expiredRetryTimeout = System.currentTimeMillis() + expiredRefreshRetryDelay;
                            logger.trace("Current request is renewing token synchronously as token is expired already");
                            getCCToken();
                            renewing = false;
                        } else {
                            logger.trace("Circuit breaker is tripped and not timeout yet!");
                            // reject all waiting requests by thrown an exception.
                            throw new ApiException(new Status(STATUS_CLIENT_CREDENTIALS_TOKEN_NOT_AVAILABLE));
                        }
                    }
                }
            } else {
                // Not expired yet, try to renew async but let requests use the old token.
                logger.trace("In renew window but token is not expired yet.");
                synchronized (TokenHandler.class) {
                    if(expire > System.currentTimeMillis()) {
                        if(!renewing || System.currentTimeMillis() > earlyRetryTimeout) {
                            renewing = true;
                            earlyRetryTimeout = System.currentTimeMillis() + earlyRefreshRetryDelay;
                            logger.trace("Retrieve token async is called while token is not expired yet");

                            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                            executor.schedule(() -> {
                                try {
                                    getCCToken();
                                    renewing = false;
                                    logger.trace("Async get token is completed.");
                                } catch (Exception e) {
                                    logger.error("Async retrieve token error", e);
                                    // swallow the exception here as it is on a best effort basis.
                                }
                            }, 50, TimeUnit.MILLISECONDS);
                            executor.shutdown();
                        }
                    }
                }
            }
        }
        logger.trace("Check secondary token is done!");
    }

    private void getCCToken() throws ClientException {
        TokenRequest tokenRequest = new ClientCredentialsRequest();
        TokenResponse tokenResponse = OauthHelper.getTokenResult(tokenRequest).getResult();
        synchronized (lock) {
            jwt = tokenResponse.getAccessToken();
            // the expiresIn is seconds and it is converted to millisecond in the future.
            expire = System.currentTimeMillis() + tokenResponse.getExpiresIn() * 1000;
            logger.info("Get client credentials token {} with expire_in {} seconds", jwt, tokenResponse.getExpiresIn());
        }
    }
}
