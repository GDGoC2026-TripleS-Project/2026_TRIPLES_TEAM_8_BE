package com.example.gread.app.bookDetail.controller;

import com.example.gread.app.bookDetail.dto.BookDetailResponse;
import com.example.gread.app.bookDetail.service.BookService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Detail", description = "도서 상세 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "도서 상세 조회", description = "도서 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTemplate<BookDetailResponse>> getBookDetail(
            @Parameter(description = "도서 고유 ID", example = "1")
            @PathVariable Long id) {

        BookDetailResponse response = bookService.getBookById(id);

        return ApiResponseTemplate.success(SuccessCode.GET_BOOK_SUCCESS, response);
    }
}