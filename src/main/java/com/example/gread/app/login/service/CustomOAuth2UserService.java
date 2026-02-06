package com.example.gread.app.login.service;

import com.example.gread.app.login.config.TokenProvider;
import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.Role;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String sub = (String) attributes.get("sub");
        String googleName = (String) attributes.get("name");

        User user = saveOrUpdate(email, sub, googleName);

        //test용 토큰 발급
        var tokenDto = tokenProvider.createToken(user.getId());
        authService.saveOrUpdateRefreshToken(user.getId(), tokenDto.getRefreshToken());
        log.info("=============================================");
        log.info("### 테스트용 토큰 발급 완료");
        log.info("AccessToken: {}", tokenDto.getAccessToken());
        log.info("RefreshToken: {}", tokenDto.getRefreshToken());
        log.info("=============================================");
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
                        .name(googleName)
                        .role(Role.USER)
                        .build());

        user.setName(googleName);
        userRepository.save(user);


        if (user.getProfile() == null) {
            String tempNickname = "GUEST_" + java.util.UUID.randomUUID().toString().substring(0, 8);

            Profile profile = Profile.builder()
                    .user(user)
                    .nickname(tempNickname)
                    .build();

            profileRepository.save(profile);
            user.setProfile(profile);
        }
        return user;
    }
}