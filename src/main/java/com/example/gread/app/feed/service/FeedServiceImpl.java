package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.feed.dto.FeedResponseDto;
import com.example.gread.app.feed.dto.MyFeedResponseDto;
import com.example.gread.app.feed.repository.FeedBookRepository;
import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
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
    public MyFeedResponseDto getMyFeed(Long userId) {
        List<Book> books;
        List<Integer> majorCode = Collections.emptyList();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ReaderType readerType = user.getReaderType();
            if (readerType != null) {
                majorCode = readerType.getCategoryCodes();
                List<String> majorNames = readerType.getMajorNames();
                if (!majorNames.isEmpty()) {
                    books = bookRepository.findByMajorNameIn(majorNames);
                    if (!books.isEmpty()) {
                        return MyFeedResponseDto.of(majorCode, books.stream().map(this::toDto).collect(Collectors.toList()));
                    }
                }
            }
        }

        books = bookRepository.findAll();
        List<FeedResponseDto> bookDtos = books.stream().map(this::toDto).collect(Collectors.toList());
        return MyFeedResponseDto.of(majorCode, bookDtos);
    }

    private FeedResponseDto toDto(Book book) {
        long reviewCount = reviewRepository.countByBookId(book.getId());
        return FeedResponseDto.of(book, reviewCount);
    }
}
