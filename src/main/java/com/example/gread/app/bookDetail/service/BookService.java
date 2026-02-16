package com.example.gread.app.bookDetail.service;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.bookDetail.repository.BookRepository;
import com.example.gread.app.review.repository.ReviewRepository; // 1. ReviewRepository 추가
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository; // 2. 의존성 주입 추가

    @Transactional(readOnly = true)
    public BookDetailResponse getBookById(Long id) {
        // 1. DB에서 도서 조회
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        // 2. 해당 도서의 리뷰 개수 조회
        long reviewCount = reviewRepository.countByBookId(id);

        // 3. 도서 엔티티와 리뷰 개수를 응답 DTO로 변환하여 반환
        return BookDetailResponse.from(book, reviewCount);
    }
}