package com.mo3.tictactoe.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataResponseDTO {


    private long id;
    private String username;
    private boolean gameState;
    private int wins;
    private int losses;
    private int draws;
    private int totalGames;


}
