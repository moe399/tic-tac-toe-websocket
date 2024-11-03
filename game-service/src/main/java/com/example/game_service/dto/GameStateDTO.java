package com.example.game_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameStateDTO {

    private Long currentPlayer;
    private int [] [] gameArray;


}
