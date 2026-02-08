package com.example.gread.login.service;

import com.example.gread.login.dto.TokenDto;
import com.example.gread.login.config.TokenProvider;
import com.example.gread.login.domain.RefreshToken;
import com.example.gread.login.domain.Role;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.RefreshTokenRepository;
import com.example.gread.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    /**
     * 구글 로그인 및 회원가입 (LOGIN-02)
     */
    public TokenDto googleLogin(String googleSub, String email) {
        // 1. 구글 고유 식별자로 기존 유저인지 확인, 없으면 신규 가입
        User user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.builder()
                        .googleSub(googleSub)
                        .email(email)
                        .role(Role.USER)
                        .build()));

        // 2. Access Token & Refresh Token 생성
        TokenDto tokenDto = tokenProvider.createToken(user.getId());

        // 3. Refresh Token 저장 혹은 업데이트 (LOGIN-03 대비)
        saveOrUpdateRefreshToken(user.getId(), tokenDto.getRefreshToken());

        return tokenDto;
    }

    /**
     * 토큰 재발급 (LOGIN-03)
     */
    public TokenDto reissue(String refreshTokenValue) {
        // 1. 리프레시 토큰 자체의 유효성(만료 여부 등) 검증
        if (!tokenProvider.validateToken(refreshTokenValue)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다. 다시 로그인하세요.");
        }

        // 2. DB에서 해당 토큰을 가지고 있는지 확인 (로그아웃 여부 확인)
        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("서버에 존재하지 않는 토큰입니다."));

        // 3. 새로운 토큰 세트 생성
        TokenDto newTokenDto = tokenProvider.createToken(refreshToken.getUserId());

        // 4. DB의 리프레시 토큰 값 최신화
        refreshToken.updateValue(newTokenDto.getRefreshToken());

        return newTokenDto;
    }

    /**
     * 로그아웃 (LOGIN-04)
     */
    public void logout(Long userId) {
        // DB에서 리프레시 토큰을 삭제하여 더 이상 재발급이 불가능하게 함
        refreshTokenRepository.deleteById(userId);
    }

    /**
     * 회원 탈퇴 (LOGIN-05)
     */
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 유저 삭제 (CascadeType.ALL 설정으로 인해 Profile, Review 등도 함께 삭제됨)
        userRepository.delete(user);

        // 리프레시 토큰도 함께 삭제
        refreshTokenRepository.deleteById(userId);
    }

    /**
     * 내부 로직: 리프레시 토큰 저장/업데이트
     */
    private void saveOrUpdateRefreshToken(Long userId, String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(token -> {
                    token.updateValue(tokenValue);
                    return token;
                })
                .orElse(RefreshToken.builder()
                        .userId(userId)
                        .tokenValue(tokenValue)
                        .build());

        refreshTokenRepository.save(refreshToken);
    }
}