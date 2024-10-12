package com.example.game_service.helpers;

public class FeignCookieRequestContext {



    private static final ThreadLocal<String> cookieHolder = new ThreadLocal<>();

    public static void setCookie(String cookie){
        System.out.println("Cookie has been set in context:" + cookie);
        cookieHolder.set(cookie);
    }

    public static String getCookie(){
        System.out.println("Printing cookie: " + cookieHolder.get());
        return cookieHolder.get();
    }


    public static void clear(){
        cookieHolder.remove();
    }


}
