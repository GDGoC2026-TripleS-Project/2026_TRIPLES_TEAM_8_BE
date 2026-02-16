package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.bookDetail.repository.BookRepository;
import com.example.gread.app.feed.dto.FeedResponse;
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
    public List<FeedResponse> getAllBooksForMain(String majorName) {
        List<Book> books;

        // majorName이 없거나, "전체"라는 텍스트가 들어오면 전체 조회
        if (majorName == null || majorName.isEmpty() || majorName.equals("전체")) {
            books = bookRepository.findAll();
        } else {
            // 특정 카테고리(창작, 에세이 등)가 들어오면 필터링 조회
            books = bookRepository.findByMajorName(majorName);
        }

        return books.stream()
                .map(book -> {
                    long count = reviewRepository.countByBookId(book.getId());
                    return FeedResponse.from(book, count);
                })
                .collect(Collectors.toList());
    }
}