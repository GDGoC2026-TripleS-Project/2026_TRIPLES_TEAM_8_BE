package com.example.gread.app.bookDetail;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "openai_books") // 1. 테이블 이름 일치시키기
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") // 2. DB 컬럼명과 매핑
    private Long bookId;

    private String title;
    private String author;
    private String publisher;

    // 추가된 컬럼들 반영
    private String major_category;
    private String minor_code;
    private String minor_name;

    private String keyword1;
    private String keyword2;
}