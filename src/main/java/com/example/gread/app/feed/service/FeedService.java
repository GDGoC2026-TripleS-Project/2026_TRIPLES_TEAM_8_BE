package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.FeedResponseDto;
import com.example.gread.app.feed.dto.MyFeedResponseDto;

import java.util.List;

public interface FeedService {
    List<FeedResponseDto> getBooks(String category);
    MyFeedResponseDto getMyFeed(Long userId);
}
