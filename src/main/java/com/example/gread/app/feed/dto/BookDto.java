package com.example.gread.app.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private String thumbnailUrl;

    private String keyword1;
    private String keyword2;
}
