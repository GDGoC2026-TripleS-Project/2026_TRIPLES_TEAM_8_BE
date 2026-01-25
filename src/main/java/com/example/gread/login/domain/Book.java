package com.example.gread.login.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    private String isbn; // 도서 고유 번호

    @Column(columnDefinition = "TEXT")
    private String content; // 포스트잇에 보여줄 상세 내용
    private String category; // 8개 중 하나의 카테고리 정보
}