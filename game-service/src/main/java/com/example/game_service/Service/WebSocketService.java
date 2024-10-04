package com.example.game_service.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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


    public void processMessage(String gameSessionID, String message, WebSocketSession webSocketSession) throws IOException {


      try {
          gameService.handleGameMove(gameSessionID, message);


          webSocketSession.sendMessage(new TextMessage("Success"));
      }
      catch (IOException e){
          e.printStackTrace();
          webSocketSession.sendMessage(new TextMessage("Failure"));
      }
    }



    public boolean checkIfGameSessionExists(String gamesessionId){


        return redisTemplate.hasKey(gamesessionId).booleanValue();

    }



    public void broadcastGameArray(String gameSessionID){

        Set<String> keysList = sessions.keySet();


        for (String key : keysList) {

            if (key.equals(gameSessionID)) {


                List<WebSocketSession> webSocketSessions = sessions.get(key);

                for (WebSocketSession webSocketSession : webSocketSessions) {
                    if (webSocketSession.isOpen()) {

                        try{
                            webSocketSession.sendMessage(new TextMessage(gameService.returnCurrentGameState(gameSessionID)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }




    }









}
