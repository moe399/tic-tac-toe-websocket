package com.example.game_service.helpers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String cookieToUse = FeignCookieRequestContext.getCookie();
        // Retrieve the cookies from the request
        Cookie[] cookie = new Cookie[1];

        cookie[0] = new Cookie(cookieToUse, request.getRequestURI());
        System.out.println("Request sending");

        response.addCookie(cookie[0]);
        return true;
    }

}
