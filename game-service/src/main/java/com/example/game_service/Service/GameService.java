package com.example.game_service.Service;


import com.example.game_service.exception.GameNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {


    RedisTemplate<String, Object> redisTemplate;



    public String startGame(String sessionId, HttpServletRequest request, HttpServletResponse response){





        if(redisTemplate.hasKey(sessionId) == null || redisTemplate.hasKey(sessionId) == false){

            throw new GameNotFoundException("Game was not found");
        }


        String host = request.getServerName();
        int port = request.getServerPort();
        String websocketUrl = "ws://" + host + ":" + port + "/ws/game?sessionId=" + sessionId;

        return websocketUrl;




    }



}
