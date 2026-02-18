package com.example.gread.app.feed.dto;

import com.example.gread.app.bookDetail.domain.Book;
import lombok.*;

@Getter @Builder @AllArgsConstructor
public class FeedResponse {
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private String keyword1;
    private String keyword2;
    private long reviewCount; // 리뷰 개수 추가

    public static FeedResponse from(Book book, long reviewCount) {
        return FeedResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .reviewCount(reviewCount)
                .build();
    }
}
