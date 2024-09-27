package com.example.game_service.handler;

import com.example.game_service.Service.WebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {




    private final WebSocketService webSocketService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("Connection established");
        webSocketService.registerSession(session.getId(), session);

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message){
        System.out.println("Messaged Received: " + message);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // The WebSocket has been closed
    }











}
