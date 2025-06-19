package com.gate.houi.hoseoHoui.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return Map.of("authenticated", false);
        }
        return Map.of(
            "authenticated", true,
            "attributes", oauth2User.getAttributes(),
            "authorities", oauth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );
    }
}
