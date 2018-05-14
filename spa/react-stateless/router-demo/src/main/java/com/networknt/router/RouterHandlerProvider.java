package com.networknt.router;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.ProxyHandler;

/**
 * This is client side router which can be used by legacy client which cannot leverage client.jar from
 * light platform. For example, PEGA workflow or client that is built with other languages other than Java.
 *
 * This router supports all the HTTP methods and handles interaction with OAuth 2.0 provider for client
 * credentials token. Basically, it does client side service discovery, security, traceability and give
 * the client a static IP and port to invoke APIs built on top of light-*-4j frameworks.
 *
 * In order to make the service discovery work, the client must pass in the serviceId, tag and hash key
 * if consistent hash is used. Except these three extra headers must be passed in, the request would be
 * the same like you are invoking downstream servers with known IP and port.
 *
 * The OAuth 2.0 client_id and client_secret is set up in client.yml and secret.yml for the router instance.
 *
 * The router will create HTTP 2.0 connections to each downstream services and cache the connection for a
 * period of time which is configurable. As HTTP 2.0 connection is multiplex, there is only one connection
 * per downstream service.
 *
 */
public class RouterHandlerProvider implements HandlerProvider {

    static RouterConfig config = (RouterConfig)Config.getInstance().getJsonObjectConfig(Constants.CONFIG_NAME, RouterConfig.class);

    /**
     * This provider will proxy all request to backend APIs except /authorization
     *
     * @return HttpHandler the final handle in the chain.
     */
    @Override
    public HttpHandler getHandler() {
        // As we are building a client side router for the light platform, the assumption is the server will
        // be on HTTP 2.0 TSL always. No need to handle HTTP 1.1 case here.
        RouterProxyClient routerProxyClient = new RouterProxyClient();
        return new ProxyHandler(routerProxyClient, config.getMaxRequestTime(), ResponseCodeHandler.HANDLE_404);
    }
}
