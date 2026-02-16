package com.example.gread.global.exception.handler;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* DTO Validation (@Valid) - @Size(message="...")를 추출하여 반환 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, errorMessage);
    }

    /* Param Validation (@RequestParam) */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraint(ConstraintViolationException e) {
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION, e.getMessage());
    }

    /* 비즈니스 예외 (중복 닉네임 등) */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException e) {
        log.error("Business Error: {}", e.getErrorCode().getMessage());
        return ApiResponseTemplate.error(e.getErrorCode());
    }

    /* 그 외 모든 예외 (500 에러 상세 로그 확인용) */
    @ExceptionHandler(Exception.class)
    public Object handleEtc(Exception e) {
        log.error("Unknown Server Error: ", e); // 서버 로그에 실제 에러 원인을 찍어줍니다.
        return ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}