package com.example.kakao.config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.kakao.entity.KakaoUserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    public String generateToken(KakaoUserEntity user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // 토큰 생성기
    private String makeToken(Date expiry, KakaoUserEntity user){
        Date now = new Date();

        return Jwts.builder()
                   .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더타입 : JWT
                   .setIssuer(jwtProperties.getIssuer())
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .setSubject(user.getUsername())
                   .claim("idx", user.getIdx())
                   .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                   .compact();
    }

    // 유효한 토큰인지 확인
    public boolean validToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser()
                                .setSigningKey(jwtProperties.getSecretKey())
                                .parseClaimsJws(token);

            Date expiration = claims.getBody().getExpiration();
            return !expiration.before(new Date());

        } catch (ExpiredJwtException e){
            return false; // 만료인 경우
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 권한 부여하기
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
            new User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 유저번호 리턴하기
    public int getUserIdx(String token){
        Claims claims = getClaims(token);
        return claims.get("idx", Integer.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
