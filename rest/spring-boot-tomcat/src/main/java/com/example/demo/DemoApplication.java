package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class DemoApplication {

	@Bean
	public RouterFunction<ServerResponse> route() {
		return RouterFunctions
				.route(RequestPredicates.POST("/post").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						serverRequest -> ServerResponse.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body("{\"key\":\"key1\",\"value\":\"value1\"}"));
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
