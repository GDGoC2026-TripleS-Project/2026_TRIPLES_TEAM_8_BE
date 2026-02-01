package com.example.gread.login.controller;

import com.example.gread.login.dto.TokenDto;
import com.example.gread.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 구글 로그인 성공 후 호출 (LOGIN-02)
     * 프론트엔드로부터 받은 구글 정보를 바탕으로 서비스에서 로그인/회원가입 처리
     */
    @PostMapping("/google")
    public ResponseEntity<TokenDto> login(@RequestBody GoogleLoginRequest request) {
        // 실제로는 request에서 구글의 sub와 email을 꺼내서 전달합니다.
        return ResponseEntity.ok(authService.googleLogin(request.getSub(), request.getEmail()));
    }

    /**
     * 토큰 재발급 (LOGIN-03)
     * 유효한 RefreshToken을 헤더에 담아 보내면 새로운 AccessToken 세트를 발급합니다.
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestHeader("Authorization-Refresh") String refreshToken) {
        // "Bearer " 접두사 제거 로직이 필요할 수 있습니다.
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        return ResponseEntity.ok(authService.reissue(token));
    }

    /**
     * 로그아웃 (LOGIN-04)
     * DB에서 리프레시 토큰을 삭제하여 더 이상 재발급이 불가능하게 만듭니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal String userId) {
        authService.logout(Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴 (LOGIN-05)
     * 유저 정보 및 연관된 프로필, 리뷰 등을 모두 삭제합니다.
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal String userId) {
        authService.withdraw(Long.parseLong(userId));
        return ResponseEntity.noContent().build();
    }

    // 테스트를 위한 간단한 내부 DTO
    @lombok.Getter
    static class GoogleLoginRequest {
        private String sub;
        private String email;
    }
}