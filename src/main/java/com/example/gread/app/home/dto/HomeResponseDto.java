package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDto {
    private String nickname;
    private String readerType;
    private String readerTitle;
    private List<ReviewResponseDto> reviews;

    public static HomeResponseDto forGuest(List<ReviewResponseDto> reviews) {
        return HomeResponseDto.builder()
                .nickname("방문자")
                .readerType(null)
                .readerTitle(null)
                .reviews(reviews)
                .build();
    }
}