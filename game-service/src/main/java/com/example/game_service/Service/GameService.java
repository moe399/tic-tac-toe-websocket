package com.example.game_service.Service;

import com.example.game_service.dto.GameInfoDTO;
import com.example.game_service.dto.NewGameDTO;
import com.example.game_service.dto.GameStateDTO;
import com.example.game_service.dto.UserGameUpdateDTO;
import com.example.game_service.entity.Game;
import com.example.game_service.entity.Player;
import com.example.game_service.exception.GameNotFoundException;
import com.example.game_service.exception.NotYourTurnException;
import com.example.game_service.helpers.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GameService implements GameInterface {
    // This game service is for the local game entit
    RedisTemplate<String, Object> redisTemplate;
    private final HashMap<String, Game> gamesMap = new HashMap<>();
    private final MatchmakingServiceClient matchmakingServiceClient;
    private final UserServiceClient userServiceClient;
    private final List<GameObserver> gameObservers = new ArrayList<>();

    private final WebSocketService webSocketService;



    @Autowired
    public GameService(RedisTemplate<String, Object> redisTemplate, MatchmakingServiceClient matchmakingServiceClient, UserServiceClient userServiceClient, @Lazy WebSocketService webSocketService) {
        this.redisTemplate = redisTemplate;
        this.matchmakingServiceClient = matchmakingServiceClient;
        this.userServiceClient = userServiceClient;
        this.webSocketService = webSocketService;
    }

    public NewGameDTO createGameInMap(String gameSessionId, Long player1Id, Long player2Id, String usernamePlayer1, String usernamePlayer2) {
        // this method sets up game and adds to map
        Player player1 = new Player(player1Id, 'O', 0, usernamePlayer1);
        Player player2 = new Player(player2Id, 'X', 1, usernamePlayer2);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        Game game = new Game(playerList, this);
        game.setGamesessionId(gameSessionId);
        System.out.println("Added Player to playerlist: " + playerList.get(0).getPlayerName() + " and " + playerList.get(1).getPlayerName());
        // fetch user id or both players and set them as game id's, or username...
        gamesMap.put(gameSessionId, game);
        System.out.println("Created game in map with both players - session id: " + gameSessionId);
        NewGameDTO newGameDTO = new NewGameDTO(gameSessionId, player1Id, player2Id, player1.getLetter(), player2.getLetter());
        return newGameDTO;
    }

    public void removeGameFromMap(String gameSessionId) {
        gamesMap.remove(gameSessionId);
        System.out.println("Removed game from map with session id: " + gameSessionId);
        // Also maybe have to call matchmaking for this, to remove game from redis
    }




    public void handleGameMove(String gameSessionId, String message, WebSocketSession session) throws JsonProcessingException {
        if (gamesMap.containsKey(gameSessionId)) {


            System.out.println("Game found!");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(message);
            String userId = node.get("userId").asText();
            System.out.println(userId + "vs" + gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName());
            // another if statement here to check if user is in game at all
            if (!userId.matches(gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName().toString())) {
                // TEST THIS!!
                throw new NotYourTurnException("Not your turn");

            }
            String content = node.get("content").asText();
            System.out.println("UserId: " + userId);
            System.out.println("Content: " + content);
            gamesMap.get(gameSessionId).playRound(Long.valueOf(userId), content);


        } else {
            throw new GameNotFoundException("Game not found with games map");

        }


    }

    public int [] [] returnGameArray(String gameSessionId) {
        if (gamesMap.containsKey(gameSessionId)) {
            System.out.println("Game found!");
            return gamesMap.get(gameSessionId).getGameBoardArrayNormal();


        } else {
            System.out.println("Game not found");
            for (String key : gamesMap.keySet()) {
                System.out.println(key);
            }
        }

        int [][] ret = new int[0][0];
        return ret;
    }

    public GameStateDTO returnCurrentGameState(String gameSessionId) {
        Gson gson = new Gson();
        if (gamesMap.containsKey(gameSessionId)) {


            GameStateDTO gameStateDTO = new GameStateDTO();

            gameStateDTO.setCurrentPlayer(gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName());


            gameStateDTO.setGameArray(returnGameArray(gameSessionId));


            return gameStateDTO;


//            String gameArray = returnGameArray(gameSessionId);
//            String currentPlayer = gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName().toString();
//            Map<String, String> gameState = new HashMap<>();
//            gameState.put("currentPlayer", currentPlayer);
//            gameState.put("gameArray", gameArray);
//            String json = gson.toJson(gameState);
//            return json;


        } else {
            return new GameStateDTO();
        }
    }


    public GameInfoDTO returnGameInfo(String gameSessionId) {

        if(gamesMap.containsKey(gameSessionId)){

            Game currentGame = gamesMap.get(gameSessionId);

            Long userId1 = currentGame.getPlayerList().get(0).getPlayerName();
            Long userId2 = currentGame.getPlayerList().get(1).getPlayerName();
            String usernamePlayer1 = currentGame.getPlayerList().get(0).getUsername();
            String usernamePlayer2 = currentGame.getPlayerList().get(1).getUsername();
            char user1Letter = currentGame.getPlayerList().get(0).getLetter();
            char user2Letter = currentGame.getPlayerList().get(1).getLetter();
            GameInfoDTO gameInfoDTO = new GameInfoDTO(gameSessionId, userId1, userId2, usernamePlayer1, usernamePlayer2, user1Letter, user2Letter);

            return gameInfoDTO;
        }

        else{
            return new GameInfoDTO(gameSessionId, 0, 0, null,  null, 'F',  'F');
        }

    }


    // Callback function - called from within gameinstance end method
    @Override
    public String endGameWithWinner(Player winner, Player loser, boolean draw, String gamesessionId) {
        System.out.println("End game in gameservice with winner called");

        WebSocketSession webSocketSession = webSocketService.returnWebSocketSession(gamesessionId);

       String cookie = webSocketSession.getAttributes().get("Cookie").toString();




        System.out.println("Cookie from websocket Sesh: " + cookie);

        notifyObserversThatGameEndedWithWinner(winner, loser, draw,  gamesessionId);

        // maybe call user service to update the player games played and wins and also the other player as loser (feign client)
        // gracefully end game:
        // 1. remove game from map
        gamesMap.remove(gamesessionId);
        // 2. in matchmaking service remove the game session from redis store
        matchmakingServiceClient.deleteGame(gamesessionId);


        // 3. update userservice and (a). increment user game, and wins/losses
        // , (b), change userIngame state to false;
        // FOR WINNER
        UserGameUpdateDTO userDTOForWinner = new UserGameUpdateDTO();
        userDTOForWinner.setWin(1);
        userDTOForWinner.setLoss(0);
        userDTOForWinner.setDraw(0);
        userDTOForWinner.setId(winner.getPlayerName());







        userServiceClient.updateUserGameStats(userDTOForWinner);
        CookieStorage.clearCookie();

        return "End Game with winner called!!!";
    }


    public String endGameBeforeEnd(String gamesessionId) throws IOException {
        System.out.println("End game before end called");

        gamesMap.remove(gamesessionId);
        matchmakingServiceClient.deleteGame(gamesessionId);

        webSocketService.onGameEndEarly(gamesessionId);


        CookieStorage.clearCookie();

        return "End Game before end called!!!";
    }








    public void addObserver(GameObserver gameObserver) {
        gameObservers.add(gameObserver);
    }

    public void removeObserver(GameObserver gameObserver) {
        gameObservers.remove(gameObserver);
    }

    public void notifyObserversThatGameEndedWithWinner(Player winner, Player loser, boolean draw, String gamessionId) {
        // there should only be one observer ..?
        gameObservers.get(0).onGameComplete(winner, loser, draw, gamessionId);

    }





}










