package com.gate.houi.backend.security;

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

import com.gate.houi.backend.data.enumType.ErrorType;
import com.gate.houi.backend.exception.BaseException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // OAuth 관련 경로는 인증 체크에서 제외
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/oauth/") || requestPath.contains("/login/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        
        // Authorization 헤더가 없으면 다음 필터로 넘김
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 추출 및 검증 로직
        String jwt = authorizationHeader.substring(7);
        
        try {
            String userId = jwtTokenProvider.extractUsername(jwt);
            
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                
                if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                            
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (BaseException baseException) {
            throw new BaseException(ErrorType.TOKEN_EXPIRED.getErrorCode(), ErrorType.TOKEN_EXPIRED.getErrorMessage());
        } catch (Exception e) {
            throw new BaseException(ErrorType.TOKEN_VALIDATION_FAILED.getErrorCode(), ErrorType.TOKEN_VALIDATION_FAILED.getErrorMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}