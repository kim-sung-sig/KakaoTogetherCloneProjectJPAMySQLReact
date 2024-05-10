package com.example.kakao.global.jwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.service.UserService;
import com.example.kakao.global.jwt.dto.JWTDto;
import com.example.kakao.global.jwt.entity.RefreshToken;
import com.example.kakao.global.jwt.repository.RefreshTokenRepository;
import com.example.kakao.global.jwt.util.JWTUtil;

@Service
public class TokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    public boolean check(String refreshToken){
        boolean result = false;
        Optional<RefreshToken> dbRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(dbRefreshToken.isPresent()){
            result = true;
        }
        return result;
    }

    // 토큰 재발급 하기
    public JWTDto createNewAccessToken(String refreshToken){

        // 유효성 검사
        if(!jwtUtil.validateToken(refreshToken)){
            return null;
        }
        if(jwtUtil.isExpired(refreshToken)){
            return null;
        }

        // 등록된 리프레쉬 토큰인지 검사
        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(!refreshTokenEntity.isPresent()){
            return null;
        }

        // 등록된 유저의 리프레쉬 토큰인지 검사
        Long userId = refreshTokenEntity.get().getUserId();
        Optional<UserEntity> userEntity = userService.findById(userId);
        if(!userEntity.isPresent()){
            return null;
        }
        
        // 모든 검사 통과
        JWTDto jwtDto = jwtUtil.createJwt(userEntity.get());

        return jwtDto;
    }

}
