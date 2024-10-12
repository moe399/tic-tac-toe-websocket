package com.example.game_service.helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CookieHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("Intercepting before handshake");
        HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession();
        String cookie = request.getHeaders().getFirst("Cookie");
        FeignCookieRequestContext.setCookie(cookie);
        System.out.println("Cookie b4 handshake: " + cookie);

        if (cookie != null) {
            // Store the cookies in WebSocketSession attributes for later use


            session.setAttribute("Cookie", cookie );

            attributes.put("Cookie", cookie);

        }

        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
