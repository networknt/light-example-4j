
package com.networknt.example.pdf.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionState;
import com.networknt.exception.ClientException;
import com.networknt.openapi.ResponseValidator;
import com.networknt.schema.SchemaValidatorsConfig;
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
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.StandardCharsets.UTF_8;


@Disabled
public class PdfReportGetHandlerTest {
    // @RegisterExtension
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(PdfReportGetHandlerTest.class);
    static final String JSON_MEDIA_TYPE = "application/json";

    @Test
    public void testPdfReportGetHandlerTest() throws ClientException {
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
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.GET);
            connection.sendRequest(request, client.createClientCallback(reference, latch));
            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.restore(token);
        }
        byte[] body = reference.get().getAttachment(Http2Client.RESPONSE_BODY).getBytes(UTF_8);
        logger.info("result length:" + body.length);
        int statusCode = reference.get().getResponseCode();
        Assertions.assertEquals(200, statusCode);
    }
}
