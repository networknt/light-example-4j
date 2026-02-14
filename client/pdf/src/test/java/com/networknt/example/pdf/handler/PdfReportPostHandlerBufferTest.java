
package com.networknt.example.pdf.handler;

import com.networknt.client.Http2Client;
import com.networknt.client.simplepool.SimpleConnectionState;
import com.networknt.client.listener.ByteBufferReadChannelListener;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.*;
import io.undertow.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


public class PdfReportPostHandlerBufferTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(PdfReportPostHandlerBufferTest.class);
    static final boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = server.getServerConfig().isEnableHttps();
    static final int httpPort = server.getServerConfig().getHttpPort();
    static final int httpsPort = server.getServerConfig().getHttpsPort();
    static final String url = enableHttp2 || enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;
    static final String FORM_DATA_TYPE = "multipart/form-data";
    public static final AttachmentKey<String> RESPONSE_BODY = AttachmentKey.create(String.class);
    public static AttachmentKey<ByteBuffer> BUFFER_BODY = AttachmentKey.create(ByteBuffer.class);
    @Test
    public void testPdfReportPostHandlerTest() throws ClientException {

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
        String httpMethod = "post";
        try {
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.POST);

           // request.getRequestHeaders().put(Headers.CONTENT_TYPE, FORM_DATA_TYPE);

            request.getRequestHeaders().put(Headers.CONTENT_TYPE, FORM_DATA_TYPE);
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sample.pdf");
            HashMap<String, Object> requestMap = new HashMap<>();
            requestMap.put("name", "test_sample.pdf");
            requestMap.put("profileFile", IOUtils.toByteArray(resourceAsStream));
        //    String requestBody = Config.getInstance().getMapper().writeValueAsString(requestMap);
            //customized header parameters
            connection.sendRequest(request, createClientCallback(reference, latch, ByteBuffer.wrap(SerializationUtils.serialize(requestMap))));

            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {

            client.restore(token);

        }
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        System.out.println(body);
        Optional<HeaderValues> contentTypeName = Optional.ofNullable(reference.get().getResponseHeaders().get(Headers.CONTENT_TYPE));

        int statusCode = reference.get().getResponseCode();
        Assert.assertEquals(200, statusCode);
    }

    public ClientCallback<ClientExchange> createClientCallback(final AtomicReference<ClientResponse> reference, final CountDownLatch latch, final ByteBuffer requestBody) {
        return new ClientCallback<ClientExchange>() {
            public void completed(ClientExchange result) {
                new ByteBufferWriteChannelListener(requestBody).setup(result.getRequestChannel());
                result.setResponseListener(new ClientCallback<ClientExchange>() {
                    public void completed(final ClientExchange result) {
                        reference.set(result.getResponse());
                        (new ByteBufferReadChannelListener(result.getConnection().getBufferPool()) {
                            protected void bufferDone(List<Byte> out) {
                                byte[] byteArray = new byte[out.size()];
                                int index = 0;
                                for (byte b : out) {
                                    byteArray[index++] = b;
                                }
                                result.getResponse().putAttachment(BUFFER_BODY, (ByteBuffer.wrap(byteArray)));
                                latch.countDown();
                            }

                            protected void error(IOException e) {
                                latch.countDown();
                            }
                        }).setup(result.getResponseChannel());
                    }
                    public void failed(IOException e) {
                        latch.countDown();
                    }
                });

                try {
                    result.getRequestChannel().shutdownWrites();
                    if (!result.getRequestChannel().flush()) {
                        result.getRequestChannel().getWriteSetter().set(ChannelListeners.flushingChannelListener((ChannelListener)null, (ChannelExceptionHandler)null));
                        result.getRequestChannel().resumeWrites();
                    }
                } catch (IOException var3) {
                    latch.countDown();
                }

            }

            public void failed(IOException e) {
                latch.countDown();
            }
        };
    }
}
