/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.undertow.examples.reverseproxy;

import io.undertow.Undertow;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Stuart Douglas
 */
public class ReverseProxyServer {

    public static void main(final String[] args) {
        try {
            LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient()
                    .addHost(new URI("http://localhost:8080"))
                    .setConnectionsPerThread(20);

            Undertow reverseProxy = Undertow.builder()
                    .addHttpListener(8000, "localhost")
                    .setIoThreads(4)
                    .setHandler(ProxyHandler.builder().setProxyClient(loadBalancer).setMaxRequestTime( 30000).build())
                    .build();
            reverseProxy.start();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
