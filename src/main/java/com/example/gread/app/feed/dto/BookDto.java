package com.example.gread.app.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> keywords;
}
