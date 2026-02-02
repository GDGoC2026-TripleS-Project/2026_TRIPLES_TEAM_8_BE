package com.example.gread.login.service;

import com.example.gread.login.domain.Profile;
import com.example.gread.login.domain.Review;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Map<String, Object> getHomeData(User user) {
        Profile profile = user.getProfile();
        List<Review> recommendedReviews = fetchReviewsByPreference(profile);

        // 1. 리뷰 리스트를 Map 형태의 리스트로 변환 (DTO 역할 대체)
        List<Map<String, Object>> recommendations = recommendedReviews.stream()
                .map(review -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("reviewId", review.getReviewId());
                    map.put("bookTitle", review.getBook().getTitle());
                    map.put("bookImage", review.getBook().getImageUrl());
                    map.put("content", review.getContent());
                    map.put("category", profile.getPreferenceTags());
                    return map;
                })
                .collect(Collectors.toList());

        // 2. 최종 응답 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", profile.getNickname());
        response.put("testResultCode", profile.getTestResultCode());
        response.put("recommendations", recommendations);

        return response;
    }

    private List<Review> fetchReviewsByPreference(Profile profile) {
        String tags = profile.getPreferenceTags();
        if (tags == null || tags.isBlank()) {
            return reviewRepository.findRandomReviews(5);
        }

        String mainTag = tags.split(",")[0].trim();
        List<Review> result = new ArrayList<>(reviewRepository.findRecommendedReviews(mainTag, 5));

        if (result.size() < 5) {
            result.addAll(reviewRepository.findRandomReviews(5 - result.size()));
        }
        return result;
    }
}