package com.mo3.tictactoe.matchmakingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataResponseDTO {


    private long id;
    private String username;
    private boolean gameState;


}
