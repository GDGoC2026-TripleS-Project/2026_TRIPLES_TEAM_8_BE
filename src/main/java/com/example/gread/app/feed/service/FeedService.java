package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.feed.dto.FeedResponse;
import com.example.gread.app.bookDetail.repository.BookRepository;
import com.example.gread.app.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<FeedResponse> getAllBooksForMain() {
        // 1. 모든 도서 조회
        List<Book> books = bookRepository.findAll();

        // 2. 루프를 돌며 각 도서 엔티티 + 리뷰 개수를 DTO로 변환
        return books.stream()
                .map(book -> {
                    long count = reviewRepository.countByBookId(book.getId());
                    return FeedResponse.from(book, count);
                })
                .collect(Collectors.toList());
    }
}
