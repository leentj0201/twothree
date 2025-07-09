import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Church, ChurchStatus } from '../../types';
import apiService from '../../services/api';
import type { RootState } from '../index';

// 비동기 액션들
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

export const fetchChurchById = createAsyncThunk(
  'church/fetchChurchById',
  async (id: number, { rejectWithValue }) => {
    try {
      const church = await apiService.getChurchById(id);
      if (!church) {
        throw new Error('교회를 찾을 수 없습니다.');
      }
      return church;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 정보를 불러오는데 실패했습니다.');
    }
  }
);

export const createChurch = createAsyncThunk(
  'church/createChurch',
  async (churchData: Omit<Church, 'id' | 'createdAt' | 'updatedAt'>, { rejectWithValue }) => {
    try {
      const newChurch = await apiService.createChurch(churchData);
      return newChurch;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 생성에 실패했습니다.');
    }
  }
);

export const updateChurch = createAsyncThunk(
  'church/updateChurch',
  async ({ id, churchData }: { id: number; churchData: Partial<Church> }, { rejectWithValue }) => {
    try {
      const updatedChurch = await apiService.updateChurch(id, churchData);
      return updatedChurch;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 수정에 실패했습니다.');
    }
  }
);

export const deleteChurch = createAsyncThunk(
  'church/deleteChurch',
  async (id: number, { rejectWithValue }) => {
    try {
      await apiService.deleteChurch(id);
      return id;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 삭제에 실패했습니다.');
    }
  }
);

export const searchChurches = createAsyncThunk(
  'church/searchChurches',
  async (keyword: string, { rejectWithValue }) => {
    try {
      const churches = await apiService.searchChurches(keyword);
      return churches;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '교회 검색에 실패했습니다.');
    }
  }
);

// 상태 타입 정의
interface ChurchState {
  churches: Church[];
  selectedChurch: Church | null;
  loading: boolean;
  error: string | null;
  searchKeyword: string;
  selectedStatus: string;
  totalCount: number;
  activeCount: number;
  inactiveCount: number;
  pendingCount: number;
}

// 초기 상태
const initialState: ChurchState = {
  churches: [],
  selectedChurch: null,
  loading: false,
  error: null,
  searchKeyword: '',
  selectedStatus: 'ALL',
  totalCount: 0,
  activeCount: 0,
  inactiveCount: 0,
  pendingCount: 0,
};

// Slice 생성
const churchSlice = createSlice({
  name: 'church',
  initialState,
  reducers: {
    // 동기 액션들
    setSearchKeyword: (state, action: PayloadAction<string>) => {
      state.searchKeyword = action.payload;
    },
    setSelectedStatus: (state, action: PayloadAction<string>) => {
      state.selectedStatus = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    clearSelectedChurch: (state) => {
      state.selectedChurch = null;
    },
    updateStatistics: (state) => {
      state.totalCount = state.churches.length;
      state.activeCount = state.churches.filter(c => c.status === ChurchStatus.ACTIVE).length;
      state.inactiveCount = state.churches.filter(c => c.status === ChurchStatus.INACTIVE).length;
      state.pendingCount = state.churches.filter(c => c.status === ChurchStatus.PENDING).length;
    },
  },
  extraReducers: (builder) => {
    // fetchChurches
    builder
      .addCase(fetchChurches.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchChurches.fulfilled, (state, action) => {
        state.loading = false;
        state.churches = action.payload;
        churchSlice.caseReducers.updateStatistics(state);
      })
      .addCase(fetchChurches.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // fetchChurchById
    builder
      .addCase(fetchChurchById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchChurchById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedChurch = action.payload;
      })
      .addCase(fetchChurchById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // createChurch
    builder
      .addCase(createChurch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createChurch.fulfilled, (state, action) => {
        state.loading = false;
        state.churches.push(action.payload);
        churchSlice.caseReducers.updateStatistics(state);
      })
      .addCase(createChurch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // updateChurch
    builder
      .addCase(updateChurch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateChurch.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.churches.findIndex(c => c.id === action.payload.id);
        if (index !== -1) {
          state.churches[index] = action.payload;
        }
        if (state.selectedChurch?.id === action.payload.id) {
          state.selectedChurch = action.payload;
        }
        churchSlice.caseReducers.updateStatistics(state);
      })
      .addCase(updateChurch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // deleteChurch
    builder
      .addCase(deleteChurch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteChurch.fulfilled, (state, action) => {
        state.loading = false;
        state.churches = state.churches.filter(c => c.id !== action.payload);
        if (state.selectedChurch?.id === action.payload) {
          state.selectedChurch = null;
        }
        churchSlice.caseReducers.updateStatistics(state);
      })
      .addCase(deleteChurch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // searchChurches
    builder
      .addCase(searchChurches.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchChurches.fulfilled, (state, action) => {
        state.loading = false;
        state.churches = action.payload;
        churchSlice.caseReducers.updateStatistics(state);
      })
      .addCase(searchChurches.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

// 액션 생성자들 export
export const {
  setSearchKeyword,
  setSelectedStatus,
  clearError,
  clearSelectedChurch,
} = churchSlice.actions;

// Selector들
export const selectChurches = (state: RootState) => state.church.churches;
export const selectSelectedChurch = (state: RootState) => state.church.selectedChurch;
export const selectChurchLoading = (state: RootState) => state.church.loading;
export const selectChurchError = (state: RootState) => state.church.error;
export const selectSearchKeyword = (state: RootState) => state.church.searchKeyword;
export const selectSelectedStatus = (state: RootState) => state.church.selectedStatus;
export const selectChurchStatistics = (state: RootState) => ({
  total: state.church.totalCount,
  active: state.church.activeCount,
  inactive: state.church.inactiveCount,
  pending: state.church.pendingCount,
});

// 필터링된 교회 목록 selector
export const selectFilteredChurches = (state: RootState) => {
  const { churches, selectedStatus } = state.church;
  if (selectedStatus === 'ALL') {
    return churches;
  }
  return churches.filter(church => church.status === selectedStatus);
};

export default churchSlice.reducer; 