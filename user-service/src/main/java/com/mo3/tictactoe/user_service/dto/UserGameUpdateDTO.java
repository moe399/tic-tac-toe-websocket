package com.mo3.tictactoe.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGameUpdateDTO {

    Long id;
    private int win;
    private int loss;
    private int draw;


}
