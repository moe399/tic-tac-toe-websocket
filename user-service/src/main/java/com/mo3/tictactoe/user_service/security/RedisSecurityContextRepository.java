package com.mo3.tictactoe.user_service.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.net.HttpCookie;
import java.util.UUID;


@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private RedisTemplate<String, Object> redisTemplate;



    @Autowired
    public RedisSecurityContextRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public RedisSecurityContextRepository() {
        System.out.println("RedisSecurityContextRepository instantiated");
    }



    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        System.out.println("LOADING CONTEXT!!!");

        HttpServletRequest request = requestResponseHolder.getRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            return null;
        }

        String jsessionId = "";

        for(int i = 0; i < cookies.length; i++) {

            if(cookies[i].getName().equals("JSESSIONID")) {
                System.out.println("JSESSIONID FOUND! IN LoaD OCntext: " + cookies[i].getValue() );
                jsessionId = cookies[i].getValue();

            }

        }

        SecurityContext context = (SecurityContext) redisTemplate.opsForValue().get(jsessionId);


        return context;
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        return SecurityContextRepository.super.loadDeferredContext(request);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Saving ocntext in redis repo");



        String jsessionId = UUID.randomUUID().toString();

            Cookie cookie = new Cookie("JSESSIONID", jsessionId);
//            cookie.setHttpOnly(true);
            cookie.setSecure(true);  // Ensure it's secure if using HTTPS
            cookie.setPath("/");

            response.addCookie(cookie);

            redisTemplate.opsForValue().set(jsessionId, context);


    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }
}
