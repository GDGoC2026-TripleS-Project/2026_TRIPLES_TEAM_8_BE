package com.example.gread.app.login.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId; // 유저 PK를 ID로 사용 (유저당 하나의 리프레시 토큰만 유지)

    @Column(name = "token_value", nullable = false)
    private String tokenValue;

    public void updateValue(String newToken) {
        this.tokenValue = newToken;
    }
}