package com.example.gread.app.login.service;

import com.example.gread.app.login.domain.RefreshToken;
import com.example.gread.app.login.domain.Role;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.repository.RefreshTokenRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.app.login.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    /**
     * 구글 소셜 로그인 (프론트에서 sub, email을 받는 방식)
     */
    public TokenDto googleLogin(String googleSub, String email) {
        User user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.builder()
                        .googleSub(googleSub)
                        .email(email)
                        .role(Role.USER)
                        .build()));

        return generateNewTokenSet(user.getId());
    }

    /**
     * 토큰 세트 생성 및 리프레시 토큰 DB 저장
     */
    public TokenDto generateNewTokenSet(Long userId) {
        TokenDto tokenDto = tokenProvider.createToken(userId);
        saveOrUpdateRefreshToken(userId, tokenDto.getRefreshToken());
        return tokenDto;
    }

    public void saveOrUpdateRefreshToken(Long userId, String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .map(token -> {
                    token.updateValue(tokenValue);
                    return token;
                })
                .orElseGet(() -> new RefreshToken(userId, tokenValue));

        refreshTokenRepository.save(refreshToken);
        log.info("### DB에 리프레시 토큰 저장 완료 (UserId: {})", userId);
    }

    public TokenDto reissue(String refreshTokenValue) {
        if (!tokenProvider.validateToken(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(refreshTokenValue)
                .orElseThrow(() -> {
                    log.error("### [Reissue 실패] DB에 해당 토큰이 없음");
                    return new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
                });

        TokenDto newTokenDto = tokenProvider.createToken(refreshToken.getUserId());
        refreshToken.updateValue(newTokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return newTokenDto;
    }

    public void logout(Long userId) {
        //refreshTokenRepository.deleteById(userId);
        log.info("### 로그아웃: DB에서 리프레시 토큰 삭제 완료 (ID: {})", userId);
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
        refreshTokenRepository.deleteById(userId);
        log.info("### 회원 탈퇴 완료 (ID: {})", userId);
    }
}