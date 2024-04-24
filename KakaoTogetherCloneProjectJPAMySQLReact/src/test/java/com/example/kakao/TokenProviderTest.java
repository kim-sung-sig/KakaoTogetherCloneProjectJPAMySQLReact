package com.example.kakao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.kakao.config.jwt.JwtProperties;
import com.example.kakao.config.jwt.TokenProvider;
import com.example.kakao.entity.KakaoUserEntity;
import com.example.kakao.repository.KakaoUserRepository;

import io.jsonwebtoken.Jwts;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private KakaoUserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken(){
        KakaoUserEntity testUser = userRepository.save(
            KakaoUserEntity.builder()
                .username("test")
                .password("123456")
                .type("form")
                .name("name")
                .nameNum(1)
                .email("user@gamil.com")
                .build()
        );

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        Integer idx = Jwts.parser()
                        .setSigningKey(jwtProperties.getSecretKey())
                        .parseClaimsJws(token)
                        .getBody()
                        .get("idx", Integer.class);

        assertEquals(idx, testUser.getIdx());
    }

    @DisplayName("validToken() : 만료된 토큰인 경우 유효성 검증 실패하는가?")
    @Test
    void validToken_invaildToken(){
        String token = JwtFactory.builder()
                        .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                        .build()
                        .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertEquals(result, false);
    }

    @DisplayName("validToken() : 유효한 토큰인 경우 유효성 검증 성공하는가?")
    @Test
    void validToken_vaildToken(){
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertEquals(result, true);
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 벙보를 가져올 수 있는가?")
    @Test
    void getAuthentication(){
        String username = "test";
        String token = JwtFactory.builder()
                            .subject(username)
                            .build()
                            .createToken(jwtProperties);
        
        Authentication authentication = tokenProvider.getAuthentication(token);

        assertEquals(((UserDetails) authentication.getPrincipal()).getUsername(), username);
    }

    @DisplayName("getIdx(): 토큰 기반으로 인증 벙보를 가져올 수 있는가?")
    @Test
    void getIdx(){
        Integer idx = 1;
        String token = JwtFactory.builder()
                            .claims(Map.of("idx", idx))
                            .build()
                            .createToken(jwtProperties);
        
        Integer userIdxByToken = tokenProvider.getUserIdx(token);

        assertEquals(userIdxByToken, idx);
    }
}
