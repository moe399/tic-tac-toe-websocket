package com.mo3.tictactoe.matchmakingservice.service;


import com.mo3.tictactoe.matchmakingservice.helpers.UserServiceClient;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MatchmakingService {


//    private RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Object> hashOperations;

    private final UserServiceClient userServiceClient;


    public Long createGame(){
        System.out.println("reached create game service");
        String gameSessionID = UUID.randomUUID().toString();


        long id = Long.parseLong(userServiceClient.getID());
        System.out.println("ID!!! from server: " + id);

        return id;

    }



}
