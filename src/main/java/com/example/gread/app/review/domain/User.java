package com.example.gread.app.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nickname;

    public User(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
