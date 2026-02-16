package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Page<BookDetailResponse> getBooks(String category, String keyword, Pageable pageable);
    Page<BookDetailResponse> getMyFeed(Long userId, Pageable pageable);
}
