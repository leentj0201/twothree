import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Member, MemberStatus, MemberRole } from '../../types';
import apiService from '../../services/api';

// 비동기 액션들
export const fetchMembersByChurch = createAsyncThunk(
  'member/fetchMembersByChurch',
  async (churchId: number, { rejectWithValue }) => {
    try {
      const members = await apiService.getMembersByChurch(churchId);
      return members;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 목록을 불러오는데 실패했습니다.');
    }
  }
);

export const fetchMemberById = createAsyncThunk(
  'member/fetchMemberById',
  async (id: number, { rejectWithValue }) => {
    try {
      const member = await apiService.getMemberById(id);
      if (!member) {
        throw new Error('멤버를 찾을 수 없습니다.');
      }
      return member;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 정보를 불러오는데 실패했습니다.');
    }
  }
);

export const createMember = createAsyncThunk(
  'member/createMember',
  async (memberData: Omit<Member, 'id' | 'createdAt' | 'updatedAt'>, { rejectWithValue }) => {
    try {
      const newMember = await apiService.createMember(memberData);
      return newMember;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 생성에 실패했습니다.');
    }
  }
);

export const updateMember = createAsyncThunk(
  'member/updateMember',
  async ({ id, memberData }: { id: number; memberData: Partial<Member> }, { rejectWithValue }) => {
    try {
      const updatedMember = await apiService.updateMember(id, memberData);
      return updatedMember;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 수정에 실패했습니다.');
    }
  }
);

export const deleteMember = createAsyncThunk(
  'member/deleteMember',
  async (id: number, { rejectWithValue }) => {
    try {
      await apiService.deleteMember(id);
      return id;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 삭제에 실패했습니다.');
    }
  }
);

export const searchMembers = createAsyncThunk(
  'member/searchMembers',
  async ({ churchId, keyword }: { churchId: number; keyword: string }, { rejectWithValue }) => {
    try {
      const members = await apiService.searchMembers(churchId, keyword);
      return members;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : '멤버 검색에 실패했습니다.');
    }
  }
);

// 상태 타입 정의
interface MemberState {
  members: Member[];
  selectedMember: Member | null;
  loading: boolean;
  error: string | null;
  searchKeyword: string;
  selectedStatus: string;
  selectedRole: string;
  currentChurchId: number | null;
  totalCount: number;
  activeCount: number;
  inactiveCount: number;
  pendingCount: number;
  roleCounts: Record<MemberRole, number>;
}

// 초기 상태
const initialState: MemberState = {
  members: [],
  selectedMember: null,
  loading: false,
  error: null,
  searchKeyword: '',
  selectedStatus: 'ALL',
  selectedRole: 'ALL',
  currentChurchId: null,
  totalCount: 0,
  activeCount: 0,
  inactiveCount: 0,
  pendingCount: 0,
  roleCounts: {
    [MemberRole.PASTOR]: 0,
    [MemberRole.ELDER]: 0,
    [MemberRole.DEACON]: 0,
    [MemberRole.MEMBER]: 0,
  },
};

// Slice 생성
const memberSlice = createSlice({
  name: 'member',
  initialState,
  reducers: {
    // 동기 액션들
    setSearchKeyword: (state, action: PayloadAction<string>) => {
      state.searchKeyword = action.payload;
    },
    setSelectedStatus: (state, action: PayloadAction<string>) => {
      state.selectedStatus = action.payload;
    },
    setSelectedRole: (state, action: PayloadAction<string>) => {
      state.selectedRole = action.payload;
    },
    setCurrentChurchId: (state, action: PayloadAction<number>) => {
      state.currentChurchId = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    clearSelectedMember: (state) => {
      state.selectedMember = null;
    },
    updateStatistics: (state) => {
      state.totalCount = state.members.length;
      state.activeCount = state.members.filter(m => m.status === MemberStatus.ACTIVE).length;
      state.inactiveCount = state.members.filter(m => m.status === MemberStatus.INACTIVE).length;
      state.pendingCount = state.members.filter(m => m.status === MemberStatus.PENDING).length;
      
      // 역할별 통계
      state.roleCounts = {
        [MemberRole.PASTOR]: state.members.filter(m => m.role === MemberRole.PASTOR).length,
        [MemberRole.ELDER]: state.members.filter(m => m.role === MemberRole.ELDER).length,
        [MemberRole.DEACON]: state.members.filter(m => m.role === MemberRole.DEACON).length,
        [MemberRole.MEMBER]: state.members.filter(m => m.role === MemberRole.MEMBER).length,
      };
    },
  },
  extraReducers: (builder) => {
    // fetchMembersByChurch
    builder
      .addCase(fetchMembersByChurch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchMembersByChurch.fulfilled, (state, action) => {
        state.loading = false;
        state.members = action.payload;
        memberSlice.caseReducers.updateStatistics(state);
      })
      .addCase(fetchMembersByChurch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // fetchMemberById
    builder
      .addCase(fetchMemberById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchMemberById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedMember = action.payload;
      })
      .addCase(fetchMemberById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // createMember
    builder
      .addCase(createMember.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createMember.fulfilled, (state, action) => {
        state.loading = false;
        state.members.push(action.payload);
        memberSlice.caseReducers.updateStatistics(state);
      })
      .addCase(createMember.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // updateMember
    builder
      .addCase(updateMember.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateMember.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.members.findIndex(m => m.id === action.payload.id);
        if (index !== -1) {
          state.members[index] = action.payload;
        }
        if (state.selectedMember?.id === action.payload.id) {
          state.selectedMember = action.payload;
        }
        memberSlice.caseReducers.updateStatistics(state);
      })
      .addCase(updateMember.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // deleteMember
    builder
      .addCase(deleteMember.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteMember.fulfilled, (state, action) => {
        state.loading = false;
        state.members = state.members.filter(m => m.id !== action.payload);
        if (state.selectedMember?.id === action.payload) {
          state.selectedMember = null;
        }
        memberSlice.caseReducers.updateStatistics(state);
      })
      .addCase(deleteMember.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // searchMembers
    builder
      .addCase(searchMembers.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchMembers.fulfilled, (state, action) => {
        state.loading = false;
        state.members = action.payload;
        memberSlice.caseReducers.updateStatistics(state);
      })
      .addCase(searchMembers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

// 액션 생성자들 export
export const {
  setSearchKeyword,
  setSelectedStatus,
  setSelectedRole,
  setCurrentChurchId,
  clearError,
  clearSelectedMember,
} = memberSlice.actions;

// Selector들
export const selectMembers = (state: { member: MemberState }) => state.member.members;
export const selectSelectedMember = (state: { member: MemberState }) => state.member.selectedMember;
export const selectMemberLoading = (state: { member: MemberState }) => state.member.loading;
export const selectMemberError = (state: { member: MemberState }) => state.member.error;
export const selectSearchKeyword = (state: { member: MemberState }) => state.member.searchKeyword;
export const selectSelectedStatus = (state: { member: MemberState }) => state.member.selectedStatus;
export const selectSelectedRole = (state: { member: MemberState }) => state.member.selectedRole;
export const selectCurrentChurchId = (state: { member: MemberState }) => state.member.currentChurchId;
export const selectMemberStatistics = (state: { member: MemberState }) => ({
  total: state.member.totalCount,
  active: state.member.activeCount,
  inactive: state.member.inactiveCount,
  pending: state.member.pendingCount,
  roles: state.member.roleCounts,
});

// 필터링된 멤버 목록 selector
export const selectFilteredMembers = (state: { member: MemberState }) => {
  const { members, selectedStatus, selectedRole } = state.member;
  let filtered = members;

  if (selectedStatus !== 'ALL') {
    filtered = filtered.filter(member => member.status === selectedStatus);
  }

  if (selectedRole !== 'ALL') {
    filtered = filtered.filter(member => member.role === selectedRole);
  }

  return filtered;
};

export default memberSlice.reducer; 