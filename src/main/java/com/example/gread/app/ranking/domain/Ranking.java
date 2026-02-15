package com.example.gread.app.ranking.domain;

import com.example.gread.app.login.domain.Profile;
import com.example.gread.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ranking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Profile profile;

    @Column
    private long reviewCount;

    @Column(name = "ranking_position")
    private long rank;

    @Column
    private boolean top5;

    public Ranking(Profile profile, long reviewCount, long rank) {
        this.profile = profile;
        this.reviewCount = reviewCount;
        this.rank = rank;
    }

    // 리뷰 작성 시 reviewCount 증가
    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void updateRank(long rank) {
        this.rank = rank;
    }
}