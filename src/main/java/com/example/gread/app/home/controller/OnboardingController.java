package com.example.gread.app.home.controller;

import com.example.gread.app.home.dto.OnboardingResponseDto;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.service.OnboardingService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<ApiResponseTemplate<OnboardingResponseDto>> saveResult(
            Authentication authentication,
            @RequestBody OnboardingRequestDto request) {
        OnboardingResponseDto response = onboardingService.updateOnboarding(authentication.getName(), request);
        return ApiResponseTemplate.success(SuccessCode.OK, response);
    }
}