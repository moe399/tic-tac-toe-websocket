package com.mo3.tictactoe.user_service.controller;

import com.mo3.tictactoe.user_service.dto.UserDataResponseDTO;
import com.mo3.tictactoe.user_service.dto.UserGameUpdateDTO;
import com.mo3.tictactoe.user_service.dto.UserIdResponseDTO;
import com.mo3.tictactoe.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    final Logger logger = Logger.getLogger(UserController.class.getName());


    @GetMapping("/user/getuserdetails")
    public ResponseEntity<UserDataResponseDTO> getUserDetails(){


       try{
           return ResponseEntity.ok(userService.getUserData());
       }

       catch (Exception e){
           logger.warning("Error returning UserID in controller: ");
           logger.warning(e.getMessage());
           UserDataResponseDTO userDataResponseDTO = new UserDataResponseDTO();
           return ResponseEntity.badRequest().body(userDataResponseDTO);
       }



    }



    @PostMapping("/user/update/{state}" )
    public ResponseEntity<UserDataResponseDTO> updateUser(@PathVariable String state){

        Boolean stateToSendToService = false;

        if(state.matches("true")){
            stateToSendToService = true;
        }

        else if(state.matches("false")){
            stateToSendToService = false;
        }

        else{
            return ResponseEntity.badRequest().body(new UserDataResponseDTO());
        }


        return ResponseEntity.ok(userService.updateUserGameStatus(stateToSendToService));

    }



    @PostMapping("/user/updatecount")
    public ResponseEntity<String> updateUserGameCount(@RequestBody UserGameUpdateDTO userGameUpdateDTO){
        System.out.println("Update user game count function controller reached");
        try{
            userService.updateUserGameCount(userGameUpdateDTO);
            return ResponseEntity.ok("success");
        }

        catch (Exception e){
            logger.warning("Error updating user game count");
            return ResponseEntity.badRequest().body("error");

        }

    }



}
