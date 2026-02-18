package com.example.gread.global.config;

import com.example.gread.app.login.config.JwtAuthenticationFilter;
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
    // [중요] 빌드 에러 방지를 위해 에러 로그상 확인된 정확한 위치의 클래스를 주입받거나,
    // 클래스가 없다면 이 필드와 exceptionHandling 설정을 제거해야 합니다.
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
                        // [유지] 비로그인 권한 허용 경로 (운영에 필수)
                        .requestMatchers(
                                "/api/reviews/{reviewId}", "/api/books/{bookId}/reviews",
                                "/api/reviews/ranking/latest", "/api/reviews/ranking",
                                "/api/books/{bookId}/reviews/count"
                        ).permitAll()
                        .requestMatchers("/api/login/**", "/oauth2/**").permitAll()
                        .requestMatchers("/api/home/**", "/api/feed/explore").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            log.info("### 소셜 로그인 성공! 운영 도메인으로 토큰 리다이렉트를 시작합니다.");

                            String email = authentication.getName();
                            User user = userRepository.findByEmail(email)
                                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

                            TokenDto tokenDto = tokenProvider.createToken(user.getId());
                            authService.saveOrUpdateRefreshToken(user.getId(), tokenDto.getRefreshToken());

                            // [병합] 운영 도메인(HTTPS)을 기본으로 하되, 토큰 파라미터를 명확히 전달
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

        // [병합] 로컬 개발 환경과 실제 운영 서버 도메인 모두 허용
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://sss-gread.duckdns.org"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Authorization-Refresh"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}