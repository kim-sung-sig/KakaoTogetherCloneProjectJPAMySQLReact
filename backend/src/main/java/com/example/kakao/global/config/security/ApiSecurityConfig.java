package com.example.kakao.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig {

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable());
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests((auth) -> {
                auth
                    // 게시글
                    .requestMatchers("/api/*/dreamBoards").permitAll()
                    .requestMatchers("/api/*/dreamBoards/**").permitAll()
                    // .requestMatchers(HttpMethod.GET, "/api/*/dreamBoards").permitAll()
                    // .requestMatchers(HttpMethod.GET, "/api/*/dreamBoards/**").permitAll()
                    // .requestMatchers(HttpMethod.POST, "/api/*/dreamBoards/**").hasAnyRole("ADMIN", "USER")
                    // .requestMatchers(HttpMethod.PUT, "/api/*/dreamBoards/**").hasAnyRole("ADMIN", "USER")
                    // .requestMatchers(HttpMethod.DELETE, "/api/*/dreamBoards/**").hasAnyRole("ADMIN", "USER")
                    
                    // 좋아요

                    // 후원

                    // 댓글
                    
                    .anyRequest().authenticated();
            });
        return http.build();
    }
}
