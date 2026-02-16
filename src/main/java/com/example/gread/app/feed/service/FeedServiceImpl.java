package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.feed.repository.FeedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedBookRepository bookRepository;

    @Override
    public Page<BookDetailResponse> getBooks(String category, String keyword, Pageable pageable) {
        Page<Book> books;
        if (StringUtils.hasText(keyword)) {
            books = bookRepository.findByKeyword(keyword, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }
        return books.map(this::toDto);
    }

    @Override
    public Page<BookDetailResponse> getMyFeed(Long userId, Pageable pageable) {
        // 현재는 전체 도서 목록 반환
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(this::toDto);
    }

    private BookDetailResponse toDto(Book book) {
        return BookDetailResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .majorName(book.getMajorName())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .build();
    }
}
