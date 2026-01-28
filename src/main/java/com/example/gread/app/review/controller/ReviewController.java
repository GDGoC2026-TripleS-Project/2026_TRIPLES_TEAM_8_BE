package com.example.gread.app.review.controller;

import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ReviewResDto> createReview(
            @RequestBody ReviewReqDto reviewReqDto,
            @PathVariable Long userId,
            @PathVariable Long bookId
    ) {

        ReviewResDto createReview = reviewService.postReview(reviewReqDto, userId, bookId);
        return ResponseEntity.ok(createReview);
    }

    @GetMapping("/users/me/reviews")
    public ResponseEntity<List<ReviewResDto>> getUserReviews(
            @PathVariable Long userId
    ) {
        List<ReviewResDto> reviews = reviewService.findReviewByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<ReviewResDto>> getBookReviews(
            @PathVariable Long bookId
    ) {
        List<ReviewResDto> reviews = reviewService.findReviewByBookId(bookId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReviewById(
            @PathVariable Long reviewId,
            @RequestParam Long userId
    ) {
        reviewService.deleteReviewById(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}
