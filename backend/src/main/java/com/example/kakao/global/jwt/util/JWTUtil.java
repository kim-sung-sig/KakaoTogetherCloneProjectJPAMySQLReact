package com.example.kakao.global.jwt.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.global.jwt.dto.JWTDto;
import com.example.kakao.global.jwt.entity.RefreshToken;
import com.example.kakao.global.jwt.repository.RefreshTokenRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    private final long ACCESS_TOKEN_EXPRIE_TIME = 1L * 1000 * 60 * 15; // 15m
    private final long REFRESH_TOKEN_EXPIRE_TIME = 1L * 1000 * 60 * 60 * 24 * 15;// 15d

    public JWTUtil (@Value("${custom.jwt.secretKey}") String originSecretKey, RefreshTokenRepository refreshTokenRepository){
        this.secretKey = new SecretKeySpec(originSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 토큰 생성
     * @param id // 회원번호
     * @param username // 아이디
     * @param role // 권한
     * @param expiredMs // 만료일
     * @return
     */
    public JWTDto createJwt(UserEntity userEntity) {
        String accessToken = Jwts.builder()
                .claim("category", "access")
                .claim("id", userEntity.getId())
                .claim("role", userEntity.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPRIE_TIME))
                .signWith(secretKey)
                .compact();
        
        String refreshToken = Jwts.builder()
                .claim("category", "refresh")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();
        
        RefreshToken refreshTokenEntity = new RefreshToken(userEntity.getId(), refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
        return new JWTDto(accessToken, refreshToken);
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
