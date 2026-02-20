package com.example.gread.app.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MyFeedResponseDto {
    private List<Integer> majorCode;
    private List<FeedResponseDto> books;
}
