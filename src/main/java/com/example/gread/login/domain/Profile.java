package com.example.gread.login.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String testResultCode; // 심리테스트 결과 (ex: 'TYPE_A')
    private String preferenceTags; // 선택 키워드 2개 저장

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // User 테이블의 PK를 참조
    private User user;
    
    @Builder
    public Profile(User user) {
        this.user = user;
    }
}