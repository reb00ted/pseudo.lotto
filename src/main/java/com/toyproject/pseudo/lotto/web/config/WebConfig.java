package com.toyproject.pseudo.lotto.web.config;

import com.toyproject.pseudo.lotto.domain.user.UserRepository;
import com.toyproject.pseudo.lotto.web.interceptor.LoginCheckInterceptor;
import com.toyproject.pseudo.lotto.web.interceptor.UserInfoInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Random;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/home", "/users/join", "/users/login", "/css/**", "/*.ico", "/error", "/error/**");

        registry.addInterceptor(new UserInfoInterceptor(userRepository))
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error/**");

    }

    @Bean
    public Random random() {
        return new Random();
    }
}
