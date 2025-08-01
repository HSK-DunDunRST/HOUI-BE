# HOUI Backend - 호서대학교 보건진료센터 진료 접수 시스템

호서대학교 보건진료센터 진료접수 및 처방내역 서비스의 백엔드 API 서버입니다.  

## 핵심 기능
### 1. 사용자 관리 및 인증
- **Google OAuth2.0 로그인**: 호서대학교 이메일(@hoseo.edu)을 통한 안전한 로그인  
- **JWT 기반 인증**: Access Token + Refresh Token을 활용한 보안 인증
- **사용자 프로필 관리**: 학생 정보 조회 및 이용 통계 확인

### 2. 진료 접수 관리
- **온라인 진료 접수**: 증상 입력 후 간편한 진료 접수 등록
- **접수(처방) 내역 조회**: 개인별 진료 접수(처방) 내역 확인
- **대기열 관리**: 실시간 대기 인원 수 확인 및 순번 안내

### 3. 공지사항 시스템
- **공지사항 조회**: 최신 공지사항 및 전체 공지사항 확인
- **공지사항 CRUD**: 관리자를 위한 공지사항 등록, 수정, 삭제 기능

### 4. 이용 내역 관리
- **개인 이용 내역**: 개인별 진료 기록 및 처방 내역 조회
- **통계 관리**: 관리자용 전체 이용 현황 및 통계 데이터 제공 (구현 논의중)
- **캠퍼스별 관리**: 아산캠퍼스/천안캠퍼스별 진료 데이터 분리 관리 (구현 미정)


## 기술 스택
### 프레임워크
- **Spring Boot 3.5.0**: 독립 실행형 Spring 기반 애플리케이션을 빠르게 개발할 수 있는 프레임워크
- **Spring Security**: 인증 및 보안 처리를 위한 프레임워크
- **Spring Data JPA**: 관계형 데이터 관리를 위한 ORM(Object Relational Mapping) 기술
- **Spring Boot Validation**: 입력 데이터 검증을 위한 라이브러리

### 데이터베이스
- **MySQL**: 안정적이고 확장 가능한 관계형 데이터베이스 관리 시스템

### 인증 및 보안
- **Google OAuth2.0**: 구글 계정을 통한 소셜 로그인 서비스
- **JWT Library**: JWT 토큰 생성 및 검증을 위한 라이브러리

### 개발 도구
- **Java 17**: 최신 Java 기능을 활용하여 개발
- **Gradle**: 프로젝트 빌드 및 의존성 관리 도구
- **Lombok**: 반복적인 코드(getter, setter 등)를 자동 생성하는 라이브러리

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/gate/houi/hoseoHoui/
│   │   ├── HoseoHouiApplication.java    # 메인 애플리케이션 클래스
│   │   ├── config/                      # 설정 클래스들
│   │   ├── controller/                  # REST API 컨트롤러
│   │   │   ├── GoogleOAuthController.java    # 구글 OAuth 인증
│   │   │   ├── NoticeController.java         # 공지사항 관리
│   │   │   ├── ReceptionController.java      # 진료 접수 관리
│   │   │   ├── UseHistoryController.java     # 이용 내역 조회
│   │   │   └── MyInfoController.java         # 사용자 정보 관리
│   │   ├── domain/                      # 도메인 모델
│   │   │   ├── entity/                  # JPA 엔티티
│   │   │   ├── enumType/               # Enum 타입 정의
│   │   │   └── common/                 # 공통 엔티티
│   │   ├── dto/                        # 데이터 전송 객체
│   │   │   ├── auth/                   # 인증 관련 DTO
│   │   │   ├── account/                # 계정 관련 DTO
│   │   │   ├── notice/                 # 공지사항 DTO
│   │   │   └── reception/              # 진료 접수 DTO
│   │   ├── exception/                  # 예외 처리
│   │   ├── repository/                 # 데이터 액세스 계층
│   │   ├── security/                   # 보안 관련 클래스
│   │   │   ├── JwtTokenProvider.java   # JWT 토큰 생성/검증
│   │   │   ├── JwtAuthenticationFilter.java # JWT 인증 필터
│   │   │   └── SecurityConfig.java     # 보안 설정
│   │   └── service/                    # 비즈니스 로직
│   │       ├── AuthService.java        # 인증 서비스
│   │       ├── AccountService.java     # 계정 서비스
│   │       ├── NoticeService.java      # 공지사항 서비스
│   │       ├── ReceptionService.java   # 진료 접수 서비스
│   │       ├── UseHistoryService.java  # 이용 내역 서비스
│   │       └── RefreshTokenService.java # 리프레시 토큰 서비스
│   └── resources/
│       ├── application.yml             # 메인 설정 파일
│       └── banner.txt                  # 애플리케이션 배너
└── test/                              # 테스트 코드
```

## 보안 구성

### JWT 토큰 관리
- **Access Token**: 30분 유효, API 인증용으로 사용
- **Refresh Token**: 7일 유효, 토큰 갱신용으로 사용
- **클럭 스큐 허용**: 60초 허용으로 서버 간 시간 동기화 문제 해결

### 인증 및 권한 관리
- **JWT 기반 인증 필터**: 모든 API 요청에 대한 토큰 검증
- **Spring Security 설정**: 엔드포인트별 접근 제어 및 권한 관리
- **Google OAuth2.0 통합**: 호서대학교 이메일 도메인 검증

## 개발 가이드라인

### 코드 스타일 및 아키텍처
- **Lombok 활용**: `@RequiredArgsConstructor`, `@Builder`, `@Data` 등을 사용하여 보일러플레이트 코드 최소화
- **Builder 패턴**: 객체 생성 시 가독성과 유지보수성을 위해 적극 활용
- **계층형 아키텍처**: Controller → Service → Repository 구조로 관심사 분리
- **트랜잭션 관리**: `@Transactional` 어노테이션을 활용한 데이터 일관성 보장

### 예외 처리 전략
- **커스텀 예외 클래스**: 비즈니스 로직에 맞는 의미있는 예외 정의
- **Global Exception Handler**: 중앙 집중식 예외 처리로 일관된 에러 응답
- **에러 응답 표준화**: ErrorResponse 클래스를 통한 일관된 에러 메시지 형식

### API 설계 원칙
- **RESTful API**: HTTP 메서드와 상태 코드를 올바르게 사용하는 REST 원칙 준수
- **ResponseEntity 활용**: 일관된 HTTP 응답 형식과 상태 코드 관리
- **DTO 패턴**: 계층 간 데이터 전송을 위한 전용 객체 사용
- **Validation**: `@Valid`, `@NotNull`, `@NotEmpty` 등을 활용한 입력 검증

### 보안 개발 가이드라인
- **민감 정보 보호**: application-*.yml 파일에 하드코딩 금지, 환경 변수 또는 외부 설정 사용
- **SQL Injection 방지**: JPA Query Method와 @Query 어노테이션의 안전한 사용
- **XSS 방지**: 사용자 입력 데이터에 대한 적절한 검증 및 이스케이프 처리


## 📄 라이선스

This project is licensed under the MIT License.
