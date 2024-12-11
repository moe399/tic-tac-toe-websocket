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
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {




    private final WebSocketService webSocketService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {

        // add logic to see if this game even exists, if not disconnect
        


        String query = session.getUri().getQuery();
//        String gameSessionID = query.split("=")[0];

        Map<String, String> queryParams = Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

        String gameSessionID = queryParams.get("sessionId");
        System.out.println("extracted gamesessionid " + gameSessionID);

        if (!webSocketService.checkIfGameSessionExists(gameSessionID)){
            System.out.println("Session doesnt exist, closing connection!");
            session.close();

        }


        webSocketService.registerSession(gameSessionID, session);


    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {




        String query = session.getUri().getQuery();
//        String gameSessionID = query.split("=")[1];


        Map<String, String> queryParams = Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

        String gameSessionID = queryParams.get("sessionId");
        System.out.println("extracted gamesessionid " + gameSessionID);


        if (!webSocketService.checkIfGameSessionExists(gameSessionID)){
            System.out.println("Session doesnt exist, closing connection!");
            session.close();

        }
        System.out.println("Cookies: " + session.getAttributes().toString());

        try {
            webSocketService.processMessage(gameSessionID, message.getPayload(), session);
            webSocketService.broadcastGameArray(gameSessionID);
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // The WebSocket has been closed




    }










}
