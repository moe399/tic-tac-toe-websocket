package com.mo3.tictactoe.matchmakingservice.config;

import com.mo3.tictactoe.matchmakingservice.helpers.FeignClientInterceptor;
import com.mo3.tictactoe.matchmakingservice.helpers.SessionInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final SessionInterceptor sessionInterceptor;
    private final FeignClientInterceptor feignClientInterceptor;


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor()
//                .addPathPatterns("/**");
//    }

}
