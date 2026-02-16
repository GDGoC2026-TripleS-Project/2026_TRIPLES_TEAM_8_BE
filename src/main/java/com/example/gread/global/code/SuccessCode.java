package com.example.gread.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    /* ================= 성공 코드 정의 ================= */
    OK(HttpStatus.OK, "요청 성공"),
    UPDATE_SUCCESS(HttpStatus.OK, "정보 업데이트 성공"),
    DELETE_SUCCESS(HttpStatus.OK, "탈퇴가 완료되었습니다."),

    // 추가된 도서 상세 조회 성공 코드
    GET_BOOK_SUCCESS(HttpStatus.OK, "도서 상세 정보 조회 성공");

    private final HttpStatus httpStatus;
    private final String message;
}