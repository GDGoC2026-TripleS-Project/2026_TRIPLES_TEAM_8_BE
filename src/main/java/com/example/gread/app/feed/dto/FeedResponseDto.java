package com.example.gread.app.feed.dto;

import com.example.gread.app.bookDetail.domain.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "피드 도서 정보 응답 DTO")
public class FeedResponseDto {

    @Schema(description = "도서 ID", example = "1")
    private Long bookId;

    @Schema(description = "도서 제목", example = "어린 왕자")
    private String title;

    @Schema(description = "저자", example = "앙투안 드 생텍쥐페리")
    private String author;

    @Schema(description = "출판사", example = "문학동네")
    private String publisher;

    @Schema(description = "대분류 카테고리 (majorName)", example = "창작")
    private String majorName;

    @Schema(description = "키워드 1", example = "철학")
    private String keyword1;

    @Schema(description = "키워드 2", example = "동심")
    private String keyword2;

    @Schema(description = "리뷰 개수", example = "15")
    private long reviewCount;

    public static FeedResponseDto of(Book book, long reviewCount) {
        return FeedResponseDto.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .majorName(book.getMajorName())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .reviewCount(reviewCount)
                .build();
    }
}
