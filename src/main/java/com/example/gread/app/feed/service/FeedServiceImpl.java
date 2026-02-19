package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.feed.dto.FeedResponseDto;
import com.example.gread.app.feed.repository.FeedBookRepository;
import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedServiceImpl implements FeedService {

    private final FeedBookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<FeedResponseDto> getBooks(String category) {
        List<Book> books;
        boolean hasCategory = StringUtils.hasText(category) && !category.equals("전체");

        if (hasCategory) {
            books = bookRepository.findByMajorName(category);
        } else {
            books = bookRepository.findAll();
        }
        return books.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<FeedResponseDto> getMyFeed(Long userId, Pageable pageable) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ReaderType readerType = user.getReaderType();
            if (readerType != null) {
                List<String> majorNames = readerType.getMajorNames();
                if (!majorNames.isEmpty()) {
                    return bookRepository.findByMajorNameIn(majorNames, pageable).map(this::toDto);
                }
            }
        }
        // 사용자를 찾지 못하거나 ReaderType 정보가 없는 경우 전체 도서 목록 반환
        return bookRepository.findAll(pageable).map(this::toDto);
    }

    private FeedResponseDto toDto(Book book) {
        long reviewCount = reviewRepository.countByBookId(book.getId());
        return FeedResponseDto.of(book, reviewCount);
    }
}
