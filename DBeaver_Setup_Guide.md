# 🦫 DBeaver 설치 및 설정 가이드

## 📥 DBeaver 다운로드 및 설치

### 1. 다운로드
- **공식 사이트**: https://dbeaver.io/
- **Community Edition** (무료) 다운로드
- 운영체제에 맞는 버전 선택:
  - Windows: `.exe` 또는 `.zip`
  - macOS: `.dmg`
  - Linux: `.tar.gz` 또는 패키지 매니저

### 2. 설치
- 다운로드한 파일 실행
- 설치 마법사 따라하기
- 기본 설정으로 설치 권장

## 🔧 Twothree 프로젝트 연결 설정

### 1. H2 데이터베이스 연결 (개발 환경)

#### 연결 생성
1. DBeaver 실행
2. **새 데이터베이스 연결** 클릭 (🔌 아이콘)
3. **H2** 선택 → **다음**

#### 연결 설정
```
연결 이름: Twothree H2 (Dev)
URL: jdbc:h2:mem:testdb
사용자 이름: sa
비밀번호: (비어있음)
```

#### 고급 설정
```
드라이버 속성:
- MODE: PostgreSQL
- DB_CLOSE_DELAY: -1
- DB_CLOSE_ON_EXIT: FALSE
```

### 2. PostgreSQL 데이터베이스 연결 (프로덕션 환경)

#### 연결 생성
1. **새 데이터베이스 연결** 클릭
2. **PostgreSQL** 선택 → **다음**

#### 연결 설정
```
연결 이름: Twothree PostgreSQL (Prod)
호스트: localhost
포트: 5432
데이터베이스: twothree_db
사용자 이름: twothree_user
비밀번호: twothree_password
```

#### 고급 설정
```
드라이버 속성:
- ApplicationName: Twothree
- DefaultRowPrefetch: 50
- UseServerSidePrepare: true
```

## 🚀 빠른 시작

### 1. 개발 환경 설정
```bash
# 1. Spring Boot 애플리케이션 시작
cd backend
./gradlew bootRun

# 2. DBeaver에서 H2 연결 테스트
```

### 2. 프로덕션 환경 설정
```bash
# 1. 데이터베이스 환경 시작
./scripts/db-start.sh

# 2. DBeaver에서 PostgreSQL 연결 테스트
```

## 📊 데이터베이스 구조 확인

### 테이블 목록
- **Church**: 교회 정보
- **Department**: 부서 정보  
- **Member**: 멤버 정보
- **User**: 사용자 정보
- **MemberDepartment**: 멤버-부서 관계
- **Content**: 콘텐츠 정보
- **ContentDepartment**: 콘텐츠-부서 관계

### 샘플 쿼리

#### 1. 모든 교회 조회
```sql
SELECT * FROM church;
```

#### 2. 교회별 부서 수
```sql
SELECT 
    c.name as church_name,
    COUNT(d.id) as department_count
FROM church c
LEFT JOIN department d ON c.id = d.church_id
GROUP BY c.id, c.name;
```

#### 3. 교회별 멤버 수
```sql
SELECT 
    c.name as church_name,
    COUNT(m.id) as member_count
FROM church c
LEFT JOIN member m ON c.id = m.church_id
GROUP BY c.id, c.name;
```

#### 4. 부서별 멤버 조회
```sql
SELECT 
    d.name as department_name,
    m.name as member_name,
    m.role as member_role
FROM department d
JOIN member_department md ON d.id = md.department_id
JOIN member m ON md.member_id = m.id
ORDER BY d.name, m.name;
```

## 🛠️ DBeaver 유용한 기능들

### 1. SQL 편집기
- **Ctrl + Enter**: 쿼리 실행
- **Ctrl + Shift + Enter**: 현재 쿼리만 실행
- **Ctrl + /**: 주석 처리

### 2. 데이터 내보내기/가져오기
- **CSV, Excel, JSON** 형식 지원
- **데이터 내보내기**: 우클릭 → **데이터 내보내기**
- **데이터 가져오기**: 우클릭 → **데이터 가져오기**

### 3. ER 다이어그램
- **ERD 생성**: 테이블 우클릭 → **ER 다이어그램 생성**
- **관계 시각화**: 테이블 간 관계 확인

### 4. 데이터 편집
- **인라인 편집**: 셀 더블클릭
- **새 행 추가**: 마지막 행에서 Tab 키
- **행 삭제**: Ctrl + Delete

## 🔍 문제 해결

### 연결 실패
1. **Spring Boot 실행 확인**
   ```bash
   # H2 연결의 경우
   cd backend && ./gradlew bootRun
   ```

2. **Docker 컨테이너 확인**
   ```bash
   # PostgreSQL 연결의 경우
   docker-compose ps
   docker-compose logs postgres
   ```

3. **포트 확인**
   ```bash
   # H2: 8080, PostgreSQL: 5432
   lsof -i :8080
   lsof -i :5432
   ```

### 드라이버 문제
1. **드라이버 다운로드**: 연결 설정에서 **드라이버 다운로드** 클릭
2. **수동 드라이버 추가**: 필요한 경우 JAR 파일 직접 추가

### 성능 최적화
1. **연결 풀 설정**: 고급 설정에서 연결 풀 크기 조정
2. **쿼리 최적화**: EXPLAIN PLAN 사용
3. **인덱스 확인**: 테이블 구조에서 인덱스 확인

## 📚 유용한 단축키

| 기능 | Windows/Linux | macOS |
|------|---------------|-------|
| 새 연결 | Ctrl + D | Cmd + D |
| 새 SQL 편집기 | Ctrl + ] | Cmd + ] |
| 쿼리 실행 | Ctrl + Enter | Cmd + Enter |
| 쿼리 포맷팅 | Ctrl + Shift + F | Cmd + Shift + F |
| 북마크 | Ctrl + B | Cmd + B |
| 검색 | Ctrl + F | Cmd + F |

## 🎯 추천 워크플로우

### 개발자 워크플로우
1. **H2 연결**로 개발 중 데이터 확인
2. **SQL 편집기**에서 복잡한 쿼리 작성
3. **ER 다이어그램**으로 테이블 관계 확인
4. **데이터 내보내기**로 테스트 데이터 생성

### DBA 워크플로우
1. **PostgreSQL 연결**로 프로덕션 데이터 관리
2. **성능 모니터링**으로 쿼리 최적화
3. **백업/복원** 기능 활용
4. **사용자 권한** 관리

## 🔐 보안 주의사항

1. **비밀번호 저장**: 연결 설정에서 비밀번호 저장 여부 선택
2. **SSL 연결**: 프로덕션 환경에서는 SSL 사용 권장
3. **접근 제한**: 필요한 사용자만 DBeaver 접근 권한 부여
4. **정기 업데이트**: DBeaver 최신 버전 유지 