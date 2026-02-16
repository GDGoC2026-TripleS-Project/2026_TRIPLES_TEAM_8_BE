package com.example.gread.app.bookDetail.dto;

import com.example.gread.app.bookDetail.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder // 빌더 패턴 적용
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 생성자 추가
public class BookDetailResponse {
    private Long bookId;      // 자바 카멜케이스(camelCase) 적용 권장
    private String title;
    private String author;
    private String publisher;
    private String keyword1;
    private String keyword2;

    /**
     * Entity를 DTO로 변환하는 정적 팩토리 메서드
     * 서비스 레이어에서 BookDetailResponse.from(book) 형태로 사용합니다.
     */
    public static BookDetailResponse from(Book book) {
        return BookDetailResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .keyword1(book.getKeyword1())
                .keyword2(book.getKeyword2())
                .build();
    }
}