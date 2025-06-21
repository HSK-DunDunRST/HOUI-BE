package com.gate.houi.hoseoHoui.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    
    // 토큰 검증이 필요 없는 경로 설정
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
        "/", "/login", "/oauth2", "/oauth"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("[JWT 필터] 요청 URI: " + request.getRequestURI());
        
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            System.out.println("[JWT 필터] Authorization 헤더가 존재합니다.");
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtTokenProvider.extractUsername(jwt);
                System.out.println("[JWT 필터] 토큰에서 사용자 ID를 추출했습니다: " + username);
            } catch (Exception e) {
                System.out.println("[JWT 필터] 토큰 검증 실패: " + e.getMessage());
            }
        } else {
            System.out.println("[JWT 필터] Authorization 헤더가 없거나 Bearer 형식이 아닙니다.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("[JWT 필터] 보안 컨텍스트에 인증 정보가 없습니다. 사용자를 로드합니다.");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                System.out.println("[JWT 필터] 토큰이 유효합니다. 인증 처리를 진행합니다.");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("[JWT 필터] 보안 컨텍스트에 인증 정보를 설정했습니다.");
            } else {
                System.out.println("[JWT 필터] 토큰이 유효하지 않습니다.");
            }
        }
        
        filterChain.doFilter(request, response);
    }
}