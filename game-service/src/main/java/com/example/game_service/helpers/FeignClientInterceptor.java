package com.example.game_service.helpers;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
    public class FeignClientInterceptor implements RequestInterceptor {

        private final HttpServletRequest httpServletRequest;


        @Override
        public void apply(RequestTemplate requestTemplate) {
            String cookie = httpServletRequest.getHeader("Cookie");  // Capture the Cookie header
            if (cookie != null && cookie.contains("JSESSIONID")) {
                requestTemplate.header("Cookie", cookie);  // Add the Cookie header to the Feign request
                System.out.println("Adding JSESSIONID to Feign request: " + cookie);  // Debugging log
            }
        }
    }











