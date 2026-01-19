package com.example.gread.global.exception.handler;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* DTO Validation (@RequestBody @Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException e) {
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION);
    }

    /* Param Validation (@RequestParam, @PathVariable) */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraint(ConstraintViolationException e) {
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION);
    }

    /* 비즈니스 예외 */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException e) {
        return ApiResponseTemplate.error(e.getErrorCode());
    }

    /* 나머지 */
    @ExceptionHandler(Exception.class)
    public Object handleEtc(Exception e) {
        return ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}
