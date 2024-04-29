package com.example.kakao.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.kakao.repository.UserRepository;

@Component
public class JWTService {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    public Boolean validateToken(String token) {
        final String username = jwtUtil.getUsername(token);
        return userRepository.findByUsername(username)
                .map(user -> jwtUtil.validateToken(token, user))
                .orElse(false);
    }
}
