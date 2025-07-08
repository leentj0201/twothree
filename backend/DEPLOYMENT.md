# Twothree Backend Deployment Guide

## 환경 구성

이 프로젝트는 두 가지 환경을 지원합니다:

- **INT (Integration)**: 개발/테스트 환경 (PostgreSQL 데이터베이스)
- **PROD (Production)**: 운영 환경 (PostgreSQL 데이터베이스)

## 사전 요구사항

- Docker
- Docker Compose
- curl (상태 확인용)

## 배포 방법

### INT 환경 배포 (개발/테스트)

```bash
# INT 환경 배포
./deploy-int.sh

# 또는 수동으로 실행
docker-compose --profile int up --build -d
```

**접속 정보:**
- 애플리케이션: http://localhost:8081
- API 문서: http://localhost:8081/swagger-ui.html
- PostgreSQL: localhost:5433

### PROD 환경 배포 (운영)

```bash
# PROD 환경 배포
./deploy-prod.sh

# 또는 수동으로 실행
docker-compose --profile prod up --build -d
```

**접속 정보:**
- 애플리케이션: http://localhost:8080
- API 문서: http://localhost:8080/swagger-ui.html
- PostgreSQL: localhost:5432

## 환경 변수

### INT 환경
- `SPRING_PROFILES_ACTIVE=int`
- `PORT=8080`
- `DATABASE_URL=jdbc:postgresql://postgres-int:5432/twothree_int`
- `DATABASE_USERNAME=postgres`
- `DATABASE_PASSWORD=password`

### PROD 환경
- `SPRING_PROFILES_ACTIVE=prod`
- `PORT=8080`
- `DATABASE_URL=jdbc:postgresql://postgres-prod:5432/twothree`
- `DATABASE_USERNAME=postgres`
- `DATABASE_PASSWORD=password`
- `JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`

## 컨테이너 관리

### 로그 확인
```bash
# INT 환경 로그
docker-compose --profile int logs -f twothree-backend-int

# PROD 환경 로그
docker-compose --profile prod logs -f twothree-backend-prod
```

### 컨테이너 중지
```bash
# INT 환경 중지
docker-compose --profile int down

# PROD 환경 중지
docker-compose --profile prod down
```

### 컨테이너 재시작
```bash
# INT 환경 재시작
docker-compose --profile int restart

# PROD 환경 재시작
docker-compose --profile prod restart
```

## 데이터베이스 관리

### INT 환경 PostgreSQL 접속
```bash
# 컨테이너 내부 접속
docker exec -it postgres-int psql -U postgres -d twothree_int

# 외부에서 접속 (포트 5433이 열려있는 경우)
psql -h localhost -p 5433 -U postgres -d twothree_int
```

### PROD 환경 PostgreSQL 접속
```bash
# 컨테이너 내부 접속
docker exec -it postgres-prod psql -U postgres -d twothree

# 외부에서 접속 (포트 5432가 열려있는 경우)
psql -h localhost -p 5432 -U postgres -d twothree
```

### 데이터 백업
```bash
# INT 환경 데이터 백업
docker exec postgres-int pg_dump -U postgres twothree_int > backup_int.sql

# PROD 환경 데이터 백업
docker exec postgres-prod pg_dump -U postgres twothree > backup_prod.sql
```

## 문제 해결

### 애플리케이션이 시작되지 않는 경우
1. 로그 확인: `docker-compose logs [service-name]`
2. 포트 충돌 확인: `netstat -tulpn | grep :8080`
3. Docker 리소스 확인: `docker system df`

### 데이터베이스 연결 문제
1. PostgreSQL 컨테이너 상태 확인: `docker ps | grep postgres`
2. 데이터베이스 로그 확인: `docker logs postgres-prod`
3. 네트워크 연결 확인: `docker network ls`

## 보안 고려사항

### PROD 환경에서 변경해야 할 설정
1. **JWT Secret Key**: 환경 변수로 설정하여 기본값 변경
2. **데이터베이스 비밀번호**: 강력한 비밀번호로 변경
3. **포트 노출**: 필요한 포트만 노출
4. **HTTPS**: 리버스 프록시를 통한 HTTPS 설정 권장

### 환경 변수 설정 예시
```bash
export JWT_SECRET_KEY="your-secure-jwt-secret-key"
export DATABASE_PASSWORD="your-secure-database-password"
docker-compose --profile prod up --build -d
``` 