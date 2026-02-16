package com.example.gread.app.login.domain;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.home.domain.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter // 업데이트를 위해 추가
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "google_sub", nullable = false)
    private String googleSub;

    // 닉네임 대신 구글 기본 이름을 저장 (중복 허용)
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "reader_type")
    private ReaderType readerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // mappedBy 확인
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public void updateOnboarding(String newNickname, ReaderType readerType) {
        this.readerType = readerType;
        if (this.profile != null) {
            this.profile.setNickname(newNickname);
        }
    }
}