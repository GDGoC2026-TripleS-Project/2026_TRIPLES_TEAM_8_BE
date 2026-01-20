package com.example.gread.app;

import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test", description = "테스트")
@RestController
@RequestMapping("/test") //공통응답포멧 테스트 코드입니다 필요없을시 삭제하고 시작하세요
public class TestController {
    @Tag(name = "test", description = "테스트")
    @GetMapping("/ok")
    public ResponseEntity<ApiResponseTemplate<String>> ok() {
        return ApiResponseTemplate.success(SuccessCode.OK, "hello");
    }
    @Tag(name = "test", description = "테스트")
    @GetMapping("/biz-error")
    public ResponseEntity<ApiResponseTemplate<Object>> bizError() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }

}

