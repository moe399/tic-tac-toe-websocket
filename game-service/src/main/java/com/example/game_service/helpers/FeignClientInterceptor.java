package com.example.game_service.helpers;


import com.example.game_service.Service.WebSocketService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@AllArgsConstructor
    public class FeignClientInterceptor implements RequestInterceptor {




    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("Interceptor feign called");





        String cookie = "JSESSIONID=ad08d7e7-674a-4cf1-a2e8-c85dc2852589;SESSION=YWQwOGQ3ZTctNjc0YS00Y2YxLWEyZTgtYzg1ZGMyODUyNTg5;";

            System.out.println("Printing cookie: " + cookie);
            if (cookie != null && cookie.contains("JSESSIONID")) {
                requestTemplate.header("Cookie", cookie);




                System.out.println("Adding JSESSIONID to Feign request: " + cookie);
            }
            System.out.println(requestTemplate.headers().toString());
        System.out.println("Type: " + requestTemplate.method().toString());
        System.out.println(requestTemplate.request().requestTemplate().toString());




        }



}









