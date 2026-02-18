package com.example.gread.app.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OnboardingRequestDto {

    @Size(min = 2, max = 10, message = "닉네임을 2~10자로 입력해주세요")
    private String nickname;

    private String readerType; // 유형 이름 (예: TYPE_A)

    private String preferenceTags; // 선택 키워드 (예: "태그1,태그2")
}
