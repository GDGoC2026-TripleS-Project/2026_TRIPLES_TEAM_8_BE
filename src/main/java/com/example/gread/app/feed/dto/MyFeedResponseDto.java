package com.example.gread.app.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MyFeedResponseDto {
    private List<String> majorCode; // 타입을 List<String>으로 변경
    private List<FeedResponseDto> books;
}
