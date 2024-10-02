package com.example.game_service.Service;


import com.example.game_service.entity.Game;
import com.example.game_service.exception.GameNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TypeKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class GameService {

    // This game service is for the local game entit

    RedisTemplate<String, Object> redisTemplate;

    private HashMap<String, Object> gamesMap = new HashMap<>();

//    public String registerGame(){
//
//
//
//    }


    public void createGameInMap(String gameSessionId, Long player1Id, Long player2Id) {

        // this method sets up game and adds to map

        Game game = new Game();


        // fetch user id or both players and set them as game id's, or username...



        gamesMap.put(gameSessionId, game);


        System.out.println("Created game in map with session id: " + gameSessionId);

    }

    public void removeGameFromMap(String gameSessionId){

        gamesMap.remove(gameSessionId);
        System.out.println("Removed game from map with session id: " + gameSessionId);

        // Also maybe have to call matchmaking for this, to remove game from redis

    }


    public void handleGameMove(String gameSessionId, String message) throws JsonProcessingException {

        if(gamesMap.containsKey(gameSessionId)){
            System.out.println("Game found!");







            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode node = objectMapper.readTree(message);

            String userId = node.get("userId").asText();
            String content = node.get("content").asText();


            System.out.println("UserId: " + userId);
            System.out.println("Content: " + content);





        }
        else{
            System.out.println("Game not found");


           for (String key: gamesMap.keySet()){
               System.out.println(key);
           }
        }

        System.out.println("Printing game move: " + message);


    }






}
