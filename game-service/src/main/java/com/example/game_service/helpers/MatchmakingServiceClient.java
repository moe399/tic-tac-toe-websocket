package com.example.game_service.helpers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// prod change to lb://
@FeignClient(name = "matchmaking-service", url = "${MATCHMAKING_SERVICE_URL}")
public interface MatchmakingServiceClient {


    @PostMapping("/deletegame/{gamesessionId}")
    public String deleteGame(@PathVariable String gamesessionId);


}
