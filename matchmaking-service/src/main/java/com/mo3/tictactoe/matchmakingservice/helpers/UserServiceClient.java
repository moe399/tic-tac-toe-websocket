package com.mo3.tictactoe.matchmakingservice.helpers;


import com.mo3.tictactoe.matchmakingservice.dto.UserDataResponseDTO;
import com.mo3.tictactoe.matchmakingservice.dto.UserIdResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// prod change to lb://
@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/user/getuserdetails")
    UserDataResponseDTO getUserDetails();

    @PostMapping("/user/update/{state}")
    UserDataResponseDTO updateUserGameStatus(@PathVariable("state") String state);

}
