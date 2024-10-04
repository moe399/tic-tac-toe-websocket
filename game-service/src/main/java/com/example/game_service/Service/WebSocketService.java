package com.example.game_service.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WebSocketService {


    private final GameService gameService;

    private Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    private final RedisTemplate<String, Object> redisTemplate;


    public void registerSession(String gameSessionID, WebSocketSession webSocketSession){


        sessions.computeIfAbsent(gameSessionID, k -> new ArrayList<>()).add(webSocketSession);
//        gameService.createGameInMap(gameSessionID);

    }


    public void processMessage(String gameSessionID, String message, WebSocketSession webSocketSession){


      try {
          String response = gameService.handleGameMove(gameSessionID, message);

          webSocketSession.sendMessage(new TextMessage(response));
      }
      catch (IOException e){
          e.printStackTrace();
      }
    }



    public boolean checkIfGameSessionExists(String gamesessionId){


        return redisTemplate.hasKey(gamesessionId).booleanValue();

    }









}
