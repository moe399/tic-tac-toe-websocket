package com.example.game_service.handler;

import com.example.game_service.Service.WebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {




    private final WebSocketService webSocketService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session){

        // add logic to see if this game even exists, if not disconnect

        String query = session.getUri().getQuery();
        String gameSessionID = query.split("=")[1];


        webSocketService.registerSession(gameSessionID, session);


    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message){

        String query = session.getUri().getQuery();
        String gameSessionID = query.split("=")[1];

      webSocketService.processMessage(gameSessionID, message.getPayload(), session);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // The WebSocket has been closed
    }










}
