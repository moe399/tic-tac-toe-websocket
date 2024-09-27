package com.mo3.tictactoe.matchmakingservice.helpers;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// prod change to lb://
@FeignClient(name = "game-service", url = "http://localhost:8083")
public interface GameServiceClient {


    @PostMapping("/startgame/{sessionId}")
     String startGame(@PathVariable("sessionId") String sessionId);




}
