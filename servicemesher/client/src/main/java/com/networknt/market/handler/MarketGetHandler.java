package com.networknt.market.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import com.networknt.exception.ApiException;
import com.networknt.handler.LightHttpHandler;
import com.networknt.market.model.Market;
import com.networknt.market.service.MarketService;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketGetHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(MarketGetHandler.class);
    private static final ObjectMapper objectMapper = Config.getInstance().getMapper();
    private static MarketService marketService = SingletonServiceFactory.getBean(MarketService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
        Market market = marketService.getMarket();
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(objectMapper.writeValueAsString(market));
      } catch (ApiException e) {
			logger.error("Error Occurred: " + e.getMessage());
            setExchangeStatus(exchange, e.getStatus());
            exchange.getResponseSender().send(e.getMessage());
		}
    }
}
