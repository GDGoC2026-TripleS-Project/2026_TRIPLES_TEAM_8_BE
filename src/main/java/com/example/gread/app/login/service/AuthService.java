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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    // === 이 메서드가 빠져있어서 에러가 났던 것입니다! ===
    public TokenDto googleLogin(String googleSub, String email) {
        User user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.builder()
                        .googleSub(googleSub)
                        .email(email)
                        .role(Role.USER)
                        .build()));

        TokenDto tokenDto = tokenProvider.createToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(), tokenDto.getRefreshToken());

        return tokenDto;
    }

    private void saveOrUpdateRefreshToken(Long userId, String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .orElse(new RefreshToken(userId, tokenValue));
        refreshToken.updateValue(tokenValue);
        refreshTokenRepository.save(refreshToken);
    }

    public TokenDto reissue(String refreshTokenValue) {
        if (!tokenProvider.validateToken(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(refreshTokenValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));

        TokenDto newTokenDto = tokenProvider.createToken(refreshToken.getUserId());
        refreshToken.updateValue(newTokenDto.getRefreshToken());

        return newTokenDto;
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
        refreshTokenRepository.deleteById(userId);
    }
}