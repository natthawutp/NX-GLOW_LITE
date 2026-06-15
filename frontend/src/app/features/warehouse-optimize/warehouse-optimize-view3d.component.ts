import { CommonModule } from '@angular/common';
import { Component, OnDestroy, ViewChild, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize, Subscription, interval } from 'rxjs';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseLiveSceneComponent } from './shared/warehouse-live-scene.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import {
  LiveLocationState,
  SlottingAssignment,
  StockDelta,
  StockSyncSnapshot,
  WorkingStatusSnapshotResponse,
  WorkingStatusZoneSnapshot,
  WarehouseLayout,
  WarehouseLayoutLocation
} from './shared/warehouse-optimize.models';
import { normalizeLayout } from './shared/warehouse-optimize-layout.utils';
import { buildWorkingStatusZones } from './shared/warehouse-working-status.utils';

@Component({
  selector: 'wms-warehouse-optimize-view3d',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    InputTextModule,
    PageHeaderComponent,
    WarehouseLiveSceneComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="3D Live" subtitle="Read-only digital warehouse view with customer-aware stock sync and live delta polling" icon="pi pi-box">
      <span
        *ngIf="stockSyncStatusText"
        class="sync-state"
        [class.sync-state-syncing]="stockSyncState === 'syncing'"
        [class.sync-state-success]="stockSyncState === 'success'"
        [class.sync-state-error]="stockSyncState === 'error'"
        aria-live="polite">
        <span *ngIf="isSyncingStock" class="sync-spinner" aria-hidden="true"></span>
        {{ stockSyncStatusText }}
      </span>
      <button
        pButton
        [label]="isSyncingStock ? 'Syncing Stock...' : 'Sync Stock Data'"
        [icon]="isSyncingStock ? 'pi pi-spinner pi-spin' : 'pi pi-sync'"
        class="p-button-sm"
        [disabled]="!selectedProfileId || !selectedCustomerCode || isSyncingStock"
        (click)="syncStock()">
      </button>
    </wms-page-header>

    <wms-warehouse-optimize-nav></wms-warehouse-optimize-nav>

    <section class="panel toolbar">
      <div class="field">
        <label>Profile</label>
        <p-dropdown
          [options]="profileOptions"
          [ngModel]="selectedProfileId"
          (ngModelChange)="selectProfile($event)"
          [disabled]="isSyncingStock"
          optionLabel="label"
          optionValue="value"
          placeholder="Choose a profile"
          styleClass="w-full">
        </p-dropdown>
      </div>
      <div class="field">
        <label>Customer filter</label>
        <p-dropdown
          [options]="customerOptions"
          [ngModel]="selectedCustomerCode"
          (ngModelChange)="changeCustomer($event)"
          [disabled]="isSyncingStock"
          optionLabel="label"
          optionValue="value"
          placeholder="Choose a customer"
          styleClass="w-full">
        </p-dropdown>
      </div>
      <div class="status-card">
        <span>Last sync</span>
        <strong>{{ snapshot?.syncedAt || 'Not synced' }}</strong>
      </div>
      <div class="field field-search">
        <label>Search</label>
        <div class="search-row">
          <input
            pInputText
            type="text"
            [ngModel]="searchText"
            (ngModelChange)="updateSearch($event)"
            placeholder="Location or product code"
            class="search-input" />
          <button
            pButton
            type="button"
            icon="pi pi-times"
            class="p-button-text p-button-sm"
            aria-label="Clear 3D search"
            [disabled]="!searchText"
            (click)="clearSearch()">
          </button>
        </div>
      </div>
      <div class="status-card">
        <span>Polling</span>
        <strong>{{ pollingActive ? 'Every 60s' : 'Stopped' }}</strong>
      </div>
      <div class="status-card">
        <span>Search Results</span>
        <strong>{{ searchSummaryLabel }}</strong>
      </div>
    </section>

    <section class="content-grid" [class.side-collapsed]="summaryPanelCollapsed">
      <section class="panel scene-panel">
        <div class="panel-head">
          <h3>Live Warehouse</h3>
        <div class="panel-head-actions">
            <span
              *ngIf="workingStatusStatusText"
              class="scene-state"
              [class.scene-state-loading]="workingStatusState === 'loading'"
              [class.scene-state-success]="workingStatusState === 'success'"
              [class.scene-state-error]="workingStatusState === 'error'">
              <span *ngIf="isLoadingWorkingStatuses" class="sync-spinner" aria-hidden="true"></span>
              {{ workingStatusStatusText }}
            </span>
            <span>{{ liveStates.length }} mapped locations</span>
            <button
              *ngIf="summaryPanelCollapsed"
              pButton
              type="button"
              class="p-button-text p-button-sm"
              icon="pi pi-angle-left"
              aria-label="Expand live summary"
              (click)="toggleSummaryPanel()">
            </button>
          </div>
        </div>
        <div class="scene-shell">
          <wms-warehouse-live-scene
            [layout]="layout"
            [assignments]="assignments"
            [liveStates]="liveStates"
            [workingStatusZones]="workingStatusZones"
            [highlightLocations]="searchMatches">
          </wms-warehouse-live-scene>

          <div class="scene-loading-overlay" *ngIf="isSyncingStock" aria-live="polite">
            <span class="sync-spinner" aria-hidden="true"></span>
            <strong>Syncing stock data</strong>
            <span>Reading live stock from Oracle and updating the 3D warehouse view.</span>
          </div>
        </div>
      </section>

      <section class="side-column" [class.collapsed]="summaryPanelCollapsed">
        <section class="panel collapsed-rail" *ngIf="summaryPanelCollapsed">
          <div class="panel-head">
            <h3>Live Summary</h3>
            <button
              pButton
              type="button"
              class="p-button-text p-button-sm"
              icon="pi pi-angle-left"
              aria-label="Expand live summary"
              (click)="toggleSummaryPanel()">
            </button>
          </div>
        </section>

        <ng-container *ngIf="!summaryPanelCollapsed">
          <section class="panel">
            <div class="panel-head">
              <h3>Live Summary</h3>
              <button
                pButton
                type="button"
                class="p-button-text p-button-sm"
                icon="pi pi-angle-right"
                aria-label="Collapse live summary"
                (click)="toggleSummaryPanel()">
              </button>
            </div>
            <div class="summary-grid">
              <div>
                <span>Occupied</span>
                <strong>{{ occupiedCount }}</strong>
              </div>
              <div>
                <span>Empty</span>
                <strong>{{ emptyCount }}</strong>
              </div>
              <div>
                <span>Physical Qty</span>
                <strong>{{ totalPhysicalQty }}</strong>
              </div>
              <div>
                <span>Available Qty</span>
                <strong>{{ totalAvailableQty }}</strong>
              </div>
              <div>
                <span>Blinking</span>
                <strong>{{ blinkingCount }}</strong>
              </div>
              <div>
                <span>Not synced</span>
                <strong>{{ unsyncedCount }}</strong>
              </div>
            </div>
          </section>

          <section class="panel">
            <div class="panel-head">
              <h3>Diagnostics</h3>
            </div>
            <div class="diagnostics-grid">
              <div>
                <h4>Unmatched Stock Rows</h4>
                <ul>
                  <li *ngFor="let item of snapshot?.diagnostics?.unmatchedStockLocations">{{ item }}</li>
                </ul>
              </div>
              <div>
                <h4>Unmatched Layout Locations</h4>
                <ul>
                  <li *ngFor="let item of snapshot?.diagnostics?.unmatchedLayoutLocations">{{ item }}</li>
                </ul>
              </div>
            </div>
          </section>

          <section class="panel" *ngIf="searchText">
            <div class="panel-head">
              <h3>Search Matches</h3>
              <span>{{ searchMatches.length }}</span>
            </div>
            <div class="search-results-empty" *ngIf="!searchResults.length">No matching locations.</div>
            <div class="search-results" *ngIf="searchResults.length">
              <button
                *ngFor="let result of searchResults"
                pButton
                type="button"
                class="p-button-text search-result-button"
                (click)="focusLocation(result.location)">
                <span class="search-result-location">{{ result.location }}</span>
                <span class="search-result-meta">
                  {{ result.productCode || '-' }} | {{ result.occupied ? 'Occupied' : 'Empty' }}
                </span>
              </button>
            </div>
          </section>
        </ng-container>
      </section>
    </section>
  `,
  styles: [`
    :host { display: block; min-height: 100vh; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .sync-state {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      min-height: 36px;
      padding: 0 12px;
      border-radius: 999px;
      border: 1px solid #dbe2ea;
      background: #fff;
      color: #475569;
      font-size: 12px;
      font-weight: 600;
    }
    .sync-state-syncing {
      border-color: #93c5fd;
      background: #eff6ff;
      color: #1d4ed8;
    }
    .sync-state-success {
      border-color: #86efac;
      background: #f0fdf4;
      color: #166534;
    }
    .sync-state-error {
      border-color: #fca5a5;
      background: #fef2f2;
      color: #b91c1c;
    }
    .sync-spinner {
      width: 14px;
      height: 14px;
      border-radius: 999px;
      border: 2px solid currentColor;
      border-right-color: transparent;
      display: inline-block;
      animation: warehouse-spin 0.9s linear infinite;
    }
    .toolbar { display: grid; grid-template-columns: repeat(6, minmax(0, 1fr)); gap: 14px; margin-bottom: 18px; }
    .field { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .field-search { grid-column: span 2; }
    .search-row { display: grid; grid-template-columns: minmax(0, 1fr) auto; gap: 8px; align-items: center; }
    .search-input { width: 100%; }
    .status-card { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 14px; display: flex; flex-direction: column; gap: 4px; }
    .status-card span { font-size: 12px; color: #64748b; }
    .status-card strong { font-size: 16px; color: #0f172a; word-break: break-word; }
    .content-grid {
      display: grid;
      grid-template-columns: minmax(0, 1fr) minmax(360px, 420px);
      gap: 18px;
      align-items: stretch;
      min-height: clamp(560px, calc(100vh - 240px), 1600px);
    }
    .content-grid.side-collapsed { grid-template-columns: minmax(0, 1fr) 88px; }
    .scene-panel {
      min-height: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
    }
    .scene-shell {
      position: relative;
      flex: 1;
      min-height: 0;
      display: flex;
    }
    wms-warehouse-live-scene {
      display: block;
      flex: 1;
      min-height: 0;
      height: 100%;
      width: 100%;
    }
    .scene-loading-overlay {
      position: absolute;
      inset: 0;
      z-index: 2;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 10px;
      border-radius: 8px;
      background: rgba(248, 250, 252, 0.88);
      backdrop-filter: blur(4px);
      color: #1e293b;
      text-align: center;
      pointer-events: all;
    }
    .scene-loading-overlay strong { font-size: 16px; color: #0f172a; }
    .scene-loading-overlay span:last-child { font-size: 12px; color: #64748b; max-width: 320px; }
    .side-column { display: flex; flex-direction: column; gap: 18px; min-width: 0; min-height: 100%; }
    .side-column.collapsed { gap: 0; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .panel-head-actions { display: flex; align-items: center; gap: 10px; color: #64748b; font-size: 12px; }
    .scene-state {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      min-height: 32px;
      padding: 0 10px;
      border-radius: 999px;
      border: 1px solid #dbe2ea;
      background: #fff;
      color: #475569;
      font-size: 11px;
      font-weight: 600;
    }
    .scene-state-loading {
      border-color: #93c5fd;
      background: #eff6ff;
      color: #1d4ed8;
    }
    .scene-state-success {
      border-color: #86efac;
      background: #f0fdf4;
      color: #166534;
    }
    .scene-state-error {
      border-color: #fca5a5;
      background: #fef2f2;
      color: #b91c1c;
    }
    .collapsed-rail { min-height: 100%; height: 100%; padding: 12px 8px; position: sticky; top: 0; }
    .collapsed-rail .panel-head { flex-direction: column; justify-content: flex-start; align-items: center; gap: 16px; margin-bottom: 0; }
    .collapsed-rail .panel-head h3 {
      writing-mode: vertical-rl;
      transform: rotate(180deg);
      font-size: 13px;
      color: #475569;
      min-height: 180px;
      text-align: center;
    }
    .summary-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
    .summary-grid div { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 12px; display: flex; flex-direction: column; gap: 4px; }
    .summary-grid span { font-size: 12px; color: #64748b; }
    .summary-grid strong { font-size: 18px; color: #0f172a; }
    .diagnostics-grid { display: grid; grid-template-columns: 1fr; gap: 16px; }
    .diagnostics-grid h4 { margin: 0 0 8px; font-size: 13px; color: #334155; }
    .diagnostics-grid ul { margin: 0; padding-left: 18px; max-height: 220px; overflow: auto; color: #475569; font-size: 12px; }
    .search-results { display: flex; flex-direction: column; gap: 8px; max-height: 260px; overflow: auto; }
    .search-result-button {
      width: 100%;
      justify-content: flex-start;
      text-align: left;
      padding: 10px 12px;
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      background: #f8fafc;
      color: #0f172a;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: 4px;
    }
    .search-result-location { font-size: 12px; font-weight: 700; color: #0f172a; word-break: break-word; }
    .search-result-meta { font-size: 11px; color: #64748b; word-break: break-word; }
    .search-results-empty { color: #64748b; font-size: 12px; }
    .w-full { width: 100%; }
    @keyframes warehouse-spin {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }
    @media (max-width: 1200px) {
      .toolbar, .content-grid { grid-template-columns: 1fr; }
      .content-grid { min-height: auto; }
      .scene-panel { min-height: 72vh; }
      .collapsed-rail { min-height: 72vh; }
      .field-search { grid-column: auto; }
    }
  `]
})
export class WarehouseOptimizeView3dComponent implements OnDestroy {
  @ViewChild(WarehouseLiveSceneComponent)
  private sceneComponent?: WarehouseLiveSceneComponent;

  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);
  private readonly messageService = inject(MessageService);

  summaryPanelCollapsed = false;
  selectedProfileId: number | null = null;
  selectedCustomerCode = '';
  layout: WarehouseLayout | null = null;
  snapshot: StockSyncSnapshot | null = null;
  workingStatusSnapshot: WorkingStatusSnapshotResponse | null = null;
  workingStatusZones: WorkingStatusZoneSnapshot[] = [];
  liveStates: LiveLocationState[] = [];
  assignments: SlottingAssignment[] = [];
  pollingActive = false;
  isSyncingStock = false;
  isPollingDelta = false;
  isLoadingWorkingStatuses = false;
  stockSyncState: 'idle' | 'syncing' | 'success' | 'error' = 'idle';
  stockSyncStatusText = '';
  workingStatusState: 'idle' | 'loading' | 'success' | 'error' = 'idle';
  workingStatusStatusText = '';
  searchText = '';
  searchMatches: string[] = [];
  searchResults: Array<{ location: string; productCode: string | null; occupied: boolean }> = [];
  private pollingSub?: Subscription;
  private workingStatusPollingSub?: Subscription;
  private pollingErrorToastShown = false;
  private workingStatusErrorToastShown = false;

  get profileOptions(): Array<{ label: string; value: number }> {
    return this.state.profiles().map(profile => ({
      label: `${profile.profileName} (${profile.warehouseCode})`,
      value: profile.id
    }));
  }

  get customerOptions(): Array<{ label: string; value: string }> {
    return this.state.customers().map(customer => ({
      label: `${customer.customerCode} - ${customer.customerName}`,
      value: customer.customerCode
    }));
  }

  get occupiedCount(): number {
    return this.liveStates.filter(location => Number(location.physicalQty ?? 0) > 0).length;
  }

  get emptyCount(): number {
    return this.liveStates.filter(location => Number(location.physicalQty ?? 0) <= 0).length;
  }

  get totalPhysicalQty(): string {
    return this.formatQuantity(this.liveStates.reduce((sum, location) => sum + Number(location.physicalQty ?? 0), 0));
  }

  get totalAvailableQty(): string {
    return this.formatQuantity(this.liveStates.reduce((sum, location) => sum + Number(location.availableQty ?? 0), 0));
  }

  get blinkingCount(): number {
    return this.liveStates.filter(location => location.blink).length;
  }

  get unsyncedCount(): number {
    return Math.max(0, (this.layout ? this.layout.aisles.flatMap(aisle => aisle.locations).length : 0) - this.liveStates.length);
  }

  get searchSummaryLabel(): string {
    if (!this.searchText.trim()) {
      return 'No filter';
    }
    return `${this.searchMatches.length} matched`;
  }

  constructor() {
    this.state.initialize();

    effect(() => {
      const draft = this.state.designDraft();
      if (draft) {
        this.selectedProfileId = draft.profileId;
        this.layout = normalizeLayout(draft.layout);
        this.stopPolling();
        this.stopWorkingStatusPolling();
        this.snapshot = null;
        this.workingStatusSnapshot = null;
        this.liveStates = [];
        this.assignments = [];
        this.resetSyncFeedback();
        if (draft.profileId) {
          this.loadAssignments(draft.profileId);
        } else {
          this.refreshSearchResults();
        }
        this.syncWorkingStatusTracking();
        return;
      }

      const profile = this.state.selectedProfile();
      if (!profile) {
        this.layout = null;
        this.assignments = [];
        this.stopWorkingStatusPolling();
        this.workingStatusSnapshot = null;
        this.workingStatusZones = [];
        this.resetWorkingStatusFeedback();
        this.resetSyncFeedback();
        this.refreshSearchResults();
        return;
      }
      this.selectedProfileId = profile.id;
      this.layout = normalizeLayout(JSON.parse(profile.layoutData));
      this.stopPolling();
      this.stopWorkingStatusPolling();
      this.snapshot = null;
      this.workingStatusSnapshot = null;
      this.liveStates = [];
      this.resetSyncFeedback();
      this.loadAssignments(profile.id);
      this.syncWorkingStatusTracking();
    });

    effect(() => {
      this.selectedCustomerCode = this.state.activeCustomerCode();
      this.syncWorkingStatusTracking();
    });
  }

  ngOnDestroy(): void {
    this.stopPolling();
    this.stopWorkingStatusPolling();
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.resetSyncFeedback();
    this.state.selectProfile(profileId);
  }

  changeCustomer(customerCode: string): void {
    this.selectedCustomerCode = customerCode;
    this.state.setActiveCustomer(customerCode);
    this.stopPolling();
    this.stopWorkingStatusPolling();
    this.snapshot = null;
    this.workingStatusSnapshot = null;
    this.liveStates = [];
    this.resetSyncFeedback();
    this.resetWorkingStatusFeedback();
    this.refreshSearchResults();
    this.syncWorkingStatusTracking();
  }

  toggleSummaryPanel(): void {
    this.summaryPanelCollapsed = !this.summaryPanelCollapsed;
  }

  syncStock(): void {
    if (!this.selectedProfileId || !this.selectedCustomerCode || this.isSyncingStock) {
      return;
    }
    this.isSyncingStock = true;
    this.stockSyncState = 'syncing';
    this.stockSyncStatusText = 'Syncing stock data...';

    this.service.syncStock(
      this.selectedProfileId,
      this.selectedCustomerCode,
      this.currentLayoutOverride()
    ).pipe(
      finalize(() => {
        this.isSyncingStock = false;
      })
    ).subscribe({
      next: snapshot => {
        this.snapshot = snapshot;
        this.liveStates = snapshot.locations;
        this.refreshSearchResults();
        this.startPolling();
        this.syncWorkingStatusTracking();

        const occupied = snapshot.locations.filter(location => Number(location.physicalQty ?? 0) > 0).length;
        const physicalQty = this.formatQuantity(snapshot.locations.reduce((sum, location) => sum + Number(location.physicalQty ?? 0), 0));
        const unmatchedStockCount = snapshot.diagnostics?.unmatchedStockLocations?.length ?? 0;
        this.stockSyncState = 'success';
        this.stockSyncStatusText = `Synced ${occupied} occupied locations`;
        this.messageService.add({
          severity: 'success',
          summary: 'Stock sync complete',
          detail: `${occupied} occupied locations, ${physicalQty} physical quantity loaded.`
        });

        if (occupied === 0 && unmatchedStockCount > 0) {
          this.messageService.add({
            severity: 'warn',
            summary: 'No stock matched current layout',
            detail: 'Oracle returned live stock rows, but their location keys did not match this layout. Generate from DB or save the matching layout and sync again.'
          });
        }
      },
      error: error => {
        this.stockSyncState = 'error';
        this.stockSyncStatusText = 'Stock sync failed';
        this.messageService.add({
          severity: 'error',
          summary: 'Stock sync failed',
          detail: error?.error?.message || error?.message || 'Could not load live stock data for this warehouse.'
        });
      }
    });
  }

  updateSearch(value: string): void {
    this.searchText = value ?? '';
    this.refreshSearchResults();
  }

  clearSearch(): void {
    this.updateSearch('');
  }

  focusLocation(locationCode: string): void {
    this.sceneComponent?.focusLocation(locationCode);
  }

  private startPolling(): void {
    this.stopPolling();
    if (!this.selectedProfileId || !this.selectedCustomerCode || !this.snapshot?.cursor) {
      return;
    }

    this.pollingActive = true;
    this.pollingSub = interval(60000).subscribe(() => {
      if (!this.selectedProfileId || !this.selectedCustomerCode || !this.snapshot?.cursor) {
        return;
      }
      if (this.isSyncingStock || this.isPollingDelta) {
        return;
      }

      this.isPollingDelta = true;
      this.stockSyncState = 'syncing';
      this.stockSyncStatusText = 'Checking for live stock changes...';
      this.service.loadStockDelta(
        this.selectedProfileId,
        this.selectedCustomerCode,
        this.snapshot.cursor,
        this.currentLayoutOverride()
      ).pipe(
        finalize(() => {
          this.isPollingDelta = false;
        })
      ).subscribe({
        next: delta => {
          this.mergeDelta(delta);
          this.refreshWorkingStatusSnapshot(true);
          this.stockSyncState = 'success';
          this.stockSyncStatusText = delta.changedLocations.length
            ? `Updated ${delta.changedLocations.length} live location${delta.changedLocations.length === 1 ? '' : 's'}`
            : 'Live stock is up to date';
          this.pollingErrorToastShown = false;
        },
        error: error => {
          this.stockSyncState = 'error';
          this.stockSyncStatusText = 'Live refresh failed';
          if (!this.pollingErrorToastShown) {
            this.messageService.add({
              severity: 'error',
              summary: 'Live refresh failed',
              detail: error?.error?.message || error?.message || 'Could not refresh live stock changes for this warehouse.'
            });
            this.pollingErrorToastShown = true;
          }
        }
      });
    });
  }

  private stopPolling(): void {
    this.pollingSub?.unsubscribe();
    this.pollingSub = undefined;
    this.pollingActive = false;
  }

  private syncWorkingStatusTracking(): void {
    this.updateWorkingStatusZones();
    this.stopWorkingStatusPolling();

    if (!this.layout) {
      this.workingStatusSnapshot = null;
      this.workingStatusZones = [];
      this.resetWorkingStatusFeedback();
      return;
    }

    if (!this.workingStatusZones.length) {
      this.workingStatusSnapshot = null;
      this.workingStatusState = 'idle';
      this.workingStatusStatusText = 'No working-status zones';
      return;
    }

    if (!this.selectedCustomerCode) {
      this.workingStatusSnapshot = null;
      this.workingStatusState = 'idle';
      this.workingStatusStatusText = 'Customer required';
      return;
    }

    if (!this.snapshot) {
      this.workingStatusSnapshot = null;
      this.workingStatusState = 'idle';
      this.workingStatusStatusText = 'Sync stock to start workflow status';
      return;
    }

    this.refreshWorkingStatusSnapshot(false);
    this.startWorkingStatusPolling();
  }

  private startWorkingStatusPolling(): void {
    this.stopWorkingStatusPolling();
    if (!this.selectedCustomerCode || !this.workingStatusZones.length || !this.snapshot) {
      return;
    }

    this.workingStatusPollingSub = interval(60000).subscribe(() => {
      if (this.isLoadingWorkingStatuses) {
        return;
      }
      this.refreshWorkingStatusSnapshot(true);
    });
  }

  private stopWorkingStatusPolling(): void {
    this.workingStatusPollingSub?.unsubscribe();
    this.workingStatusPollingSub = undefined;
  }

  private refreshWorkingStatusSnapshot(polling = false): void {
    if (!this.selectedCustomerCode || !this.workingStatusZones.length || !this.snapshot || this.isLoadingWorkingStatuses) {
      return;
    }

    this.isLoadingWorkingStatuses = true;
    this.workingStatusState = 'loading';
    this.workingStatusStatusText = polling ? 'Refreshing workflow status...' : 'Loading workflow status...';

    this.service.loadWorkingStatusSnapshot(this.selectedCustomerCode).pipe(
      finalize(() => {
        this.isLoadingWorkingStatuses = false;
      })
    ).subscribe({
      next: snapshot => {
        this.workingStatusSnapshot = snapshot;
        this.updateWorkingStatusZones();
        this.workingStatusState = 'success';
        this.workingStatusStatusText = `${this.workingStatusZones.length} workflow zone${this.workingStatusZones.length === 1 ? '' : 's'} live`;
        this.workingStatusErrorToastShown = false;
      },
      error: error => {
        this.workingStatusState = 'error';
        this.workingStatusStatusText = 'Workflow status unavailable';
        if (!polling && !this.workingStatusErrorToastShown) {
          this.messageService.add({
            severity: 'error',
            summary: 'Workflow status failed',
            detail: error?.error?.message || error?.message || 'Could not load inbound and outbound workflow status data.'
          });
          this.workingStatusErrorToastShown = true;
        }
      }
    });
  }

  private updateWorkingStatusZones(): void {
    this.workingStatusZones = buildWorkingStatusZones(this.layout, this.workingStatusSnapshot);
  }

  private mergeDelta(delta: StockDelta): void {
    const states = new Map(this.liveStates.map(location => [location.location, { ...location }]));
    const changedCodes = delta.changedLocations.map(location => location.location);

    for (const changed of delta.changedLocations) {
      states.set(changed.location, { ...changed, blink: true });
    }

    this.liveStates = Array.from(states.values()).sort((left, right) => left.location.localeCompare(right.location));
    this.snapshot = {
      profileId: delta.profileId,
      customerCode: delta.customerCode,
      syncedAt: new Date().toISOString(),
      cursor: delta.cursor,
      diagnostics: delta.diagnostics,
      locations: this.liveStates
    };
    this.refreshSearchResults();

    if (changedCodes.length) {
      window.setTimeout(() => {
        this.liveStates = this.liveStates.map(location =>
          changedCodes.includes(location.location) ? { ...location, blink: false } : location
        );
        if (this.snapshot) {
          this.snapshot = { ...this.snapshot, locations: this.liveStates };
        }
        this.refreshSearchResults();
      }, 15000);
    }
  }

  private loadAssignments(profileId: number): void {
    this.service.getAssignments(profileId).subscribe(response => {
      this.assignments = response.assignments;
      this.refreshSearchResults();
    });
  }

  private refreshSearchResults(): void {
    const query = this.normalizeSearch(this.searchText);
    if (!query || !this.layout) {
      this.searchMatches = [];
      this.searchResults = [];
      return;
    }

    const liveByLocation = new Map(this.liveStates.map(location => [location.location, location]));
    const assignmentByLocation = new Map(this.assignments.map(assignment => [assignment.location, assignment]));

    const matches = this.layout.aisles
      .flatMap(aisle => aisle.locations)
      .map(location => this.toSearchResult(location, liveByLocation.get(location.location), assignmentByLocation.get(location.location), query))
      .filter((result): result is { location: string; productCode: string | null; occupied: boolean; score: number } => !!result)
      .sort((left, right) => left.score - right.score || left.location.localeCompare(right.location));

    this.searchMatches = matches.map(result => result.location);
    this.searchResults = matches.slice(0, 120).map(({ score, ...result }) => result);
  }

  private toSearchResult(
    location: WarehouseLayoutLocation,
    liveState: LiveLocationState | undefined,
    assignment: SlottingAssignment | undefined,
    query: string
  ): { location: string; productCode: string | null; occupied: boolean; score: number } | null {
    const productCode = liveState?.productCode || assignment?.productSku || location.slottedSku || null;
    const candidates = [
      location.location,
      location.zone,
      productCode,
      assignment?.productName,
      liveState?.productCategory,
      assignment?.productCategory
    ];
    const score = this.searchScore(query, location.location, productCode, candidates);
    if (score < 0) {
      return null;
    }
    return {
      location: location.location,
      productCode,
      occupied: Number(liveState?.physicalQty ?? 0) > 0,
      score
    };
  }

  private searchScore(query: string, locationCode: string, productCode: string | null, candidates: Array<string | null | undefined>): number {
    const normalizedLocation = this.normalizeSearch(locationCode);
    const normalizedProduct = this.normalizeSearch(productCode);
    if (normalizedLocation === query) {
      return 0;
    }
    if (normalizedProduct === query) {
      return 1;
    }
    if (normalizedLocation.startsWith(query)) {
      return 2;
    }
    if (normalizedProduct.startsWith(query)) {
      return 3;
    }
    return candidates.some(value => this.normalizeSearch(value).includes(query)) ? 4 : -1;
  }

  private currentLayoutOverride(): Record<string, unknown> | null {
    return this.layout
      ? this.layout as unknown as Record<string, unknown>
      : null;
  }

  private normalizeSearch(value: string | null | undefined): string {
    return (value || '').trim().toLowerCase();
  }

  private formatQuantity(value: number): string {
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: Number.isInteger(value) ? 0 : 2,
      maximumFractionDigits: Number.isInteger(value) ? 0 : 2
    }).format(value);
  }

  private resetSyncFeedback(): void {
    this.stockSyncState = 'idle';
    this.stockSyncStatusText = '';
  }

  private resetWorkingStatusFeedback(): void {
    this.workingStatusState = 'idle';
    this.workingStatusStatusText = '';
    this.workingStatusErrorToastShown = false;
  }
}
