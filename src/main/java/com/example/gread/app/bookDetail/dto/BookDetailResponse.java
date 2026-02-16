package com.example.gread.app.bookDetail.dto;

import com.example.gread.app.bookDetail.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailResponse {
    private Long bookId;
    private String title;
    private String originalTitle;
    private String author;
    private String publisher;
    private String majorName;
    private String minorCode;
    private String minorName;
    private String keyword1;
    private String keyword2;
    private String aiAnalysis;
    long reviewCount;

    /**
     * Entity의 모든 컬럼 데이터를 DTO로 변환
     */
    public static BookDetailResponse from(Book book,long reviewCount) {
        return BookDetailResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .originalTitle(book.getOriginalTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .majorName(book.getMajorName())
                .minorCode(book.getMinorCode())
                .minorName(book.getMinorName())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .aiAnalysis(book.getAiAnalysis())
                .reviewCount(book.getReviews().size())
                .build();
    }
}