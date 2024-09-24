package com.mo3.tictactoe.matchmakingservice.controller;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchmakingController {



    @GetMapping("/hello")
    public ResponseEntity<String> testMap(){

        System.out.println("hello");


        return ResponseEntity.ok("hello");
    }


}
