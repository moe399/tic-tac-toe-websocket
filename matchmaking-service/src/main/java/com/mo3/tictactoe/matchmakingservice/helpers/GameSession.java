package com.mo3.tictactoe.matchmakingservice.helpers;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSession implements Serializable {


    private Long player1id;
    private Long player2id;
    private String usernamePlayer1;
    private String usernamePlayer2;
    private String gameSessionId;

    public GameSession(Long player1id){
        this.player1id = player1id;
    }




}
