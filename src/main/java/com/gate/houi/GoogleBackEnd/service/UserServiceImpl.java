package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.apiPayload.code.exception.BaseException;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.ErrorType;
import com.gate.houi.GoogleBackEnd.common.security.JwtTokenProvider;
import com.gate.houi.GoogleBackEnd.common.util.CookieUtil;
import com.gate.houi.GoogleBackEnd.converter.UserConverter;
import com.gate.houi.GoogleBackEnd.dto.req.UserReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.GoogleUserInfoResDto;
import com.gate.houi.GoogleBackEnd.dto.res.UserDtoRes;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Campus;
import com.gate.houi.GoogleBackEnd.entity.enums.Provider;
import com.gate.houi.GoogleBackEnd.entity.enums.UserRole;
import com.gate.houi.GoogleBackEnd.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    // 테스트 용 로그인 처리: 이메일로 유저 찾고 토큰 발급
    @Transactional(readOnly = true)
    public UserDtoRes.UserLoginRes login(HttpServletRequest request, HttpServletResponse response, UserReqDto.LoginReq loginDto) {
        if(loginDto == null) { throw new BaseException(ErrorType.MISSING_FILED_ERROR);}
        String email = loginDto.getEmail();

        UserEntity userEntity = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createAccessToken(userEntity.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getId());

        return UserConverter.signInRes(userEntity, accessToken, refreshToken, userEntity.getUserName());
    }


    // 로컬(이메일) 로그인 (쿠기x)
    @Transactional(readOnly = true)
    public UserDtoRes.UserLoginRes loginLocal(String email, String rawPassword) {

        // 구글 계정이면 로컬 로그인 불가
        userRepository.findByUserEmailAndOauthProvider(email, Provider.GOOGLE).ifPresent(u -> {
            throw new BaseException(ErrorType.LOCAL_LOGIN_FOR_GOOGLE_EMAIL);
        });

        UserEntity userEntity = userRepository.findByUserEmailAndOauthProvider(email, Provider.LOCAL)
                .orElseThrow(() -> new BaseException(ErrorType._UNAUTHORIZED));

        if (userEntity.getUserPwd() == null || !passwordEncoder.matches(rawPassword, userEntity.getUserPwd())) {
            throw new BaseException(ErrorType._UNAUTHORIZED);
        }

        String accessToken = jwtTokenProvider.createAccessToken(userEntity.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getId());
        return UserConverter.signInRes(userEntity, accessToken, refreshToken, userEntity.getUserName());
    }

    // 로컬(이메일) 로그인 (쿠키 포함)
    @Transactional(readOnly = true)
    public UserDtoRes.UserLoginRes loginLocalWeb(HttpServletRequest request, HttpServletResponse response,
                                                 String email, String rawPassword) {
        UserDtoRes.UserLoginRes res = loginLocal(email, rawPassword); // 기존 검증/AT·RT 발급 로직 재사용

        // 리프레시 토큰 쿠키 저장 (구글 웹과 동일)
        CookieUtil.deleteCookie(request, response, "refreshToken");
        CookieUtil.addCookie(response, "refreshToken", res.getRefreshToken(),
                JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_COOKIE);

        return res;
    }


    // 로컬(이메일) 회원가입
    public void signUpLocal(String email, String rawPassword,  String name) {
        if (email == null || email.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new BaseException(ErrorType._BAD_REQUEST);
        }

        // 이미 '구글 조직'로 존재하는 이메일이면 가입 불가
        userRepository.findByUserEmailAndOauthProvider(email, Provider.GOOGLE).ifPresent(u -> {
            throw new BaseException(ErrorType.EMAIL_REGISTERED_WITH_GOOGLE);
        });

        // 이미 LOCAL로 존재하면 중복
        if (userRepository.findByUserEmailAndOauthProvider(email, Provider.LOCAL).isPresent()) {
            throw new BaseException(ErrorType.EMAIL_REGISTERED_WITH_LOCAL);
        }

        UserEntity userEntity = UserEntity.builder()
                .userEmail(email)
                .userName(name)
                .userPwd(passwordEncoder.encode(rawPassword))
                .userCampus(null)
                .oauthProvider(Provider.LOCAL)
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(userEntity);
    }

    // 로그아웃 처리: 액세스 토큰 블랙리스트 추가 및 리프레시 토큰 삭제 (앱)
    @Override
    public void logout(String accessToken) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken();
        jwtTokenProvider.handleLogout(accessToken, refreshToken);
    }

    @Override
    public void logoutWeb(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken();
        jwtTokenProvider.handleLogout(accessToken, refreshToken);
        // Cookie 에 있는 RefreshToken 의 데이터를 value 0, 만료 0 으로 초기화
        CookieUtil.addCookie(response, "refreshToken", null, 0);
    }

    // 구글 조직 로그인 시 신규 회원가입 또는 기존 회원 조회
    public UserEntity googleSignup(GoogleUserInfoResDto userInfo) {
        log.info("googleSignup userInfo = {}, {}, {}", userInfo.getId(), userInfo.getName(), userInfo.getEmail());
        return userRepository.findByUserEmail(userInfo.getEmail())
                .orElseGet(() -> {

                    String studentId = userInfo.getEmail().split("@")[0];

                    UserEntity newUser = UserEntity.builder()
                            .userEmail(userInfo.getEmail())
                            .userName(userInfo.getName())
                            .studentId(studentId)
                            .userPwd(null)
                            .userCampus(Campus.ASAN)
                            .oauthProvider(Provider.GOOGLE)
                            .role(UserRole.STUDENT)
                            .build();
                    userRepository.save(newUser);
                    return newUser;
                });
    }

    // 구글 로그인 처리 후 토큰 발급(앱)
    public UserDtoRes.UserLoginRes googleLogin(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity) {
        String accessToken = jwtTokenProvider.createAccessToken(userEntity.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getId());

        return UserConverter.signInRes(userEntity, accessToken, refreshToken, userEntity.getUserName());
    }

    // 구글 로그인 처리 후 토큰 발급(웹-쿠키 추가)
    public UserDtoRes.UserLoginRes googleLoginWeb(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity) {
        String accessToken = jwtTokenProvider.createAccessToken(userEntity.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getId());

        // 리플레시 토큰 쿠키 저장
        CookieUtil.deleteCookie(request, response, "refreshToken");
        CookieUtil.addCookie(response, "refreshToken", refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_COOKIE);

        UserDtoRes.UserLoginRes res =
                UserConverter.signInRes(userEntity, accessToken, refreshToken, userEntity.getUserName());
        res.setRefreshToken(null); // 웹 응답에서 숨김
        return res;

    }

    @Override
    public UserDtoRes.UserInfoRes googleUserInfo(Long userId) {
        // 3. DB에서 사용자 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        if(userEntity.getUserCampus().toString().equals(Campus.ASAN.toString())) {
            // 4. DTO로 변환
            return UserDtoRes.UserInfoRes.builder()
                    .userName(userEntity.getUserName())
                    .studentId(userEntity.getStudentId())
                    .campusType("아산 캠퍼스")
                    .build();
        } else if(userEntity.getUserCampus().toString().equals(Campus.CHEONAN.toString())) {
            // 4. DTO로 변환
            return UserDtoRes.UserInfoRes.builder()
                    .userName(userEntity.getUserName())
                    .studentId(userEntity.getStudentId())
                    .campusType("천안 캠퍼스")
                    .build();
        }
        return UserDtoRes.UserInfoRes.builder()
                .userName(userEntity.getUserName())
                .studentId(userEntity.getStudentId())
                .build();
    }

    public UserDtoRes.UserLoginRes rotateTokensForApp(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BaseException(ErrorType.TOKEN_NOT_FOUND);
        }

        Long userId;
        try {
            // 서명/형태/만료 등 기본 파싱. 유효성은 아래 validateRefreshToken에서 한 번 더 교차검증.
            userId = jwtTokenProvider.getUserIdInToken(refreshToken);
        } catch (io.jsonwebtoken.JwtException e) {
            throw new BaseException(ErrorType.TOKEN_INVALID);
        }

        // Redis 저장 RT와의 일치 + 토큰 자체의 유효성
        if (!jwtTokenProvider.validateRefreshToken(refreshToken, userId)) {
            throw new BaseException(ErrorType.TOKEN_NOT_FOUND);
        }

        // 사용자 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        // 새 토큰 회전
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        // 앱 응답: RT 포함
        return UserConverter.signInRes(userEntity, newAccessToken, newRefreshToken, userEntity.getUserName());
    }

}
