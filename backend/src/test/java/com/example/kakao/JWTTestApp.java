package com.example.kakao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.kakao.global.jwt.JWTUtil;

import io.jsonwebtoken.security.Keys;

@SpringBootTest
public class JWTTestApp {

    @Value("${custom.jwt.secretKey}")
    private String orginSecretKey;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("시크릿 키 체크")
    void checkSecretKey(){
        assertNotNull(orginSecretKey);
    }

    @Test
    @DisplayName("시크릿 키 암호화")
    void encriptSecretKey(){
        String encode = Base64.getEncoder().encodeToString(orginSecretKey.getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(encode.getBytes());
        System.out.println(secretKey);
        assertNotNull(secretKey);
    }

    @Test
    @DisplayName("jwtUtil을 이용한 키 암호화")
    void useasfJWTUtil(){
        SecretKey secretKey = jwtUtil.getSecretKey();
        SecretKey secretKey2 = jwtUtil.getSecretKey();

        assertEquals(secretKey, secretKey2);
    }

    @Test
    @DisplayName("jwt 토큰 만들기 시험")
    void createJwt(){
        String token = jwtUtil.createJwt("accessToken", 1L, "test", "ROLE_USER", 1000*60*1L); // 1분!
        
        System.out.println("accessToken : " + token);
        assertNotNull(token);
    }

    @Test
    @DisplayName("jwt 토큰 유효성 검사! 1 - 내가 토큰이 맞는지 검증")
    void validJwt(){
        String token = jwtUtil.createJwt("accessToken", 1L, "test", "ROLE_USER", 1000*60*1L); // 1분!

        System.out.println("accessToken : " + token);
        assertNotNull(token);
    }

    @Test
    @DisplayName("jwt 토큰 유효성 검사! 2 - 시간 검증")
    void valid2Jwt(){
        String token = jwtUtil.createJwt("accessToken", 1L, "test", "ROLE_USER", 1000*60*1L); // 1분!
        assertFalse(jwtUtil.isExpired(token));
        try {
            Thread.sleep(1000*7*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("jwt 토큰 정보검사")
    void valid3Jwt(){
        String token = jwtUtil.createJwt("accessToken", 1L, "test", "ROLE_USER", 1000*60*1L); // 1분!

        System.out.println("accessToken : " + token);
        assertNotNull(token);
    }

}
