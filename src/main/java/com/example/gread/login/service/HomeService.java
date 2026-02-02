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
public class HomeService {

    private final ReviewRepository reviewRepository;

    /**
     * 홈 화면에 필요한 데이터를 DTO 없이 Map으로 조립하여 반환합니다.
     */
    public Map<String, Object> getHomeData(User user) {
        Profile profile = user.getProfile();

        // 1. 추천 리뷰 5개 가져오기
        List<Review> recommendedReviews = fetchReviewsByPreference(profile);

        // 2. 리뷰 리스트를 Map 리스트로 변환 (책 제목, 이미지, 줄거리 포함)
        List<Map<String, Object>> recommendations = recommendedReviews.stream()
                .map(review -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("reviewId", review.getReviewId());
                    item.put("bookTitle", review.getBook().getTitle());     // Book 엔티티 연관관계
                    item.put("bookImage", review.getBook().getImageUrl()); // 책 그림 URL
                    item.put("content", review.getContent());              // 줄거리
                    item.put("category", profile.getPreferenceTags());     // 추천 카테고리
                    return item;
                })
                .collect(Collectors.toList());

        // 3. 전체 응답 데이터 조립
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", profile.getNickname());
        response.put("testResultCode", profile.getTestResultCode()); // 독자 유형
        response.put("recommendations", recommendations);

        return response;
    }

    /**
     * 태그 기반 랜덤 추천 로직
     */
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