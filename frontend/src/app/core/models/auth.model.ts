export interface LoginUser {
  userId: string;
  displayName: string;
  email: string;
  roles: string[];
  companyCode: string;
  warehouseCode: string;
  customerCode: string;
  locale: string;
}

export interface LoginRequest {
  email: string;
  password: string;
  locale: string;
}

export interface TenantOption {
  warehouseCode: string;
  customerCode: string;
  customerName: string;
}

export interface TenantSelectionRequest {
  warehouseCode: string;
  customerCode: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: LoginUser;
  tenants: TenantOption[];
  tenantSelected: boolean;
}

export interface ApiResponse<T> {
  status: 'SUCCESS' | 'WARNING' | 'ERROR';
  data: T;
  totalRecords: number;
  page: number;
  size: number;
  messages: ApiMessage[];
}

export interface ApiMessage {
  code: string;
  message: string;
  field?: string;
  severity: 'INFO' | 'WARN' | 'ERROR';
}

export interface PageRequest {
  page: number;
  size: number;
  sortField?: string;
  sortOrder?: 'ASC' | 'DESC';
}
