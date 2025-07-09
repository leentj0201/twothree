# Twothree 데이터베이스 환경 가이드

## 개요

이 프로젝트는 두 가지 데이터베이스 환경을 제공합니다:

1. **개발 환경**: H2 인메모리 데이터베이스 (기본)
2. **프로덕션 환경**: PostgreSQL 데이터베이스

## 🚀 빠른 시작

### 1. 개발 환경 (H2 - 기본)

```bash
cd backend
./gradlew bootRun
```

- **H2 콘솔**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (비어있음)

### 2. 프로덕션 환경 (PostgreSQL)

```bash
# 1. 데이터베이스 환경 시작
./scripts/db-start.sh

# 2. Spring Boot 애플리케이션 시작
cd backend
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## 📊 데이터베이스 관리

### PostgreSQL 환경 관리

```bash
# 데이터베이스 시작
./scripts/db-start.sh

# 데이터베이스 중지
./scripts/db-stop.sh

# 데이터베이스 초기화 (모든 데이터 삭제)
./scripts/db-reset.sh
```

### 접속 정보

#### PostgreSQL
- **Host**: localhost
- **Port**: 5432
- **Database**: twothree_db
- **Username**: twothree_user
- **Password**: twothree_password

#### pgAdmin (웹 관리 도구)
- **URL**: http://localhost:5050
- **Email**: admin@twothree.com
- **Password**: admin123

#### Redis
- **Host**: localhost
- **Port**: 6379

## 🔍 데이터베이스 조회 API

데이터베이스 조회를 위한 테스트 API가 제공됩니다:

### 기본 엔드포인트
- `GET /api/db-test/health` - 데이터베이스 연결 상태 확인
- `GET /api/db-test/stats` - 데이터베이스 통계 정보

### Church 관련
- `GET /api/db-test/churches` - 모든 교회 조회
- `GET /api/db-test/churches/{id}` - 특정 교회 조회
- `GET /api/db-test/church-with-departments/{churchId}` - 교회와 부서 정보 함께 조회

### Department 관련
- `GET /api/db-test/departments` - 모든 부서 조회
- `GET /api/db-test/departments/church/{churchId}` - 특정 교회의 부서 조회

### Member 관련
- `GET /api/db-test/members` - 모든 멤버 조회
- `GET /api/db-test/members/church/{churchId}` - 특정 교회의 멤버 조회

### User 관련
- `GET /api/db-test/users` - 모든 사용자 조회

## 🛠️ 개발 도구

### 1. H2 콘솔 (개발 환경)
- URL: http://localhost:8080/h2-console
- 실시간 데이터베이스 조회 및 수정 가능

### 2. pgAdmin (프로덕션 환경)
- URL: http://localhost:5050
- PostgreSQL 전용 웹 관리 도구
- 데이터베이스 구조 시각화
- SQL 쿼리 실행

### 3. Docker Compose
```bash
# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs postgres
docker-compose logs pgadmin

# 컨테이너 접속
docker-compose exec postgres psql -U twothree_user -d twothree_db
```

## 📁 데이터베이스 파일

- `backend/src/main/resources/application.yml` - H2 설정 (개발)
- `backend/src/main/resources/application-prod.yml` - PostgreSQL 설정 (프로덕션)
- `backend/src/main/resources/data.sql` - 초기 샘플 데이터
- `backend/src/main/resources/init.sql` - PostgreSQL 초기화 스크립트
- `docker-compose.yml` - Docker 환경 설정

## 🔧 문제 해결

### PostgreSQL 연결 실패
1. Docker 컨테이너가 실행 중인지 확인: `docker-compose ps`
2. 포트 충돌 확인: `lsof -i :5432`
3. 컨테이너 재시작: `docker-compose restart postgres`

### 데이터 초기화
1. `./scripts/db-reset.sh` 실행
2. Spring Boot 애플리케이션 재시작

### 권한 문제
```bash
# 스크립트 실행 권한 부여
chmod +x scripts/*.sh
```

## 📝 샘플 데이터

초기 데이터는 `data.sql`에 정의되어 있으며 다음을 포함합니다:

- 2개의 교회 (새생명교회, 은혜교회)
- 3개의 부서 (예배부, 청년부, 주일학교)
- 3명의 멤버 (김목사, 이집사, 박청년)
- 2명의 사용자 (admin, user)

## 🔐 보안 주의사항

- 프로덕션 환경에서는 반드시 강력한 비밀번호를 사용하세요
- 데이터베이스 접속 정보를 환경 변수로 관리하는 것을 권장합니다
- pgAdmin 접속 정보를 변경하는 것을 권장합니다 