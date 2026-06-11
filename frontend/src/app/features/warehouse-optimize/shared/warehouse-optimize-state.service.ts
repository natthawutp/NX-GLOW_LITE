import { Injectable, computed, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';
import { AuthService } from '@core/services/auth.service';
import {
  CustomerOption,
  WarehouseDesignDraft,
  WarehouseProfileDetail,
  WarehouseProfileSummary
} from './warehouse-optimize.models';
import { WarehouseOptimizeService } from './warehouse-optimize.service';

const PROFILE_STORAGE_KEY = 'warehouse_optimize_profile_id';
const CUSTOMER_STORAGE_KEY = 'warehouse_optimize_customer_code';

@Injectable({ providedIn: 'root' })
export class WarehouseOptimizeStateService {
  private readonly service = inject(WarehouseOptimizeService);
  private readonly authService = inject(AuthService);

  readonly profiles = signal<WarehouseProfileSummary[]>([]);
  readonly customers = signal<CustomerOption[]>([]);
  readonly selectedProfile = signal<WarehouseProfileDetail | null>(null);
  readonly designDraft = signal<WarehouseDesignDraft | null>(null);
  readonly selectedProfileId = signal<number | null>(readStoredNumber(PROFILE_STORAGE_KEY));
  readonly activeCustomerCode = signal<string>(localStorage.getItem(CUSTOMER_STORAGE_KEY) || this.authService.getCustomerCode());
  readonly loadingProfiles = signal(false);
  readonly loadingCustomers = signal(false);
  readonly loadingProfile = signal(false);
  readonly initialized = signal(false);
  readonly selectedProfileSummary = computed(() =>
    this.profiles().find(profile => profile.id === this.selectedProfileId()) ?? null
  );

  initialize(search = ''): void {
    if (this.initialized()) {
      return;
    }
    this.initialized.set(true);
    const initialProfileId = this.selectedProfileId();
    if (initialProfileId && !this.selectedProfile()) {
      this.selectProfile(initialProfileId);
    }
    this.refreshCustomers();
    this.refreshProfiles(search);
  }

  refreshCustomers(): void {
    this.loadingCustomers.set(true);
    this.service.getCustomers().pipe(
      finalize(() => this.loadingCustomers.set(false))
    ).subscribe(customers => {
      this.customers.set(customers);
      const allowedCustomer = customers.some(customer => customer.customerCode === this.activeCustomerCode());
      if (!allowedCustomer) {
        const fallback = customers[0]?.customerCode || this.authService.getCustomerCode();
        this.setActiveCustomer(fallback);
      }
    });
  }

  refreshProfiles(search = ''): void {
    this.loadingProfiles.set(true);
    this.service.getProfiles(search).pipe(
      finalize(() => this.loadingProfiles.set(false))
    ).subscribe(profiles => {
      this.profiles.set(profiles);
      const currentId = this.selectedProfileId();
      if (currentId && profiles.some(profile => profile.id === currentId)) {
        if (this.selectedProfile()?.id === currentId) {
          return;
        }
        this.selectProfile(currentId);
        return;
      }
      if (profiles[0]) {
        this.selectProfile(profiles[0].id);
      }
    });
  }

  selectProfile(profileId: number): void {
    if (!profileId) {
      return;
    }
    const draft = this.designDraft();
    if (draft && draft.profileId !== profileId) {
      this.clearDesignDraft();
    }
    this.selectedProfileId.set(profileId);
    localStorage.setItem(PROFILE_STORAGE_KEY, String(profileId));
    this.loadingProfile.set(true);
    this.service.getProfile(profileId).pipe(
      finalize(() => this.loadingProfile.set(false))
    ).subscribe(profile => this.selectedProfile.set(profile));
  }

  patchSelectedProfile(profile: WarehouseProfileDetail): void {
    this.selectedProfile.set(profile);
    this.selectedProfileId.set(profile.id);
    localStorage.setItem(PROFILE_STORAGE_KEY, String(profile.id));
    this.profiles.update(list => {
      const index = list.findIndex(item => item.id === profile.id);
      const summary: WarehouseProfileSummary = {
        id: profile.id,
        companyCode: profile.companyCode,
        warehouseCode: profile.warehouseCode,
        customerCode: profile.customerCode,
        profileName: profile.profileName,
        description: profile.description,
        warehouseLength: profile.warehouseLength,
        warehouseWidth: profile.warehouseWidth,
        createdAt: profile.createdAt,
        updatedAt: profile.updatedAt,
        sharedProfile: profile.sharedProfile
      };
      if (index === -1) {
        return [summary, ...list];
      }
      const next = [...list];
      next[index] = summary;
      return next;
    });
  }

  setActiveCustomer(customerCode: string): void {
    this.activeCustomerCode.set(customerCode);
    localStorage.setItem(CUSTOMER_STORAGE_KEY, customerCode);
  }

  setDesignDraft(draft: WarehouseDesignDraft): void {
    this.designDraft.set(draft);
  }

  clearDesignDraft(): void {
    this.designDraft.set(null);
  }
}

function readStoredNumber(key: string): number | null {
  const raw = localStorage.getItem(key);
  if (!raw) {
    return null;
  }
  const parsed = Number(raw);
  return Number.isFinite(parsed) ? parsed : null;
}
