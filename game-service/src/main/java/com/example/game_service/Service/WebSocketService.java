package com.example.game_service.Service;


import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class WebSocketService {


    private final GameService gameService;

    private Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    private final RedisTemplate<String, Object> redisTemplate;


    public void registerSession(String gameSessionID, WebSocketSession webSocketSession){


        sessions.computeIfAbsent(gameSessionID, k -> new ArrayList<>()).add(webSocketSession);
        gameService.createGameInMap(gameSessionID);

    }


    public void processMessage(String gameSessionID, String message, WebSocketSession webSocketSession){

        gameService.handleGameMove(gameSessionID, message);

    }



    public boolean checkIfGameSessionExists(String gamesessionId){


        return redisTemplate.hasKey(gamesessionId).booleanValue();

    }









}
