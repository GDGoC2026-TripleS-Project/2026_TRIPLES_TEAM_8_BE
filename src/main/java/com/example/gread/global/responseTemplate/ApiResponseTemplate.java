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

    private final int status;        // HTTP 상태 코드
    private final boolean success;   // 성공 여부
    private final String message;    // 메시지
    private final String code;       // 내부 코드
    private final T data;            // 실제 데이터

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

    /* ================= 실패 응답 ================= */

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

    public static <T> ResponseEntity<ApiResponseTemplate<T>> error(
            ErrorCode errorCode,
            T data
    ) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponseTemplate.<T>builder()
                        .status(errorCode.getHttpStatus().value())
                        .success(false)
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .data(data)
                        .build());
    }

}
