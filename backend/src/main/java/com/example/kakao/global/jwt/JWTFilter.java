package com.example.kakao.global.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.oauth.CustomOAuth2User;
import com.example.kakao.global.jwt.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION); // accessToken

        String accessToken = getAccessToken(authorizationHeader);

        if (accessToken == null || accessToken.isEmpty()) { // 토큰이 없다면 다음 필터로 넘김
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        if(jwtUtil.isExpired(accessToken)){
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserEntity userEntity = UserEntity.builder()
                .id(jwtUtil.getId(accessToken))
                .username(jwtUtil.getUsername(accessToken))
                .role(jwtUtil.getRole(accessToken))
                .build();

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(TOKEN_PREFIX)){
            return null;
        }
        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

}
