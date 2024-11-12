package com.example.game_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Player {


    private Long playerName;
    private boolean isTurn;
    private char letter;
    private int playerNumber;
    private String username;

    public Player(Long playerName, char letter, int playerNumber, String username) {
        this.playerName = playerName;;
        this.letter = letter;
        this.playerNumber = playerNumber;
        this.username = username;
    }

}
