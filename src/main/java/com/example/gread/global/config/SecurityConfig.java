package com.example.gread.global.config;

import com.example.gread.app.login.config.JwtAuthenticationFilter;
import com.example.gread.app.login.config.JwtAuthenticationEntryPoint; // 추가 확인 필요
import com.example.gread.app.login.config.TokenProvider;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.dto.TokenDto;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.login.service.AuthService;
import com.example.gread.app.login.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-resources/**", "/webjars/**",
                                "/", "/index.html"
                        ).permitAll()
                        // [병합] 비로그인 유저도 볼 수 있는 API 경로들을 모두 허용
                        .requestMatchers(
                                "/api/reviews/{reviewId}", "/api/books/{bookId}/reviews",
                                "/api/reviews/ranking/latest", "/api/reviews/ranking",
                                "/api/books/{bookId}/reviews/count",
                                "/api/home/**", "/api/feed/explore"
                        ).permitAll()
                        .requestMatchers("/api/login/**", "/oauth2/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            log.info("### 소셜 로그인 성공! 토큰을 발급하여 운영 도메인으로 리다이렉트합니다.");

                            String email = authentication.getName();
                            User user = userRepository.findByEmail(email)
                                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

                            // 토큰 생성 및 리프레시 토큰 DB 저장
                            TokenDto tokenDto = tokenProvider.createToken(user.getId());
                            authService.saveOrUpdateRefreshToken(user.getId(), tokenDto.getRefreshToken());

                            // [배포 설정] 운영 서버 도메인으로 리다이렉트 (로컬 테스트 시 localhost:3000으로 수정)
                            String targetUrl = UriComponentsBuilder.fromUriString("https://sss-gread.duckdns.org/onboarding")
                                    .queryParam("accessToken", tokenDto.getAccessToken())
                                    .queryParam("refreshToken", tokenDto.getRefreshToken())
                                    .build().toUriString();

                            response.sendRedirect(targetUrl);
                        })
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // [배포 설정] 로컬과 운영 서버 도메인 모두 허용하여 프론트 통신 보장
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://sss-gread.duckdns.org"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // 프론트에서 Header를 통해 토큰을 꺼낼 수 있도록 노출 설정
        configuration.setExposedHeaders(List.of("Authorization", "Authorization-Refresh"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}