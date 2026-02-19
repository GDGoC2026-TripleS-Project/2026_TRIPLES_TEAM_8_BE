package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.FeedResponseDto;

import java.util.List;

public interface FeedService {
    List<FeedResponseDto> getBooks(String category);
    List<FeedResponseDto> getMyFeed(Long userId);
}
