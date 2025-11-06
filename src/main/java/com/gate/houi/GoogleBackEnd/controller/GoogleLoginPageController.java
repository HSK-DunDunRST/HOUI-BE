package com.gate.houi.GoogleBackEnd.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1/login")
public class GoogleLoginPageController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Operation(summary = "구글 로그인 페이지", description = "구글 로그인 화면으로 리다이렉트")
    @GetMapping("/page")
    public String loginPage() {
        log.info("구글 리디렉션 URI: {}", googleRedirectUri);
        String location = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code"
                + "&scope=https://www.googleapis.com/auth/userinfo.email+profile";

        return "redirect:" + location;
    }
}