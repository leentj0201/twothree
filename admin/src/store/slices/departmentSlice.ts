import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Department, DepartmentStatus, DepartmentCategory } from '../../types';
import apiService from '../../services/api';

// 비동기 액션들
export const fetchDepartmentsByChurch = createAsyncThunk(
  'department/fetchDepartmentsByChurch',
  async (churchId: number, { rejectWithValue }) => {
    try {
      const departments = await apiService.getDepartmentsByChurch(churchId);
      return departments;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 목록을 불러오는데 실패했습니다.');
    }
  }
);

export const fetchDepartmentById = createAsyncThunk(
  'department/fetchDepartmentById',
  async (id: number, { rejectWithValue }) => {
    try {
      const department = await apiService.getDepartmentById(id);
      if (!department) {
        throw new Error('부서를 찾을 수 없습니다.');
      }
      return department;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 정보를 불러오는데 실패했습니다.');
    }
  }
);

export const createDepartment = createAsyncThunk(
  'department/createDepartment',
  async (departmentData: Omit<Department, 'id' | 'createdAt' | 'updatedAt'>, { rejectWithValue }) => {
    try {
      const newDepartment = await apiService.createDepartment(departmentData);
      return newDepartment;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 생성에 실패했습니다.');
    }
  }
);

export const updateDepartment = createAsyncThunk(
  'department/updateDepartment',
  async ({ id, departmentData }: { id: number; departmentData: Partial<Department> }, { rejectWithValue }) => {
    try {
      const updatedDepartment = await apiService.updateDepartment(id, departmentData);
      return updatedDepartment;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 수정에 실패했습니다.');
    }
  }
);

export const deleteDepartment = createAsyncThunk(
  'department/deleteDepartment',
  async (id: number, { rejectWithValue }) => {
    try {
      await apiService.deleteDepartment(id);
      return id;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 삭제에 실패했습니다.');
    }
  }
);

export const searchDepartments = createAsyncThunk(
  'department/searchDepartments',
  async ({ churchId, keyword }: { churchId: number; keyword: string }, { rejectWithValue }) => {
    try {
      const departments = await apiService.searchDepartments(churchId, keyword);
      return departments;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '부서 검색에 실패했습니다.');
    }
  }
);

// 상태 타입 정의
interface DepartmentState {
  departments: Department[];
  selectedDepartment: Department | null;
  loading: boolean;
  error: string | null;
  searchKeyword: string;
  selectedStatus: string;
  selectedCategory: string;
  currentChurchId: number | null;
  totalCount: number;
  activeCount: number;
  inactiveCount: number;
  categoryCounts: Record<DepartmentCategory, number>;
}

// 초기 상태
const initialState: DepartmentState = {
  departments: [],
  selectedDepartment: null,
  loading: false,
  error: null,
  searchKeyword: '',
  selectedStatus: 'ALL',
  selectedCategory: 'ALL',
  currentChurchId: null,
  totalCount: 0,
  activeCount: 0,
  inactiveCount: 0,
  categoryCounts: {
    [DepartmentCategory.WORSHIP]: 0,
    [DepartmentCategory.EDUCATION]: 0,
    [DepartmentCategory.SERVICE]: 0,
    [DepartmentCategory.ADMINISTRATION]: 0,
    [DepartmentCategory.OTHER]: 0,
  },
};

// Slice 생성
const departmentSlice = createSlice({
  name: 'department',
  initialState,
  reducers: {
    // 동기 액션들
    setSearchKeyword: (state, action: PayloadAction<string>) => {
      state.searchKeyword = action.payload;
    },
    setSelectedStatus: (state, action: PayloadAction<string>) => {
      state.selectedStatus = action.payload;
    },
    setSelectedCategory: (state, action: PayloadAction<string>) => {
      state.selectedCategory = action.payload;
    },
    setCurrentChurchId: (state, action: PayloadAction<number>) => {
      state.currentChurchId = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    clearSelectedDepartment: (state) => {
      state.selectedDepartment = null;
    },
    updateStatistics: (state) => {
      state.totalCount = state.departments.length;
      state.activeCount = state.departments.filter(d => d.status === DepartmentStatus.ACTIVE).length;
      state.inactiveCount = state.departments.filter(d => d.status === DepartmentStatus.INACTIVE).length;
      
      // 카테고리별 통계
      state.categoryCounts = {
        [DepartmentCategory.WORSHIP]: state.departments.filter(d => d.category === DepartmentCategory.WORSHIP).length,
        [DepartmentCategory.EDUCATION]: state.departments.filter(d => d.category === DepartmentCategory.EDUCATION).length,
        [DepartmentCategory.SERVICE]: state.departments.filter(d => d.category === DepartmentCategory.SERVICE).length,
        [DepartmentCategory.ADMINISTRATION]: state.departments.filter(d => d.category === DepartmentCategory.ADMINISTRATION).length,
        [DepartmentCategory.OTHER]: state.departments.filter(d => d.category === DepartmentCategory.OTHER).length,
      };
    },
  },
  extraReducers: (builder) => {
    // fetchDepartmentsByChurch
    builder
      .addCase(fetchDepartmentsByChurch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDepartmentsByChurch.fulfilled, (state, action) => {
        state.loading = false;
        state.departments = action.payload;
        departmentSlice.caseReducers.updateStatistics(state);
      })
      .addCase(fetchDepartmentsByChurch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // fetchDepartmentById
    builder
      .addCase(fetchDepartmentById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDepartmentById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedDepartment = action.payload;
      })
      .addCase(fetchDepartmentById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // createDepartment
    builder
      .addCase(createDepartment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createDepartment.fulfilled, (state, action) => {
        state.loading = false;
        state.departments.push(action.payload);
        departmentSlice.caseReducers.updateStatistics(state);
      })
      .addCase(createDepartment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // updateDepartment
    builder
      .addCase(updateDepartment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateDepartment.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.departments.findIndex(d => d.id === action.payload.id);
        if (index !== -1) {
          state.departments[index] = action.payload;
        }
        if (state.selectedDepartment?.id === action.payload.id) {
          state.selectedDepartment = action.payload;
        }
        departmentSlice.caseReducers.updateStatistics(state);
      })
      .addCase(updateDepartment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // deleteDepartment
    builder
      .addCase(deleteDepartment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteDepartment.fulfilled, (state, action) => {
        state.loading = false;
        state.departments = state.departments.filter(d => d.id !== action.payload);
        if (state.selectedDepartment?.id === action.payload) {
          state.selectedDepartment = null;
        }
        departmentSlice.caseReducers.updateStatistics(state);
      })
      .addCase(deleteDepartment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // searchDepartments
    builder
      .addCase(searchDepartments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchDepartments.fulfilled, (state, action) => {
        state.loading = false;
        state.departments = action.payload;
        departmentSlice.caseReducers.updateStatistics(state);
      })
      .addCase(searchDepartments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

// 액션 생성자들 export
export const {
  setSearchKeyword,
  setSelectedStatus,
  setSelectedCategory,
  setCurrentChurchId,
  clearError,
  clearSelectedDepartment,
} = departmentSlice.actions;

// Selector들
export const selectDepartments = (state: { department: DepartmentState }) => state.department.departments;
export const selectSelectedDepartment = (state: { department: DepartmentState }) => state.department.selectedDepartment;
export const selectDepartmentLoading = (state: { department: DepartmentState }) => state.department.loading;
export const selectDepartmentError = (state: { department: DepartmentState }) => state.department.error;
export const selectSearchKeyword = (state: { department: DepartmentState }) => state.department.searchKeyword;
export const selectSelectedStatus = (state: { department: DepartmentState }) => state.department.selectedStatus;
export const selectSelectedCategory = (state: { department: DepartmentState }) => state.department.selectedCategory;
export const selectCurrentChurchId = (state: { department: DepartmentState }) => state.department.currentChurchId;
export const selectDepartmentStatistics = (state: { department: DepartmentState }) => ({
  total: state.department.totalCount,
  active: state.department.activeCount,
  inactive: state.department.inactiveCount,
  categories: state.department.categoryCounts,
});

// 필터링된 부서 목록 selector
export const selectFilteredDepartments = (state: { department: DepartmentState }) => {
  const { departments, selectedStatus, selectedCategory } = state.department;
  let filtered = departments;

  if (selectedStatus !== 'ALL') {
    filtered = filtered.filter(department => department.status === selectedStatus);
  }

  if (selectedCategory !== 'ALL') {
    filtered = filtered.filter(department => department.category === selectedCategory);
  }

  return filtered;
};

export default departmentSlice.reducer; 