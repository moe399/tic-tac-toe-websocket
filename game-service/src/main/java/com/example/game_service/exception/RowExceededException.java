package com.example.game_service.exception;

public class RowExceededException extends RuntimeException {
    public RowExceededException(String message) {
        super(message);
    }
}
