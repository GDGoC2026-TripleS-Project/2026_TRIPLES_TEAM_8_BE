package com.example.gread.app.mypage.controller;

import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileApiResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
@Tag(name = "MyPage", description = "내 정보 및 내가 쓴 리뷰 API")
public class MyPageController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자 내 프로필 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<UserProfileDto>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        return ApiResponseTemplate.success(SuccessCode.OK, userService.getMyProfile(Long.parseLong(userId)));
    }

    @Operation(summary = "내 정보 수정", description = "내 닉네임, 프로필 이미지 URL 등을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileApiResponse.class)))
    })
    @PutMapping
    public ResponseEntity<ApiResponseTemplate<UserProfileDto>> updateMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @RequestBody UpdateUserRequest request) {
        return ApiResponseTemplate.success(SuccessCode.OK, userService.updateMyProfile(Long.parseLong(userId), request));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 회원 탈퇴를 처리한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping
    public ResponseEntity<ApiResponseTemplate<Void>> deleteMyAccount(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        userService.deleteMyAccount(Long.parseLong(userId));
        return ApiResponseTemplate.success(SuccessCode.OK, null);
    }
}
