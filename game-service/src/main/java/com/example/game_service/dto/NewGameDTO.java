package com.example.game_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewGameDTO {

    private String gameSessionID;
    private Long userID1;
    private Long userID2;
    private char userLetter1;
    private char userLetter2;


}
