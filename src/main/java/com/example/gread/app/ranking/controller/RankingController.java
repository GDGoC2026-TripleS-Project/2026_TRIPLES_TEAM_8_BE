package com.example.gread.app.ranking.controller;

import com.example.gread.app.ranking.dto.RankingResDto;
import com.example.gread.app.ranking.service.RankingService;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.exception.BusinessException;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

        return ApiResponseTemplate.success(SuccessCode.RANKING_LIST_OK, top5);
    }

    @Operation(summary = "내 랭킹 조회", description = "자신의 순위를 조회합니다.(만약 본인의 리뷰수가 0개라면 0위로 표시됩니다.")
    @GetMapping("/reviews/ranking/me")
    public ResponseEntity<ApiResponseTemplate<RankingResDto>> getMyRanking(
            Authentication authentication
    ) {

        Long profileId = Long.parseLong(authentication.getName());

        if (authentication == null) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        RankingResDto myRanking = rankingService.getMyRanking(profileId);

        return ApiResponseTemplate.success(SuccessCode.RANKING_ME_OK, myRanking);
    }

    @Operation(summary = "유저 리뷰 개수 조회", description = "유저가 작성한 리뷰의 개수를 조회합니다.")
    @GetMapping("/reviews/ranking/me/count")
    public ResponseEntity<ApiResponseTemplate<Long>> getReviewCountByBook(
            Authentication authentication
    ) {

        Long profileId = Long.parseLong(authentication.getName());

        if (authentication == null) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        long reviewCountByBook = rankingService.getReviewCountByProfileId(profileId);

        return ApiResponseTemplate.success(SuccessCode.RANKING_REVIEW_COUNT_OK, reviewCountByBook);
    }
}
