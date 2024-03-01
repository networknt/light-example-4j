package com.networknt.kafka.handler;

import com.networknt.client.Http2Client;
import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.kafka.UserQueryStartupHook;
import com.networknt.monad.Failure;
import com.networknt.monad.Result;
import com.networknt.monad.Success;
import com.networknt.server.Server;
import com.networknt.status.Status;
import com.networknt.utility.NetUtils;
import com.networknt.utility.NioUtils;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class QueryUserIdGetHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryUserIdGetHandler.class);
    static Map<String, ClientConnection> connCache = new ConcurrentHashMap<>();
    static Http2Client client = Http2Client.getInstance();

    private static final String OBJECT_NOT_FOUND = "ERR11637";
    private static final String GENERIC_EXCEPTION = "ERR10014";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String userId = exchange.getQueryParameters().get("userId").getFirst();
        if(logger.isTraceEnabled()) logger.trace("userId = " + userId);

        ReadOnlyKeyValueStore<String, String> keyValueStore = UserQueryStartupHook.streams.getUserIdStore();
        String data = keyValueStore.get(userId);
        if(data != null) {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(data);
        } else {
            StreamsMetadata metadata = UserQueryStartupHook.streams.getUserIdStreamsMetadata(userId);
            if(logger.isDebugEnabled()) logger.debug("found address in another instance " + metadata.host() + ":" + metadata.port());
            String url = "https://" + metadata.host() + ":" + metadata.port();
            if(NetUtils.getLocalAddressByDatagram().equals(metadata.host()) && Server.getServerConfig().getHttpsPort() == metadata.port()) {
                setExchangeStatus(exchange, OBJECT_NOT_FOUND, "user", userId);
                return;
            } else {
                Result<String> resultStatus = getUserById(userId, exchange, url);
                if (resultStatus.isSuccess()) {
                    exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
                    exchange.getResponseSender().send(resultStatus.getResult());
                    return;
                }
            }
            setExchangeStatus(exchange, OBJECT_NOT_FOUND, "user", userId);
            return;
        }
    }

    public static Result<String> getUserById(String userId, HttpServerExchange exchange, String url) {
        Result<String> result = null;
        try {
            ClientConnection conn = connCache.get(url);
            if(conn == null || !conn.isOpen()) {
                conn = client.connect(new URI(url), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
                connCache.put(url, conn);
            }
            // Create one CountDownLatch that will be reset in the callback function
            final CountDownLatch latch = new CountDownLatch(1);
            // Create an AtomicReference object to receive ClientResponse from callback function
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();
            String path = "/query/" + userId;
            final ClientRequest request = new ClientRequest().setMethod(Methods.GET).setPath(path);
            String token = exchange.getRequestHeaders().getFirst(Headers.AUTHORIZATION);
            if(token != null) request.getRequestHeaders().put(Headers.AUTHORIZATION, token);
            request.getRequestHeaders().put(Headers.HOST, "localhost");
            conn.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await();
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            if(statusCode != 200) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                result = Failure.of(status);
            } else result = Success.of(body);
        } catch (Exception e) {
            logger.error("Exception:", e);
            Status status = new Status(GENERIC_EXCEPTION, e.getMessage());
            result = Failure.of(status);
        }
        return result;
    }

}
