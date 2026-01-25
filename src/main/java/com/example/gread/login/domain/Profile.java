package com.example.gread.login.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // User 테이블의 PK를 참조
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    private String testResultCode; // 8가지 독자 유형 결과 저장
    private String preferenceTags; // 선택 키워드 2개 저장
}