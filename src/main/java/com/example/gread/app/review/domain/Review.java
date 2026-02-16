package com.example.gread.app.review.domain;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.User;
import com.example.gread.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.Format;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReviewColor reviewColor;
    
    @Column
    private String title;

    @Column
    private String reviewContent;

    @Column
    private String category;

    public Review(Profile profile, Book book, ReviewColor reviewColor, String reviewContent, String category) {
        this.profile = profile;
        this.book = book;
        this.reviewColor = reviewColor;
        this.reviewContent = reviewContent;
        this.category = category;
    }

    public void update(ReviewColor reviewColor, String reviewContent) {
        this.reviewColor = reviewColor;
        this.reviewContent = reviewContent;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
}
