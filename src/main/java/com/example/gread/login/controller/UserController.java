package com.example.gread.login.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 현재 로그인한 사용자의 ID를 확인하는 테스트 API
     * JwtAuthenticationFilter와 TokenProvider가 정상 작동하면 userId가 출력됩니다.
     */
    @GetMapping("/me")
    public String getMyId(@AuthenticationPrincipal String userId) {
        // userId는 TokenProvider에서 setSubject(userId.toString())로 넣었던 값입니다.
        return "현재 로그인한 유저 ID(PK)는: " + userId;
    }
}