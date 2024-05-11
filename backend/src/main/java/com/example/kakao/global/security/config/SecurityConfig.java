package com.example.kakao.global.security.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.kakao.global.security.identity.oauth.CustomOAuth2UserService;
import com.example.kakao.global.security.jwt.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
    
    public SecurityConfig(
        AuthenticationConfiguration authenticationConfiguration,
        JWTUtil jwtUtil,
        CustomOAuth2UserService customOAuth2UserService,
        CustomSuccessHandler customSuccessHandler
    ){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.csrf((auth) -> auth.disable());
        
        http.httpBasic((basic) -> basic.disable());
        
        http.formLogin((auth) -> auth.disable());

        // 로그인 필터
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // oauth
        http.oauth2Login((oauth2) -> {
            oauth2.userInfoEndpoint((userInfoEndpointConfig) -> {
                userInfoEndpointConfig.userService(customOAuth2UserService);
            })
            .successHandler(customSuccessHandler)// 성공핸들러
            .failureHandler(null); // 실패핸들러
        });

        // 경로별 인가작업
        http.authorizeHttpRequests((auth) -> {
            auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/upload/**", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/login", "/join").permitAll()
                // 스웨거
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // api
                .requestMatchers(HttpMethod.POST, "/api/*/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/dreamBoard").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/dreamBoard/**").permitAll()
                .anyRequest().authenticated();
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
