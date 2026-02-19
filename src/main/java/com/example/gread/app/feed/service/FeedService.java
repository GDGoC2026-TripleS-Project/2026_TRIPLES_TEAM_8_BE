package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    List<BookDetailResponse> getBooks(String category);
    Page<BookDetailResponse> getMyFeed(Long userId, Pageable pageable);
}
