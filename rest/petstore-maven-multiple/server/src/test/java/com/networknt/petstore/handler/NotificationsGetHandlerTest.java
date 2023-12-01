
package com.networknt.petstore.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionHolder;
import com.networknt.exception.ClientException;
import com.networknt.server.ServerConfig;
import com.networknt.openapi.OpenApiHandler;
import com.networknt.openapi.ResponseValidator;
import com.networknt.openapi.SchemaValidator;
import com.networknt.status.Status;
import com.networknt.utility.StringUtils;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


@Disabled
@ExtendWith(TestServer.class)
public class NotificationsGetHandlerTest {

    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(NotificationsGetHandlerTest.class);
    static final boolean enableHttp2 = ServerConfig.getInstance().isEnableHttp2();
    static final boolean enableHttps = ServerConfig.getInstance().isEnableHttps();
    static final int httpPort = ServerConfig.getInstance().getHttpPort();
    static final int httpsPort = ServerConfig.getInstance().getHttpsPort();
    static final String url = enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;
    static final String JSON_MEDIA_TYPE = "application/json";
    final Http2Client client = Http2Client.getInstance();

    @Test
    public void testNotificationsGetHandlerTest() throws ClientException {
        final CountDownLatch latch = new CountDownLatch(1);
        SimpleConnectionHolder.ConnectionToken connectionToken = null;
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        String requestUri = "/v1/notifications";
        String httpMethod = "get";
        try {
            if(enableHttps) {
                connectionToken = client.borrow(new URI(url), Http2Client.WORKER, client.getDefaultXnioSsl(), Http2Client.BUFFER_POOL, enableHttp2 ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true): OptionMap.EMPTY);
            } else {
                connectionToken = client.borrow(new URI(url), Http2Client.WORKER, Http2Client.BUFFER_POOL, OptionMap.EMPTY);
            }
            ClientConnection connection = (ClientConnection) connectionToken.getRawConnection();
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.GET);
            
            //customized header parameters 
            request.getRequestHeaders().put(new HttpString("host"), "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            
            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.restore(connectionToken);
        }
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        Optional<HeaderValues> contentTypeName = Optional.ofNullable(reference.get().getResponseHeaders().get(Headers.CONTENT_TYPE));
        SchemaValidator schemaValidator = new SchemaValidator(OpenApiHandler.helper.openApi3);
        ResponseValidator responseValidator = new ResponseValidator(schemaValidator);
        int statusCode = reference.get().getResponseCode();
        Status status;
        if(contentTypeName.isPresent()) {
            status = responseValidator.validateResponseContent(body, requestUri, httpMethod, String.valueOf(statusCode), contentTypeName.get().getFirst());
        } else {
            status = responseValidator.validateResponseContent(body, requestUri, httpMethod, String.valueOf(statusCode), JSON_MEDIA_TYPE);
        }
        assertNotNull(status);
    }
}

