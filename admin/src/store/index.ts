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
        // Date 객체나 함수 등은 직렬화 검사에서 제외
        ignoredActions: ['persist/PERSIST'],
        ignoredPaths: ['ui.notifications'],
      },
    }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch; 