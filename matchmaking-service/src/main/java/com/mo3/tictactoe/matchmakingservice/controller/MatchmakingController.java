package com.mo3.tictactoe.matchmakingservice.controller;

import com.mo3.tictactoe.matchmakingservice.service.MatchmakingService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MatchmakingController {


    private final MatchmakingService matchmakingService;

    @GetMapping("/hello")
    public ResponseEntity<String> testMap(){

        System.out.println("hello");


        return ResponseEntity.ok("hello");
    }


    @GetMapping("/testid")
    public ResponseEntity<Long> getID(){
        System.out.println("Reached get id controller");
        return ResponseEntity.ok(matchmakingService.createGame());
    }


}
