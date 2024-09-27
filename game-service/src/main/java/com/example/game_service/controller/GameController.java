package com.example.game_service.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
public class GameController {


    private final GameService gameService;

    @GetMapping("/test")
    public String testContoller(){

        return "Hello from game service";

    }


    @PostMapping("/startgame/{sessionId}")
    public ResponseEntity<String> startGame(@PathVariable("sessionId") String sessionId, HttpServletRequest request, HttpServletResponse response){

        try{
           String websocketUrl = gameService.startGame(sessionId, request, response);
            return ResponseEntity.ok(websocketUrl);
        }

        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Cannot start game");

        }



    }


}
