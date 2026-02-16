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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<RankingResDto> getTop5() {

        return rankingRepository.findTop5ByReviewCountGreaterThanOrderByReviewCountDesc(0L)
                .stream()
                .map(RankingResDto::from)
                .toList();
    }

    @Transactional
    public void updateRanking() {

        List<Ranking> rankings =
                rankingRepository.findAllByOrderByReviewCountDesc();

        long rank = 1;

        for (Ranking r : rankings) {
            r.updateRank(rank++);
        }
    }

    @Scheduled(fixedRate = 6000) //1분마다
    @Transactional
    public void refreshRanking() {
        updateRanking();
    }

    @Transactional(readOnly = true)
    public RankingResDto getMyRanking(Long profileId) {
        List<Ranking> allRankings = rankingRepository.findAllByOrderByReviewCountDesc();

        for (int i = 0; i < allRankings.size(); i++) {
            Ranking ranking = allRankings.get(i);
            if (ranking.getProfile().getId().equals(profileId) && ranking.getReviewCount() != 0) {
                int rank = i + 1;
                return RankingResDto.from(ranking);
            }
        }

        /* 리뷰가 하나도 없어서 랭킹을 못 매기는 경우 */

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        long reviewCount = reviewRepository.countByProfileId(profileId);

        return RankingResDto.builder()
                .nickname(profile.getNickname())
                .reviewCount(reviewCount)
                .rank(0)
                .build();
    }

    @Transactional(readOnly = true)
    public Long getReviewCountByProfileId(Long profileId) {
        return reviewRepository.countByProfileId(profileId);
    }
}