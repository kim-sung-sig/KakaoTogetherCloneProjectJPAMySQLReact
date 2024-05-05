package com.example.kakao.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

    @Value("${custom.jwt.secretKey}")
    private String originSecretKey;

    private SecretKey secretKey;

    private SecretKey _getSecretKey(){
        return new SecretKeySpec(originSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public SecretKey getSecretKey(){
        if(secretKey == null) secretKey = _getSecretKey();
        return secretKey;
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰 생성
     * @param id // 회원번호
     * @param username // 아이디
     * @param role // 권한
     * @param expiredMs // 만료일
     * @return
     */
    public String createJwt(String category, Long id, String username, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("category", category) // accessToken OR refreshToken
            .claim("id", id)
            .claim("username", username)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis())) // 생성시각
            .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료시각
            .signWith(getSecretKey())
            .compact();
    }

    /**
     * 만료인지 확인
     * @param token
     * @return
     */
    public Boolean isExpired(String token) {
        try{
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (JwtException e){
            return true; // 예외가 발생하면 만료된것임
        }
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }
    
}
