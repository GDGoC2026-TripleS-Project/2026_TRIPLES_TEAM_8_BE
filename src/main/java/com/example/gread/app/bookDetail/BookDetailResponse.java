package com.example.gread.app.bookDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDetailResponse {
    private Long book_id;
    private String title;
    private String author;
    private String publisher;
    private String keyword1;
    private String keyword2;

    // Entity를 DTO로 변환하는 생성자
    public BookDetailResponse(Book book) {
        this.book_id = book.getBookId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.keyword1 = book.getKeyword1();
        this.keyword2 = book.getKeyword2();
    }
}