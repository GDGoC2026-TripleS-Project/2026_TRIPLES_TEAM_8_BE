package com.example.gread.app.login.controller;

import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // 1. 중요: import 추가
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    // 2. 리턴 타입을 ResponseEntity로 감싸기
    public ResponseEntity<ApiResponseTemplate<TokenDto>> login(@RequestBody GoogleLoginRequest request) {
        return ApiResponseTemplate.success(SuccessCode.OK, authService.googleLogin(request.getSub(), request.getEmail()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(@RequestHeader("Authorization-Refresh") String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        return ApiResponseTemplate.success(SuccessCode.OK,authService.reissue(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(@AuthenticationPrincipal String userId) {
        authService.withdraw(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.OK,null);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(@AuthenticationPrincipal String userId) {
        authService.withdraw(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.OK,null);
    }

    @lombok.Getter
    static class GoogleLoginRequest {
        private String sub;
        private String email;
    }
}