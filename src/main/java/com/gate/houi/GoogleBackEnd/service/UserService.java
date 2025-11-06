package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.dto.req.UserReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleUserInfoResDto;
import com.gate.houi.GoogleBackEnd.dto.res.UserDtoRes;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService {
    UserDtoRes.UserLoginRes loginLocal(String email, String rawPassword);
    void signUpLocal(String email, String rawPassword, String name);
    UserDtoRes.UserLoginRes loginLocalWeb(HttpServletRequest request, HttpServletResponse response, String email, String rawPassword);
    UserDtoRes.UserLoginRes login(HttpServletRequest request, HttpServletResponse response, UserReqDto.LoginReq loginDto);
    void logout(String accessToken);
    void logoutWeb(HttpServletRequest request, HttpServletResponse response, String accessToken);
    UserEntity googleSignup(GoogleUserInfoResDto userInfo);
    UserDtoRes.UserLoginRes googleLogin(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity);
    UserDtoRes.UserLoginRes googleLoginWeb(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity);
    UserDtoRes.UserInfoRes googleUserInfo(Long userId);
    UserDtoRes.UserLoginRes rotateTokensForApp(String refreshToken);
}
