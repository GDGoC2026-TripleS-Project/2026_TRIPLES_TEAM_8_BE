package com.example.gread.app.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 프로필 응답")
public class UserProfileApiResponse {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    @Schema(description = "응답 코드", example = "OK")
    private String code;

    @Schema(description = "응답 메시지", example = "요청에 성공하였습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private UserProfileDto data;
}
