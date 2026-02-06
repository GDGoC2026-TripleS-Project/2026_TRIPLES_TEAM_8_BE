package com.example.gread.app.home.controller;

import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.service.OnboardingService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController("homeOnboardingController")
@RequestMapping("/auth/register/social")
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;

    @GetMapping
    public ResponseEntity<String> getOnboardingPage() {
        return ResponseEntity.ok("온보딩 페이지에 접근했습니다. 이제 데이터를 POST로 보내주세요.");
    }

    @PostMapping
    public ResponseEntity<ApiResponseTemplate<Void>> saveResult(
            Authentication authentication,
            @RequestBody OnboardingRequestDto request) {

        onboardingService.updateOnboarding(authentication.getName(), request);
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }
}