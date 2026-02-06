package com.example.gread.global.responseTemplate;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonPropertyOrder({
        "status",
        "success",
        "code",
        "message",
        "data"
})
public class ApiResponseTemplate<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private final String code;
    private final T data;

    /* ================= 성공 응답 ================= */

    public static <T> ResponseEntity<ApiResponseTemplate<T>> success(
            SuccessCode successCode,
            T data
    ) {
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .body(ApiResponseTemplate.<T>builder()
                        .status(successCode.getHttpStatus().value())
                        .success(true)
                        .code(successCode.name())
                        .message(successCode.getMessage())
                        .data(data)
                        .build());
    }

    /* ================= 실패 응답 (기본) ================= */

    public static <T> ResponseEntity<ApiResponseTemplate<T>> error(
            ErrorCode errorCode
    ) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponseTemplate.<T>builder()
                        .status(errorCode.getHttpStatus().value())
                        .success(false)
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .data(null)
                        .build());
    }

    /* ================= 실패 응답 (커스텀 메시지 포함) ================= */
    public static <T> ResponseEntity<ApiResponseTemplate<T>> error(
            ErrorCode errorCode,
            String customMessage
    ) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponseTemplate.<T>builder()
                        .status(errorCode.getHttpStatus().value())
                        .success(false)
                        .code(errorCode.name())
                        .message(customMessage)
                        .data(null)
                        .build());
    }
}