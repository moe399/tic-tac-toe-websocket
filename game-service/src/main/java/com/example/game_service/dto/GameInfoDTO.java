package com.example.game_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameInfoDTO {

   private String gameSessionID;
   private long user1ID;
   private long user2ID;
   private String usernamePlayer1;
   private String usernamePlayer2;
   private char user1Letter;
   private char user2Letter;




}
