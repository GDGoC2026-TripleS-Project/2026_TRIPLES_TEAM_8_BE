package com.example.gread.app.feed.controller;

import com.example.gread.app.feed.dto.FeedResponse;
import com.example.gread.app.feed.service.FeedService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class FeedController {
    private final FeedService feedService; // 변수명을 feedService로 맞췄습니다.

    @GetMapping
    public ResponseEntity<ApiResponseTemplate<List<FeedResponse>>> getBookList(
            @RequestParam(required = false) String majorName // 쿼리 파라미터 추가
    ) {
        List<FeedResponse> response = feedService.getAllBooksForMain(majorName);
        return ApiResponseTemplate.success(SuccessCode.GET_BOOK_LIST_SUCCESS, response);
    }
}
