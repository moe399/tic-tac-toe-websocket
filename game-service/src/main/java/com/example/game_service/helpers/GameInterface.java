package com.example.game_service.helpers;

import com.example.game_service.entity.Player;

public interface GameInterface {


    public String endGameWithWinner(Player winner, Player loser, boolean draw, String gamessionId);


}
