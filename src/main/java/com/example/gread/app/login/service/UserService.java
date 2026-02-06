package com.example.gread.app.login.service;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ReaderType getUserReaderType(String email) {
        return userRepository.findByEmail(email)
                .map(User::getReaderType)
                .orElse(ReaderType.TYPE_A);
    }
}