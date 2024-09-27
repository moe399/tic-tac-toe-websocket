package com.mo3.tictactoe.matchmakingservice.controller;

import com.mo3.tictactoe.matchmakingservice.helpers.GameSession;
import com.mo3.tictactoe.matchmakingservice.service.MatchmakingService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MatchmakingController {


    private final MatchmakingService matchmakingService;

    @GetMapping("/hello")
    public ResponseEntity<String> testMap(){

        System.out.println("hello");


        return ResponseEntity.ok("hello");
    }


    @PostMapping("/creategame")
    public ResponseEntity<String> createGame(){
        try {
            String gameId = matchmakingService.createGame();  // Call createGame() only once
            if (gameId.isEmpty()) {

                return ResponseEntity.badRequest().body("User in game");
            }
            System.out.println("Reached get id controller");
            return ResponseEntity.ok("Successfully created game: " + gameId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





    @GetMapping("/getgame")
    public ResponseEntity<List<GameSession>> getGames(){

        return ResponseEntity.ok(matchmakingService.listAllGames());
    }

    @PostMapping("/joingame/{gamesessionid}")
    public ResponseEntity<String> joinGame(@PathVariable String gamesessionid){

        try{
            String websocketurl = matchmakingService.joinGame(gamesessionid);
            return ResponseEntity.ok("Successfully joined game: " + websocketurl);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body("Unable to join game: " + gamesessionid);
        }


    }





}
