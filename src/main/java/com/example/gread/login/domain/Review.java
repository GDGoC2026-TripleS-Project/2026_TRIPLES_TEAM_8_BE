package com.example.gread.login.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ConnectionBuilder;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    // Review.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // DB 외래키 컬럼명
    private User user; // <--- 이 이름이 'user'여야 User 엔티티의 mappedBy="user"와 연결됩니다.

    public static ConnectionBuilder builder() {
        return null;
    }
}