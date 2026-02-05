package com.example.gread.app.review.controller;

import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.service.ReviewService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService bookReviewService;

    /* 리뷰 작성 */
    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponseTemplate<ReviewResDto>> createReview(
            Authentication authentication,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());

        ReviewResDto review = bookReviewService.postReview(dto, userId, bookId);
        return ApiResponseTemplate.success(SuccessCode.OK, review);
    }

    /* 내 리뷰 조회 */
    @GetMapping("/users/me/reviews")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getMyReviews(
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());

        List<ReviewResDto> reviews = bookReviewService.findReviewByUserId(userId);
        return ApiResponseTemplate.success(SuccessCode.OK, reviews);
    }

    /* 책 리뷰 조회 */
    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getBookReviews(
            @PathVariable Long bookId
    ) {
        List<ReviewResDto> reviews = bookReviewService.findReviewByBookId(bookId);
        return ApiResponseTemplate.success(SuccessCode.OK, reviews);
    }

    /* 최신순 리뷰 조회 */
    @GetMapping("{bookId}/reviews/ranking/latest")
    public ResponseEntity<ApiResponseTemplate<List<ReviewResDto>>> getLatestReviews(
            @PathVariable Long bookId
    ) {
        List<ReviewResDto> reviews = bookReviewService.findLatestReviewsByBookId(bookId);

        return ApiResponseTemplate.success(SuccessCode.OK, reviews);
    }

    /* 리뷰 수정 */
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseTemplate<ReviewResDto>> updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReqDto dto
    ) {
        Long userId = Long.parseLong(authentication.getName());

        ReviewResDto updatedReview =
                bookReviewService.updateReview(dto, reviewId, userId);

        return ApiResponseTemplate.success(SuccessCode.OK, updatedReview);
    }

    /* 리뷰 삭제 */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponseTemplate<Void>> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId
    ) {
        Long userId = Long.parseLong(authentication.getName());

        bookReviewService.deleteReviewById(reviewId, userId);
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }
}