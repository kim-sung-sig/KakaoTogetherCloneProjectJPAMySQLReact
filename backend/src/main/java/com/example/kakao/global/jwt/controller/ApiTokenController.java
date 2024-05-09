package com.example.kakao.global.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.global.RsData.RsData;
import com.example.kakao.global.jwt.dto.request.CreateAccessTokenRequest;
import com.example.kakao.global.jwt.dto.response.CreateAccessTokenResponse;
import com.example.kakao.global.jwt.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ApiTokenController {
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/v1/token")
    public RsData< CreateAccessTokenResponse > postMethodName(@RequestBody CreateAccessTokenRequest request) {
        if(request == null){// 요청 파라메타가 잘 넘어왓는지 확인

        }
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        
        // 새로운 토큰이 잘 만들어 졌나 확인
        if(newAccessToken == null || newAccessToken.isEmpty()){ // 생성실패
            return RsData.of("F-", "newAccessToken");
        } else { // 생성 성공
            return RsData.of("S-", "newAccessToken", new CreateAccessTokenResponse(newAccessToken));
        }
    }
    

}
