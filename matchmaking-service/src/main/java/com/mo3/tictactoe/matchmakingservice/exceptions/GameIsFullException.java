package com.mo3.tictactoe.matchmakingservice.exceptions;


public class GameIsFullException extends RuntimeException{

    public GameIsFullException(String message){
        super(message);
    }

}
