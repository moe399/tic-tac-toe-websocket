package com.example.game_service.controller;


import com.example.game_service.Service.GameService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GameController {


    private final GameService gameService;

    @GetMapping("/test")
    public String testContoller(){

        return "Hello from game service";

    }



    @PostMapping("/startgame/{sessionId}/{player1id}/{player2id}")
    public ResponseEntity<String> startGame(@PathVariable String sessionId, @PathVariable Long player1id, @PathVariable Long player2id){

        try {
            gameService.createGameInMap(sessionId, player1id, player2id);
            return ResponseEntity.ok("Game started");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to start game");
        }

    }


    @PostMapping("/endgamebefore/{gameSessionId}")
    public ResponseEntity<String> endGameBeforeEnd(@PathVariable String gameSessionId){



        System.out.println("Game ctrller has been reached");

        try{
            gameService.endGameBeforeEnd(gameSessionId);
            return ResponseEntity.ok("Game ended");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to end game");
        }

    }


}
