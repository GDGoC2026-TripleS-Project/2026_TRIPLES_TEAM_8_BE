package com.example.gread.app.feed.controller;

import com.example.gread.app.feed.dto.FeedResponseDto;
import com.example.gread.app.feed.service.FeedService;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.exception.BusinessException;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "도서 피드 API")
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "탐색 피드 조회 (비회원/공통)", description = "카테고리로 도서 목록을 필터링하여 조회합니다. 페이지네이션과 키워드 검색은 지원하지 않습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping("/explore")
    public ResponseEntity<ApiResponseTemplate<List<FeedResponseDto>>> getBooks(
            @Parameter(description = "도서 카테고리 (선택) - 예: '전체', '창작', '에세이', '유머', '저널리즘'. 미지정 시 전체 도서 조회.")
            @RequestParam(required = false) String category
    ) {
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getBooks(category));
    }

    @Operation(summary = "내 피드 조회 (회원 전용)", description = "로그인한 사용자의 관심사에 맞는 도서 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<Page<FeedResponseDto>>> getMyFeed(
            @Parameter(hidden = true) Authentication authentication, // Swagger에서 숨김 처리
            @Parameter(description = "페이징 정보 (page: 페이지 번호(0부터 시작), size: 페이지 크기(기본 10), sort: 정렬 기준(예: id,desc / title,asc))")
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = getUserId(authentication);
        return ApiResponseTemplate.success(SuccessCode.OK, feedService.getMyFeed(userId, pageable));
    }

    private Long getUserId(Authentication authentication) {
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            // 인증 정보는 있지만 ID 형식이 올바르지 않은 경우
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }
    }
}
