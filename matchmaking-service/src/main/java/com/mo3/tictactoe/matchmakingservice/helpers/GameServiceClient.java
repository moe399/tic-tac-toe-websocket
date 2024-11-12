package com.mo3.tictactoe.matchmakingservice.helpers;


import com.mo3.tictactoe.matchmakingservice.dto.NewGameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// prod change to lb://
@FeignClient(name = "game-service", url = "http://localhost:8083")
public interface GameServiceClient {


    @PostMapping("/startgame/{sessionId}/{player1id}/{player2id}/{usernamePlayer1}/{usernamePlayer2}")
    NewGameDTO startGame(@PathVariable("sessionId") String sessionId, @PathVariable Long player1id, @PathVariable Long player2id, @PathVariable String usernamePlayer1, @PathVariable String usernamePlayer2);




}
