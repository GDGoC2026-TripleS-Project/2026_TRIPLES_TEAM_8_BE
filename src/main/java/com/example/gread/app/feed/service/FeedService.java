package com.example.gread.app.feed.service;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;

import java.util.List;

public interface FeedService {
    List<BookDetailResponse> getBooks(String category, String keyword);
    List<BookDetailResponse> getMyFeed(Long userId);
}