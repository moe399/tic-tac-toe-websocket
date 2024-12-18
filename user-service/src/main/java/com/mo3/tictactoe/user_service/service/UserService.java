package com.mo3.tictactoe.user_service.service;

import com.mo3.tictactoe.user_service.dto.UserDataResponseDTO;
import com.mo3.tictactoe.user_service.dto.UserGameUpdateDTO;
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
        userDataResponseDTO.setWins(user.getGamesWon());
        userDataResponseDTO.setDraws(user.getGamesDrawn());
        userDataResponseDTO.setLosses(user.getGamesLost());
        userDataResponseDTO.setTotalGames(user.getGamesPlayed());

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



    public void updateUserGameCount (UserGameUpdateDTO userGameUpdateDTO){
        System.out.println("Updating user: " + userGameUpdateDTO.getId() + " with a: " + userGameUpdateDTO.getWin());
        User user = userRepository.findById(userGameUpdateDTO.getId()).get();

        user.setGamesWon(user.getGamesWon() + userGameUpdateDTO.getWin());
        user.setGamesDrawn(user.getGamesDrawn() + userGameUpdateDTO.getDraw());
        user.setGamesLost(user.getGamesLost() + userGameUpdateDTO.getLoss());
        user.setGamesPlayed(user.getGamesPlayed() + 1);
        user.setIsUserInGame(false);

        userRepository.save(user);


    }






}
