package com.example.gread.app.home.controller;

import com.example.gread.app.home.dto.HomeResponseDto;
import com.example.gread.app.home.service.HomeService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponseTemplate<HomeResponseDto>> getHomeData(Authentication authentication) {
        String subject = (authentication != null) ? authentication.getName() : null;

        HomeResponseDto response = homeService.getHomeData(subject);
        return ApiResponseTemplate.success(SuccessCode.OK, response);
    }
}