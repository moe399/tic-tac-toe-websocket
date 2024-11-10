package com.mo3.tictactoe.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDTO {

    public LoginResponseDTO(String message, String username, Long id) {
        this.message = message;
        this.username = username;
        this.id = id;
    }

    private String message;
    private String username;
    private Long id;


}
