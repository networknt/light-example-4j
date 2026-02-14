
package com.networknt.example.pdf.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionState;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.*;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Methods;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.*;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.charset.StandardCharsets.UTF_8;


public class PdfReportGetHandlerBufferTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(PdfReportGetHandlerBufferTest.class);
    static final boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = server.getServerConfig().isEnableHttps();
    static final int httpPort = server.getServerConfig().getHttpPort();
    static final int httpsPort = server.getServerConfig().getHttpsPort();
    static final String url = enableHttp2 || enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;
    static final String JSON_MEDIA_TYPE = "application/json";
    AttachmentKey<ByteBuffer> BUFFER_BODY = AttachmentKey.create(ByteBuffer.class);

    @Test
    public void testPdfReportGetHandlerTest() throws ClientException {

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
        String httpMethod = "get";
        try {
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.GET);

            //customized header parameters
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
        Assert.assertNotNull(body);
      //  System.out.println(body.length);
        int statusCode = reference.get().getResponseCode();

        Assert.assertEquals(200, statusCode);
    }

//    public ClientCallback<ClientExchange> createClientCallback(final AtomicReference<ClientResponse> reference, final CountDownLatch latch) {
//        return new ClientCallback<ClientExchange>() {
//            public void completed(ClientExchange result) {
//                result.setResponseListener(new ClientCallback<ClientExchange>() {
//                    public void completed(final ClientExchange result) {
//                        reference.set(result.getResponse());
//                        (new ByteBufferReadChannelListener(result.getConnection().getBufferPool()) {
//                            protected void bufferDone(List<Byte> out) {
//                                byte[] byteArray = new byte[out.size()];
//                                int index = 0;
//                                for (byte b : out) {
//                                    byteArray[index++] = b;
//                                }
//                                result.getResponse().putAttachment(BUFFER_BODY, (ByteBuffer.wrap(byteArray)));
//                                latch.countDown();
//                            }
//
//                            protected void error(IOException e) {
//                                latch.countDown();
//                            }
//                        }).setup(result.getResponseChannel());
//                    }
//                    public void failed(IOException e) {
//                        latch.countDown();
//                    }
//                });
//
//                try {
//                    result.getRequestChannel().shutdownWrites();
//                    if (!result.getRequestChannel().flush()) {
//                        result.getRequestChannel().getWriteSetter().set(ChannelListeners.flushingChannelListener((ChannelListener)null, (ChannelExceptionHandler)null));
//                        result.getRequestChannel().resumeWrites();
//                    }
//                } catch (IOException var3) {
//                    latch.countDown();
//                }
//
//            }
//
//            public void failed(IOException e) {
//                latch.countDown();
//            }
//        };
//    }

}
