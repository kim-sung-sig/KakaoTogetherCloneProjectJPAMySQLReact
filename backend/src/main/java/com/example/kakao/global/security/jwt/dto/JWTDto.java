package com.example.kakao.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTDto{

    String accessToken;
    String refreshToken;
    
}
