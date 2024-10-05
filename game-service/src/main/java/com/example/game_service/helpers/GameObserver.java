package com.example.game_service.helpers;

import com.example.game_service.entity.Player;

public interface GameObserver {


    public void onGameComplete(Player player, String gamesessionId );

}
