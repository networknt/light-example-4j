
package com.networknt.example.pdf.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionState;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.*;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.*;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.StandardCharsets.UTF_8;


@Disabled
public class PdfReportGetHandlerBufferTest {
    // @RegisterExtension
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(PdfReportGetHandlerBufferTest.class);
    static final String JSON_MEDIA_TYPE = "application/json";
    AttachmentKey<ByteBuffer> BUFFER_BODY = AttachmentKey.create(ByteBuffer.class);

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
            connection.sendRequest(request, client.byteBufferClientCallback(reference, latch));
            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.restore(token);
        }
        byte[] body = reference.get().getAttachment(client.BUFFER_BODY).array();
        logger.info("result length:" + body.length);
        Assertions.assertNotNull(body);
        int statusCode = reference.get().getResponseCode();
        Assertions.assertEquals(200, statusCode);
    }
}
