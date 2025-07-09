// 교회 관련 타입
export interface Church {
  id: number;
  name: string;
  description?: string;
  address: string;
  phone?: string;
  email?: string;
  website?: string;
  pastorName?: string;
  pastorPhone?: string;
  pastorEmail?: string;
  logoUrl?: string;
  bannerUrl?: string;
  status: ChurchStatus;
  createdAt: string;
  updatedAt: string;
}

export enum ChurchStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  PENDING = 'PENDING'
}

// 멤버 관련 타입
export interface Member {
  id: number;
  name: string;
  email: string;
  phone?: string;
  address?: string;
  birthDate?: string;
  gender?: Gender;
  profileImageUrl?: string;
  baptismDate?: string;
  membershipDate?: string;
  notes?: string;
  status: MemberStatus;
  role: MemberRole;
  churchId: number;
  departmentId?: number;
  createdAt: string;
  updatedAt: string;
}

export enum MemberStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  PENDING = 'PENDING'
}

export enum MemberRole {
  PASTOR = 'PASTOR',
  ELDER = 'ELDER',
  DEACON = 'DEACON',
  MEMBER = 'MEMBER'
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

// 부서 관련 타입
export interface Department {
  id: number;
  name: string;
  description?: string;
  category: DepartmentCategory;
  status: DepartmentStatus;
  churchId: number;
  createdAt: string;
  updatedAt: string;
}

export enum DepartmentCategory {
  WORSHIP = 'WORSHIP',
  EDUCATION = 'EDUCATION',
  SERVICE = 'SERVICE',
  ADMINISTRATION = 'ADMINISTRATION',
  OTHER = 'OTHER'
}

export enum DepartmentStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

// API 응답 타입
export interface ApiResponse<T> {
  timestamp: string;
  success: boolean;
  message?: string;
  data?: T;
  errorCode?: string;
}

// 페이지네이션 타입
export interface PageRequest {
  page: number;
  size: number;
  sort?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// 검색 요청 타입
export interface SearchRequest {
  keyword?: string;
  status?: string;
  page?: number;
  size?: number;
} 