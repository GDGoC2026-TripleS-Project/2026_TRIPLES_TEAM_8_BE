package com.example.gread.app.ranking.controller;

import com.example.gread.app.ranking.dto.RankingResDto;
import com.example.gread.app.ranking.service.RankingService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "랭킹", description = "랭킹 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹 조회", description = "1위부터 5위까지의 유저들을 조회합니다.")
    @GetMapping("/reviews/ranking")
    public ResponseEntity<ApiResponseTemplate<List<RankingResDto>>> getTop5() {

        List<RankingResDto> top5 = rankingService.getTop5();

        return ApiResponseTemplate.success(SuccessCode.OK, top5);
    }

    @Operation(summary = "내 랭킹 조회", description = "자신의 순위를 조회합니다.")
    @GetMapping("/reviews/ranking/{userId}")
    public ResponseEntity<ApiResponseTemplate<RankingResDto>> getMyRanking(
            @PathVariable Long userId
    ) {

        RankingResDto myRanking = rankingService.getMyRanking(userId);

        return ApiResponseTemplate.success(SuccessCode.OK, myRanking);
    }
}
