package com.networknt.ad.handler;

import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.server.Server;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This API is a sink API that means it only be called by others but not calling anyone else.
 *
 * @author Steve
 */
public class DataGetHandler implements LightHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // get the current address and port from the Server. Cannot use the server.yml value as the dynamic port might be used.
        int port = Server.currentPort;
        String address = Server.currentAddress;
        List<String> messages = new ArrayList<>();
        messages.add("API D: Message 1 from " + address + ":" + port);
        messages.add("API D: Message 2 from " + address + ":" + port);
        exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
    }
}
