package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.BookDto;
import com.example.gread.app.feed.entity.Book;
import com.example.gread.app.feed.repository.FeedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedBookRepository bookRepository;

    @Override
    public Page<BookDto> getBooks(String category, String keyword, Pageable pageable) {
        // 현재 category 필터는 미적용, 키워드와 제목에 대해 검색
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrKeyword1ContainingIgnoreCaseOrKeyword2ContainingIgnoreCase(keyword, keyword, keyword, pageable);
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
