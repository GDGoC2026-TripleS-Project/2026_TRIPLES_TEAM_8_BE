package com.example.gread.login.service;

import com.example.gread.domain.Book;
import com.example.gread.domain.User;
import com.example.gread.repository.BookRepository;
import com.example.gread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 취향 결과 코드에 맞는 도서 포스트잇 5개를 무작위로 추출
     */
    public List<Book> getRandomPostitsByContent(Long userId) {
        // 1. 사용자 조회 및 취향 코드 획득
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String userCategory = user.getProfile().getTestResultCode(); // 8가지 유형 중 하나

        // 2. 해당 카테고리의 모든 도서 조회
        List<Book> categoryBooks = bookRepository.findByCategory(userCategory);

        // 3. 무작위로 섞기 (새로고침 대응)
        Collections.shuffle(categoryBooks);

        // 4. 최대 5개까지만 반환
        return categoryBooks.stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}