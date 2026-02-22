package com.example.gread.app.ranking.service;

import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.ranking.domain.Ranking;
import com.example.gread.app.ranking.dto.RankingResDto;
import com.example.gread.app.ranking.repository.RankingRepository;
import com.example.gread.app.review.repository.ReviewRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void updateRankings() {
        List<Object[]> reviewCounts = reviewRepository.countReviewsByProfile();
        rankingRepository.deleteAll();

        int rank = 1;
        long lastReviewCount = -1;

        for (Object[] row : reviewCounts) {
            Profile profile = (Profile) row[0];
            Long count = (Long) row[1];
            long reviewCount = count != null ? count : 0;

            if (reviewCount == 0) continue; // 리뷰 없는 사용자는 랭킹에 포함하지 않음

            if (lastReviewCount != -1 && reviewCount < lastReviewCount) {
                rank++;
            }

            Ranking ranking = new Ranking(profile, reviewCount, rank);
            rankingRepository.save(ranking);

            lastReviewCount = reviewCount;
        }
    }

    @Transactional(readOnly = true)
    public List<RankingResDto> getTop5() {
        List<Ranking> rankings = rankingRepository.findAllByOrderByRankAsc();

        List<RankingResDto> result = new ArrayList<>();

        for (Ranking r : rankings) {
            // 리뷰가 0개인 경우 랭킹 리스트에서 제외 (방어 코드)
            if (r.getReviewCount() == 0) continue;

            if (result.size() >= 5) break;

            result.add(RankingResDto.builder()
                    .nickname(r.getProfile().getNickname())
                    .reviewCount(r.getReviewCount())
                    .rank(r.getRank())
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public RankingResDto getMyRanking(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 현재 리뷰 수 조회
        long myReviewCount = reviewRepository.countByProfileId(profileId);

        // 리뷰가 0이면 순위도 0 반환
        if (myReviewCount == 0) {
            return RankingResDto.builder()
                    .nickname(profile.getNickname())
                    .reviewCount(0)
                    .rank(0)
                    .build();
        }

        long rank = rankingRepository.countByReviewCountGreaterThan(myReviewCount) + 1;

        return RankingResDto.builder()
                .nickname(profile.getNickname())
                .reviewCount(myReviewCount)
                .rank(rank)
                .build();
    }

    @Transactional(readOnly = true)
    public Long getReviewCountByProfileId(Long profileId) {
        return reviewRepository.countByProfileId(profileId);
    }
}