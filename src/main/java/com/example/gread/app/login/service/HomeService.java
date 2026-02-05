package com.example.gread.app.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
// 반드시 springframework 패키지의 Transactional을 임포트해야 readOnly 옵션을 쓸 수 있습니다.
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {


    public Map<String, Object> getRecommendData() {
        Map<String, Object> result = new HashMap<>();

        // 1. 추천 메시지 (예시)
        result.put("message", "오늘의 독서 추천입니다.");

        // 2. 추천 도서 리스트 (실제 DB 연동 전까지는 빈 리스트나 더미 데이터를 반환)
        // 나중에 BookRepository 등을 주입받아 실제 데이터를 넣으시면 됩니다.
        List<String> recommendedBooks = new ArrayList<>();
        result.put("books", recommendedBooks);

        // 3. 사용자 맞춤 정보 등 추가 가능
        result.put("bannerUrl", "https://example.com/banner.png");

        return result;
    }

    /* 추가로 필요한 홈 서비스 관련 로직들(예: 베스트셀러 조회 등)을
       이 아래에 작성하시면 됩니다.
    */
}