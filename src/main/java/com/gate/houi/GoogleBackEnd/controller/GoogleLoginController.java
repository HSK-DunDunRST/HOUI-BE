package com.gate.houi.GoogleBackEnd.controller;

import com.gate.houi.GoogleBackEnd.apiPayload.ApiResponse;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleUserInfoResDto;
import com.gate.houi.GoogleBackEnd.dto.res.UserDtoRes;
import com.gate.houi.GoogleBackEnd.service.GoogleService;
import com.gate.houi.GoogleBackEnd.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class GoogleLoginController {

    private final GoogleService googleService;
    private final UserService userService;

    @GetMapping("/google/callback")
    public ApiResponse<UserDtoRes.UserLoginRes> callBack(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
        String accessToken = googleService.getIdTokenFromGoogle(code);
        GoogleUserInfoResDto userInfo = googleService.verifyAndParseIdToken(accessToken);

        return ApiResponse.onSuccess(userService.googleLoginWeb(request, response, userService.googleSignup(userInfo)));
    }
}
