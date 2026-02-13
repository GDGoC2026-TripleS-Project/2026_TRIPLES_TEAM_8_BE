package com.example.gread.app.mypage.controller;

import com.example.gread.app.mypage.dto.ReviewDto;
import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileDto;
import com.example.gread.app.mypage.service.UserService;
import com.example.gread.global.code.SuccessCode;
import com.example.gread.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@Tag(name = "MyPage", description = "내 정보 및 내가 쓴 리뷰 API")
public class MyPageController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자 내 프로필 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<UserProfileDto>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "username") String email) {
        return ApiResponseTemplate.success(SuccessCode.OK, userService.getMyProfile(email));
    }

    @Operation(summary = "내 정보 수정", description = "내 닉네임, 프로필 이미지 URL 등을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @PutMapping
    public ResponseEntity<ApiResponseTemplate<UserProfileDto>> updateMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody UpdateUserRequest request) {
        return ApiResponseTemplate.success(SuccessCode.OK, userService.updateMyProfile(email, request));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 회원 탈퇴를 처리한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping
    public ResponseEntity<ApiResponseTemplate<Void>> deleteMyAccount(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "username") String email) {
        userService.deleteMyAccount(email);
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }

    @Operation(summary = "내가 쓴 리뷰 목록 조회", description = "내가 작성한 리뷰 목록을 페이징 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseTemplate.class)))
    })
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponseTemplate<Page<ReviewDto>>> getMyReviews(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "username") String email,
            @Parameter(description = "페이지 번호")
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponseTemplate.success(SuccessCode.OK, userService.getMyReviews(email, pageable));
    }
}
