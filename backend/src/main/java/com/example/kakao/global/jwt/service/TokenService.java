package com.example.kakao.global.jwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.service.UserService;
import com.example.kakao.global.jwt.util.JWTUtil;

@Service
public class TokenService {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    public String createNewAccessToken(String refreshToken){
        if(!jwtUtil.validateToken(refreshToken)){
            return "";
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        Optional<UserEntity> userEntity = userService.findById(userId);
        if(!userEntity.isPresent()){
            return "";
        }
        UserEntity ue = userEntity.get();
        return jwtUtil.createJwt("accessToken", ue.getId(), ue.getUsername(), ue.getRole(), 1000L*60*15);
    }
}
