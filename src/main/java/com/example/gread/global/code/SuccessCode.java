package com.example.gread.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {


    OK(HttpStatus.OK, "요청 성공"),
    UPDATE_SUCCESS(HttpStatus.OK, "정보 업데이트 성공"),
    DELETE_SUCCESS(HttpStatus.OK, "탈퇴가 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}