# 교회 관리 시스템 - Flutter 앱

이 Flutter 앱은 백엔드 API와 연동하여 교회, 교인, 부서를 관리하는 모바일 애플리케이션입니다.

## 기능

- **교회 관리**: 교회 정보 등록, 수정, 삭제
- **교인 관리**: 교인 정보 등록, 수정, 삭제
- **부서 관리**: 부서 정보 등록, 수정, 삭제
- **실시간 데이터 동기화**: 백엔드 API와 실시간 연동

## 기술 스택

- **Flutter**: 3.8.1+
- **Dart**: 3.8.1+
- **Provider**: 상태 관리
- **HTTP**: API 통신
- **JSON Serialization**: 데이터 직렬화

## 설치 및 실행

### 1. Flutter 설치

Flutter SDK가 설치되어 있지 않다면 다음 명령어로 설치하세요:

```bash
# macOS
brew install flutter

# 또는 Flutter 공식 사이트에서 다운로드
# https://flutter.dev/docs/get-started/install
```

### 2. 의존성 설치

```bash
flutter pub get
```

### 3. JSON 직렬화 코드 생성

```bash
flutter packages pub run build_runner build
```

### 4. 앱 실행

```bash
flutter run
```

## 프로젝트 구조

```
lib/
├── enums/                 # Enum 클래스들
│   ├── church_status.dart
│   ├── member_status.dart
│   ├── member_role.dart
│   ├── department_status.dart
│   └── department_category.dart
├── models/                # 데이터 모델
│   ├── church.dart
│   ├── member.dart
│   └── department.dart
├── providers/             # 상태 관리
│   ├── church_provider.dart
│   ├── member_provider.dart
│   └── department_provider.dart
├── screens/               # UI 화면
│   ├── home_screen.dart
│   ├── church_list_screen.dart
│   ├── member_list_screen.dart
│   └── department_list_screen.dart
├── services/              # API 서비스
│   └── api_service.dart
└── main.dart              # 앱 진입점
```

## API 설정

앱은 기본적으로 `http://localhost:8080/api`를 백엔드 서버로 사용합니다.

서버 URL을 변경하려면 `lib/services/api_service.dart` 파일의 `baseUrl` 상수를 수정하세요:

```dart
static const String baseUrl = 'http://your-server-url:port/api';
```

## 주요 화면

### 1. 홈 화면
- 교회 관리 시스템 메인 화면
- 교회, 교인, 부서 관리 메뉴 제공
- 새 교회 추가 기능

### 2. 교회 목록 화면
- 등록된 교회 목록 표시
- 교회 정보 상세 보기
- 교회 정보 수정/삭제 기능

### 3. 교인 목록 화면
- 선택된 교회의 교인 목록 표시
- 교인 정보 상세 보기
- 교인 정보 수정/삭제 기능
- 역할별 필터링 (관리자, 목사, 리더, 교인)

### 4. 부서 목록 화면
- 선택된 교회의 부서 목록 표시
- 부서 정보 상세 보기
- 부서 정보 수정/삭제 기능
- 카테고리별 필터링 (교육, 전도, 예배 등)

## 데이터 모델

### Church (교회)
- id: 고유 식별자
- name: 교회명
- description: 설명
- address: 주소
- phone: 전화번호
- email: 이메일
- website: 웹사이트
- logoUrl: 로고 URL
- status: 상태 (활성/비활성/대기중)

### Member (교인)
- id: 고유 식별자
- name: 이름
- email: 이메일
- phone: 전화번호
- address: 주소
- birthDate: 생년월일
- gender: 성별
- profileImageUrl: 프로필 이미지 URL
- churchId: 소속 교회 ID
- departmentId: 소속 부서 ID
- status: 상태 (활성/비활성/대기중)
- role: 역할 (관리자/목사/리더/교인)

### Department (부서)
- id: 고유 식별자
- name: 부서명
- description: 설명
- color: 색상
- icon: 아이콘
- churchId: 소속 교회 ID
- category: 카테고리 (교육/전도/예배/봉사/청년/어린이/기도/재정/행정/기타)
- status: 상태 (활성/비활성)

## 개발 가이드

### 새 화면 추가
1. `lib/screens/` 디렉토리에 새 화면 파일 생성
2. `main.dart`에서 라우팅 설정
3. 필요한 Provider 연결

### 새 모델 추가
1. `lib/models/` 디렉토리에 모델 파일 생성
2. `@JsonSerializable()` 어노테이션 추가
3. `flutter packages pub run build_runner build` 실행

### 새 API 추가
1. `lib/services/api_service.dart`에 API 메서드 추가
2. 해당 Provider에 상태 관리 로직 추가
3. UI에서 Provider 사용

## 문제 해결

### 빌드 오류
```bash
# 의존성 정리
flutter clean
flutter pub get

# JSON 직렬화 코드 재생성
flutter packages pub run build_runner build --delete-conflicting-outputs
```

### API 연결 오류
1. 백엔드 서버가 실행 중인지 확인
2. `api_service.dart`의 `baseUrl` 설정 확인
3. 네트워크 연결 상태 확인

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.
