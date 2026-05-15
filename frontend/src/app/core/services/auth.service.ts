import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError, BehaviorSubject } from 'rxjs';
import { environment } from '@env/environment';
import { LoginRequest, LoginResponse, LoginUser, ApiResponse, TenantOption, TenantSelectionRequest } from '@core/models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private currentUser = signal<LoginUser | null>(null);
  private _tenantSelected = signal<boolean>(false);
  private _tenants = signal<TenantOption[]>([]);

  readonly user = this.currentUser.asReadonly();
  readonly isAuthenticated = computed(() => !!this.currentUser() && !this.isTokenExpired());
  readonly hasTenantSelected = computed(() => this._tenantSelected());
  readonly tenants = this._tenants.asReadonly();
  readonly userRoles = computed(() => this.currentUser()?.roles ?? []);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadStoredUser();
  }

  /**
   * Step 1: Login with email + password.
   * Stores temp token+user, saves tenant list.
   */
  login(request: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        if (response.status === 'SUCCESS' && response.data) {
          this.storeTokens(response.data);
          this.currentUser.set(response.data.user);
          this._tenantSelected.set(false);
          this._tenants.set(response.data.tenants || []);
          localStorage.setItem('wms_user', JSON.stringify(response.data.user));
          localStorage.setItem('wms_tenant_selected', 'false');
          localStorage.setItem('wms_tenants', JSON.stringify(response.data.tenants || []));
          localStorage.setItem('wms_locale', request.locale || environment.defaultLocale);
        }
      }),
      catchError(err => {
        this.clearAuth();
        return throwError(() => err);
      })
    );
  }

  /**
   * Step 2: Select a tenant (warehouse + customer).
   * Replaces temp token with final token containing tenant claims.
   */
  selectTenant(request: TenantSelectionRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/select-tenant`, request).pipe(
      tap(response => {
        if (response.status === 'SUCCESS' && response.data) {
          this.storeTokens(response.data);
          this.currentUser.set(response.data.user);
          this._tenantSelected.set(true);
          localStorage.setItem('wms_user', JSON.stringify(response.data.user));
          localStorage.setItem('wms_tenant_selected', 'true');
          localStorage.removeItem('wms_tenants');
        }
      }),
      catchError(err => {
        return throwError(() => err);
      })
    );
  }

  refreshToken(): Observable<ApiResponse<LoginResponse>> {
    const refreshToken = localStorage.getItem(environment.jwtRefreshKey);
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap(response => {
        if (response.status === 'SUCCESS' && response.data) {
          this.storeTokens(response.data);
          if (response.data.tenantSelected) {
            this._tenantSelected.set(true);
            localStorage.setItem('wms_tenant_selected', 'true');
          }
        }
      }),
      catchError(err => {
        this.logout();
        return throwError(() => err);
      })
    );
  }

  logout(): void {
    const token = this.getAccessToken();
    if (token) {
      this.http.post(`${this.apiUrl}/logout`, {}).subscribe({
        error: () => {} // ignore logout errors
      });
    }
    this.clearAuth();
    this.router.navigate(['/login']);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(environment.jwtTokenKey);
  }

  isTokenExpired(): boolean {
    const expiry = localStorage.getItem(environment.tokenExpiryKey);
    if (!expiry) return true;
    return Date.now() > parseInt(expiry, 10);
  }

  hasRole(role: string): boolean {
    return this.userRoles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.userRoles().includes(role));
  }

  getCompanyCode(): string {
    return this.currentUser()?.companyCode ?? '';
  }

  getWarehouseCode(): string {
    return this.currentUser()?.warehouseCode ?? '';
  }

  getCustomerCode(): string {
    return this.currentUser()?.customerCode ?? '';
  }

  private storeTokens(response: LoginResponse): void {
    localStorage.setItem(environment.jwtTokenKey, response.accessToken);
    localStorage.setItem(environment.jwtRefreshKey, response.refreshToken);
    const expiresAt = Date.now() + response.expiresIn;
    localStorage.setItem(environment.tokenExpiryKey, expiresAt.toString());
  }

  private loadStoredUser(): void {
    try {
      const userJson = localStorage.getItem('wms_user');
      if (userJson && !this.isTokenExpired()) {
        this.currentUser.set(JSON.parse(userJson));
        this._tenantSelected.set(localStorage.getItem('wms_tenant_selected') === 'true');
        const tenantsJson = localStorage.getItem('wms_tenants');
        if (tenantsJson) {
          this._tenants.set(JSON.parse(tenantsJson));
        }
      } else if (this.isTokenExpired()) {
        this.clearAuth();
      }
    } catch {
      this.clearAuth();
    }
  }

  private clearAuth(): void {
    localStorage.removeItem(environment.jwtTokenKey);
    localStorage.removeItem(environment.jwtRefreshKey);
    localStorage.removeItem(environment.tokenExpiryKey);
    localStorage.removeItem('wms_user');
    localStorage.removeItem('wms_tenant_selected');
    localStorage.removeItem('wms_tenants');
    this.currentUser.set(null);
    this._tenantSelected.set(false);
    this._tenants.set([]);
  }
}
