package com.example.gread.app.login.domain;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @Column(name = "profile_id")
    private Long id; // User의 ID를 그대로 사용하기 위해 @GeneratedValue 제거

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // User의 PK(user_id)를 Profile의 PK(profile_id)로 매핑 (공유 PK 전략)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "test_result_code")
    private String testResultCode;

    @Column(name = "preference_tags")
    private String preferenceTags;

    @Enumerated(EnumType.STRING)
    @Column(name = "reader_type")
    private ReaderType readerType;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}