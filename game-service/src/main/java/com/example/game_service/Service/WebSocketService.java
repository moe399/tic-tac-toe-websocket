package com.example.game_service.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {



    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    public void registerSession(String gameSessionID, WebSocketSession webSocketSession){


        sessions.put(gameSessionID, webSocketSession);


    }


    public void processMessage(String gameSessionID, String message){
        System.out.println("Received from Client: " + message);

    }










}
