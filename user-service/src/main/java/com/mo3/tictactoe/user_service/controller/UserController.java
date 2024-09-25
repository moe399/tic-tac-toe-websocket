package com.mo3.tictactoe.user_service.controller;

import com.mo3.tictactoe.user_service.dto.UserIdResponseDTO;
import com.mo3.tictactoe.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    final Logger logger = Logger.getLogger(UserController.class.getName());


    @GetMapping("/user/getid")
    public ResponseEntity<UserIdResponseDTO> getUserDetails(){


       try{
           return userService.getUserId();
       }

       catch (Exception e){
           logger.warning("Error returning UserID in controller: ");
           logger.warning(e.getMessage());
           UserIdResponseDTO userIdResponseDTO = new UserIdResponseDTO();
           return ResponseEntity.badRequest().body(userIdResponseDTO);
       }

    }



}
