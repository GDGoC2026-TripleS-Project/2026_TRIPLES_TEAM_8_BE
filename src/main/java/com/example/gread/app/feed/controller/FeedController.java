package com.example.gread.app.feed.controller;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.feed.service.FeedService;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "도서 피드 API")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/explore")
    public ResponseEntity<ApiResponseTemplate<List<BookDetailResponse>>> getBooks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getBooks(category, keyword));
    }

    @GetMapping
    public ResponseEntity<ApiResponseTemplate<List<BookDetailResponse>>> getMyFeed(
            Authentication authentication
    ) {
        Long userId = getUserId(authentication); // 이제 여기서 빨간 줄 안 뜹니다!
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getMyFeed(userId));
    }

    // 누락되었던 ID 추출 메서드 추가
    private Long getUserId(Authentication authentication) {
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }
        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }
    }
}