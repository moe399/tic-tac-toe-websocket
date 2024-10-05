package com.example.game_service.Service;

import com.example.game_service.entity.Game;
import com.example.game_service.entity.Player;
import com.example.game_service.exception.GameNotFoundException;
import com.example.game_service.exception.NotYourTurnException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TypeKey;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {
    // This game service is for the local game entit
    RedisTemplate<String, Object> redisTemplate;
    private HashMap<String, Game> gamesMap = new HashMap<>();

    public void createGameInMap(String gameSessionId, Long player1Id, Long player2Id) {
        // this method sets up game and adds to map
        Player player1 = new Player(player1Id, 'O', 0);
        Player player2 = new Player(player2Id, 'X', 1);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        Game game = new Game(playerList);
        System.out.println("Added Player to playerlist: " + playerList.get(0).getPlayerName() + " and " + playerList.get(1).getPlayerName());
        // fetch user id or both players and set them as game id's, or username...
        gamesMap.put(gameSessionId, game);
        System.out.println("Created game in map with both players - session id: " + gameSessionId);

    }

    public void removeGameFromMap(String gameSessionId) {
        gamesMap.remove(gameSessionId);
        System.out.println("Removed game from map with session id: " + gameSessionId);
        // Also maybe have to call matchmaking for this, to remove game from redis
    }

    public void handleGameMove(String gameSessionId, String message) throws JsonProcessingException {
        if (gamesMap.containsKey(gameSessionId)) {
            System.out.println("Game found!");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(message);
            String userId = node.get("userId").asText();
            System.out.println(userId + "vs" + gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName());

            // another if statement here to check if user is in game at all
            if(!userId.matches(gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName().toString())){
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

    public String returnGameArray(String gameSessionId) {
        if (gamesMap.containsKey(gameSessionId)) {
            System.out.println("Game found!");
            return gamesMap.get(gameSessionId).getGameBoardString();


        } else {
            System.out.println("Game not found");
            for (String key : gamesMap.keySet()) {
                System.out.println(key);
            }
        }
        return "Error returning game map ";
    }


    public String returnCurrentGameState(String gameSessionId) {

        Gson gson = new Gson();

        if (gamesMap.containsKey(gameSessionId)) {

            String gameArray = returnGameArray(gameSessionId);
            String currentPlayer = gamesMap.get(gameSessionId).getCurrentPlayer().getPlayerName().toString();
            Map<String, String> gameState = new HashMap<>();
            gameState.put("currentPlayer", currentPlayer);
            gameState.put("gameArray", gameArray);

            String json = gson.toJson(gameState);

            return json;


        } else {
           return "{}";
        }
    }



    }










