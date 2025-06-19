package com.gate.houi.hoseoHoui.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.gate.houi.hoseoHoui.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final CustomOAuth2UserService customOAuth2UserService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (REST API에서는 일반적으로 비활성화)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login/**").permitAll()
                .requestMatchers("/reception/**").authenticated() // 진료 접수는 인증 필요
                .requestMatchers("/history/**").authenticated() // 이용 내역은 인증 필요
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
            );
            
        return http.build();
    }

}
