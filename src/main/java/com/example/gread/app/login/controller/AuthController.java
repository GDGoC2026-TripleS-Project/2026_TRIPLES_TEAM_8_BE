package com.example.gread.app.login.controller;

import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "🔑 Auth", description = "인증 및 계정 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "구글 로그인/회원가입", description = "구글에서 받은 sub(ID)와 email을 이용해 로그인을 진행하고 토큰을 발급합니다.")
    @PostMapping("/google")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> login(@RequestBody GoogleLoginRequest request) {
        return ApiResponseTemplate.success(SuccessCode.OK, authService.googleLogin(request.getSub(), request.getEmail()));
    }

    @Operation(summary = "JWT 토큰 재발급 (Reissue)",
            description = "헤더의 Authorization-Refresh(Refresh Token)를 이용하여 새 Access Token을 발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(
            @Parameter(description = "Bearer {RefreshToken}", required = true)
            @RequestHeader("Authorization-Refresh") String refreshToken) {

        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        return ApiResponseTemplate.success(SuccessCode.OK, authService.reissue(token));
    }

    @Operation(summary = "로그아웃", description = "사용자의 로그아웃을 처리하여 인증 세션을 종료합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(@AuthenticationPrincipal String userId) {
        // 기존에 withdraw가 호출되던 부분을 logout으로 수정했습니다.
        authService.logout(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }

    @Operation(summary = "회원 탈퇴", description = "유저의 모든 정보를 DB에서 삭제하고 서비스를 탈퇴 처리합니다.")
    @DeleteMapping("/withdraw") // 탈퇴는 보통 DELETE 메서드를 사용합니다.
    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(@AuthenticationPrincipal String userId) {
        authService.withdraw(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.DELETE_SUCCESS, null);
    }

    @Getter
    static class GoogleLoginRequest {
        @io.swagger.v3.oas.annotations.media.Schema(description = "구글 유저 고유 ID (sub)", example = "1029384756")
        private String sub;
        @io.swagger.v3.oas.annotations.media.Schema(description = "구글 유저 이메일", example = "user@gmail.com")
        private String email;
    }
}