package com.mo3.tictactoe.user_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "game-service", url = "http://localhost:8083")
public interface GameServiceClient {


    @PostMapping("/endgamebefore/{gameSessionid}")
    public void endGameBefore(@PathVariable("gameSessionid") String gameSessionid);



}
