# 상태관리 (State Management) 구현

## 개요

Admin 프론트엔드에 Redux Toolkit을 사용한 전역 상태관리를 구현했습니다. 이를 통해 애플리케이션의 상태를 중앙에서 관리하고, 컴포넌트 간 데이터 공유를 효율적으로 처리할 수 있습니다.

## 기술 스택

- **Redux Toolkit**: 현대적인 Redux 개발을 위한 도구
- **React Redux**: React와 Redux 연결
- **TypeScript**: 타입 안전성 보장

## 구조

### 1. Store 설정 (`src/store/index.ts`)

```typescript
import { configureStore } from '@reduxjs/toolkit';
import churchReducer from './slices/churchSlice';
import memberReducer from './slices/memberSlice';
import departmentReducer from './slices/departmentSlice';
import authReducer from './slices/authSlice';
import uiReducer from './slices/uiSlice';

export const store = configureStore({
  reducer: {
    church: churchReducer,
    member: memberReducer,
    department: departmentReducer,
    auth: authReducer,
    ui: uiReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST'],
        ignoredPaths: ['ui.notifications'],
      },
    }),
});
```

### 2. Slice 구조

각 도메인별로 독립적인 slice를 생성하여 관심사를 분리했습니다:

#### Church Slice (`src/store/slices/churchSlice.ts`)
- 교회 목록 관리
- 교회 CRUD 작업
- 검색 및 필터링
- 통계 정보

#### Member Slice (`src/store/slices/memberSlice.ts`)
- 멤버 목록 관리
- 멤버 CRUD 작업
- 역할별 필터링
- 교회별 멤버 관리

#### Department Slice (`src/store/slices/departmentSlice.ts`)
- 부서 목록 관리
- 부서 CRUD 작업
- 카테고리별 필터링
- 교회별 부서 관리

#### Auth Slice (`src/store/slices/authSlice.ts`)
- 사용자 인증 상태
- 로그인/로그아웃
- 토큰 관리
- 사용자 정보

#### UI Slice (`src/store/slices/uiSlice.ts`)
- 사이드바 상태
- 테마 설정
- 알림 관리
- 모달 상태
- 로딩 상태

### 3. 비동기 액션 (Async Thunks)

각 slice에서 API 호출을 위한 비동기 액션을 정의했습니다:

```typescript
export const fetchChurches = createAsyncThunk(
  'church/fetchChurches',
  async (_, { rejectWithValue }) => {
    try {
      const churches = await apiService.getChurches();
      return churches;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 목록을 불러오는데 실패했습니다.');
    }
  }
);
```

### 4. Selector 패턴

성능 최적화를 위해 selector를 사용하여 상태 접근을 최적화했습니다:

```typescript
export const selectFilteredChurches = (state: RootState) => {
  const { churches, selectedStatus } = state.church;
  if (selectedStatus === 'ALL') {
    return churches;
  }
  return churches.filter(church => church.status === selectedStatus);
};
```

### 5. 타입 안전성

TypeScript를 사용하여 타입 안전성을 보장했습니다:

```typescript
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
```

## 주요 기능

### 1. 상태 관리
- **중앙 집중식 상태**: 모든 애플리케이션 상태를 store에서 관리
- **불변성**: Immer를 사용하여 상태 업데이트를 간단하게 처리
- **타입 안전성**: TypeScript로 컴파일 타임 오류 방지

### 2. 비동기 처리
- **Async Thunks**: API 호출을 위한 비동기 액션
- **로딩 상태**: 각 작업의 로딩 상태를 추적
- **에러 처리**: 통합된 에러 처리 및 사용자 알림

### 3. 성능 최적화
- **Selector**: 메모이제이션을 통한 불필요한 리렌더링 방지
- **정규화**: 중복 데이터 제거 및 효율적인 상태 구조
- **지연 로딩**: 필요할 때만 데이터 로드

### 4. 개발자 경험
- **Redux DevTools**: 상태 변화 추적 및 디버깅
- **액션 로깅**: 모든 액션과 상태 변화 기록
- **타입 추론**: 자동 완성 및 타입 체크

## 사용 예시

### 컴포넌트에서 상태 사용

```typescript
import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { fetchChurches, selectChurches, selectChurchLoading } from '../store/slices/churchSlice';

const ChurchList: React.FC = () => {
  const dispatch = useAppDispatch();
  const churches = useAppSelector(selectChurches);
  const loading = useAppSelector(selectChurchLoading);

  useEffect(() => {
    dispatch(fetchChurches());
  }, [dispatch]);

  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div>
      {churches.map(church => (
        <div key={church.id}>{church.name}</div>
      ))}
    </div>
  );
};
```

### 액션 디스패치

```typescript
import { useAppDispatch } from '../store/hooks';
import { createChurch, addNotification } from '../store/slices/churchSlice';

const CreateChurchForm: React.FC = () => {
  const dispatch = useAppDispatch();

  const handleSubmit = async (churchData: ChurchFormData) => {
    try {
      await dispatch(createChurch(churchData)).unwrap();
      dispatch(addNotification({
        type: 'success',
        message: '교회가 성공적으로 생성되었습니다.',
      }));
    } catch (error) {
      dispatch(addNotification({
        type: 'error',
        message: '교회 생성에 실패했습니다.',
      }));
    }
  };

  return (
    // 폼 컴포넌트
  );
};
```

## 알림 시스템

중앙화된 알림 시스템을 구현했습니다:

```typescript
// 알림 추가
dispatch(addNotification({
  type: 'success',
  message: '작업이 성공했습니다.',
  duration: 5000, // 5초 후 자동 제거
}));

// 알림 제거
dispatch(removeNotification(notificationId));
```

## 로딩 상태 관리

전역 및 특정 작업별 로딩 상태를 관리합니다:

```typescript
// 전역 로딩 상태
const globalLoading = useAppSelector(selectGlobalLoading);

// 특정 작업 로딩 상태
const isCreatingChurch = useAppSelector(selectIsLoadingByKey('createChurch'));
```

## 모달 상태 관리

모달의 열림/닫힘 상태를 중앙에서 관리합니다:

```typescript
// 모달 열기
dispatch(openModal({ type: 'deleteConfirm', data: { id: 1 } }));

// 모달 닫기
dispatch(closeModal());
```

## 장점

1. **예측 가능한 상태 변화**: 모든 상태 변화가 액션을 통해 이루어짐
2. **중앙 집중식 상태 관리**: 애플리케이션 전체 상태를 한 곳에서 관리
3. **개발자 도구**: Redux DevTools로 상태 변화 추적 가능
4. **성능 최적화**: Selector를 통한 효율적인 리렌더링
5. **타입 안전성**: TypeScript로 런타임 오류 방지
6. **테스트 용이성**: 순수 함수로 구성되어 테스트하기 쉬움

## 다음 단계

1. **상태 지속성**: Redux Persist를 사용한 상태 저장
2. **실시간 업데이트**: WebSocket을 통한 실시간 상태 동기화
3. **캐싱 전략**: React Query와의 통합으로 캐싱 최적화
4. **코드 분할**: 동적 import를 통한 번들 크기 최적화
5. **성능 모니터링**: 상태 변화에 대한 성능 측정 도구 추가 