package com.mo3.tictactoe.api_gateway.filters;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
@AllArgsConstructor
public class SessionCheckFilter extends AbstractGatewayFilterFactory<SessionCheckFilter.Config> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static class Config {


    }

    public SessionCheckFilter() {
        super(Config.class);
    }


    final Logger logger = Logger.getLogger(SessionCheckFilter.class.getName());

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            logger.info("SessionFilter entered");


            String requestPath = exchange.getRequest().getURI().getPath();


            if (requestPath.startsWith("/auth/")) {
                logger.info("Skipping session validation for path: " + requestPath);
                return chain.filter(exchange);
            }


            if (exchange.getRequest().getCookies().get("JSESSIONID") != null) {

                String jsessionid = exchange.getRequest().getCookies()
                        .getFirst("JSESSIONID")
                        .getValue();

                logger.info("JSESSIONID PASSED: " + jsessionid);


                List<Object> sessionData = redisTemplate.opsForHash()
                        .values("spring:session:sessions:" + jsessionid);

                if (!sessionData.isEmpty()) {
                    logger.info("Returned session from Redis: " + sessionData);

                    return chain.filter(exchange);
                } else {
                    logger.warning("Invalid or expired session ID: " + jsessionid);

                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                logger.warning("JSESSIONID cookie is missing for path: " + requestPath);

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }


}
