package com.example.gread.app.bookDetail;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "curated_books_final")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    private String title;

    @Column(name = "original_title") // 이미지의 original_title 매핑
    private String originalTitle;

    private String author;
    private String publisher;

    @Column(name = "major_name") // 이미지의 major_name 매핑
    private String majorName;

    @Column(name = "minor_code") // 이미지의 minor_code 매핑
    private String minorCode;

    @Column(name = "minor_name") // 이미지의 minor_name 매핑
    private String minorName;

    private String keyword1;
    private String keyword2;

    @Column(name = "ai_analysis", columnDefinition = "TEXT") // 이미지의 ai_analysis 매핑
    private String aiAnalysis;
}