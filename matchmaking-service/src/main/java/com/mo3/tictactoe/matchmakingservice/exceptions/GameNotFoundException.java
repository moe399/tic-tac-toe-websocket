package com.mo3.tictactoe.matchmakingservice.exceptions;

public class GameNotFoundException extends RuntimeException{



    public GameNotFoundException(String message){
         super(message);
    }


}
