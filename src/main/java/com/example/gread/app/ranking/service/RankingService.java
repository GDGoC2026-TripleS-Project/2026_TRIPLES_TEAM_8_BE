package com.example.gread.app.ranking.service;

import com.example.gread.app.ranking.domain.Ranking;
import com.example.gread.app.ranking.dto.RankingResDto;
import com.example.gread.app.ranking.repository.RankingRepository;
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

    @Transactional(readOnly = true)
    public List<RankingResDto> getTop5() {

        return rankingRepository.findTop5ByOrderByReviewCountDesc()
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

    @Scheduled(fixedRate = 6000) // 1분마다
    @Transactional
    public void refreshRanking() {
        updateRanking();
    }

    @Transactional(readOnly = true)
    public RankingResDto getMyRanking(Long userId) {
        List<Ranking> allRankings = rankingRepository.findAllByOrderByReviewCountDesc();

        for (int i = 0; i < allRankings.size(); i++) {
            Ranking ranking = allRankings.get(i);
            if (ranking.getProfile().getId().equals(userId)) {
                int rank = i + 1;
                return RankingResDto.from(ranking);
            }
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }
}