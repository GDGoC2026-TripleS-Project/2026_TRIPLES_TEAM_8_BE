package com.example.gread.app.home.controller;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/onboarding")
    public ResponseEntity<?> saveOnboarding(@Valid @RequestBody OnboardingRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.updateOnboarding(request.getNickname(), ReaderType.valueOf(request.getReaderType()));
        userRepository.save(user);

        return ResponseEntity.ok("온보딩 완료!");
    }
}
