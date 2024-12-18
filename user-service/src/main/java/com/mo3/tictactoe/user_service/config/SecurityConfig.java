package com.mo3.tictactoe.user_service.config;

import com.mo3.tictactoe.user_service.repository.UserRepository;
import com.mo3.tictactoe.user_service.security.CustomUserDetailService;
import com.mo3.tictactoe.user_service.security.RedisSecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final RedisSecurityContextRepository redisSecurityContextRepository;

//    private final SecurityContextRepository securityContextRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {




        http.csrf().disable();
        http.authorizeHttpRequests(req -> req.requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated());

        http.formLogin(form -> form.disable());

        http.httpBasic().disable();

//        http.securityContext((context) -> context.securityContextRepository(securityContextRepository));



        http.securityContext().securityContextRepository(redisSecurityContextRepository);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.httpBasic(basic -> basic.securityContextRepository(redisSecurityContextRepository));
        http.formLogin(login -> login.disable());
        return http.build();


    }





    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());

        return authenticationManagerBuilder.build();

    }



}
