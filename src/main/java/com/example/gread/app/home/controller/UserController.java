package com.example.gread.app.home.controller;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "👤 User", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 유저의 프로필 정보를 가져옵니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<?> saveOnboarding(@Valid @RequestBody OnboardingRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.completeOnboarding(request.getNickname(), ReaderType.valueOf(request.getReaderType()));
        userRepository.save(user);

        return ResponseEntity.ok("온보딩 완료!");
    }
}
