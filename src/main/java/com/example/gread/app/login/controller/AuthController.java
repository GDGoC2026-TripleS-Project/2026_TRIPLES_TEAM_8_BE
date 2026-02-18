package com.example.gread.app.login.controller;

import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Tag(name = "🔑 Auth", description = "인증 및 계정 관리 API")
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "구글 로그인 진입점", description = "구글 로그인 페이지로 리다이렉트하여 인증을 시작합니다.")
    @GetMapping
    public void googleLogin(HttpServletResponse response) throws IOException {
        log.info("### 구글 로그인 인증 시작 (Redirect to OAuth2)");
        response.sendRedirect("/oauth2/authorization/google");
    }

    @Operation(summary = "최종 토큰 발급", description = "리다이렉트 시 받은 임시 인증 코드를 사용하여 최종 토큰(Access, Refresh)과 이메일을 발급받습니다.")
    @PostMapping("/callback")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> getFinalTokens(
            @RequestBody AuthCodeRequest request) {
        TokenDto tokenDto = authService.exchangeCodeForTokens(request.getCode());
        return ApiResponseTemplate.success(SuccessCode.OK, tokenDto);
    }

    @Getter
    @NoArgsConstructor
    public static class AuthCodeRequest {
        private String code;
    }

    @Operation(summary = "JWT 토큰 재발급 (Reissue)",
            description = "헤더의 Authorization-Refresh(Bearer 포함 가능)를 이용하여 새 토큰을 발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(
            @Parameter(description = "Bearer {RefreshToken}", required = true)
            @RequestHeader("Authorization-Refresh") String refreshToken) {

        log.info("### 토큰 재발급 요청");
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        return ApiResponseTemplate.success(SuccessCode.OK, authService.reissue(token));
    }

    @Operation(summary = "로그아웃", description = "DB에서 리프레시 토큰을 삭제하여 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(@AuthenticationPrincipal String userId) {
        if (userId == null) {
            log.error("### 로그아웃 실패: 인증 정보를 찾을 수 없음");
            return ResponseEntity.status(401).build();
        }

        log.info("### 로그아웃 요청 (UserId: {})", userId);
        authService.logout(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }

    @Operation(summary = "회원 탈퇴", description = "유저 정보 및 리프레시 토큰을 영구 삭제합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(@AuthenticationPrincipal String userId) {
        if (userId == null) {
            log.error("### 회원 탈퇴 실패: 인증 정보를 찾을 수 없음");
            return ResponseEntity.status(401).build();
        }

        log.info("### 회원 탈퇴 진행 (UserId: {})", userId);
        authService.withdraw(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.DELETE_SUCCESS, null);
    }
}
