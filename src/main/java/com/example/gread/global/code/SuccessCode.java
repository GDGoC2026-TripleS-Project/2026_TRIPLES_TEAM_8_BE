package com.example.gread.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {


    OK(HttpStatus.OK, "요청 성공"),
    UPDATE_SUCCESS(HttpStatus.OK, "정보 업데이트 성공"),
    DELETE_SUCCESS(HttpStatus.OK, "탈퇴가 완료되었습니다."),

    // 리뷰 관련
    REVIEW_OK(HttpStatus.OK, "리뷰 조회 성공"),
    REVIEW_LIST_OK(HttpStatus.OK, "리뷰 목록 조회 성공"),
    REVIEW_COUNT_OK(HttpStatus.OK, "리뷰 개수 조회 성공"),

    REVIEW_CREATED(HttpStatus.CREATED, "리뷰 작성 성공"),

    REVIEW_UPDATED(HttpStatus.OK, "리뷰 수정 성공"),

    REVIEW_DELETED(HttpStatus.OK, "리뷰 삭제가 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}