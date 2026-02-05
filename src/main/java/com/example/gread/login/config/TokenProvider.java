package com.example.gread.login.config;

import com.example.gread.login.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    private final Key key;
    private final long accessTokenValidityTime = 1000L * 60 * 30; // 30분
    private final long refreshTokenValidityTime = 1000L * 60 * 60 * 24 * 7; // 7일

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
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
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Authentication getAuthentication(String token) {
        // 1. 토큰 복호화 (Claims 추출)
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 2. Subject에 담긴 userId 꺼내기
        String userId = claims.getSubject();

        // 3. 권한 설정 (현재 프로젝트는 간단하므로 ROLE_USER로 고정하거나 Claims에서 꺼낼 수 있습니다)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // 4. Authentication 객체 생성 (Principal에 userId를 담음)
        return new UsernamePasswordAuthenticationToken(userId, "", Collections.singleton(authority));
    }
}