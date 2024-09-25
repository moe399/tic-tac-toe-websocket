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


        public ResponseEntity<UserIdResponseDTO> getUserId(){

            User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

           UserIdResponseDTO userIdResponseDTO = new UserIdResponseDTO();
            userIdResponseDTO.setId(user.getId());


            return ResponseEntity.ok(userIdResponseDTO);



        }








}
