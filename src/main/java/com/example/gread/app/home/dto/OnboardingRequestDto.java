package com.example.gread.app.home.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OnboardingRequestDto {
    String email;
    @Size(min = 2, max=10, message = "닉네임을 2~10자로 입력해주세요")
    private String nickname;
    private String readerType;
    private String testResultCode;
}