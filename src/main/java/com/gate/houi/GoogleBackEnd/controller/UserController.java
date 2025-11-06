package com.gate.houi.GoogleBackEnd.controller;

import com.gate.houi.GoogleBackEnd.apiPayload.ApiResponse;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.SuccessStatus;
import com.gate.houi.GoogleBackEnd.common.security.JwtTokenProvider;
import com.gate.houi.GoogleBackEnd.dto.req.TokenReqDto;
import com.gate.houi.GoogleBackEnd.dto.req.UserReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleUserInfoResDto;
import com.gate.houi.GoogleBackEnd.dto.res.UserDtoRes;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.service.GoogleService;
import com.gate.houi.GoogleBackEnd.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleService googleService;

    @Operation(summary = "구글 로그인(앱)", description = "앱에서 '구글 액세스 토큰'을 전달")
    @PostMapping("/google-login")
    public ApiResponse<UserDtoRes.UserLoginRes> googleLogin(@RequestBody @Valid TokenReqDto.AccessTokenReq request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("UserController -> Google Token : {}", request);
        GoogleUserInfoResDto userInfo = googleService.verifyAndParseIdToken(request.getIdToken());
        UserEntity user = userService.googleSignup(userInfo);
        return ApiResponse.onSuccess(userService.googleLogin(httpRequest, httpResponse, user));
    }

    @Operation(summary = "내 정보 조회(앱)", description = "JWT 액세스 토큰으로 사용자 인증 후 이름과 학번 반환")
    @GetMapping("/my-info")
    public ApiResponse<UserDtoRes.UserInfoRes> getMyInfo(HttpServletRequest request) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userService.googleUserInfo(userId));
    }

    @Operation(summary = "로그아웃(앱)", description = "액세스 토큰을 무효화하여 로그아웃")
    @PostMapping("/logout")
    public ApiResponse<SuccessStatus> logout() {
        String accessToken = jwtTokenProvider.resolveAccessToken();
        userService.logout(accessToken);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Operation(summary = "로컬 로그인(앱)", description = "이메일/비밀번호로 로그인하고 토큰 발급")
    @PostMapping("/local/login")
    public ApiResponse<UserDtoRes.UserLoginRes> loginLocal(
            @RequestBody @Valid UserReqDto.LoginReq req) {
        var res = userService.loginLocal(req.getEmail(), req.getPassword());
        return ApiResponse.onSuccess(res);
    }

    @Operation(summary = "로컬 회원가입(앱)", description = "이메일/비밀번호로 회원가입하고 토큰 발급")
    @PostMapping("/local/signup")
    public ApiResponse<UserDtoRes.UserLoginRes> signUpLocal(
            @RequestBody @Valid UserReqDto.SignUpReq req) {

        userService.signUpLocal(req.getEmail(), req.getPassword(), req.getName());
        // 가입 직후 자동 로그인처럼 토큰 발급
        var res = userService.loginLocal( req.getEmail(), req.getPassword());
        return ApiResponse.onSuccess(res);
    }

    @Operation(summary = "토큰 재발급(앱)", description = "리프레시 토큰을 사용하여 새로운 액세스/리프레시 토큰 발급")
    @PostMapping("/refresh")
    public ApiResponse<UserDtoRes.UserLoginRes> refresh() {
        // 앱은 헤더로 리플레시 토큰 확인, 액세스 토큰은 확인 x
        String refreshToken = jwtTokenProvider.resolveRefreshToken();
        var res = userService.rotateTokensForApp(refreshToken);
        return ApiResponse.onSuccess(res);
    }
}