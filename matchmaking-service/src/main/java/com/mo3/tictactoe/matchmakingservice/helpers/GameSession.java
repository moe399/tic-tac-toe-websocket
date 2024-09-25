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

    public GameSession(Long player1id){
        this.player1id = player1id;
    }




}
