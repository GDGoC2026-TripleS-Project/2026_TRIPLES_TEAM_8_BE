package com.example.gread.app.review.controller;

import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.service.ReviewService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "리뷰", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "리뷰 색상과 리뷰 내용을 입력받아서 리뷰를 생성합니다.")
    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponseTemplate<ReviewResDto>> createReview(
            Authentication authentication,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewReqDto dto
    ) {

        Long profileId = Long.parseLong(authentication.getName());

        ReviewResDto review = reviewService.postReview(dto, profileId, bookId);
        return ApiResponseTemplate.success(SuccessCode.REVIEW_CREATED, review);
    }

    @Operation(summary = "내 리뷰 조회", description = "내 리뷰를 조회합니다.")
    @GetMapping("/users/me/reviews")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getMyReviews(
            Authentication authentication
    ) {
        Long profileId = Long.parseLong(authentication.getName());

        List<ReviewResDto> reviews = reviewService.findReviewByProfileId(profileId);
        return ApiResponseTemplate.success(SuccessCode.REVIEW_LIST_OK, reviews);
    }

    @Operation(summary = "책 리뷰 조회", description = "책을 기반으로 리뷰를 조회합니다.")
    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getBookReviews(
            @PathVariable Long bookId
    ) {
        List<ReviewResDto> reviews = reviewService.findReviewByBookId(bookId);
        return ApiResponseTemplate.success(SuccessCode.REVIEW_LIST_OK, reviews);
    }

    @Operation(summary = "최신순 책 리뷰 조회", description = "최신순으로 책 리뷰를 조회합니다.")
    @GetMapping("{bookId}/reviews/ranking/latest")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getLatestReviews(
            @PathVariable Long bookId
    ) {
        List<ReviewResDto> reviews = reviewService.findLatestReviewsByBookId(bookId);

        return ApiResponseTemplate.success(SuccessCode.REVIEW_LIST_OK, reviews);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 색상과 리뷰 내용을 입력받아서 리뷰를 수정합니다.")
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseTemplate<ReviewResDto>> updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReqDto dto
    ) {
        Long profileId = Long.parseLong(authentication.getName());

        ReviewResDto updatedReview =
                reviewService.updateReview(dto, reviewId, profileId);

        return ApiResponseTemplate.success(SuccessCode.REVIEW_UPDATED, updatedReview);
    }

    @Operation(summary = "책 리뷰 개수 조회", description = "책에 작성된 리뷰의 개수를 조회합니다.")
    @GetMapping("/books/{bookId}/reviews/count")
    public ResponseEntity<ApiResponseTemplate<Long>> getReviewCountByBook(
            @PathVariable Long bookId
    ) {

        long reviewCountByBook = reviewService.getReviewCountByBook(bookId);

        return ApiResponseTemplate.success(SuccessCode.REVIEW_COUNT_OK, reviewCountByBook);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseTemplate<Void>> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId
    ) {
        Long userId = Long.parseLong(authentication.getName());

        reviewService.deleteReviewById(reviewId, userId);
        return ApiResponseTemplate.success(SuccessCode.REVIEW_DELETED, null);
    }
}