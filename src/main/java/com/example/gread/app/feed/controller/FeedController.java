package com.example.gread.app.feed.controller;

import com.example.gread.app.feed.dto.BookDto;
import com.example.gread.app.feed.service.FeedService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "도서 피드 API")
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "탐색 피드 조회 (비회원)", description = "카테고리와 키워드로 도서 목록 필터링, 페이징 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping("/explore")
    public ResponseEntity<ApiResponseTemplate<Page<BookDto>>> getBooks(
            @Parameter(description = "도서 카테고리 (선택)")
            @RequestParam(required = false) String category,
            @Parameter(description = "검색 키워드 (선택)")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "페이징 정보")
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getBooks(category, keyword, pageable));
    }

    @Operation(summary = "내 피드 조회 (회원)", description = "로그인한 사용자의 관심사에 맞는 도서 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<Page<BookDto>>> getMyFeed(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Parameter(description = "페이징 정보")
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getMyFeed(Long.parseLong(userId), pageable));
    }
}
