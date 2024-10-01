package com.example.game_service.Service;


import com.example.game_service.entity.Game;
import com.example.game_service.exception.GameNotFoundException;
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


    public void createGameInMap(String gameSessionId){

        Game game = new Game();

        gamesMap.put(gameSessionId, game);

    }


    public void handleGameMove(String gameSessionId, String gameMove){

        if(gamesMap.containsKey(gameSessionId)){
            System.out.println("Game found!");
        }
        else{
            System.out.println("Game not found");


           for (String key: gamesMap.keySet()){
               System.out.println(key);
           }
        }

        System.out.println("Printing game move: " + gameMove);


    }






}
