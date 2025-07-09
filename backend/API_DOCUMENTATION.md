# 📚 API 문서화 가이드

## 개요
이 문서는 교회 관리 시스템의 API 문서화 방법과 사용법을 설명합니다.

## 🚀 Swagger UI 접속

### 개발 환경
```
http://localhost:8080/swagger-ui/index.html
```

### 프로덕션 환경
```
https://api.twothree.com/swagger-ui/index.html
```

## 📋 API 그룹별 설명

### 1. 교회 관리 API (`/api/churches`)
교회 정보의 CRUD 작업을 담당합니다.

#### 주요 엔드포인트
- `POST /api/churches/list` - 교회 목록 조회
- `GET /api/churches/excel` - 교회 목록 Excel 다운로드
- `POST /api/churches/get` - 교회 상세 조회
- `POST /api/churches/create` - 교회 등록
- `POST /api/churches/update` - 교회 정보 수정
- `POST /api/churches/delete` - 교회 삭제

### 2. 멤버 관리 API (`/api/members`)
교회 멤버 정보의 CRUD 작업을 담당합니다.

#### 주요 엔드포인트
- `POST /api/members/list` - 멤버 목록 조회
- `POST /api/members/list-by-church` - 교회별 멤버 목록
- `POST /api/members/get` - 멤버 상세 조회
- `POST /api/members/create` - 멤버 등록
- `POST /api/members/update` - 멤버 정보 수정
- `POST /api/members/delete` - 멤버 삭제

### 3. 부서 관리 API (`/api/departments`)
교회 부서 정보의 CRUD 작업을 담당합니다.

#### 주요 엔드포인트
- `POST /api/departments/list` - 부서 목록 조회
- `POST /api/departments/get` - 부서 상세 조회
- `POST /api/departments/create` - 부서 등록
- `POST /api/departments/update` - 부서 정보 수정
- `POST /api/departments/delete` - 부서 삭제

### 4. 인증 API (`/api/auth`)
사용자 인증 및 권한 관리를 담당합니다.

#### 주요 엔드포인트
- `POST /api/auth/login` - 로그인
- `POST /api/auth/register` - 회원가입
- `POST /api/auth/refresh` - 토큰 갱신
- `POST /api/auth/logout` - 로그아웃

## 🔐 인증 방식

### JWT 토큰 인증
```bash
# 요청 헤더에 토큰 추가
Authorization: Bearer <your-jwt-token>
```

### 기본 인증 (선택사항)
```bash
# 요청 헤더에 기본 인증 추가
Authorization: Basic <base64-encoded-credentials>
```

## 📊 응답 형식

### 성공 응답
```json
{
  "success": true,
  "data": {
    // 실제 데이터
  },
  "message": "요청이 성공적으로 처리되었습니다.",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### 에러 응답
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "유효성 검사 실패",
    "details": [
      "이름은 필수입니다.",
      "이메일 형식이 올바르지 않습니다."
    ]
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## 📝 요청 예시

### 교회 등록
```bash
curl -X POST "http://localhost:8080/api/churches/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "name": "감리교회",
    "description": "서울 강남구에 위치한 교회",
    "address": "서울시 강남구 테헤란로 123",
    "phone": "02-1234-5678",
    "email": "church@example.com",
    "pastorName": "김목사",
    "pastorPhone": "010-1234-5678",
    "pastorEmail": "pastor@example.com",
    "status": "ACTIVE"
  }'
```

### 멤버 검색
```bash
curl -X POST "http://localhost:8080/api/members/search-by-church" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "churchId": 1,
    "keyword": "김"
  }'
```

## 🔍 검색 및 필터링

### 교회 검색
- **키워드 검색**: 교회명, 주소, 설명에서 검색
- **상태별 필터링**: ACTIVE, INACTIVE, PENDING

### 멤버 검색
- **교회별 검색**: 특정 교회의 멤버만 검색
- **부서별 필터링**: 특정 부서의 멤버만 조회
- **역할별 필터링**: 목사, 장로, 집사 등 역할별 조회
- **생년월일 범위**: 특정 기간에 태어난 멤버 조회
- **입교일 범위**: 특정 기간에 입교한 멤버 조회

## 📈 페이지네이션

대용량 데이터 조회 시 페이지네이션을 지원합니다.

```json
{
  "content": [
    // 실제 데이터 배열
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true,
  "numberOfElements": 20
}
```

## 🚨 에러 코드

| 코드 | 설명 |
|------|------|
| `VALIDATION_ERROR` | 유효성 검사 실패 |
| `NOT_FOUND` | 리소스를 찾을 수 없음 |
| `DUPLICATE_ERROR` | 중복 데이터 |
| `UNAUTHORIZED` | 인증 실패 |
| `FORBIDDEN` | 권한 없음 |
| `INTERNAL_ERROR` | 서버 내부 오류 |

## 📋 상태 코드

| 상태 코드 | 설명 |
|-----------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 204 | 삭제 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스를 찾을 수 없음 |
| 422 | 유효성 검사 실패 |
| 500 | 서버 내부 오류 |

## 🔧 개발자 도구

### Postman Collection
API 테스트를 위한 Postman Collection을 제공합니다.
- [교회 관리 API Collection](./postman/Church_API.postman_collection.json)
- [멤버 관리 API Collection](./postman/Member_API.postman_collection.json)

### API 테스트 스크립트
```bash
# API 테스트 실행
./scripts/test-api.sh
```

## 📚 추가 리소스

- [데이터베이스 스키마](./DATABASE_SCHEMA.md)
- [인증 가이드](./AUTHENTICATION_GUIDE.md)
- [배포 가이드](./DEPLOYMENT.md)
- [개발 환경 설정](./DEVELOPMENT_SETUP.md)

## 🤝 기여하기

API 문서화 개선 제안이나 버그 리포트는 이슈로 등록해주세요.

---

**마지막 업데이트**: 2024년 1월 1일
**버전**: 1.0.0 