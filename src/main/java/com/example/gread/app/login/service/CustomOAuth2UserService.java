package com.example.gread.app.login.service;

import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.Role;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    // === 이 메서드가 반드시 있어야 합니다! ===
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 구글 유저 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String sub = (String) attributes.get("sub");
        String googleName = (String) attributes.get("name");

        // 유저 저장 및 프로필 생성 (본인 담당 로직)
        User user = saveOrUpdate(email, sub, googleName);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes,
                "email"
        );
    }

    private User saveOrUpdate(String email, String sub, String googleName) {
        User user = userRepository.findByGoogleSub(sub)
                .orElseGet(() -> User.builder()
                        .email(email)
                        .googleSub(sub)
                        .role(Role.USER)
                        .build());
        userRepository.save(user);

        if (user.getProfile() == null) {
            Profile profile = new Profile();
            profile.setUser(user);
            profile.setNickname(googleName);
            profileRepository.save(profile);
            user.setProfile(profile);
        }
        return user;
    }
}