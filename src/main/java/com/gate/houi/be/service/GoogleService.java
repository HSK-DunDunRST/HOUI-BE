package com.gate.houi.be.service;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.dto.res.GoogleTokenResDto;
import com.gate.houi.be.dto.res.GoogleUserInfoResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService {

    private static final String GOOGLE_TOKEN_URL_HOST = "https://oauth2.googleapis.com";
    private static final String GOOGLE_USERINFO_URL_HOST = "https://www.googleapis.com/oauth2/v3";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * 구글 OAuth 토큰 교환 (authorization code → access token)
     */
    public String getAccessTokenFromGoogle(String code) {
        GoogleTokenResDto response = WebClient.create(GOOGLE_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED)))
                .bodyToMono(GoogleTokenResDto.class)
                .block();

        String accessToken = Objects.requireNonNull(response).getAccessToken();
        log.info("Google Access Token : {}", accessToken);
        return accessToken;
    }

    /**
     * 구글 사용자 정보 가져오기 (access token → userinfo)
     */
    public GoogleUserInfoResDto getUserInfo(String accessToken) {
        GoogleUserInfoResDto userInfo = WebClient.create(GOOGLE_USERINFO_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/userinfo")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED)))
                .bodyToMono(GoogleUserInfoResDto.class)
                .block();

        log.info("Google Authentication Successful");
        log.info("Google User Info : name={}, email={}", userInfo.getStudentName(),userInfo.getStudentEmail());

        return userInfo;
    }
}