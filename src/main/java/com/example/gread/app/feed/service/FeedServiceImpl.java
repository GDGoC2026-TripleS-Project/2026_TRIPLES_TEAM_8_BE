package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.feed.repository.FeedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedBookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailResponse> getBooks(String category, String keyword) {
        // "전체" 혹은 빈 값일 때 필터 해제
        String filterCategory = (category == null || category.isEmpty() || category.equals("전체")) ? null : category;
        return bookRepository.findByFilters(filterCategory, keyword).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailResponse> getMyFeed(Long userId) {
        return bookRepository.findAll().stream().map(this::toDto).toList();
    }

    private BookDetailResponse toDto(Book book) {
        return BookDetailResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .majorName(book.getMajorName())
                .build();
    }
}