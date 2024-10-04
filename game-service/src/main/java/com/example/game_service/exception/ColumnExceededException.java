package com.example.game_service.exception;

public class ColumnExceededException extends RuntimeException{

    public ColumnExceededException(String message) {
        super(message);
    }


}
