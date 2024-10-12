package com.example.game_service.handler;

import com.example.game_service.Service.WebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;

@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {




    private final WebSocketService webSocketService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {

        // add logic to see if this game even exists, if not disconnect
        


        String query = session.getUri().getQuery();
        String gameSessionID = query.split("=")[1];


        if (!webSocketService.checkIfGameSessionExists(gameSessionID)){
            System.out.println("Session doesnt exist, closing connection!");
            session.close();

        }


        webSocketService.registerSession(gameSessionID, session);


    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {


        String query = session.getUri().getQuery();
        String gameSessionID = query.split("=")[1];

        if (!webSocketService.checkIfGameSessionExists(gameSessionID)){
            System.out.println("Session doesnt exist, closing connection!");
            session.close();

        }
        System.out.println("Cookies: " + session.getAttributes().toString());

      webSocketService.processMessage(gameSessionID, message.getPayload(), session);

        webSocketService.broadcastGameArray(gameSessionID);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // The WebSocket has been closed
    }










}
