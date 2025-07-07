# 교회 관리 시스템 API

## 개요
교회, 회원, 교회부서를 관리하는 RESTful API 시스템입니다.

## 주요 기능
- 교회 정보 관리 (CRUD)
- 교회부서 관리 (계층 구조 지원)
- 회원 관리 (교회별, 부서별, 역할별)
- 검색 및 필터링 기능

## API 엔드포인트

### 교회 (Churches)

#### 교회 목록 조회
```
GET /api/churches
```

#### 교회 상세 조회
```
GET /api/churches/{id}
GET /api/churches/name/{name}
```

#### 교회 상태별 조회
```
GET /api/churches/status/{status}
```

#### 교회 검색
```
GET /api/churches/search?keyword={keyword}
```

#### 교회 생성
```
POST /api/churches
Content-Type: application/json

{
  "name": "교회명",
  "description": "교회 설명",
  "address": "주소",
  "phone": "전화번호",
  "email": "이메일",
  "website": "웹사이트",
  "pastorName": "담임목사명",
  "pastorPhone": "목사 전화번호",
  "pastorEmail": "목사 이메일",
  "logoUrl": "로고 URL",
  "bannerUrl": "배너 URL",
  "status": "ACTIVE"
}
```

#### 교회 수정
```
PUT /api/churches/{id}
```

#### 교회 삭제
```
DELETE /api/churches/{id}
```

#### 중복 확인
```
GET /api/churches/check/name/{name}
GET /api/churches/check/email/{email}
```

### 부서 (Departments)

#### 부서 목록 조회
```
GET /api/departments
GET /api/departments/church/{churchId}
GET /api/departments/church/{churchId}/active
GET /api/departments/church/{churchId}/root
```

#### 하위 부서 조회
```
GET /api/departments/parent/{parentDepartmentId}
```

#### 부서 상세 조회
```
GET /api/departments/{id}
```

#### 부서 검색
```
GET /api/departments/church/{churchId}/search?keyword={keyword}
```

#### 부서 생성
```
POST /api/departments
Content-Type: application/json

{
  "name": "부서명",
  "description": "부서 설명",
  "color": "#FF0000",
  "icon": "icon-name",
  "churchId": 1,
  "parentDepartmentId": null,
  "status": "ACTIVE"
}
```

#### 부서 수정
```
PUT /api/departments/{id}
```

#### 부서 삭제
```
DELETE /api/departments/{id}
```

#### 중복 확인
```
GET /api/departments/check/name?name={name}&churchId={churchId}
```

### 회원 (Members)

#### 회원 목록 조회
```
GET /api/members
GET /api/members/church/{churchId}
GET /api/members/church/{churchId}/page?page=0&size=10
```

#### 회원 필터링
```
GET /api/members/church/{churchId}/status/{status}
GET /api/members/department/{departmentId}
GET /api/members/church/{churchId}/department/{departmentId}
GET /api/members/church/{churchId}/role/{role}
```

#### 회원 상세 조회
```
GET /api/members/{id}
GET /api/members/email/{email}
```

#### 회원 검색
```
GET /api/members/church/{churchId}/search?keyword={keyword}
```

#### 날짜 범위 조회
```
GET /api/members/church/{churchId}/birth-date?startDate=2023-01-01&endDate=2023-12-31
GET /api/members/church/{churchId}/membership-date?startDate=2023-01-01&endDate=2023-12-31
```

#### 회원 생성
```
POST /api/members
Content-Type: application/json

{
  "name": "회원명",
  "email": "이메일",
  "phone": "전화번호",
  "address": "주소",
  "birthDate": "1990-01-01",
  "gender": "MALE",
  "status": "ACTIVE",
  "role": "MEMBER",
  "profileImageUrl": "프로필 이미지 URL",
  "baptismDate": "2020-01-01",
  "membershipDate": "2020-01-01",
  "notes": "비고",
  "churchId": 1,
  "departmentId": 1
}
```

#### 회원 수정
```
PUT /api/members/{id}
```

#### 회원 삭제
```
DELETE /api/members/{id}
```

#### 중복 확인
```
GET /api/members/check/email/{email}
GET /api/members/check/email?email={email}&churchId={churchId}
```

## 데이터 모델

### Church (교회)
- id: 고유 식별자
- name: 교회명 (고유)
- description: 교회 설명
- address: 주소
- phone: 전화번호
- email: 이메일
- website: 웹사이트
- pastorName: 담임목사명
- pastorPhone: 목사 전화번호
- pastorEmail: 목사 이메일
- logoUrl: 로고 URL
- bannerUrl: 배너 URL
- status: 상태 (ACTIVE, INACTIVE, SUSPENDED)
- createdAt: 생성일시
- updatedAt: 수정일시

### Department (부서)
- id: 고유 식별자
- name: 부서명
- description: 부서 설명
- color: 색상 코드
- icon: 아이콘명
- churchId: 소속 교회 ID
- parentDepartmentId: 상위 부서 ID (계층 구조)
- status: 상태 (ACTIVE, INACTIVE, ARCHIVED)
- createdAt: 생성일시
- updatedAt: 수정일시

### Member (회원)
- id: 고유 식별자
- name: 회원명
- email: 이메일 (고유)
- phone: 전화번호
- address: 주소
- birthDate: 생년월일
- gender: 성별 (MALE, FEMALE, OTHER)
- status: 상태 (ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN)
- role: 역할 (ADMIN, PASTOR, ELDER, DEACON, MEMBER, GUEST)
- profileImageUrl: 프로필 이미지 URL
- baptismDate: 세례일
- membershipDate: 입교일
- notes: 비고
- churchId: 소속 교회 ID
- departmentId: 소속 부서 ID
- createdAt: 생성일시
- updatedAt: 수정일시

## 상태 코드
- 200: 성공
- 201: 생성 성공
- 204: 삭제 성공
- 400: 잘못된 요청
- 404: 리소스를 찾을 수 없음
- 500: 서버 오류

## 사용 예시

### 1. 교회 생성
```bash
curl -X POST http://localhost:8080/api/churches \
  -H "Content-Type: application/json" \
  -d '{
    "name": "새로운교회",
    "description": "새로운교회입니다",
    "address": "서울시 강남구",
    "phone": "02-1234-5678",
    "email": "church@example.com",
    "status": "ACTIVE"
  }'
```

### 2. 부서 생성
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "청년부",
    "description": "청년부입니다",
    "color": "#FF0000",
    "churchId": 1,
    "status": "ACTIVE"
  }'
```

### 3. 회원 생성
```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "email": "hong@example.com",
    "phone": "010-1234-5678",
    "gender": "MALE",
    "churchId": 1,
    "departmentId": 1,
    "status": "ACTIVE",
    "role": "MEMBER"
  }'
``` 