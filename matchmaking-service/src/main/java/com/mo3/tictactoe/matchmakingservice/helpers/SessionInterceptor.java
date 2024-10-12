package com.mo3.tictactoe.matchmakingservice.helpers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        // Retrieve the cookies from the request
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("JSESSIONID".equals(cookie.getName())) {
//                    String jsessionId = cookie.getValue();
//                    System.out.println("JSESSIONID: " + jsessionId);
//                }
//            }
//        }
//        assert cookies != null;
//        response.addCookie(cookies[0]);
//        return true;
//    }

}
