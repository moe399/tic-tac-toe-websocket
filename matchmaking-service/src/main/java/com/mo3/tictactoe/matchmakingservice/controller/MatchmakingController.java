package com.mo3.tictactoe.matchmakingservice.controller;

import com.mo3.tictactoe.matchmakingservice.dto.CreateGameSuccessDTO;
import com.mo3.tictactoe.matchmakingservice.dto.NewGameDTO;
import com.mo3.tictactoe.matchmakingservice.exceptions.UserAlreadyInGameException;
import com.mo3.tictactoe.matchmakingservice.helpers.GameSession;
import com.mo3.tictactoe.matchmakingservice.service.MatchmakingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            return ResponseEntity.ok(gameId);
        } catch (UserAlreadyInGameException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        catch (Exception e) {

            return ResponseEntity.internalServerError().body(e.getMessage());

        }



    }





    @GetMapping("/getgame")
    public ResponseEntity<List<GameSession>> getGames(){

        return ResponseEntity.ok(matchmakingService.listAllGames());
    }



    @GetMapping("/getavailablegame")
    public ResponseEntity<List<GameSession>> getAvailableGames(){
        return ResponseEntity.ok(matchmakingService.listAvailableGames());
    }



    @PostMapping("/joingame/{gamesessionid}")
    public ResponseEntity<NewGameDTO> joinGame(@PathVariable String gamesessionid, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
//
//        try{
//            NewGameDTO createGameSuccessDTO = matchmakingService.joinGame(gamesessionid,httpServletRequest, httpServletResponse);
//            return ResponseEntity.ok(createGameSuccessDTO);
//        }
//
//        catch (Exception e){
//            return ResponseEntity.badRequest().body(new NewGameDTO(null, null, null,));
//        }

        NewGameDTO newGameDTO = matchmakingService.joinGame(gamesessionid, httpServletRequest, httpServletResponse);

        return ResponseEntity.ok(newGameDTO);


    }






    @PostMapping("/deletegame/{gamesessionId}")
    public ResponseEntity<String> removeGame(@PathVariable String gamesessionId){
        System.out.println("Matchmaking service - delete game called ");
        try{
           String gameIdFromService = matchmakingService.removeGame(gamesessionId);
           return ResponseEntity.ok("Successfully removed game: " + gameIdFromService);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }




}
