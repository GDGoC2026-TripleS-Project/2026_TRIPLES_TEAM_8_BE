package com.example.gread.app.bookDetail.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "curated_books_final") // 실제 테이블명 매핑
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") // DB: book_id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "original_title") // DB: original_title
    private String originalTitle;

    private String author;
    private String publisher;

    @Column(name = "major_name") // DB: major_name -> majorName 필드와 매핑
    private String majorName;

    @Column(name = "minor_code") // DB: minor_code
    private String minorCode;

    @Column(name = "minor_name") // DB: minor_name
    private String minorName;

    private String keyword1;
    private String keyword2;

    @Column(name = "ai_analysis", columnDefinition = "TEXT") // DB: ai_analysis 매핑
    private String aiAnalysis;
}