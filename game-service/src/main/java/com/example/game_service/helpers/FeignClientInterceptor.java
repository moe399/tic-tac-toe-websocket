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
        System.out.println("PRINTING COOKIE FROM CookieSTorage " + CookieStorage.getCookie());


        String cookie = CookieStorage.getCookie();


            System.out.println("Printing cookie: " + cookie);
            if (cookie != null && cookie.contains("JSESSIONID")) {
                requestTemplate.header("Cookie", cookie);




                System.out.println("Adding JSESSIONID to Feign request: " + cookie);
            }

            else{
                System.out.println("cookie not found: " + cookie + " in feignclinentinterceptonr" );
            }
            System.out.println(requestTemplate.headers().toString());
        System.out.println("Type: " + requestTemplate.method().toString());
        System.out.println(requestTemplate.request().requestTemplate().toString());




        }



}









