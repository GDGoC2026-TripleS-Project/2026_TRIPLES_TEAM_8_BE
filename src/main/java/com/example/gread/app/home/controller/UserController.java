package com.example.gread.app.home.controller;

import com.example.gread.app.home.dto.OnboardingResponseDto;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.service.OnboardingService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final OnboardingService onboardingService;

    @Operation(summary = "온보딩 정보 저장", description = "사용자의 닉네임, 독서 유형 등 온보딩 정보를 저장합니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponseTemplate<OnboardingResponseDto>> saveOnboarding(
            Authentication authentication,
            @Valid @RequestBody OnboardingRequestDto request) {

        OnboardingResponseDto response = onboardingService.updateOnboarding(authentication.getName(), request);
        return ApiResponseTemplate.success(SuccessCode.OK, response);
    }
}
