package com.example.game_service.helpers;

import com.example.game_service.config.FeignConfig;
import com.example.game_service.dto.UserDataResponseDTO;
import com.example.game_service.dto.UserGameUpdateDTO;
import feign.Headers;
import feign.Param;
import jakarta.ws.rs.HeaderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// change to lb:// prod



@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {



    @PostMapping("/user/updatecount")
    void updateUserGameStats(@RequestBody UserGameUpdateDTO updateDTO);


}
