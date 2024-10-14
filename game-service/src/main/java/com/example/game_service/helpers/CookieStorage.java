package com.example.game_service.helpers;

import java.util.concurrent.atomic.AtomicReference;

public class CookieStorage {
    private static final AtomicReference<String> cookie = new AtomicReference<>();

    public static void setCookie(String cookieValue) {
        cookie.set(cookieValue);
    }

    public static String getCookie() {
        return cookie.get();
    }

    public static void clearCookie() {
        cookie.set(null);
    }
}
