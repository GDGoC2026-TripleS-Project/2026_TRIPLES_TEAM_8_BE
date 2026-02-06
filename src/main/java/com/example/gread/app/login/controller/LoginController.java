package com.example.gread.app.login.controller;

import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.app.login.service.OnboardingService;
import com.example.gread.app.login.config.TokenProvider;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final OnboardingService onboardingService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponseTemplate<Map<String, Object>>> updateOnboarding(
            Authentication authentication,
            @RequestBody @Valid OnboardingRequestDto request) {

        if (authentication == null || authentication.getName() == null) {
            log.error("### 인증 정보가 없습니다.");
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        String subject = authentication.getName();
        log.info("### Onboarding request started for user ID: {}", subject);

        onboardingService.updateOnboarding(subject, request);

        User user = userRepository.findById(Long.parseLong(subject))
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. ID: " + subject));

        TokenDto tokenDto = tokenProvider.createToken(user.getId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", tokenDto.getAccessToken());
        responseData.put("refreshToken", tokenDto.getRefreshToken());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("nickname", user.getProfile().getNickname());

        responseData.put("userInfo", userInfo);

        log.info("### Onboarding process completed for user: {}", user.getProfile().getNickname());

        return ApiResponseTemplate.success(SuccessCode.UPDATE_SUCCESS, responseData);
    }
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        TokenDto tokenDto = authService.reissue(refreshToken);
        return ApiResponseTemplate.success(SuccessCode.OK, tokenDto);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(Authentication authentication) {
        String subject = authentication.getName();
        authService.logout(Long.parseLong(subject));
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }
    // 회원 탈퇴 연동
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(Authentication authentication) {
        authService.withdraw(Long.parseLong(authentication.getName()));
        return ApiResponseTemplate.success(SuccessCode.DELETE_SUCCESS, null);
    }
}