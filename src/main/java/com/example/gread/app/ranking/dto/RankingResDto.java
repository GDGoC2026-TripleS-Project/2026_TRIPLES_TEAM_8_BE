package com.example.gread.app.ranking.dto;

import com.example.gread.app.ranking.domain.Ranking;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankingResDto {

    private String nickname;
    private long reviewCount;
    private String rank; // long -> String 변경

    public static RankingResDto from(Ranking ranking) {
        return RankingResDto.builder()
                .nickname(ranking.getProfile().getNickname())
                .reviewCount(ranking.getReviewCount())
                .rank(String.valueOf(ranking.getRank()))
                .build();
    }
}
