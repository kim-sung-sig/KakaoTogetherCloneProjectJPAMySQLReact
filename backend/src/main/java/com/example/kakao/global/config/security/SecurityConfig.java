package com.example.kakao.global.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.kakao.domain.user.service.CustomOAuth2UserService;
import com.example.kakao.global.jwt.JWTFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomOAuth2UserService CustomOAuth2UserService;
    
    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((auth) -> auth.disable())
            .httpBasic((basic) -> basic.disable())
            .formLogin((auth) -> auth.disable())

            // 로그인 필터
            .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
            // oauth
            .oauth2Login((oauth2) -> {
                oauth2.userInfoEndpoint((userInfoEndpointConfig) -> {
                    userInfoEndpointConfig.userService(CustomOAuth2UserService);
                })
                .successHandler(customSuccessHandler)// 성공핸들러
                .failureHandler(null); // 실패핸들러
            });

        // 경로별 인가작업
        http.authorizeHttpRequests((auth) -> {
            auth
                .requestMatchers("/upload/**", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/").permitAll()
                // 토큰 재발급
                .requestMatchers("/reissue").permitAll()
                // 스웨거
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
        });

        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);

                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }
        }));

        // 세션 설정 : STATELESS
        http.sessionManagement((session) -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        return http.build();
    }
}
