# 🏗️ Twothree 백엔드 프로젝트 구조

## 📋 프로젝트 개요

**Twothree Backend**는 Spring Boot 3.5.3 기반의 교회 관리 시스템 백엔드 API 서버입니다.

### 🛠️ 기술 스택
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Build Tool**: Gradle
- **Database**: H2 (개발), PostgreSQL (프로덕션)
- **Security**: Spring Security + JWT
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Excel**: Apache POI

## 📁 프로젝트 구조

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/twothree/backend/
│   │   │   ├── BackendApplication.java          # 메인 애플리케이션 클래스
│   │   │   ├── config/                          # 설정 클래스들
│   │   │   ├── controller/                      # REST API 컨트롤러
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── entity/                          # JPA 엔티티
│   │   │   ├── enums/                           # 열거형 클래스들
│   │   │   ├── repository/                      # 데이터 접근 계층
│   │   │   └── service/                         # 비즈니스 로직 계층
│   │   └── resources/
│   │       ├── application.yml                  # 기본 설정
│   │       ├── application-prod.yml             # 프로덕션 설정
│   │       ├── application-int.yml              # 통합 환경 설정
│   │       ├── data.sql                         # 초기 데이터
│   │       └── init.sql                         # PostgreSQL 초기화
│   └── test/                                    # 테스트 코드
├── build.gradle                                 # Gradle 빌드 설정
├── Dockerfile                                   # Docker 이미지 설정
└── docker-compose.yml                           # Docker 환경 설정
```

## 🏛️ 아키텍처 패턴

### 1. 계층형 아키텍처 (Layered Architecture)

```
┌─────────────────────────────────────┐
│           Controller Layer          │ ← REST API 엔드포인트
├─────────────────────────────────────┤
│            Service Layer            │ ← 비즈니스 로직
├─────────────────────────────────────┤
│          Repository Layer           │ ← 데이터 접근
├─────────────────────────────────────┤
│            Entity Layer             │ ← 데이터 모델
└─────────────────────────────────────┘
```

### 2. 패키지별 역할

#### 📦 `controller` - REST API 컨트롤러
- **역할**: HTTP 요청/응답 처리
- **주요 클래스**:
  - `AuthController`: 인증 관련 API
  - `ChurchController`: 교회 관리 API
  - `DepartmentController`: 부서 관리 API
  - `MemberController`: 멤버 관리 API
  - `UserController`: 사용자 관리 API
  - `ContentController`: 콘텐츠 관리 API
  - `DatabaseTestController`: DB 테스트 API

#### 📦 `service` - 비즈니스 로직
- **역할**: 비즈니스 규칙 및 트랜잭션 처리
- **주요 클래스**:
  - `AuthService`: 인증 로직
  - `ChurchService`: 교회 관리 로직
  - `DepartmentService`: 부서 관리 로직
  - `MemberService`: 멤버 관리 로직
  - `UserService`: 사용자 관리 로직
  - `ContentService`: 콘텐츠 관리 로직
  - `JwtService`: JWT 토큰 처리
  - `ChurchExcelService`: Excel 내보내기

#### 📦 `repository` - 데이터 접근 계층
- **역할**: 데이터베이스 CRUD 작업
- **주요 클래스**:
  - `ChurchRepository`: 교회 데이터 접근
  - `DepartmentRepository`: 부서 데이터 접근
  - `MemberRepository`: 멤버 데이터 접근
  - `UserRepository`: 사용자 데이터 접근
  - `ContentRepository`: 콘텐츠 데이터 접근

#### 📦 `entity` - 데이터 모델
- **역할**: JPA 엔티티 (데이터베이스 테이블 매핑)
- **주요 클래스**:
  - `BaseEntity`: 공통 필드 (id, created_at, updated_at)
  - `Church`: 교회 정보
  - `Department`: 부서 정보
  - `Member`: 멤버 정보
  - `User`: 사용자 정보
  - `Content`: 콘텐츠 정보
  - `MemberDepartment`: 멤버-부서 관계
  - `ContentDepartment`: 콘텐츠-부서 관계

#### 📦 `dto` - 데이터 전송 객체
- **역할**: API 요청/응답 데이터 구조
- **주요 클래스**:
  - `BaseDto`: 공통 DTO 필드
  - `ChurchDto`: 교회 정보 DTO
  - `DepartmentDto`: 부서 정보 DTO
  - `MemberDto`: 멤버 정보 DTO
  - `UserDto`: 사용자 정보 DTO
  - `ContentDto`: 콘텐츠 정보 DTO
  - Request DTOs: API 요청 데이터

#### 📦 `config` - 설정 클래스
- **역할**: 애플리케이션 설정
- **주요 클래스**:
  - `SecurityConfig`: Spring Security 설정
  - `OpenApiConfig`: Swagger API 문서 설정
  - `JwtAuthenticationFilter`: JWT 인증 필터

#### 📦 `enums` - 열거형
- **역할**: 상수 값 정의
- **주요 클래스**:
  - `ChurchStatus`: 교회 상태 (ACTIVE, INACTIVE)
  - `DepartmentStatus`: 부서 상태 (ACTIVE, INACTIVE)
  - `DepartmentCategory`: 부서 카테고리 (WORSHIP, YOUTH, etc.)
  - `MemberRole`: 멤버 역할 (PASTOR, ELDER, MEMBER)
  - `MemberStatus`: 멤버 상태 (ACTIVE, INACTIVE)
  - `Gender`: 성별 (MALE, FEMALE)

## 🔐 보안 아키텍처

### JWT 기반 인증
```
1. 로그인 → JWT 토큰 발급
2. API 요청 → JWT 토큰 검증
3. 권한 확인 → 리소스 접근 허용/거부
```

### 보안 구성 요소
- **Spring Security**: 인증/인가 프레임워크
- **JWT**: 토큰 기반 인증
- **BCrypt**: 비밀번호 암호화
- **CORS**: 크로스 오리진 설정

## 🗄️ 데이터베이스 설계

### 주요 엔티티 관계
```
Church (1) ←→ (N) Department
Church (1) ←→ (N) Member
Department (N) ←→ (N) Member (MemberDepartment)
User (1) ←→ (1) Member
Content (N) ←→ (N) Department (ContentDepartment)
```

### 데이터베이스 환경
- **개발**: H2 인메모리 데이터베이스
- **프로덕션**: PostgreSQL
- **초기화**: `data.sql`로 샘플 데이터 자동 생성

## 🚀 API 구조

### RESTful API 설계
```
GET    /api/churches          # 교회 목록 조회
POST   /api/churches          # 교회 등록
GET    /api/churches/{id}     # 교회 상세 조회
PUT    /api/churches/{id}     # 교회 수정
DELETE /api/churches/{id}     # 교회 삭제

GET    /api/departments       # 부서 목록 조회
POST   /api/departments       # 부서 등록
GET    /api/members           # 멤버 목록 조회
POST   /api/members           # 멤버 등록
```

### API 문서
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI**: http://localhost:8080/v3/api-docs

## 🔧 개발 환경 설정

### 필수 요구사항
- Java 17+
- Gradle 7.0+
- Docker (선택사항)

### 실행 방법
```bash
# 개발 환경 (H2)
./gradlew bootRun

# 프로덕션 환경 (PostgreSQL)
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### 빌드 방법
```bash
# JAR 파일 생성
./gradlew build

# Docker 이미지 빌드
docker build -t twothree-backend .
```

## 📊 모니터링 및 로깅

### 로깅 레벨
- **개발**: DEBUG 레벨
- **프로덕션**: INFO 레벨

### 주요 로그
- SQL 쿼리 로그
- Spring Security 로그
- 애플리케이션 로그

## 🧪 테스트

### 테스트 구조
- **Unit Tests**: 개별 컴포넌트 테스트
- **Integration Tests**: API 통합 테스트
- **Repository Tests**: 데이터 접근 테스트

### 테스트 실행
```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests ChurchServiceTest
```

## 🚀 배포

### Docker 배포
```bash
# 이미지 빌드
docker build -t twothree-backend .

# 컨테이너 실행
docker run -p 8080:8080 twothree-backend
```

### 환경별 설정
- **개발**: `application.yml`
- **통합**: `application-int.yml`
- **프로덕션**: `application-prod.yml`

## 🔄 개발 워크플로우

### 1. 기능 개발
1. 엔티티 정의/수정
2. Repository 인터페이스 작성
3. Service 로직 구현
4. Controller API 구현
5. DTO 클래스 작성

### 2. 테스트
1. Unit Test 작성
2. Integration Test 작성
3. API 테스트 (Swagger UI)

### 3. 배포
1. 코드 리뷰
2. 테스트 통과 확인
3. 빌드 및 배포

## 📈 확장성 고려사항

### 현재 구조의 장점
- **계층 분리**: 관심사 분리로 유지보수성 향상
- **의존성 주입**: 느슨한 결합으로 테스트 용이성
- **RESTful API**: 표준화된 API 설계
- **JPA**: 데이터베이스 독립성

### 향후 개선 방향
- **캐싱**: Redis 도입
- **비동기 처리**: Spring WebFlux 고려
- **마이크로서비스**: 서비스 분리
- **API 버전 관리**: API 버전링 전략 