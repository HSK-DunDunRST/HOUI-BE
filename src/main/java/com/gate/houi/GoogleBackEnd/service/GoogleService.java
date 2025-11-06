package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.apiPayload.code.exception.BaseException;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.ErrorType;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleTokenResDto;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleUserInfoResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService {

    private static final String GOOGLE_TOKEN_URL_HOST = "https://oauth2.googleapis.com";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * 구글 OAuth 토큰 교환 (authorization code → id_token)
     */
    public String getIdTokenFromGoogle(String code) {
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

        return Objects.requireNonNull(response).getIdToken();
    }

    /**
     * 구글 ID 토큰 검증 및 사용자 정보 추출
     */
    public GoogleUserInfoResDto verifyAndParseIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();   // Google user ID
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                log.info("Google ID Token verified: userId={}, email={}", userId, email);

                return GoogleUserInfoResDto.builder()
                        .id(userId)
                        .email(email)
                        .name(name)
                        .build();

            } else {
                throw new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED);
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new BaseException(ErrorType.GOOGLE_AUTHENTICATION_FAILED);
        }
    }
}