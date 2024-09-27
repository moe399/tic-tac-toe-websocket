package com.example.game_service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {


    @GetMapping("/test")
    public String testContoller(){

        return "Hello from game service";

    }


}
