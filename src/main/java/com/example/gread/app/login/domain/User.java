package com.example.gread.app.login.domain;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
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

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "reader_type")
    private ReaderType readerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Column
    private String profileImageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    public void completeOnboarding(String nickname, ReaderType readerType) {
        this.readerType = readerType;
        this.role = Role.USER;

        if (this.profile != null) {
            this.profile.setNickname(nickname);
            this.profile.setTestResultCode(readerType.getTestResultCode());
        }
    }
}