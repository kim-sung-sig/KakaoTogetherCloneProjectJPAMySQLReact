package com.example.kakao.global.security.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakao.global.dto.response.RsData;
import com.example.kakao.global.security.jwt.dto.JWTDto;
import com.example.kakao.global.security.jwt.service.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiTokenController {
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/v1/token") // 토큰 재발행을 위한 주소
    public RsData<?> freshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("토큰 재발행 요청");
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if("refreshToken".equals(cookie.getName())){
                refreshToken = cookie.getValue();
                break;
            }
        }
        if(refreshToken == null){ // 요청 파라메타가 잘 넘어왓는지 확인
            log.info("리프래쉬 토큰이 없어서 반례");
            return RsData.of("F-10", "리프레쉬 토큰 없음 로그인하셈");
        }
        // 내가 발행한 것이 맞는지 확인
        if(!tokenService.check(refreshToken)){
            log.info("DB에 리프래쉬 토큰이 없어서 반례");
            return RsData.of("F-10", "이상한거임 로그임하셈");
        }

        JWTDto jwtDto = tokenService.createNewAccessToken(refreshToken);
        
        // 새로운 토큰이 잘 만들어 졌나 확인
        if(jwtDto == null){ // 생성실패
            log.info("토큰을 못만들어서 반례");
            return RsData.of("F-", "유효하지 않은 리프래쉬 토큰입니다.");
        } else { // 생성 성공
            log.info("""
                ============================================================
                토큰 생성 응답
                accessToken => {}
                refreshToken => {}
                ============================================================""", jwtDto.getAccessToken(), jwtDto.getRefreshToken());
            response.addHeader("Authorization", "Bearer " + jwtDto.getAccessToken());
            response.addCookie(createCookie("refreshToken", jwtDto.getRefreshToken()));
            return RsData.of("S-", "성공");
        }
    }
    
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24*15); // 15일
        //cookie.setSecure(true); // https 에서만
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바스크립트로 제어불가

        return cookie;
    }
}
