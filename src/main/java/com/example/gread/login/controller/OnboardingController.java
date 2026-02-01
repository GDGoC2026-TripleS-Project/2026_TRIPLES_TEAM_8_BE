package com.example.gread.login.controller;

import com.example.gread.login.domain.Profile;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.ProfileRepository;
import com.example.gread.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
public class OnboardingController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @PostMapping("/setup")
    public ResponseEntity<?> setupProfile(Authentication authentication, @RequestBody Map<String, String> request) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Profile profile = user.getProfile();

        profile.setNickname(request.get("nickname"));
        profile.setTestResultCode(request.get("testResultCode"));
        profile.setPreferenceTags(request.get("tags"));
        profileRepository.save(profile);

        return ResponseEntity.ok(Map.of("status", "SUCCESS"));
    }
}