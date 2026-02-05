package com.example.gread.app.login.config;

import com.example.gread.app.login.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityTime = 1000L * 60 * 30; // 30분
    private final long refreshTokenValidityTime = 1000L * 60 * 60 * 24 * 7; // 7일

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        // String을 byte 배열로 변환 후 HMAC 알고리즘용 Key 객체 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 (LOGIN-02)
    public TokenDto createToken(Long userId) {
        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(now + accessTokenValidityTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidityTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Authentication getAuthentication(String token) {
        // 1. 토큰 복호화 (Claims 추출)
// parserBuilder() 대신 parser()를 사용합니다.
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // 2. Subject에 담긴 userId 꺼내기
        String userId = claims.getSubject();

        // 3. 권한 설정 (현재 프로젝트는 간단하므로 ROLE_USER로 고정하거나 Claims에서 꺼낼 수 있습니다)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // 4. Authentication 객체 생성 (Principal에 userId를 담음)
        return new UsernamePasswordAuthenticationToken(userId, "", Collections.singleton(authority));
    }
}