package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.dto.res.GoogleUserInfoResDto;
import com.gate.houi.be.dto.res.UserDtoRes;
import com.gate.houi.be.service.GoogleService;
import com.gate.houi.be.service.UserService;
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
        String accessToken = googleService.getAccessTokenFromGoogle(code);
        GoogleUserInfoResDto userInfo = googleService.getUserInfo(accessToken);

        return ApiResponse.onSuccess(userService.googleLoginWeb(request, response, userService.googleSignup(userInfo)));
    }
}
