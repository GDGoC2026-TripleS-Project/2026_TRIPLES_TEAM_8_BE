package com.example.gread.login.controller;

import com.example.gread.login.domain.Book;
import com.example.gread.login.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    /**
     * 홈 화면 포스트잇 조회 (최초 진입 및 새로고침 공용)
     */
    @GetMapping("/postits/{userId}")
    public List<Book> getHomePostits(@PathVariable Long userId) {
        // HomeService에서 무작위로 섞인 5개의 도서(포스트잇 정보)를 가져옴
        return homeService.getRandomPostitsByContent(userId);
    }

    // 햄버거 바 이동을 위한 빈 엔드포인트
    @GetMapping("/search-feed")
    public String goSearchFeed() { return "도서 찾기 피드로 이동"; }

    @GetMapping("/ranking")
    public String goRanking() { return "랭킹 페이지로 이동"; }

    @GetMapping("/mypage")
    public String goMyPage() { return "마이페이지로 이동"; }
}