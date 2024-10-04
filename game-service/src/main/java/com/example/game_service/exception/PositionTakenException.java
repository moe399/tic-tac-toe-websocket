package com.example.game_service.exception;

public class PositionTakenException extends RuntimeException {
    public PositionTakenException(String message) {
        super(message);
    }
}
