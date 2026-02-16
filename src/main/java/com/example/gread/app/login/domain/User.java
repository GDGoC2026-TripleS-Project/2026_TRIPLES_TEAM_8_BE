package com.example.gread.app.login.domain;

import com.example.gread.app.home.domain.ReaderType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "google_sub", nullable = false)
    private String googleSub;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    public void completeOnboarding(String nickname, ReaderType readerType) {
        this.role = Role.USER;
        if (this.profile != null) {
            this.profile.setNickname(nickname);
            this.profile.setReaderType(readerType);
            this.profile.setTestResultCode(readerType.getTestResultCode());
        }
    }

    public ReaderType getReaderType() {
        return (this.profile != null) ? this.profile.getReaderType() : null;
    }

    public void upgradeToUser() {
        this.role = Role.USER;
    }
}