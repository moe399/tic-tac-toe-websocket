package com.example.game_service.config;

import com.example.game_service.helpers.FeignClientInterceptor;
import com.example.game_service.helpers.SessionInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final SessionInterceptor sessionInterceptor;
    private final FeignClientInterceptor feignClientInterceptor;



}
