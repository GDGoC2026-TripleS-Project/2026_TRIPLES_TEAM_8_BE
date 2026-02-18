package com.example.gread.app.review.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.bookDetail.repository.BookRepository;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.ranking.domain.Ranking;
import com.example.gread.app.ranking.repository.RankingRepository;
import com.example.gread.app.ranking.service.RankingService;
import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.repository.ReviewRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;
    private final BookRepository bookRepository;
    private final RankingRepository rankingRepository;
    private final RankingService rankingService;

    @Transactional
    public ReviewResDto postReview(ReviewReqDto dto, Long profileId, Long bookId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        // [병합 포인트] 수정 전(main)은 minorName을, 수정 후(dev2)는 category를 사용함.
        // 도서 엔티티에 정의된 필드에 맞춰 선택하세요. 여기선 dev2의 category를 기준으로 통합합니다.
        Review review = new Review(
                profile,
                book,
                dto.getReviewColor(),
                dto.getReviewContent(),
                book.getCategory()
        );

        Review savedReview = reviewRepository.save(review);

        /* 랭킹 점수(리뷰 개수) 관리 */
        Ranking ranking = rankingRepository.findRankingByProfileId(profileId)
                .orElseGet(() -> {
                    long lowestRank = rankingRepository.count() + 1;
                    return rankingRepository.save(new Ranking(profile, 0, lowestRank));
                });

        if (ranking.getId() != null) {
            ranking.increaseReviewCount();
        }

        return ReviewResDto.from(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResDto> findReviewByProfileId(Long profileId) {
        return reviewRepository.findByProfileId(profileId)
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResDto> findReviewByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    // [추가] 특정 도서의 최신 리뷰 조회 (dev2 로직)
    @Transactional(readOnly = true)
    public List<ReviewResDto> findLatestReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    // [추가] 전체 최신 리뷰 조회 (main 로직 유지 - 필요 시 사용)
    @Transactional(readOnly = true)
    public List<ReviewResDto> findAllLatestReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional
    public ReviewResDto updateReview(ReviewReqDto dto, Long reviewId, Long profileId) {
        // [주의] 메서드명이 findByReviewIdAndProfileId 인지 findByReviewId 인지 Repository 확인 필요
        Review review = reviewRepository
                .findByReviewIdAndProfileId(reviewId, profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(dto.getReviewColor(), dto.getReviewContent());
        return ReviewResDto.from(review);
    }

    @Transactional(readOnly = true)
    public Long getReviewCountByBook(Long bookId) {
        return reviewRepository.countByBookId(bookId);
    }

    @Transactional
    public void deleteReviewById(Long reviewId, Long profileId) {
        Review review = reviewRepository
                .findByReviewIdAndProfileId(reviewId, profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        Ranking ranking = rankingRepository.findRankingByProfileId(profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RANKING_NOT_FOUND));

        ranking.decreaseReviewCount();
        reviewRepository.delete(review);

        // 랭킹 수치 재계산 트리거
        rankingService.updateRanking();
    }
}