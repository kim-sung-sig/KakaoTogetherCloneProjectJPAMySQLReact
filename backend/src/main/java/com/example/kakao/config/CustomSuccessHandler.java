package com.example.kakao.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.kakao.jwt.JWTUtil;
import com.example.kakao.oauth.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
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
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        log.info("로그인 성공 ~~~~~ {} {}", username, role);
        
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("access", access);
        tokenResponse.put("refresh", refresh);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokenResponse)); // 토큰 리턴
        /*
        response.setHeader("access", access); // 엑세스 토큰 
        response.addCookie(createCookie("refresh", refresh)); // 리프레쉬 토큰
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("http://localhost:3000");
         */
    }
    /*
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24*14);
        //cookie.setSecure(true); // https 에서만
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 자바스크립트로 제어불가

        return cookie;
    }
     */
}
