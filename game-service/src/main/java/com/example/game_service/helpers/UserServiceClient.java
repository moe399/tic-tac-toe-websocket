package com.example.game_service.helpers;

import com.example.game_service.dto.UserGameUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// change to lb:// prod
@FeignClient(name = "game-service", url = "http://localhost:8083")
public interface UserServiceClient {

    @PostMapping("/user/update")
    public String updateUserGameStats(@RequestBody UserGameUpdateDTO userGameUpdateDTO);


}
