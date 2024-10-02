package com.mo3.tictactoe.matchmakingservice.service;


import com.mo3.tictactoe.matchmakingservice.dto.UserDataResponseDTO;
import com.mo3.tictactoe.matchmakingservice.exceptions.GameIsFullException;
import com.mo3.tictactoe.matchmakingservice.exceptions.GameNotFoundException;
import com.mo3.tictactoe.matchmakingservice.exceptions.UnableToStartGameException;
import com.mo3.tictactoe.matchmakingservice.exceptions.UserAlreadyInGameException;
import com.mo3.tictactoe.matchmakingservice.helpers.GameServiceClient;
import com.mo3.tictactoe.matchmakingservice.helpers.GameSession;
import com.mo3.tictactoe.matchmakingservice.helpers.UserServiceClient;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MatchmakingService {


    private RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Object> hashOperations;

    private final UserServiceClient userServiceClient;
    private final GameServiceClient gameServiceClient;
    public static final String APIGATEWAY_HOSTNAMEANDPORT = System.getenv("APIGATEWAY_HOSTNAMEANDPORT");

    // Create Game (Creates redis entry)
    // Join Game (updates redis entry)
    // Delete Game (removes redis entry, either on purpose or after game ends automatically)
    // List Games


    public String createGame() {
        System.out.println("reached create game service");
        String gameSessionID = UUID.randomUUID().toString();


        try {
            UserDataResponseDTO responseDTO = userServiceClient.getUserDetails();

            if (responseDTO.isGameState() == true) {

                throw new UserAlreadyInGameException("User is already in game");
            }

            Long player1Id = responseDTO.getId();
            GameSession sessionData = new GameSession(player1Id);
            sessionData.setGameSessionId(gameSessionID);

            // Store the session in Redis
            redisTemplate.opsForValue().set(gameSessionID, sessionData);
            String message = "\"Game session stored with ID: \" + gameSessionID";
            System.out.println(message);
            userServiceClient.updateUserGameStatus("true");
            redisTemplate.convertAndSend(gameSessionID, message );


        } catch (Exception e) {
            System.err.println("Error storing session in Redis: " + e.getMessage());
            e.printStackTrace();
            return "";
        }

        return gameSessionID;

    }


    public String joinGame(String gameSessionID, HttpServletRequest request, HttpServletResponse response) {




            UserDataResponseDTO responseDTO = userServiceClient.getUserDetails();
            GameSession gameSession = (GameSession) redisTemplate.opsForValue().get(gameSessionID);


            if(responseDTO.getId() == gameSession.getPlayer1id()){
                System.out.println("user in game");
                throw new UserAlreadyInGameException("User is already in game");
            }

            if(gameSession.getPlayer2id() != null){
                // this means 2 players in game already
                System.out.println("game is full");
                throw new GameIsFullException("Game is full");

            }


            gameSession.setPlayer2id(responseDTO.getId());
            redisTemplate.opsForValue().set(gameSessionID, gameSession);
            userServiceClient.updateUserGameStatus("true");


            // RIGHT HERE!!! - Game Service call to createActual Game and then return Websocket url to:
        // 1 - player 2 - that called this endpoint as string
        // 2 - and player 1 - who is listening on Redis

        String gameUrl = "";


        try{

            if(redisTemplate.hasKey(gameSessionID) == null || redisTemplate.hasKey(gameSessionID) == false){

                throw new GameNotFoundException("Game was not found");
            }


            String host = request.getServerName();
            int port = request.getServerPort();
            gameUrl = "ws://" + APIGATEWAY_HOSTNAMEANDPORT +"/game/ws/game/" + gameSessionID;
            gameServiceClient.startGame(gameSessionID, gameSession.getPlayer1id(), gameSession.getPlayer2id());


            // make sure to find out if this returns success or not
        }
        catch (Exception e){
            throw  new UnableToStartGameException("Unable to start game");
        }



            redisTemplate.convertAndSend(gameSessionID, "Game Ready! " + "url: " + gameUrl);

            // Start game service, redirect or something
            return gameUrl;
        }





    public List<GameSession> listAllGames() {

        Set<String> keys = redisTemplate.keys("*");

        List<GameSession> gameSessions = new ArrayList<>();

        for (String key : keys) {
            GameSession sessionData = (GameSession) redisTemplate.opsForValue().get(key);
            gameSessions.add(sessionData);
        }

        return gameSessions;
    }


}