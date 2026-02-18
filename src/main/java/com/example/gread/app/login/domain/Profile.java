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
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "preference_tags")
    private String preferenceTags;

    @Enumerated(EnumType.STRING)
    @Column(name = "reader_type")
    private ReaderType readerType;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}
