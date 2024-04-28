package com.example.kakao.jwt;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String username = obtainUsername(request);
        String password = obtainPassword(request);

		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

		//token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨) acess/ refresh
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //응답 설정
        response.setHeader("access", access); // 엑세스 토큰 
        response.addCookie(createCookie("refresh", refresh)); // 리프레쉬 토큰
        response.setStatus(HttpStatus.OK.value());
        
    }

	//로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); // https
        //cookie.setPath("/");
        cookie.setHttpOnly(true); // xss 방지
        return cookie;
    }
}
