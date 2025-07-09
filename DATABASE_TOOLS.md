# 🛠️ Twothree 데이터베이스 접속 도구 가이드

## 📋 사용 가능한 DB 접속 도구들

### 1. 🌐 웹 기반 도구들

#### H2 콘솔 (개발 환경)
- **URL**: http://localhost:8080/h2-console
- **용도**: H2 인메모리 데이터베이스 관리
- **접속 정보**:
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

#### pgAdmin (PostgreSQL 전용)
- **URL**: http://localhost:5050
- **용도**: PostgreSQL 데이터베이스 전용 관리 도구
- **접속 정보**:
  - Email: `admin@twothree.com`
  - Password: `admin123`

#### Adminer (통합 DB 관리)
- **URL**: http://localhost:8082
- **용도**: 모든 데이터베이스 통합 관리 (PostgreSQL, MySQL, SQLite 등)
- **접속 정보**:
  - System: `PostgreSQL`
  - Server: `postgres`
  - Username: `twothree_user`
  - Password: `twothree_password`
  - Database: `twothree_db`

#### Redis Commander
- **URL**: http://localhost:8081
- **용도**: Redis 데이터 관리
- **특징**: 키-값 데이터 시각화 및 편집

### 2. 🖥️ 데스크톱 도구들

#### DBeaver (권장)
- **다운로드**: https://dbeaver.io/
- **설정 파일**: `database-connections/dbeaver-connections.xml`
- **지원 DB**: PostgreSQL, H2, MySQL, Oracle 등 모든 주요 DB

#### DataGrip (JetBrains)
- **다운로드**: https://www.jetbrains.com/datagrip/
- **특징**: IntelliJ 기반의 강력한 DB 관리 도구

#### TablePlus
- **다운로드**: https://tableplus.com/
- **특징**: 깔끔한 UI와 빠른 성능

### 3. 🔧 명령줄 도구들

#### PostgreSQL CLI
```bash
# Docker 컨테이너 접속
docker-compose exec postgres psql -U twothree_user -d twothree_db

# 로컬 PostgreSQL 접속 (설치된 경우)
psql -h localhost -p 5432 -U twothree_user -d twothree_db
```

#### Redis CLI
```bash
# Docker 컨테이너 접속
docker-compose exec redis redis-cli

# 로컬 Redis 접속 (설치된 경우)
redis-cli -h localhost -p 6379
```

## 🚀 빠른 시작 가이드

### 1. 개발 환경 (H2)
```bash
# Spring Boot 시작
cd backend
./gradlew bootRun

# 브라우저에서 접속
open http://localhost:8080/h2-console
```

### 2. 프로덕션 환경 (PostgreSQL)
```bash
# 데이터베이스 환경 시작
./scripts/db-start.sh

# 브라우저에서 접속
open http://localhost:5050  # pgAdmin
open http://localhost:8082  # Adminer
open http://localhost:8081  # Redis Commander
```

## 📊 도구별 특징 비교

| 도구 | 타입 | H2 지원 | PostgreSQL 지원 | Redis 지원 | 웹 기반 | 설치 필요 |
|------|------|---------|-----------------|------------|---------|-----------|
| H2 콘솔 | 웹 | ✅ | ❌ | ❌ | ✅ | ❌ |
| pgAdmin | 웹 | ❌ | ✅ | ❌ | ✅ | ❌ |
| Adminer | 웹 | ✅ | ✅ | ❌ | ✅ | ❌ |
| Redis Commander | 웹 | ❌ | ❌ | ✅ | ✅ | ❌ |
| DBeaver | 데스크톱 | ✅ | ✅ | ❌ | ❌ | ✅ |
| DataGrip | 데스크톱 | ✅ | ✅ | ❌ | ❌ | ✅ |
| TablePlus | 데스크톱 | ✅ | ✅ | ✅ | ❌ | ✅ |

## 🔧 DBeaver 설정 가이드

### 1. DBeaver 설치
1. https://dbeaver.io/ 에서 다운로드
2. 설치 후 실행

### 2. 연결 설정
1. **새 연결** → **H2** 선택
2. **연결 설정**:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (비어있음)

### 3. PostgreSQL 연결
1. **새 연결** → **PostgreSQL** 선택
2. **연결 설정**:
   - Host: `localhost`
   - Port: `5432`
   - Database: `twothree_db`
   - Username: `twothree_user`
   - Password: `twothree_password`

## 🎯 추천 사용 시나리오

### 개발자
- **H2 콘솔**: 빠른 개발 중 데이터 확인
- **DBeaver**: 복잡한 쿼리 작성 및 데이터 분석
- **Redis Commander**: 캐시 데이터 확인

### DBA/운영자
- **pgAdmin**: PostgreSQL 전용 관리
- **Adminer**: 간단한 데이터 확인
- **명령줄 도구**: 스크립트 자동화

### 프론트엔드 개발자
- **H2 콘솔**: 개발 환경 데이터 확인
- **Adminer**: 간단한 웹 인터페이스

## 🔐 보안 주의사항

1. **프로덕션 환경**에서는 강력한 비밀번호 사용
2. **pgAdmin 접속 정보** 변경 권장
3. **방화벽 설정**으로 외부 접근 제한
4. **SSL/TLS** 사용 권장

## 🆘 문제 해결

### 포트 충돌
```bash
# 포트 사용 확인
lsof -i :5432
lsof -i :5050
lsof -i :8081
lsof -i :8082

# 컨테이너 재시작
docker-compose restart
```

### 연결 실패
1. Docker 컨테이너 상태 확인: `docker-compose ps`
2. 로그 확인: `docker-compose logs [service-name]`
3. 네트워크 확인: `docker network ls`

### 권한 문제
```bash
# 스크립트 실행 권한
chmod +x scripts/*.sh

# Docker 권한 (Linux)
sudo usermod -aG docker $USER
``` 