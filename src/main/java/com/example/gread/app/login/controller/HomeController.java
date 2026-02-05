package com.example.gread.app.login.controller;

import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.login.service.HomeService;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// HomeController.java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponseTemplate<Map<String, Object>>> getHomeData() {
        return ApiResponseTemplate.success(homeService.getRecommendData());
    }
}