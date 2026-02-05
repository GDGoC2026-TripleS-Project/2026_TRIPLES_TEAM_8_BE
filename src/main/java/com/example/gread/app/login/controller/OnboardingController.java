package com.example.gread.app.login.controller;

import com.example.gread.app.login.service.OnboardingService; // Service 임포트
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // 중요: import 추가
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
public class OnboardingController {

    // [수정] 직접 Repository를 부르지 않고 Service를 거치도록 변경
    private final OnboardingService onboardingService;

    @PostMapping("/setup")
    // [수정] 리턴 타입을 ResponseEntity<ApiResponseTemplate<...>>으로 변경
    public ResponseEntity<ApiResponseTemplate<Map<String, String>>> setupProfile(
            Authentication authentication,
            @RequestBody Map<String, String> request
    ) {
        String email = authentication.getName();

        // [수정] 비즈니스 로직은 서비스에서 처리하고 결과만 받아옴
        onboardingService.updateProfile(email, request);

        // [수정] ApiResponseTemplate.success()의 반환 타입과 일치시킴
        return ApiResponseTemplate.success(Map.of("status", "SUCCESS"));
    }
}