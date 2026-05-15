import { Component, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '@core/services/auth.service';
import { TenantOption } from '@core/models/auth.model';
import { environment } from '@env/environment';

interface WarehouseGroup {
  warehouseCode: string;
  customers: TenantOption[];
}

@Component({
  selector: 'wms-select-tenant',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    ButtonModule, MessageModule, ProgressSpinnerModule, InputTextModule
  ],
  template: `
    <div class="tenant-page">
      <!-- Left: Branding Panel (same as login) -->
      <div class="tenant-branding">
        <div class="branding-content">
          <div class="brand-logo">
            <svg width="56" height="56" viewBox="0 0 56 56" fill="none">
              <rect width="56" height="56" rx="14" fill="white" fill-opacity="0.15"/>
              <path d="M14 21L28 14L42 21V35L28 42L14 35V21Z" stroke="#8EC400" stroke-width="2.5" fill="none"/>
              <path d="M28 14V42M14 21L42 35M42 21L14 35" stroke="#8EC400" stroke-width="1.5" stroke-opacity="0.5"/>
              <circle cx="28" cy="28" r="5" fill="#8EC400"/>
            </svg>
          </div>
          <h1 class="brand-title">{{ appName }}</h1>
          <p class="brand-subtitle">{{ appSubtitle }}</p>

          <div class="brand-features">
            <div class="feature-item">
              <div class="feature-icon"><i class="pi pi-building"></i></div>
              <div class="feature-text">
                <strong>Multi-Tenant</strong>
                <span>Select your warehouse and customer to begin operations</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon"><i class="pi pi-shield"></i></div>
              <div class="feature-text">
                <strong>Secure Access</strong>
                <span>Role-based permissions per customer context</span>
              </div>
            </div>
          </div>
        </div>

        <div class="branding-footer">
          <span>&copy; {{ currentYear }} {{ companyName }}</span>
        </div>

        <div class="bg-circle bg-circle-1"></div>
        <div class="bg-circle bg-circle-2"></div>
        <div class="bg-grid"></div>
      </div>

      <!-- Right: Tenant Selection -->
      <div class="tenant-form-panel">
        <div class="tenant-container">
          <!-- Header with user info -->
          <div class="tenant-header">
            <div class="header-top">
              <div>
                <h2>Select Workspace</h2>
                <p class="header-subtitle">
                  Welcome, <strong>{{ userName() }}</strong>.
                  Choose the warehouse and customer you want to work with.
                  <span class="hint-text">Double-click a customer to enter directly.</span>
                </p>
              </div>
              <button class="logout-btn" (click)="onLogout()">
                <i class="pi pi-sign-out"></i>
                <span>Sign Out</span>
              </button>
            </div>
          </div>

          <!-- Search bar -->
          <div class="search-bar" *ngIf="!isLoading() && warehouseGroupsAll().length > 0">
            <div class="search-input-wrapper">
              <i class="pi pi-search search-icon"></i>
              <input pInputText
                     type="text"
                     name="tenantSearch"
                     [(ngModel)]="searchQuery"
                     (ngModelChange)="onSearchChange($event)"
                     placeholder="Search by warehouse or customer code..."
                     class="search-input" />
              <button *ngIf="searchQuery"
                      class="search-clear"
                      (click)="clearSearch()">
                <i class="pi pi-times"></i>
              </button>
            </div>
            <span class="search-count">
              {{ filteredCount() }} of {{ totalCount() }} customers
            </span>
          </div>

          <!-- Error message -->
          <p-message *ngIf="errorMessage()"
                     severity="error"
                     [text]="errorMessage()"
                     [style]="{ width: '100%', 'margin-bottom': '16px' }">
          </p-message>

          <!-- Loading -->
          <div *ngIf="isLoading()" class="loading-container">
            <p-progressSpinner [style]="{ width: '40px', height: '40px' }"
                               strokeWidth="4">
            </p-progressSpinner>
            <span>Setting up workspace...</span>
          </div>

          <!-- No tenants message -->
          <div *ngIf="!isLoading() && warehouseGroupsAll().length === 0" class="no-tenants">
            <i class="pi pi-info-circle"></i>
            <p>No warehouses or customers are assigned to your account. Please contact your administrator.</p>
          </div>

          <!-- No search results -->
          <div *ngIf="!isLoading() && warehouseGroupsAll().length > 0 && filteredWarehouseGroups().length === 0" class="no-tenants">
            <i class="pi pi-search"></i>
            <p>No results match "<strong>{{ searchQuery }}</strong>". Try a different warehouse or customer code.</p>
          </div>

          <!-- Scrollable tenant list -->
          <div *ngIf="!isLoading()" class="warehouse-groups-scroll">
            <div class="warehouse-groups">
              @for (group of filteredWarehouseGroups(); track group.warehouseCode; let i = $index) {
                <div class="warehouse-group" [class.collapsed]="collapsedGroups[group.warehouseCode]">
                  <button class="warehouse-label" (click)="toggleGroup(group.warehouseCode)">
                    <i class="pi pi-building"></i>
                    <span class="wh-code">{{ group.warehouseCode }}</span>
                    <span class="wh-count">{{ group.customers.length }} customer{{ group.customers.length > 1 ? 's' : '' }}</span>
                    <i class="pi collapse-icon" [class.pi-chevron-down]="collapsedGroups[group.warehouseCode]" [class.pi-chevron-up]="!collapsedGroups[group.warehouseCode]"></i>
                  </button>
                  <div class="customer-grid" *ngIf="!collapsedGroups[group.warehouseCode]">
                    @for (tenant of group.customers; track tenant.customerCode) {
                      <button class="tenant-card"
                              [class.selected]="selectedTenant()?.customerCode === tenant.customerCode
                                                && selectedTenant()?.warehouseCode === tenant.warehouseCode"
                              (click)="selectTenant(tenant)"
                              (dblclick)="onDoubleClickTenant(tenant)">
                        <div class="card-icon">
                          <i class="pi pi-users"></i>
                        </div>
                        <div class="card-info">
                          <span class="customer-code">{{ tenant.customerCode }}</span>
                          <span class="customer-name">{{ tenant.customerName }}</span>
                        </div>
                        <div class="card-check" *ngIf="selectedTenant()?.customerCode === tenant.customerCode
                                                        && selectedTenant()?.warehouseCode === tenant.warehouseCode">
                          <i class="pi pi-check"></i>
                        </div>
                      </button>
                    }
                  </div>
                </div>
              }
            </div>
          </div>

          <!-- Sticky confirm bar -->
          <div class="confirm-bar" *ngIf="selectedTenant() && !isLoading()">
            <div class="confirm-bar-info">
              <i class="pi pi-check-circle"></i>
              <span><strong>{{ selectedTenant()!.warehouseCode }}</strong> &middot; {{ selectedTenant()!.customerCode }}</span>
            </div>
            <button pButton
                    label="Enter Workspace"
                    icon="pi pi-arrow-right"
                    iconPos="right"
                    class="confirm-btn"
                    [loading]="isSubmitting()"
                    (click)="onConfirm()">
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .tenant-page {
      display: flex;
      height: 100vh;
      overflow: hidden;
    }

    /* === Branding Panel === */
    .tenant-branding {
      flex: 0 0 420px;
      background: linear-gradient(135deg, #0f0035 0%, #1A005D 50%, #2d0080 100%);
      color: white;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      padding: 48px;
      position: relative;
      overflow: hidden;
      min-height: 100vh;
    }

    .branding-content { position: relative; z-index: 2; }
    .brand-logo { margin-bottom: 24px; }
    .brand-title {
      font-size: 28px; font-weight: 800; letter-spacing: -0.5px;
      margin: 0 0 8px; line-height: 1.1;
    }
    .brand-subtitle {
      font-size: 14px; color: rgba(255,255,255,0.6); margin: 0 0 48px;
    }
    .brand-features { display: flex; flex-direction: column; gap: 24px; }
    .feature-item { display: flex; gap: 14px; align-items: flex-start; }
    .feature-icon {
      width: 36px; height: 36px; border-radius: 10px;
      background: rgba(142,196,0,0.18); display: flex;
      align-items: center; justify-content: center; flex-shrink: 0;
    }
    .feature-icon i { color: #8EC400; font-size: 17px; }
    .feature-text { display: flex; flex-direction: column; gap: 2px; }
    .feature-text strong { font-size: 14px; font-weight: 600; }
    .feature-text span { font-size: 12.5px; color: rgba(255,255,255,0.5); line-height: 1.4; }
    .branding-footer {
      position: absolute; bottom: 24px; left: 48px;
      font-size: 12px; color: rgba(255,255,255,0.3); z-index: 2;
    }
    .bg-circle {
      position: absolute; border-radius: 50%;
      border: 1px solid rgba(255,255,255,0.05);
    }
    .bg-circle-1 { width: 400px; height: 400px; top: -100px; right: -120px; }
    .bg-circle-2 { width: 300px; height: 300px; bottom: -60px; left: -80px; }
    .bg-grid {
      position: absolute; inset: 0;
      background-image:
        linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
        linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
      background-size: 40px 40px; z-index: 1;
    }

    /* === Tenant Panel === */
    .tenant-form-panel {
      flex: 1;
      display: flex;
      align-items: flex-start;
      justify-content: center;
      background: #f8f9fc;
      padding: 40px 40px 0;
      height: 100vh;
      overflow: hidden;
    }

    .tenant-container {
      width: 100%;
      max-width: 720px;
      display: flex;
      flex-direction: column;
      height: 100%;
      max-height: 100%;
    }

    .tenant-header { margin-bottom: 20px; flex-shrink: 0; }

    .hint-text {
      display: inline-block;
      margin-top: 4px;
      font-size: 12px;
      color: #9ca3af;
      font-style: italic;
    }

    .header-top {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      gap: 16px;
    }

    .tenant-header h2 {
      font-size: 26px; font-weight: 800; color: #111827;
      margin: 0 0 6px; letter-spacing: -0.3px;
    }

    .header-subtitle {
      font-size: 14px; color: #6b7280; margin: 0; line-height: 1.5;
    }

    .header-subtitle strong { color: #1A005D; }

    .logout-btn {
      display: flex; align-items: center; gap: 6px;
      padding: 8px 14px; border: 1px solid #e5e7eb;
      background: white; border-radius: 8px; cursor: pointer;
      font-size: 13px; color: #6b7280; transition: all 0.15s;
      white-space: nowrap;
    }

    .logout-btn:hover {
      border-color: #FF585D; color: #FF585D; background: #fff5f5;
    }

    /* Search Bar */
    .search-bar {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 16px;
      flex-shrink: 0;
    }

    .search-input-wrapper {
      flex: 1;
      position: relative;
      display: flex;
      align-items: center;
    }

    .search-icon {
      position: absolute;
      left: 14px;
      font-size: 14px;
      color: #9ca3af;
      pointer-events: none;
    }

    .search-input {
      width: 100%;
      padding: 10px 36px 10px 38px !important;
      border: 1px solid #e5e7eb !important;
      border-radius: 10px !important;
      font-size: 14px;
      background: white;
      transition: border-color 0.2s;
    }

    .search-input:focus {
      border-color: #8B5CF6 !important;
      box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1) !important;
    }

    .search-clear {
      position: absolute;
      right: 10px;
      width: 24px;
      height: 24px;
      border: none;
      background: #f3f4f6;
      border-radius: 50%;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: background 0.15s;
    }

    .search-clear:hover { background: #e5e7eb; }
    .search-clear i { font-size: 10px; color: #6b7280; }

    .search-count {
      font-size: 12px;
      color: #9ca3af;
      white-space: nowrap;
    }

    /* Loading */
    .loading-container {
      display: flex; align-items: center; gap: 12px;
      justify-content: center; padding: 48px; color: #6b7280;
    }

    /* No tenants */
    .no-tenants {
      display: flex; align-items: center; gap: 12px;
      padding: 24px; background: white; border-radius: 12px;
      border: 1px solid #e5e7eb; color: #6b7280;
    }
    .no-tenants i { font-size: 20px; color: #FF9E1B; }

    /* Scrollable list area */
    .warehouse-groups-scroll {
      flex: 1;
      overflow-y: auto;
      min-height: 0;
      padding-bottom: 8px;
    }

    .warehouse-groups-scroll::-webkit-scrollbar {
      width: 6px;
    }
    .warehouse-groups-scroll::-webkit-scrollbar-track {
      background: transparent;
    }
    .warehouse-groups-scroll::-webkit-scrollbar-thumb {
      background: #d1d5db;
      border-radius: 3px;
    }
    .warehouse-groups-scroll::-webkit-scrollbar-thumb:hover {
      background: #9ca3af;
    }

    /* Warehouse Groups */
    .warehouse-groups {
      display: flex; flex-direction: column; gap: 16px;
    }

    .warehouse-group {
      background: white;
      border-radius: 12px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }

    .warehouse-label {
      display: flex; align-items: center; gap: 8px;
      padding: 12px 20px;
      background: linear-gradient(135deg, #f0ecf9, #f5f3ff);
      border: none;
      border-bottom: 1px solid #e5e7eb;
      font-size: 14px; font-weight: 700; color: #1A005D;
      cursor: pointer;
      width: 100%;
      text-align: left;
      transition: background 0.15s;
    }

    .warehouse-label:hover {
      background: linear-gradient(135deg, #e8e3f5, #ede9fe);
    }

    .warehouse-group.collapsed .warehouse-label {
      border-bottom: none;
    }

    .warehouse-label .pi-building { font-size: 16px; }
    .warehouse-label .wh-code { flex: 0; white-space: nowrap; }
    .warehouse-label .wh-count {
      flex: 1;
      font-size: 12px;
      font-weight: 500;
      color: #6b7280;
      margin-left: 4px;
    }
    .warehouse-label .collapse-icon {
      font-size: 12px;
      color: #9ca3af;
      transition: transform 0.2s;
    }

    .customer-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 12px;
      padding: 16px;
    }

    .tenant-card {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 14px 16px;
      border: 2px solid #e5e7eb;
      background: white;
      border-radius: 10px;
      cursor: pointer;
      transition: all 0.2s;
      text-align: left;
      position: relative;
    }

    .tenant-card:hover {
      border-color: #c4b5fd;
      background: #faf8ff;
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(26, 0, 93, 0.08);
    }

    .tenant-card.selected {
      border-color: #1A005D;
      background: linear-gradient(135deg, #f5f3ff, #ede9fe);
      box-shadow: 0 4px 16px rgba(26, 0, 93, 0.15);
    }

    .card-icon {
      width: 38px;
      height: 38px;
      border-radius: 10px;
      background: rgba(142, 196, 0, 0.12);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .card-icon i {
      font-size: 16px;
      color: #6b9a00;
    }

    .tenant-card.selected .card-icon {
      background: rgba(26, 0, 93, 0.1);
    }

    .tenant-card.selected .card-icon i {
      color: #1A005D;
    }

    .card-info {
      display: flex;
      flex-direction: column;
      gap: 2px;
      flex: 1;
      min-width: 0;
    }

    .customer-code {
      font-size: 14px;
      font-weight: 700;
      color: #111827;
      text-transform: uppercase;
    }

    .customer-name {
      font-size: 12px;
      color: #6b7280;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .card-check {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background: #1A005D;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .card-check i { font-size: 12px; }

    /* Sticky confirm bar */
    .confirm-bar {
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 16px;
      padding: 16px 0;
      border-top: 1px solid #e5e7eb;
      background: #f8f9fc;
    }

    .confirm-bar-info {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      color: #374151;
    }

    .confirm-bar-info i {
      font-size: 18px;
      color: #1A005D;
    }

    .confirm-btn {
      min-width: 200px;
      height: 44px;
      font-size: 14px;
      font-weight: 600;
      border-radius: 10px !important;
      background: linear-gradient(135deg, #1A005D, #2d0080) !important;
      border: none !important;
      color: #ffffff !important;
      transition: transform 0.15s, box-shadow 0.15s !important;
    }

    .confirm-btn:hover:not(:disabled) {
      transform: translateY(-1px);
      box-shadow: 0 6px 20px rgba(26, 0, 93, 0.35) !important;
    }

    /* Responsive */
    @media (max-width: 1024px) {
      .tenant-branding { display: none; }
    }

    @media (max-width: 600px) {
      .customer-grid { grid-template-columns: 1fr; }
      .tenant-form-panel { padding: 24px 16px 0; }
      .header-top { flex-direction: column; }
      .confirm-bar { padding: 12px 0; }
      .confirm-bar-info { display: none; }
    }
  `]
})
export class SelectTenantComponent implements OnInit {
  readonly appName = environment.appName;
  readonly appSubtitle = environment.appSubtitle;
  readonly companyName = environment.companyName;
  isLoading = signal(false);
  isSubmitting = signal(false);
  errorMessage = signal('');
  selectedTenant = signal<TenantOption | null>(null);
  searchQuery = '';
  private searchFilter = signal('');
  currentYear = new Date().getFullYear();
  collapsedGroups: Record<string, boolean> = {};

  userName = computed(() => this.authService.user()?.displayName || this.authService.user()?.email || 'User');

  /** All warehouse groups (unfiltered) */
  warehouseGroupsAll = computed<WarehouseGroup[]>(() => {
    const tenants = this.authService.tenants();
    const groupMap = new Map<string, TenantOption[]>();

    for (const t of tenants) {
      const key = t.warehouseCode;
      if (!groupMap.has(key)) {
        groupMap.set(key, []);
      }
      groupMap.get(key)!.push(t);
    }

    return Array.from(groupMap.entries())
      .map(([warehouseCode, customers]) => ({ warehouseCode, customers }))
      .sort((a, b) => a.warehouseCode.localeCompare(b.warehouseCode));
  });

  /** Filtered warehouse groups based on search query */
  filteredWarehouseGroups = computed<WarehouseGroup[]>(() => {
    const query = this.searchFilter().trim().toLowerCase();
    const allGroups = this.warehouseGroupsAll();

    if (!query) return allGroups;

    const result: WarehouseGroup[] = [];
    for (const group of allGroups) {
      // If warehouse code matches, include entire group
      if (group.warehouseCode.toLowerCase().includes(query)) {
        result.push(group);
        continue;
      }
      // Otherwise filter customers within the group
      const matchedCustomers = group.customers.filter(c =>
        c.customerCode.toLowerCase().includes(query) ||
        c.customerName.toLowerCase().includes(query)
      );
      if (matchedCustomers.length > 0) {
        result.push({ warehouseCode: group.warehouseCode, customers: matchedCustomers });
      }
    }
    return result;
  });

  /** Total tenant count */
  totalCount = computed(() =>
    this.warehouseGroupsAll().reduce((sum, g) => sum + g.customers.length, 0)
  );

  /** Filtered tenant count */
  filteredCount = computed(() =>
    this.filteredWarehouseGroups().reduce((sum, g) => sum + g.customers.length, 0)
  );

  constructor(
    private authService: AuthService,
    private router: Router,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    // If not authenticated, go to login
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    // If tenant already selected, go to dashboard
    if (this.authService.hasTenantSelected()) {
      this.router.navigate(['/dashboard']);
      return;
    }

    // Auto-select if there's only one tenant
    const tenants = this.authService.tenants();
    if (tenants.length === 1) {
      this.selectedTenant.set(tenants[0]);
    }
  }

  selectTenant(tenant: TenantOption): void {
    this.selectedTenant.set(tenant);
    this.errorMessage.set('');
  }

  onDoubleClickTenant(tenant: TenantOption): void {
    this.selectedTenant.set(tenant);
    this.errorMessage.set('');
    this.onConfirm();
  }

  toggleGroup(warehouseCode: string): void {
    this.collapsedGroups[warehouseCode] = !this.collapsedGroups[warehouseCode];
  }

  onSearchChange(query: string): void {
    this.searchFilter.set(query);
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.searchFilter.set('');
  }

  onConfirm(): void {
    const tenant = this.selectedTenant();
    if (!tenant) return;

    this.isSubmitting.set(true);
    this.errorMessage.set('');

    this.authService.selectTenant({
      warehouseCode: tenant.warehouseCode,
      customerCode: tenant.customerCode
    }).subscribe({
      next: (response) => {
        this.isSubmitting.set(false);
        if (response.status === 'SUCCESS') {
          this.router.navigate(['/dashboard']);
        } else {
          const msg = response.messages?.[0]?.message || 'Failed to select workspace';
          this.errorMessage.set(msg);
        }
      },
      error: (err) => {
        this.isSubmitting.set(false);
        if (err.status === 401) {
          this.errorMessage.set('Session expired. Please login again.');
          setTimeout(() => this.router.navigate(['/login']), 2000);
        } else {
          this.errorMessage.set(err.error?.messages?.[0]?.message || 'An error occurred');
        }
      }
    });
  }

  onLogout(): void {
    this.authService.logout();
  }
}
