import { Church, Member, Department, ApiResponse, PageResponse, SearchRequest } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class ApiService {
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const defaultOptions: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, { ...defaultOptions, ...options });
      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'API 요청 실패');
      }
      
      return data;
    } catch (error) {
      console.error('API 요청 오류:', error);
      throw error;
    }
  }

  // 교회 관련 API
  async getChurches(): Promise<Church[]> {
    const response = await this.request<Church[]>('/churches/list', {
      method: 'POST',
      body: JSON.stringify({}),
    });
    return response.data || [];
  }

  async getChurchById(id: number): Promise<Church | null> {
    const response = await this.request<Church>('/churches/get', {
      method: 'POST',
      body: JSON.stringify({ churchId: id }),
    });
    return response.data || null;
  }

  async createChurch(church: Omit<Church, 'id' | 'createdAt' | 'updatedAt'>): Promise<Church> {
    const response = await this.request<Church>('/churches/create', {
      method: 'POST',
      body: JSON.stringify(church),
    });
    return response.data!;
  }

  async updateChurch(id: number, church: Partial<Church>): Promise<Church> {
    const response = await this.request<Church>('/churches/update', {
      method: 'POST',
      body: JSON.stringify({
        churchId: id,
        churchDto: church,
      }),
    });
    return response.data!;
  }

  async deleteChurch(id: number): Promise<boolean> {
    await this.request('/churches/delete', {
      method: 'POST',
      body: JSON.stringify({ churchId: id }),
    });
    return true;
  }

  async searchChurches(keyword: string): Promise<Church[]> {
    const response = await this.request<Church[]>('/churches/search', {
      method: 'POST',
      body: JSON.stringify({ keyword }),
    });
    return response.data || [];
  }

  // 멤버 관련 API
  async getMembersByChurch(churchId: number): Promise<Member[]> {
    const response = await this.request<Member[]>('/members/list-by-church', {
      method: 'POST',
      body: JSON.stringify({ churchId }),
    });
    return response.data || [];
  }

  async getMemberById(id: number): Promise<Member | null> {
    const response = await this.request<Member>('/members/get', {
      method: 'POST',
      body: JSON.stringify({ memberId: id }),
    });
    return response.data || null;
  }

  async createMember(member: Omit<Member, 'id' | 'createdAt' | 'updatedAt'>): Promise<Member> {
    const response = await this.request<Member>('/members/create', {
      method: 'POST',
      body: JSON.stringify(member),
    });
    return response.data!;
  }

  async updateMember(id: number, member: Partial<Member>): Promise<Member> {
    const response = await this.request<Member>('/members/update', {
      method: 'POST',
      body: JSON.stringify({
        memberId: id,
        memberDto: member,
      }),
    });
    return response.data!;
  }

  async deleteMember(id: number): Promise<boolean> {
    await this.request('/members/delete', {
      method: 'POST',
      body: JSON.stringify({ memberId: id }),
    });
    return true;
  }

  async searchMembers(churchId: number, keyword: string): Promise<Member[]> {
    const response = await this.request<Member[]>('/members/search', {
      method: 'POST',
      body: JSON.stringify({ churchId, keyword }),
    });
    return response.data || [];
  }

  // 부서 관련 API
  async getDepartmentsByChurch(churchId: number): Promise<Department[]> {
    const response = await this.request<Department[]>('/departments/church/departments', {
      method: 'POST',
      body: JSON.stringify({ churchId }),
    });
    return response.data || [];
  }

  async getDepartmentById(id: number): Promise<Department | null> {
    const response = await this.request<Department>('/departments/get', {
      method: 'POST',
      body: JSON.stringify({ departmentId: id }),
    });
    return response.data || null;
  }

  async createDepartment(department: Omit<Department, 'id' | 'createdAt' | 'updatedAt'>): Promise<Department> {
    const response = await this.request<Department>('/departments/create', {
      method: 'POST',
      body: JSON.stringify(department),
    });
    return response.data!;
  }

  async updateDepartment(id: number, department: Partial<Department>): Promise<Department> {
    const response = await this.request<Department>('/departments/update', {
      method: 'POST',
      body: JSON.stringify({
        departmentId: id,
        departmentDto: department,
      }),
    });
    return response.data!;
  }

  async deleteDepartment(id: number): Promise<boolean> {
    await this.request('/departments/delete', {
      method: 'POST',
      body: JSON.stringify({ departmentId: id }),
    });
    return true;
  }

  async searchDepartments(churchId: number, keyword: string): Promise<Department[]> {
    const response = await this.request<Department[]>('/departments/search-by-church', {
      method: 'POST',
      body: JSON.stringify({ churchId, keyword }),
    });
    return response.data || [];
  }
}

export const apiService = new ApiService();
export default apiService; 