package com.example.gread.app.bookDetail;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException; // 팀에서 사용하는 커스텀 예외 클래스
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookDetailResponse getBookById(Long id) {
        // 1. DB에서 도서 조회
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        // 2. 도서 엔티티를 응답 DTO로 변환하여 반환
        return BookDetailResponse.from(book); // 생성자 대신 정적 팩토리 메서드 권장
    }
}