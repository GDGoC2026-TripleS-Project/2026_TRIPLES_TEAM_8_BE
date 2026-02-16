package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Page<BookDto> getBooks(String category, String keyword, Pageable pageable);
    Page<BookDto> getMyFeed(Long userId, Pageable pageable);
}
