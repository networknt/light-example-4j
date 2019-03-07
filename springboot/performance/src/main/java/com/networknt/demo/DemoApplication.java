package com.networknt.demo;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.api.DeploymentInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	@Bean
	UndertowServletWebServerFactory embeddedServletWebFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer() {
			@Override
			public void customize(DeploymentInfo deploymentInfo) {
				deploymentInfo.addInitialHandlerChainWrapper(handler -> {
							return Handlers.path()
									.addPrefixPath("/", handler)
									.addPrefixPath("/light-4j", getTestHandler());
						}
				);
			}
		});

		return factory;
	}

	static HttpHandler getTestHandler() {
		return new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
				httpServerExchange.getResponseSender().send("OK");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
