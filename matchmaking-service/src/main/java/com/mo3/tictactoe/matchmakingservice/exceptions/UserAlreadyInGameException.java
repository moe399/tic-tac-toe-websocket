package com.mo3.tictactoe.matchmakingservice.exceptions;

public class UserAlreadyInGameException extends RuntimeException {
    public UserAlreadyInGameException(String message) {
        super(message);
    }
}
