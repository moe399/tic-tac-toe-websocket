package com.mo3.tictactoe.user_service.service;

import com.mo3.tictactoe.user_service.dto.UserDataResponseDTO;
import com.mo3.tictactoe.user_service.dto.UserIdResponseDTO;
import com.mo3.tictactoe.user_service.entity.User;
import com.mo3.tictactoe.user_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UserService {


    private final UserRepository userRepository;


    public UserDataResponseDTO getUserData() {

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        UserDataResponseDTO userDataResponseDTO = new UserDataResponseDTO();
        userDataResponseDTO.setUsername(user.getUsername());
        userDataResponseDTO.setId(user.getId());
        userDataResponseDTO.setGameState(user.getIsUserInGame());

        System.out.println("USER RESPONSE DTO");
        System.out.println(userDataResponseDTO);
        return userDataResponseDTO;


    }


    public UserDataResponseDTO updateUserGameStatus(boolean status) {

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        user.setIsUserInGame(status);
        userRepository.save(user);
        UserDataResponseDTO userDataResponseDTO = new UserDataResponseDTO();

        userDataResponseDTO.setId(user.getId());
        userDataResponseDTO.setGameState(status);
        userDataResponseDTO.setUsername(user.getUsername());

        return userDataResponseDTO;
    }



}
