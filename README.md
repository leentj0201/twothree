# Twothree Project

Spring Boot + React + Flutter 기반의 풀스택 프로젝트입니다.

## 프로젝트 구조

```
twothree/
├── backend/    # Spring Boot 백엔드 API 서버
├── admin/      # React 기반 어드민 웹앱
└── app/        # Flutter 모바일 앱
```

## 실행 방법

### 1. Backend (Spring Boot)
```bash
cd backend
./gradlew bootRun
```
- 서버가 실행되면 http://localhost:8080 에서 접근 가능

### 2. Admin (React)
```bash
cd admin
npm start
```
- 개발 서버가 실행되면 http://localhost:3000 에서 접근 가능

### 3. App (Flutter)
```bash
cd app
flutter run
```
- iOS 시뮬레이터나 Android 에뮬레이터에서 실행

## 기술 스택

- **Backend**: Spring Boot (Java)
- **Admin**: React (TypeScript) + PWA
- **App**: Flutter (Dart)

## 개발 환경 설정

### 필수 요구사항
- Java 17+
- Node.js 18+
- Flutter 3.0+
- Gradle

### 설치 확인
```bash
java -version
node --version
flutter --version
``` 