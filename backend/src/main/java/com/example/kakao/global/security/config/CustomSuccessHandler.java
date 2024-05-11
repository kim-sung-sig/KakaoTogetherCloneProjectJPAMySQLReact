package com.example.kakao.global.security.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.global.security.identity.oauth.CustomOAuth2User;
import com.example.kakao.global.security.jwt.dto.JWTDto;
import com.example.kakao.global.security.jwt.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = customUserDetails.getId();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        log.info("로그인 성공 ~~~~~ {} {} {}", userId, username, role);

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username(username)
                .role(role)
                .build();
        JWTDto jwtDto = jwtUtil.createJwt(userEntity); // 토큰 생성
        
        response.setHeader("accessToken", jwtDto.getAccessToken()); // 엑세스 토큰 
        response.addCookie(createCookie("refreshToken", jwtDto.getRefreshToken())); // 리프레쉬 토큰
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("/");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24); // 24시간
        //cookie.setSecure(true); // https 에서만
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바스크립트로 제어불가

        return cookie;
    }

}
