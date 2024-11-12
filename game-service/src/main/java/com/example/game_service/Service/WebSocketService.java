package com.example.game_service.Service;

import com.example.game_service.entity.Player;
import com.example.game_service.helpers.GameInterface;
import com.example.game_service.helpers.GameObserver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class WebSocketService implements GameObserver {
    private final GameService gameService;
    private Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;

    public void registerSession(String gameSessionID, WebSocketSession webSocketSession) {
        sessions.computeIfAbsent(gameSessionID, k -> new ArrayList<>()).add(webSocketSession);
        gameService.addObserver(this);
//        gameService.createGameInMap(gameSessionID);
    }

    public void processMessage(String gameSessionID, String message, WebSocketSession webSocketSession) throws IOException {
        try {

            if(message.equals("GETGAME") && webSocketSession.isOpen()){

                gameService.returnGameArray(gameSessionID);

            }

            else {
                gameService.handleGameMove(gameSessionID, message, webSocketSession);
            }

            if(webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage("Success"));

            }

        } catch (IOException e) {
            e.printStackTrace();
            webSocketSession.sendMessage(new TextMessage("Failure"));
        }
    }

    public boolean checkIfGameSessionExists(String gamesessionId) {
        return redisTemplate.hasKey(gamesessionId).booleanValue();

    }

    public void broadcastGameArray(String gameSessionID) {
        Set<String> keysList = sessions.keySet();
        for (String key : keysList) {
            if (key.equals(gameSessionID)) {
                List<WebSocketSession> webSocketSessions = sessions.get(key);
                for (WebSocketSession webSocketSession : webSocketSessions) {
                    if (webSocketSession.isOpen()) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Object gameState = gameService.returnCurrentGameState(gameSessionID);
                            String jsonString = objectMapper.writeValueAsString(gameState);

                            webSocketSession.sendMessage(new TextMessage(jsonString));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }





            }
        }


    }


    public WebSocketSession getSession(String gameSessionID) {

        Set<String> keySet = sessions.keySet();
        for (String key: keySet){
            if (key.equals(gameSessionID)) {
                List<WebSocketSession> webSocketSessions = sessions.get(key);

                return webSocketSessions.get(0);


            }
            }


        return null;

    }

    public void broadcastMessage(String gameSessionID, String message) {
        Set<String> keysList = sessions.keySet();
        for (String key : keysList) {
            if (key.equals(gameSessionID)) {
                List<WebSocketSession> webSocketSessions = sessions.get(key);
                for (WebSocketSession webSocketSession : webSocketSessions) {
                    if (webSocketSession.isOpen()) {
                        try {
                            System.out.println("web socket is open sending message");
                            webSocketSession.sendMessage(new TextMessage(message));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }


    }

    public void closeSessionConnection(String gameSessionID) {
        Set<String> keysList = sessions.keySet();
        for (String key : keysList) {
            if (key.equals(gameSessionID)) {
                List<WebSocketSession> webSocketSessions = sessions.get(key);
                for (WebSocketSession webSocketSession : webSocketSessions) {
                    if (webSocketSession.isOpen()) {
                        try {
                            webSocketSession.close();

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }


    }

    public WebSocketSession returnWebSocketSession(String gameSessionID) {
        System.out.println("Searching for game session ID: " + gameSessionID);
        WebSocketSession webSocketSession = null;
        Set<String> keysList = sessions.keySet();
        System.out.println("Keyset size: " + keysList.size());
        for (String key : keysList) {
            if (key.equals(gameSessionID)) {
                System.out.println("Key found in sessions list!");
                List<WebSocketSession> webSocketSessions = sessions.get(key);
                System.out.println("Returning WebSocketSession for game session ID: " + gameSessionID);
                webSocketSession = webSocketSessions.get(0);

            }
        }
        return webSocketSession;


    }

    @Override
    public void onGameComplete(Player winner, Player loser, boolean draw, String gamesessionId) {
        System.out.println("ON game end in observer from websocket service called!!!");
        String message = "Game complete";
        Map<String, String> jsonHashMap = new HashMap<>();
        if (draw) {
            jsonHashMap.put("Game Complete", "200");
            jsonHashMap.put("draw", "true");
        } else {
            String winPlayer = winner.getPlayerName().toString();
            String lossPlayer = loser.getPlayerName().toString();
            jsonHashMap.put("Game Complete", "200");
            jsonHashMap.put("winner", winner.getPlayerName().toString());
            jsonHashMap.put("loser", loser.getPlayerName().toString());


        }
        Gson gson = new Gson();
        String json = gson.toJson(jsonHashMap);
        System.out.println("Broadcasting message to sessions");
        broadcastMessage(gamesessionId, json);
        Set<String> keys = sessions.keySet();
        closeSessionConnection(gamesessionId);
        sessions.remove(gamesessionId);
        // close and delete session
    }




    public void onGameEndEarly(String gameSessionId) throws IOException {

        WebSocketSession webSocketSession = getSession(gameSessionId);


        String message = "Opponent left the match, this game won't be recorded";
        Map<String, String> jsonHashMap = new HashMap<>();

            jsonHashMap.put("Opponent left the match", "300");




        Gson gson = new Gson();
        String json = gson.toJson(jsonHashMap);

        webSocketSession.sendMessage(new TextMessage(json));

        webSocketSession.close();

    }
}
