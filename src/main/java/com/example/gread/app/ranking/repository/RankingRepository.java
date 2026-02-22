package com.example.gread.app.ranking.repository;

import com.example.gread.app.ranking.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking,Long> {

    Optional<Ranking> findRankingByProfileId(Long userId);

    List<Ranking> findTop5ByReviewCountGreaterThanOrderByReviewCountDesc(long count);

    List<Ranking> findAllByOrderByReviewCountDescIdAsc();

    long countByReviewCountGreaterThan(long myReviewCount);

    List<Ranking> findAllByOrderByRankAsc();
}
