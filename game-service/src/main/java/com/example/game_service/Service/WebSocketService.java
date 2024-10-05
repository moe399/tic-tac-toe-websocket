package com.example.game_service.Service;

import com.example.game_service.entity.Player;
import com.example.game_service.helpers.GameInterface;
import com.example.game_service.helpers.GameObserver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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
            gameService.handleGameMove(gameSessionID, message);
            webSocketSession.sendMessage(new TextMessage("Success"));
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
                            webSocketSession.sendMessage(new TextMessage(gameService.returnCurrentGameState(gameSessionID)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }


    }


    public void broadcastMessage(String gameSessionID, String message) {

        Set <String> keysList = sessions.keySet();

        for (String key : keysList) {
            if (key.equals(gameSessionID)) {
                List<WebSocketSession> webSocketSessions = sessions.get(key);

                for (WebSocketSession webSocketSession : webSocketSessions) {
                    if (webSocketSession.isOpen()) {
                        try{
                            System.out.println("web socket is open sending message");
                            webSocketSession.sendMessage(new TextMessage("TEST SEND BACK"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }


    }


    @Override
    public void onGameComplete(Player player, String gamesessionId) {

        System.out.println("ON game end in observer from websocket service called!!!");

        String message = "Game complete";
        String winner = player.getPlayerName().toString();

        Map<String, String > jsonHashMap = new HashMap<>();

        jsonHashMap.put("Game Complete", "200");
        jsonHashMap.put("winner", winner);

        // maybe call userserivce to get username to send back
        Gson gson = new Gson();

        String json = gson.toJson(jsonHashMap);
        System.out.println("Broadcasting message to sessions");
        broadcastMessage(gamesessionId, json );
    }
}
