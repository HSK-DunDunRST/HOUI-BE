# 보건실 앱 & 관리자 시스템

## 사용자 역할

| 역할           | 설명                                                |
| -------------- | --------------------------------------------------- |
| 학생 (Student) | 앱을 통해 온보딩, 접수, 내역 조회 등 사용           |
| 관리자 (Admin) | 웹 관리페이지에서 공지, 접수처리, 설정 관리 등 운영 |

---

## 주요 기능 요약

### 학생 (앱)

- 온보딩 (학번, 이름, 서명)
- 증상 접수 → 완료 안내
- 대기 인원 확인
- 개인정보 수정
- 이용 이력 확인
- 운영 시간 안내 + 이용 가능 여부 확인
- 인바디 측정 가능 여부 확인
- 공지사항 확인
- 보건실 연락처 확인

### 관리자 (웹 페이지)

- 로그인 (JWT)
- 공지사항 등록/삭제
- 접수 목록 조회 및 완료 처리
- 학생 목록/조회/수정
- 운영시간, 인바디 측정 가능 여부, 보건실 연락처 설정

---

## DB 테이블 설계 요약 (ERD)

| 테이블            | 주요 컬럼                                                 | 설명                  |
| ----------------- | --------------------------------------------------------- | --------------------- |
| `users`         | id, student_id, name, signature                           | 학생 정보             |
| `admin_users`   | id, username, password_hash                               | 관리자 로그인용       |
| `check_in`      | id, user_id (FK), symptoms, status                        | 접수 기록 (대기/완료) |
| `usage_history` | id, user_id (FK), date, symptoms, result                  | 이용 이력             |
| `notices`       | id, title, content, created_at                            | 공지사항              |
| `settings`      | id, open_time, close_time, inbody_available, phone_number | 전역 운영 설정        |

---

## API 구조 요약

### 학생용 API (`/api`)

| URL                        | 메서드 | 설명                             |
| :------------------------- | ------ | -------------------------------- |
| `/onboarding`            | POST   | 학번, 이름, 서명 저장            |
| `/checkin`               | POST   | 증상 접수 등록                   |
| `/checkin/waiting-count` | GET    | 대기 인원 수 확인                |
| `/users/{studentId}`     | PUT    | 개인정보 수정                    |
| `/usage/{studentId}`     | GET    | 이용 이력 조회                   |
| `/settings`              | GET    | 운영시간/인바디 여부/연락처 조회 |
| `/notices`               | GET    | 공지사항 목록 조회               |

### 관리자용 API (`/api/admin`)

| URL                        | 메서드 | 설명                     |
| -------------------------- | ------ | ------------------------ |
| `/login`                 | POST   | 관리자 로그인 (JWT 발급) |
| `/notices`               | POST   | 공지 등록                |
| `/notices/{id}`          | DELETE | 공지 삭제                |
| `/checkin`               | GET    | 접수 전체 목록 조회      |
| `/checkin/{id}/complete` | PUT    | 접수 완료 처리           |
| `/users`                 | GET    | 학생 전체 목록           |
| `/users/{id}`            | PUT    | 학생 정보 수정           |
| `/settings`              | GET    | 운영 설정 조회           |
| `/settings`              | PUT    | 운영 설정 수정           |

---

## 인증 및 보안 설계

| 구분        | 방식                                           |
| ----------- | ---------------------------------------------- |
| 학생        | 인증 없이 `student_id`로만 식별 (JWT 불필요) |
| 관리자      | 로그인 → JWT 발급 → 헤더 포함 필수           |
| 서명 저장   | Base64 문자열 or 이미지 파일로 서버 저장       |
| 관리자 보호 | `/api/admin/**`는 모두 JWT + Role 체크 필요  |
