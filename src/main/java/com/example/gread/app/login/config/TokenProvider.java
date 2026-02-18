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
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    private final SecretKey key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Long userId) {
        long now = (new Date()).getTime();
        long accessTokenValidityTime = 1000L * 60 * 60 * 24;
        long refreshTokenValidityTime = 1000L * 60 * 60 * 24 * 7;

        String accessToken = Jwts.builder()
                .subject(userId.toString())
                .expiration(new Date(now + accessTokenValidityTime))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .expiration(new Date(now + refreshTokenValidityTime))
                .signWith(key)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String userId = claims.getSubject();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        return new UsernamePasswordAuthenticationToken(userId, "", Collections.singleton(authority));
    }
}
