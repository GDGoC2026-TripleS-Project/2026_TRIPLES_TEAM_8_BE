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

    // 추가된 도서 상세 조회 성공 코드
    GET_BOOK_SUCCESS(HttpStatus.OK, "도서 상세 정보 조회 성공"),

    // 리뷰 관련
    REVIEW_OK(HttpStatus.OK, "리뷰 조회 성공"),
    REVIEW_LIST_OK(HttpStatus.OK, "리뷰 목록 조회 성공"),
    REVIEW_COUNT_OK(HttpStatus.OK, "리뷰 개수 조회 성공"),
    REVIEW_CREATED(HttpStatus.CREATED, "리뷰 작성 성공"),
    REVIEW_UPDATED(HttpStatus.OK, "리뷰 수정 성공"),
    REVIEW_DELETED(HttpStatus.OK, "리뷰 삭제가 완료되었습니다."),

    // 랭킹 관련
    RANKING_LIST_OK(HttpStatus.OK, "상위 5위 랭킹 조회 성공"),
    RANKING_ME_OK(HttpStatus.OK, "내 랭킹 조회 성공"),
    RANKING_REVIEW_COUNT_OK(HttpStatus.OK, "내 리뷰 개수 조회 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
