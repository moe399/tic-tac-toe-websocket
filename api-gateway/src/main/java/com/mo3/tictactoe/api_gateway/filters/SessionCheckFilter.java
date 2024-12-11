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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Component
@AllArgsConstructor
public class SessionCheckFilter extends AbstractGatewayFilterFactory<SessionCheckFilter.Config> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
            System.out.println("List of cookies: " );

            String jsessionid = "";


            String requestPath = exchange.getRequest().getURI().getPath();

            List<String> requestQueries = exchange.getRequest().getQueryParams().get("token");

            if (requestPath.startsWith("/auth/")) {
                logger.info("Skipping session validation for path: " + requestPath);
                return chain.filter(exchange);
            }

            List<String> cookies = exchange.getRequest().getHeaders().get("Cookie");

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            System.out.println("Cookies size: " + cookies.size());
            if(cookies == null || cookies.isEmpty()) {
                System.out.println("looking for query token");

                if(requestQueries != null && !requestQueries.isEmpty()) {
                    System.out.println("auth header found");
                    System.out.println("token is:  " + requestQueries.get(0));

                    if(redisTemplate.hasKey(requestQueries.get(0)) == true) {
                        System.out.println("Auth token from Websocket is valid");
                        return chain.filter(exchange);
                    }

                }



               logger.warning("No cookie found in request");
               exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
               return exchange.getResponse().setComplete();
            }

            exchange.getRequest().getHeaders().get("Cookie").forEach(cookie -> System.out.println(cookie));

            if (exchange.getRequest().getCookies().get("JSESSIONID") != null) {
                 jsessionid = exchange.getRequest().getCookies().getFirst("JSESSIONID").getValue();

                if (redisTemplate.hasKey(jsessionid) == true) {
                    logger.info("JSESSIONID PASSED: " + jsessionid);
                    return chain.filter(exchange);
//                Object sessionData = redisTemplate.opsForValue().get(jsessionid);
                } else {
                    logger.warning("JSESSIONID NOT PASSED: " + jsessionid);
                    return chain.filter(exchange);
                }
//                if (sessionData == null) {
//                    logger.info("Returned session from Redis: " + sessionData);
//                    return chain.filter(exchange);
//                } else {
//                    logger.warning("Invalid or expired session ID: " + jsessionid);
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                }
            } else {
                logger.warning("JSESSIONID cookie is missing for path: " + requestPath);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }


}
