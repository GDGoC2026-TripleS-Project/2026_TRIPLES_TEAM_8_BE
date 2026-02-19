package com.example.gread.global.config;

import com.example.gread.app.login.config.JwtAuthenticationFilter;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.app.login.service.CustomOAuth2UserService;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.login.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@org.springframework.core.annotation.Order(1)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${spring.security.front-redirect-uri:https://sss-gread.duckdns.org}")
    private String frontRedirectUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login/**", "/oauth2/**", "/login/oauth2/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                //로그인~
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            org.springframework.security.oauth2.core.user.OAuth2User oAuth2User =
                                    (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();

                            String email = (String) oAuth2User.getAttributes().get("email");

                            System.out.println(">>> OAuth 성공! 구글 이메일: " + email);

                            User user = userRepository.findByEmail(email)
                                    .orElseGet(() -> {
                                        System.out.println(">>> [경고] DB에 유저가 없습니다. 임시 유저를 찾거나 에러를 발생시킵니다.");
                                        return null;
                                    });

                            if (user == null) {
                                response.sendRedirect(frontRedirectUri + "/login?error=user_not_found");
                                return;
                            }

                            String authCode = authService.generateAuthCode(user.getId());
                            String targetUrl = frontRedirectUri + "/callback?code=" + authCode;

                            System.out.println(">>> 최종 리다이렉트 주소: " + targetUrl);
                            response.sendRedirect(targetUrl);
                        })
                );
                //~로그인
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}