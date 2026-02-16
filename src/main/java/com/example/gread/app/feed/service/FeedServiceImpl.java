package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.BookDto;
import com.example.gread.app.feed.entity.Book;
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
    public Page<BookDto> getBooks(String category, String keyword, Pageable pageable) {
        Page<Book> books;
        if (StringUtils.hasText(keyword)) {
            books = bookRepository.findByKeyword(keyword, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }
        return books.map(this::toDto);
    }

    @Override
    public Page<BookDto> getMyFeed(Long userId, Pageable pageable) {
        // TODO: 사용자 관심사에 맞는 도서 추천 로직 구현
        // 현재는 전체 도서 목록 반환
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(this::toDto);
    }

    private BookDto toDto(Book book) {
        return BookDto.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .thumbnailUrl(book.getThumbnailUrl())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .build();
    }
}
