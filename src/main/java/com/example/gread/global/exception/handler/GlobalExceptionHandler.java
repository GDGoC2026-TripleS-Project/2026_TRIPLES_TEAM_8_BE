package com.example.gread.global.exception.handler;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment env;

    public GlobalExceptionHandler(Environment env) {
        this.env = env;
    }

    /* =========================
       400: 요청/검증 관련
       ========================= */

    // DTO Validation (@RequestBody @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleValidation(MethodArgumentNotValidException e) {
        log.warn("[VALIDATION] {}", e.getMessage());

        // 필요하면 field error도 data로 내려줄 수 있음(지금은 최소로)
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    // Param Validation (@RequestParam, @PathVariable) with @Validated
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleConstraint(ConstraintViolationException e) {
        log.warn("[CONSTRAINT] {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    // @ModelAttribute / Query binding 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleBind(BindException e) {
        log.warn("[BIND] {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    // 타입 미스매치: ?page=abc (int 기대), /books/{id}에 문자 등
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("[TYPE_MISMATCH] {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    // 필수 RequestParam 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("[MISSING_PARAM] {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    // JSON 파싱 실패 / enum 값 오류 / 타입 변환 실패 등
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("[NOT_READABLE] {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, debugData(e));
    }

    /* =========================
       비즈니스 예외
       ========================= */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleBusiness(BusinessException e) {
        // 비즈니스 예외는 보통 warn 정도로 충분
        log.warn("[BUSINESS] code={}, message={}", e.getErrorCode().name(), e.getMessage());
        return ApiResponseTemplate.error(e.getErrorCode(), debugData(e));
    }

    /* =========================
       500: 처리되지 않은 예외
       ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleEtc(Exception e) {
        // 여기서 e를 같이 넘겨야 스택트레이스가 IntelliJ에 찍힘
        log.error("[UNHANDLED_EXCEPTION] {}", e.getMessage(), e);
        return ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_EXCEPTION, debugData(e));
    }

    /* =========================
       Debug data (local/dev only)
       ========================= */

    private Object debugData(Exception e) {
        if (!isLocalOrDev()) return null;

        // local/dev에서만 예외 타입/메시지를 data에 내려줌
        return Map.of(
                "exception", e.getClass().getName(),
                "detail", String.valueOf(e.getMessage())
        );
    }

    private boolean isLocalOrDev() {
        // active profile이 local/dev면 true
        String[] profiles = env.getActiveProfiles();
        return Arrays.stream(profiles).anyMatch(p ->
                p.equalsIgnoreCase("local") || p.equalsIgnoreCase("dev")
        );
    }
}
