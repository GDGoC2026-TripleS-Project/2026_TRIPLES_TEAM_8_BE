package com.example.gread.app.review.service;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.bookDetail.repository.BookRepository;
import com.example.gread.app.login.domain.Profile;
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

        Review review = new Review(
                profile,
                book,
                dto.getReviewColor(),
                dto.getReviewContent(),
                book.getMinorName()
        );

        Review savedReview = reviewRepository.save(review);

        /* 리뷰 개수 증가 */
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

    @Transactional(readOnly = true)
    public List<ReviewResDto> findLatestReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        return reviews.stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional
    public ReviewResDto updateReview(ReviewReqDto dto, Long reviewId, Long profileId) {

        Review review = reviewRepository
                .findByReviewIdAndProfileId(reviewId, profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(
                dto.getReviewColor(),
                dto.getReviewContent()
        );

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

        /* 리뷰 개수 감소 */
        Ranking ranking = rankingRepository.findRankingByProfileId(profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RANKING_NOT_FOUND)); // 삭제라면 반드시 존재해야 함

        ranking.decreaseReviewCount(); // 삭제이므로 감소

        reviewRepository.delete(review);

        rankingService.updateRanking();
    }
}