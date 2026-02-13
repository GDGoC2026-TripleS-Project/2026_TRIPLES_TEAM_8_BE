package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.BookDto;
import com.example.gread.app.feed.entity.Book;
import com.example.gread.app.feed.repository.BookRepository;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.domain.ReaderType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final BookRepository bookRepository;

    @Override
    public Page<BookDto> getMyFeed(User user, Pageable pageable) {
        if (user == null || user.getReaderType() == null) {
            // 사용자가 없거나, 관심 카테고리(ReaderType) 설정이 없으면 빈 목록 반환
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        ReaderType readerType = user.getReaderType();
        String category = readerType.name(); // Enum 이름을 String으로 변환 (e.g., NOVEL -> "NOVEL")

        Page<Book> books = bookRepository.findByCategory(category, pageable);
        return books.map(this::toDto);
    }

    @Override
    public Page<BookDto> getBooks(String category, String keyword, Pageable pageable) {
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // category 필터 조건 추가
            if (StringUtils.hasText(category)) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            // keyword 검색 조건 추가
            if (StringUtils.hasText(keyword)) {
                // 제목에 키워드가 포함되거나, 키워드 목록에 키워드가 포함되는 경우
                Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                Predicate keywordIsMember = criteriaBuilder.isMember(keyword, root.get("keywords"));
                predicates.add(criteriaBuilder.or(titleLike, keywordIsMember));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Book> books = bookRepository.findAll(spec, pageable);
        return books.map(this::toDto);
    }

    private BookDto toDto(Book book) {
        return BookDto.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .thumbnailUrl(book.getThumbnailUrl())
                .category(book.getCategory())
                .keywords(book.getKeywords())
                .build();
    }
}
