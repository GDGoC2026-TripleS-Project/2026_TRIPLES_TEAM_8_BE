//package com.example.gread.app.login.controller;
//
//import com.example.gread.app.login.dto.TokenDto;
//import com.example.gread.app.login.service.AuthService;
//import com.example.gread.app.login.config.TokenProvider;
//import com.example.gread.global.code.ErrorCode;
//import com.example.gread.global.code.SuccessCode;
//import com.example.gread.global.exception.BusinessException;
//import com.example.gread.global.responseTemplate.ApiResponseTemplate;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@Tag(name = "🔐 Login & Auth", description = "소셜 로그인 및 토큰 관리 API")
//@Slf4j
//@RestController
//@RequestMapping("/api/login")
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final AuthService authService;
//
//    @Operation(summary = "구글 소셜 로그인 진입점")
//    @GetMapping("/google")
//    public void googleLogin() {}
//
//    @Operation(summary = "토큰 재발급 (Reissue)")
//    @PostMapping("/reissue")
//    public ResponseEntity<ApiResponseTemplate<TokenDto>> reissue(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//        if (refreshToken == null) throw new BusinessException(ErrorCode.INVALID_TOKEN);
//        return ApiResponseTemplate.success(SuccessCode.OK, authService.reissue(refreshToken));
//    }
//
//    @Operation(summary = "로그아웃")
//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponseTemplate<Void>> logout(Authentication authentication) {
//        authService.logout(Long.parseLong(authentication.getName()));
//        return ApiResponseTemplate.success(SuccessCode.OK, null);
//    }
//
//    @Operation(summary = "회원 탈퇴")
//    @DeleteMapping("/withdraw")
//    public ResponseEntity<ApiResponseTemplate<Void>> withdraw(Authentication authentication) {
//        authService.withdraw(Long.parseLong(authentication.getName()));
//        return ApiResponseTemplate.success(SuccessCode.DELETE_SUCCESS, null);
//    }
//}