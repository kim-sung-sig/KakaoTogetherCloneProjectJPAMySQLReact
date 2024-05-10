package com.example.kakao.global.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.global.dto.response.RsData;
import com.example.kakao.global.jwt.dto.JWTDto;
import com.example.kakao.global.jwt.dto.request.CreateAccessTokenRequest;
import com.example.kakao.global.jwt.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ApiTokenController {
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/v1/login")
    public RsData <?> login(){
        return RsData.of("S-", null);
    }

    @PostMapping("/api/v1/token") // 토큰 재발행을 위한 주소
    public RsData< JWTDto > freshToken(@RequestBody CreateAccessTokenRequest request) {
        if(request == null){// 요청 파라메타가 잘 넘어왓는지 확인

        }
        
        JWTDto jwtDto = tokenService.createNewAccessToken(request.getRefreshToken());
        
        // 새로운 토큰이 잘 만들어 졌나 확인
        if(jwtDto == null){ // 생성실패
            return RsData.of("F-", "유효하지 않은 리프래쉬 토큰입니다.");
        } else { // 생성 성공
            return RsData.of("S-", "성공", jwtDto);
        }
    }
    

}
