package com.example.gread.app.login.service;

import com.example.gread.app.login.config.TokenProvider;
import com.example.gread.app.login.domain.RefreshToken;
import com.example.gread.app.login.domain.Role;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.repository.RefreshTokenRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    // 임시 코드를 저장할 메모리 저장소 (코드, 유저ID)
    private final Map<String, Long> authCodeStore = new ConcurrentHashMap<>();

    public TokenDto googleLogin(String googleSub, String email) {
        User user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.builder()
                        .googleSub(googleSub)
                        .email(email)
                        .role(Role.USER)
                        .build()));

        return generateNewTokenSet(user.getId());
    }

    public String generateAuthCode(Long userId) {
        String code = UUID.randomUUID().toString();
        authCodeStore.put(code, userId);

        // 1분 후 코드가 만료되도록 스케줄링
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            authCodeStore.remove(code);
            log.info("### 임시 인증 코드 만료: {}", code);
        }, 1, TimeUnit.MINUTES);

        log.info("### 임시 인증 코드 발급: {}, UserId: {}", code, userId);
        return code;
    }

    public TokenDto exchangeCodeForTokens(String code) {
        Long userId = authCodeStore.remove(code);
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_AUTH_CODE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        TokenDto tokenDto = generateNewTokenSet(userId);

        return TokenDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .email(user.getEmail())
                .build();
    }

    public TokenDto generateNewTokenSet(Long userId) {
        TokenDto tokenDto = tokenProvider.createToken(userId);
        saveOrUpdateRefreshToken(userId, tokenDto.getRefreshToken());
        return tokenDto;
    }

    public void saveOrUpdateRefreshToken(Long userId, String tokenValue) {
        String pureToken = tokenValue.startsWith("Bearer ") ? tokenValue.substring(7) : tokenValue;
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .map(token -> {
                    token.updateValue(pureToken);
                    return token;
                })
                .orElseGet(() -> new RefreshToken(userId, pureToken));
        refreshTokenRepository.save(refreshToken);
    }

    public TokenDto reissue(String refreshTokenValue) {
        String pureToken = refreshTokenValue.startsWith("Bearer ") ? refreshTokenValue.substring(7) : refreshTokenValue;
        if (!tokenProvider.validateToken(pureToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(pureToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
        TokenDto newTokenDto = tokenProvider.createToken(refreshToken.getUserId());
        refreshToken.updateValue(newTokenDto.getRefreshToken().startsWith("Bearer ") ? newTokenDto.getRefreshToken().substring(7) : newTokenDto.getRefreshToken());
        return newTokenDto;
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteById(userId);
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
