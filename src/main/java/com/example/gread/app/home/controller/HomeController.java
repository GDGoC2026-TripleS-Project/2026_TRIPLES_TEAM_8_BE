package com.example.gread.app.home.controller;

import com.example.gread.app.home.dto.HomeResponseDto;
import com.example.gread.app.home.service.HomeService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "🏠 Home", description = "홈 화면 맞춤형 데이터 API")
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "홈 화면 추천 데이터 조회",
            description = "메인 페이지에 필요한 사용자 정보와 추천 리뷰 5개를 조회합니다. 로그인 여부에 따라 개인화된 데이터를 제공합니다.")
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponseTemplate<HomeResponseDto>> getHomeData(Authentication authentication) {

        String subject = (authentication != null) ? authentication.getName() : null;
        HomeResponseDto homeData = homeService.getHomeData(subject);

        return ApiResponseTemplate.success(SuccessCode.OK, homeData);
    }
}