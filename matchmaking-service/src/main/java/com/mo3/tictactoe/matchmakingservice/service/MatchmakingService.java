package com.mo3.tictactoe.matchmakingservice.service;


import com.mo3.tictactoe.matchmakingservice.dto.UserIdResponseDTO;
import com.mo3.tictactoe.matchmakingservice.helpers.GameSession;
import com.mo3.tictactoe.matchmakingservice.helpers.UserServiceClient;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MatchmakingService {


    private RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Object> hashOperations;

    private final UserServiceClient userServiceClient;


    // Create Game (Creates redis entry)
    // Join Game (updates redis entry)
    // Delete Game (removes redis entry, either on purpose or after game ends automatically)
    // List Games


    public String createGame(){
        System.out.println("reached create game service");
        String gameSessionID = UUID.randomUUID().toString();


        try {
            UserIdResponseDTO responseDTO = userServiceClient.getID();
            Long player1Id = responseDTO.getId();
            GameSession sessionData = new GameSession(player1Id);

            // Store the session in Redis
            redisTemplate.opsForValue().set(gameSessionID, sessionData);
            System.out.println("Game session stored with ID: " + gameSessionID);

        } catch (Exception e) {
            System.err.println("Error storing session in Redis: " + e.getMessage());
            e.printStackTrace();
        }

        return gameSessionID;

    }


    public List<GameSession> listAllGames() {

        Set<String> keys = redisTemplate.keys("*");

        List<GameSession> gameSessions = new ArrayList<>();

        for (String key : keys) {
            GameSession sessionData = (GameSession) redisTemplate.opsForValue().get(key);
            gameSessions.add(sessionData);
        }

        return gameSessions;
    }



}