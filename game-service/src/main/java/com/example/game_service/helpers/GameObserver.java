package com.example.game_service.helpers;

import com.example.game_service.entity.Player;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface GameObserver {


    public void onGameComplete(Player winner, Player loser, boolean draw, String gamesessionId );


}
