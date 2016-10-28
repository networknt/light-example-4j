package com.networknt.webserver.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.builder.PredicatedHandlersParser;
import io.undertow.server.handlers.form.EagerFormParsingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;

import java.io.File;

import static io.undertow.Handlers.resource;


public class WebServerHandlerProvider implements HandlerProvider {
    static final String CONFIG_NAME = "webserver";

    static WebServerConfig config =
         (WebServerConfig)Config.getInstance().getJsonObjectConfig(CONFIG_NAME, WebServerConfig.class);

    public HttpHandler getHandler() {

        return Handlers.predicates(
                PredicatedHandlersParser.parse("not path-prefix('/images', '/assets', '/api') -> rewrite('/index.html')"
                        , WebServerHandlerProvider.class.getClassLoader()),
                new PathHandler(resource(new FileResourceManager(
                        new File(config.getBase()), config.getTransferMinSize())))
                        .addPrefixPath("/api/json", new JsonHandler(Config.getInstance().getMapper()))
                        .addPrefixPath("/api/text", new TextHandler())
        );
    }
}
