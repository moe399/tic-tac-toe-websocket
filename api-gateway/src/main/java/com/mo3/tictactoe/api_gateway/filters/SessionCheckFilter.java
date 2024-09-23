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
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;

@Component
@AllArgsConstructor
public class SessionCheckFilter extends AbstractGatewayFilterFactory<SessionCheckFilter.Config> {

    @Autowired
    private  RedisTemplate<String, String> redisTemplate;

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
//            logger.info("Session " + exchange.getRequest().getCookies().get("JSESSIONID"));


            if(exchange.getRequest().getCookies().get("JSESSIONID") != null){
                String jsessionidWithJessionHead = exchange.getRequest().getCookies().get("JSESSIONID").get(0).toString();
                String jsessionid = jsessionidWithJessionHead.substring(11, jsessionidWithJessionHead.length());
                System.out.println("JSESSIONID PASSED : " + jsessionid);
                System.out.println("spring:session:sessions:" + jsessionid);
                System.out.println("Returned sesh from redis " + redisTemplate.opsForHash().values("spring:session:sessions:"+jsessionid));

            }

            else{
                System.out.println("JSONID SHOULD BE NULL: " + exchange.getRequest().getCookies().get("JSESSIONID"));
            }



//            String encodedTestKey = Base64.encodeBase64String(sessionTestKey.getBytes());
//            System.out.println("ENCODE KEY "+ encodedTestKey);
//            System.out.println("SESSIONTESTKEY: " + sessionTestKey );


            // Continue the filter chain
            return chain.filter(exchange);
        };
    }



}
