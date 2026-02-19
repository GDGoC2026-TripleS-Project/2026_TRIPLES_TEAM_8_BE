package com.example.gread.app.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 프로필 정보 응답 DTO")
public class UserProfileDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "독서왕")
    private String nickname;

    @Schema(description = "독서 성향 타입 코드", example = "TYPE_A")
    private String readerType;

    @Schema(description = "독서 성향 타이틀", example = "감성 몰입형 독자")
    private String readerTitle;

    @Schema(description = "작성한 리뷰 개수", example = "5")
    private Long reviewCount;
}
