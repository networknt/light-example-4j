
package com.networknt.example.pdf.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionState;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


@Disabled
public class PdfReportPostHandlerTest {
    // @RegisterExtension
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(PdfReportPostHandlerTest.class);
    static final String FORM_DATA_TYPE = "multipart/form-data";

    @Test
    public void testPdfReportPostHandlerTest() throws ClientException {
        boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
        boolean enableHttps = server.getServerConfig().isEnableHttps();
        int httpPort = server.getServerConfig().getHttpPort();
        int httpsPort = server.getServerConfig().getHttpsPort();
        String url = enableHttp2 || enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;

        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final SimpleConnectionState.ConnectionToken token;

        try {
            token = client.borrow(new URI(url), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, enableHttp2 ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true): OptionMap.EMPTY);
        } catch (Exception e) {
            throw new ClientException(e);
        }

        final ClientConnection connection = (ClientConnection) token.getRawConnection();
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        String requestUri = "/v1/pdf/report";
        try {
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.POST);
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, FORM_DATA_TYPE);
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("name", "test_sample.pdf");
            requestMap.put("profileFile", "WebKitFormBoundaryWfPNVh4wuWBlyEyQ");
            String requestBody = Config.getInstance().getMapper().writeValueAsString(requestMap);
            connection.sendRequest(request, client.createClientCallback(reference, latch, requestBody));
            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.restore(token);
        }
        int statusCode = reference.get().getResponseCode();
        Assertions.assertEquals(200, statusCode);
    }
}
