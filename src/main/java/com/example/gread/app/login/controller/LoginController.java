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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "🔐 Login & Auth", description = "소셜 로그인, 온보딩 및 토큰 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final OnboardingService onboardingService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Operation(summary = "구글 소셜 로그인 진입점",
            description = "프론트엔드에서 이 주소로 이동시키면 구글 로그인창이 뜹니다. (실제 경로는 /oauth2/authorization/google이나 문서화를 위해 명시)")
    @GetMapping("/google")
    public void googleLogin() {
        // Spring Security Filter에서 처리됨
    }

    @Operation(summary = "신규 유저 온보딩", description = "구글 로그인 직후 닉네임, 독자 유형 등을 저장하고 정식 토큰을 발급합니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponseTemplate<Map<String, Object>>> updateOnboarding(
            Authentication authentication,
            @RequestBody @Valid OnboardingRequestDto request) {

        if (authentication == null || authentication.getName() == null) {
            log.error("### 인증 정보가 없습니다.");
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        String subject = authentication.getName();
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

        return ApiResponseTemplate.success(SuccessCode.UPDATE_SUCCESS, responseData);
    }

    @Operation(summary = "토큰 재발급 (Reissue)", description = "만료된 Access Token을 Refresh Token을 통해 갱신합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        TokenDto tokenDto = authService.reissue(refreshToken);
        return ApiResponseTemplate.success(SuccessCode.OK, tokenDto);
    }

    @Operation(summary = "로그아웃", description = "사용자의 인증 세션을 종료합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(Authentication authentication) {
        authService.logout(Long.parseLong(authentication.getName()));
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }

    @Operation(summary = "회원 탈퇴", description = "사용자의 모든 데이터를 삭제하고 서비스를 탈퇴합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(Authentication authentication) {
        authService.withdraw(Long.parseLong(authentication.getName()));
        return ApiResponseTemplate.success(SuccessCode.DELETE_SUCCESS, null);
    }
}