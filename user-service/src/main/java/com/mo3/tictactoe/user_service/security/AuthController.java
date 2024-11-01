package com.mo3.tictactoe.user_service.security;


import com.mo3.tictactoe.user_service.dto.LoginResponseDTO;
import com.mo3.tictactoe.user_service.dto.RegisterationResponseDTO;
import com.mo3.tictactoe.user_service.dto.UserAuthRequestDTO;
import com.mo3.tictactoe.user_service.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/auth/register")
    public ResponseEntity<RegisterationResponseDTO> register(@RequestBody UserAuthRequestDTO userAuthRequestDTO) {
        logger.info("Reached register controller");
        try {
            RegisterationResponseDTO registerationResponseDTO = authService.register(userAuthRequestDTO);
//            return ResponseEntity.ok(registerationResponseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerationResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterationResponseDTO("Bad request supplied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterationResponseDTO("Internal Server Error", null));
        }


    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserAuthRequestDTO userAuthRequestDTO, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Reached login controller");
        try {
            LoginResponseDTO login = authService.login(userAuthRequestDTO, response, request);
            return ResponseEntity.status(HttpStatus.OK).body(login);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDTO("Bad request supplied", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO("Internal Server Error", null));
        }


    }
    // This route has to be authenticated first to be used

    @PostMapping("/logout/{gameSessionID}")
    public ResponseEntity<String> logout(@PathVariable String gameSessionID) {
        // Check if user is in game
        if (gameSessionID != null) {
            return authService.logout(gameSessionID);


        }
        return ResponseEntity.ok("function ended, didnt go into if game!=null block, check if i passed in gameSessionid");


    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutWithoutSessionID() {
        return ResponseEntity.ok("Logged out successfully without a specific game session.");
    }

}
