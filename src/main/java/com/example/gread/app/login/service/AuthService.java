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
        String pureToken = tokenValue.startsWith("Bearer ") ? tokenValue.substring(7) : tokenValue;

        log.info("### [DB 저장 시도] UserId: {}, Token(순수): {}", userId, pureToken.substring(0, 10) + "...");

        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .map(token -> {
                    token.updateValue(pureToken);
                    return token;
                })
                .orElseGet(() -> new RefreshToken(userId, pureToken));

        refreshTokenRepository.save(refreshToken);
        log.info("### [DB 저장 완료] UserId: {}", userId);
    }

    public TokenDto reissue(String refreshTokenValue) {
        String pureToken = refreshTokenValue.startsWith("Bearer ") ? refreshTokenValue.substring(7) : refreshTokenValue;

        if (!tokenProvider.validateToken(pureToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(pureToken)
                .orElseThrow(() -> {
                    log.error("### [Reissue 실패] DB에 순수 토큰 값이 없음: {}", pureToken);
                    return new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
                });

        TokenDto newTokenDto = tokenProvider.createToken(refreshToken.getUserId());

        String newPureRefreshToken = newTokenDto.getRefreshToken().startsWith("Bearer ")
                ? newTokenDto.getRefreshToken().substring(7) : newTokenDto.getRefreshToken();

        refreshToken.updateValue(newPureRefreshToken);

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