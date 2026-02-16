package com.example.gread.app.login.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token_value", nullable = false, length = 500)
    private String tokenValue;

    public RefreshToken(Long userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }

    public void updateValue(String newToken) {
        this.tokenValue = newToken;
    }
}