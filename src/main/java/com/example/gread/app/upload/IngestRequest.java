package com.example.gread.app.upload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngestRequest {

    // ✅ seed 목록을 비우면 서버 기본 seed 사용
    private List<String> seeds;

    @Min(1)
    private int fromPage = 1;

    @Min(1)
    private int toPage = 3; // 처음엔 작게

    @Min(1) @Max(100)
    private int pageSize = 50;

    // 도서만 저장할지
    private boolean booksOnly = true;

    // paramData가 비어있을 때 저장할 category 값
    private String category = "도서";
}

