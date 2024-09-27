package com.example.game_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSessionDTO implements Serializable{




        private Long player1id;
        private Long player2id;
        private String gameSessionId;



}
