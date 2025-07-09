import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import type { RootState } from '../index';

// 알림 타입
interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
}

// UI 상태 타입
interface UIState {
  sidebarOpen: boolean;
  theme: 'light' | 'dark';
  notifications: Notification[];
  modal: {
    isOpen: boolean;
    type: string | null;
    data: any;
  };
  loading: {
    global: boolean;
    specific: Record<string, boolean>;
  };
}

// 초기 상태
const initialState: UIState = {
  sidebarOpen: false,
  theme: 'light',
  notifications: [],
  modal: {
    isOpen: false,
    type: null,
    data: null,
  },
  loading: {
    global: false,
    specific: {},
  },
};

// Slice 생성
const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    // 사이드바 토글
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },

    // 테마 변경
    setTheme: (state, action: PayloadAction<'light' | 'dark'>) => {
      state.theme = action.payload;
      localStorage.setItem('theme', action.payload);
    },

    // 알림 관리
    addNotification: (state, action: PayloadAction<Omit<Notification, 'id'>>) => {
      const id = Date.now().toString();
      const notification = { ...action.payload, id };
      state.notifications.push(notification);

      // 자동 제거 (기본 5초)
      const duration = action.payload.duration || 5000;
      setTimeout(() => {
        // 실제로는 별도의 액션을 디스패치해야 하지만, 여기서는 간단히 처리
        state.notifications = state.notifications.filter(n => n.id !== id);
      }, duration);
    },
    removeNotification: (state, action: PayloadAction<string>) => {
      state.notifications = state.notifications.filter(n => n.id !== action.payload);
    },
    clearNotifications: (state) => {
      state.notifications = [];
    },

    // 모달 관리
    openModal: (state, action: PayloadAction<{ type: string; data?: any }>) => {
      state.modal.isOpen = true;
      state.modal.type = action.payload.type;
      state.modal.data = action.payload.data || null;
    },
    closeModal: (state) => {
      state.modal.isOpen = false;
      state.modal.type = null;
      state.modal.data = null;
    },

    // 로딩 상태 관리
    setGlobalLoading: (state, action: PayloadAction<boolean>) => {
      state.loading.global = action.payload;
    },
    setSpecificLoading: (state, action: PayloadAction<{ key: string; loading: boolean }>) => {
      const { key, loading } = action.payload;
      if (loading) {
        state.loading.specific[key] = true;
      } else {
        delete state.loading.specific[key];
      }
    },
    clearSpecificLoading: (state) => {
      state.loading.specific = {};
    },
  },
});

// 액션 생성자들 export
export const {
  toggleSidebar,
  setSidebarOpen,
  setTheme,
  addNotification,
  removeNotification,
  clearNotifications,
  openModal,
  closeModal,
  setGlobalLoading,
  setSpecificLoading,
  clearSpecificLoading,
} = uiSlice.actions;

// Selector들
export const selectSidebarOpen = (state: RootState) => state.ui.sidebarOpen;
export const selectTheme = (state: RootState) => state.ui.theme;
export const selectNotifications = (state: RootState) => state.ui.notifications;
export const selectModal = (state: RootState) => state.ui.modal;
export const selectGlobalLoading = (state: RootState) => state.ui.loading.global;
export const selectSpecificLoading = (state: RootState) => state.ui.loading.specific;
export const selectIsLoading = (state: RootState) => 
  state.ui.loading.global || Object.keys(state.ui.loading.specific).length > 0;

// 특정 로딩 상태 확인
export const selectIsLoadingByKey = (key: string) => (state: RootState) => 
  state.ui.loading.specific[key] || false;

export default uiSlice.reducer; 