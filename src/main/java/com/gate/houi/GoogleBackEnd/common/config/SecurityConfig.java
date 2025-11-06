package com.gate.houi.GoogleBackEnd.common.config;

import com.gate.houi.be.common.security.JwtAuthenticationFilter;
import com.gate.houi.be.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        // 문서/스웨거 공개
                        .requestMatchers(
                                "/doc/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // 헬스 체크
                        .requestMatchers("/health").permitAll()

                        //구글/로컬/로그인
                        .requestMatchers(
                                "/api/v1/login/page/**",
                                "/api/v1/login/google/callback",
                                "/api/v1/oauth/google-login",
                                "/api/v1/oauth/local/signup",
                                "/api/v1/oauth/local/login",
                                "/api/v1/oauth/refresh"
                        ).permitAll()

                        // 정적 리소스 (이미지, CSS, JS, webjars 등)
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/webjars/**",
                                "/*.png",
                                "/static/**"
                        ).permitAll().anyRequest().authenticated()
                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(accessDeniedHandler()) // 접근 거부 핸들러 설정(new CustomAccessDeniedHandler())
//                        .authenticationEntryPoint(authenticationEntryPoint()) // 인증 실패 핸들러 설정(new CustomAuthenticationEntryPoint())
//                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가 (인증 처리)
//                .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class); // 예외 핸들러 필터를 JWT 필터 '앞'에 배치해 JWT 관련 예외를 감싸도록 함

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 일단 모든 출저 허용!!
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowCredentials(true); // 자격 증명 허용 (쿠키, 인증 정보 포함)
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}