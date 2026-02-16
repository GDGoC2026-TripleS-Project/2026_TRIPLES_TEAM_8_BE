package com.example.gread.app.bookDetail.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    private String author;
    private String publisher;

    // 알라딘 API 및 상세 페이지용
    @Column(columnDefinition = "TEXT")
    private String description; // 줄거리 (알라딘 description/fullDescription 저장용)

    private String coverUrl;

    private Integer categoryCode;

    @Column(name = "major_category")
    private String majorCategory;

    private String minorCode;
    private String minorName;

    private String keyword1;
    private String keyword2;

    public void updateCategory(Integer newCategoryCode) {
        this.categoryCode = newCategoryCode;
    }
}