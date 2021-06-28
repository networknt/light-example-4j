package com.networknt.backend.handler;

import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.server.Server;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.util.HashMap;
import java.util.Map;

public class GetDataGetHandler implements LightHttpHandler {

    static final boolean enableHttp2 = Server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = Server.getServerConfig().isEnableHttps();
    static final int httpPort = Server.getServerConfig().getHttpPort();
    static final int httpsPort = Server.getServerConfig().getHttpsPort();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, Object> examples = new HashMap<>();
        examples.put("key", "key1");
        examples.put("value", "value1");
        examples.put("enableHttp2", enableHttp2);
        examples.put("enableHttps", enableHttps);
        examples.put("httpPort", httpPort);
        examples.put("httpsPort", httpsPort);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(examples));    }
}
