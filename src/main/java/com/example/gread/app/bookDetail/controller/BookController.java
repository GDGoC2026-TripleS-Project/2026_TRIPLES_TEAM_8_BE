package com.example.gread.app.bookDetail.controller;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.bookDetail.service.BookService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTemplate<BookDetailResponse>> getBookDetail(@PathVariable Long id) {
        BookDetailResponse response = bookService.getBookById(id);

        // 팀의 공통 응답 템플릿인 ApiResponseTemplate.success()를 사용하도록 수정
        return ApiResponseTemplate.success(SuccessCode.GET_BOOK_SUCCESS, response);
    }
}