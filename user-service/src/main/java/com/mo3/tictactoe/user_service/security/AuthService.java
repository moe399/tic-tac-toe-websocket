package com.mo3.tictactoe.user_service.security;

import com.mo3.tictactoe.user_service.clients.GameServiceClient;
import com.mo3.tictactoe.user_service.dto.LoginResponseDTO;
import com.mo3.tictactoe.user_service.dto.RegisterationResponseDTO;
import com.mo3.tictactoe.user_service.dto.UserAuthRequestDTO;
import com.mo3.tictactoe.user_service.entity.User;
import com.mo3.tictactoe.user_service.repository.UserRepository;
import com.mo3.tictactoe.user_service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RedisSecurityContextRepository securityContextRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final GameServiceClient gameServiceClient;
    private final UserService userService;

    public RegisterationResponseDTO register(UserAuthRequestDTO userAuthRequest) {
        logger.info("Attempting to register user");
        try {
            if (userRepository.existsByUsername(userAuthRequest.getUsername())) {
                logger.warning("Username is already in use");
                throw new RuntimeException("User already exists");
            }
            User user = new User();
            user.setUsername(userAuthRequest.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userAuthRequest.getPassword()));
            user.setIsUserInGame(false);
            userRepository.save(user);
            logger.info("Successfully registered user");
            return new RegisterationResponseDTO("User Registered Succesfully!", user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public LoginResponseDTO login(UserAuthRequestDTO userAuthRequest, HttpServletResponse response, HttpServletRequest request) {
//        RedisSecurityContextRepository securityContextRepository = new RedisSecurityContextRepository();
        try {
            if (!userRepository.existsByUsername(userAuthRequest.getUsername())) {
                logger.info("Username not found by repository");
                throw new RuntimeException("Username or password is incorrect");

            }
            User user = userRepository.findByUsername(userAuthRequest.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAuthRequest.getUsername(), userAuthRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
            // REPLACE ABOVE WITH JUST REDIS MAYBE?
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            //
//            logger.info("Successfully logged in user");
//            HttpSession session = request.getSession();
//
            return new LoginResponseDTO("Successfully logged in", user.getUsername(), user.getId());

        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);


        }


    }


    public ResponseEntity<String> logout(String gameSessionID) {


        // Check if user is in game



        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());


        System.out.println("before gameserviceclient request");
        gameServiceClient.endGameBefore(gameSessionID);

        System.out.println("after gameserviceclient request");

        // if user is in game:
                // game service - endGameEarly()
                // matchmaking service - delete game
                // setUserInGame to false



        // delete sessionCookie from redis repo


        // return success/failure

        return ResponseEntity.ok("function ended, check if game is gone and websocket");
    }


}
