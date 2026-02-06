package com.example.gread.app.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OnboardingRequestDto {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @Size(min = 2, max = 10, message = "닉네임을 2~10자로 입력해주세요")
    private String nickname;

    private String testResultCode; // 8가지 유형 코드 (예: 1010)

    private String readerType; // 유형 이름 (예: TYPE_A)

    private String preferenceTags; // 선택 키워드 (예: "태그1,태그2")
}