package com.example.gread.app.feed.service;

import com.example.gread.app.feed.dto.FeedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    List<FeedResponseDto> getBooks(String category);
    Page<FeedResponseDto> getMyFeed(Long userId, Pageable pageable);
}
