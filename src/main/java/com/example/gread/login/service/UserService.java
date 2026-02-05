package com.example.gread.login.service;

import com.example.gread.login.domain.User;
import com.example.gread.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
@RequiredArgsConstructor // Repository를 자동으로 주입(Injection)해줍니다.
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
     */
    @Transactional // 저장 로직이므로 쓰기 권한 허용
    public Long join(User user) {
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}