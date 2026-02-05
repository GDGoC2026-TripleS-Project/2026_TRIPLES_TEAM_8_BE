package com.example.gread.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 401 Unauthorized

    // 403 Forbidden

    // 404 Not Found
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    BOOK_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 책을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    // 409 Conflict

    // 500 Internal Server Error

    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");

    private final HttpStatus httpStatus;
    private final String message;
}
