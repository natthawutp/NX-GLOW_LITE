import { CommonModule } from '@angular/common';
import { Component, OnDestroy, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription, interval } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseLiveSceneComponent } from './shared/warehouse-live-scene.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import {
  LiveLocationState,
  StockDelta,
  StockSyncSnapshot,
  WarehouseLayout
} from './shared/warehouse-optimize.models';
import { normalizeLayout } from './shared/warehouse-optimize-layout.utils';

@Component({
  selector: 'wms-warehouse-optimize-view3d',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    PageHeaderComponent,
    WarehouseLiveSceneComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="3D Live" subtitle="Read-only digital warehouse view with customer-aware stock sync and live delta polling" icon="pi pi-box">
      <button pButton label="Sync Stock Data" icon="pi pi-sync" class="p-button-sm" [disabled]="!selectedProfileId || !selectedCustomerCode" (click)="syncStock()"></button>
    </wms-page-header>

    <wms-warehouse-optimize-nav></wms-warehouse-optimize-nav>

    <section class="panel toolbar">
      <div class="field">
        <label>Profile</label>
        <p-dropdown
          [options]="profileOptions"
          [ngModel]="selectedProfileId"
          (ngModelChange)="selectProfile($event)"
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
      <div class="status-card">
        <span>Polling</span>
        <strong>{{ pollingActive ? 'Every 60s' : 'Stopped' }}</strong>
      </div>
    </section>

    <section class="content-grid" [class.side-collapsed]="summaryPanelCollapsed">
      <section class="panel scene-panel">
        <div class="panel-head">
          <h3>Live Warehouse</h3>
          <div class="panel-head-actions">
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
        <wms-warehouse-live-scene [layout]="layout" [liveStates]="liveStates"></wms-warehouse-live-scene>
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
                <span>Velocity A</span>
                <strong>{{ colorSummary['velocity-a'] || 0 }}</strong>
              </div>
              <div>
                <span>Velocity B</span>
                <strong>{{ colorSummary['velocity-b'] || 0 }}</strong>
              </div>
              <div>
                <span>Velocity C</span>
                <strong>{{ colorSummary['velocity-c'] || 0 }}</strong>
              </div>
              <div>
                <span>Category</span>
                <strong>{{ categoryCount }}</strong>
              </div>
              <div>
                <span>Empty</span>
                <strong>{{ colorSummary['empty'] || 0 }}</strong>
              </div>
              <div>
                <span>Blinking</span>
                <strong>{{ blinkingCount }}</strong>
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
        </ng-container>
      </section>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .toolbar { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 14px; margin-bottom: 18px; }
    .field { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .status-card { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 14px; display: flex; flex-direction: column; gap: 4px; }
    .status-card span { font-size: 12px; color: #64748b; }
    .status-card strong { font-size: 16px; color: #0f172a; word-break: break-word; }
    .content-grid { display: grid; grid-template-columns: minmax(0, 1fr) minmax(360px, 420px); gap: 18px; align-items: start; }
    .content-grid.side-collapsed { grid-template-columns: minmax(0, 1fr) 88px; }
    .scene-panel { min-height: 760px; }
    .side-column { display: flex; flex-direction: column; gap: 18px; min-width: 0; }
    .side-column.collapsed { gap: 0; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .panel-head-actions { display: flex; align-items: center; gap: 10px; color: #64748b; font-size: 12px; }
    .collapsed-rail { min-height: 760px; padding: 12px 8px; position: sticky; top: 0; }
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
    .w-full { width: 100%; }
    @media (max-width: 1200px) {
      .toolbar, .content-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class WarehouseOptimizeView3dComponent implements OnDestroy {
  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);

  summaryPanelCollapsed = false;
  selectedProfileId: number | null = null;
  selectedCustomerCode = '';
  layout: WarehouseLayout | null = null;
  snapshot: StockSyncSnapshot | null = null;
  liveStates: LiveLocationState[] = [];
  pollingActive = false;
  private pollingSub?: Subscription;

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

  get colorSummary(): Record<string, number> {
    return this.liveStates.reduce<Record<string, number>>((summary, location) => {
      summary[location.colorClass] = (summary[location.colorClass] || 0) + 1;
      return summary;
    }, {});
  }

  get categoryCount(): number {
    return Object.entries(this.colorSummary)
      .filter(([key]) => key.startsWith('category-'))
      .reduce((sum, [, value]) => sum + value, 0);
  }

  get blinkingCount(): number {
    return this.liveStates.filter(location => location.blink).length;
  }

  constructor() {
    this.state.initialize();

    effect(() => {
      const draft = this.state.designDraft();
      if (draft) {
        this.selectedProfileId = draft.profileId;
        this.layout = normalizeLayout(draft.layout);
        this.stopPolling();
        this.snapshot = null;
        this.liveStates = [];
        return;
      }

      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }
      this.selectedProfileId = profile.id;
      this.layout = normalizeLayout(JSON.parse(profile.layoutData));
      this.stopPolling();
      this.snapshot = null;
      this.liveStates = [];
    });

    effect(() => {
      this.selectedCustomerCode = this.state.activeCustomerCode();
    });
  }

  ngOnDestroy(): void {
    this.stopPolling();
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.state.selectProfile(profileId);
  }

  changeCustomer(customerCode: string): void {
    this.selectedCustomerCode = customerCode;
    this.state.setActiveCustomer(customerCode);
    this.stopPolling();
    this.snapshot = null;
    this.liveStates = [];
  }

  toggleSummaryPanel(): void {
    this.summaryPanelCollapsed = !this.summaryPanelCollapsed;
  }

  syncStock(): void {
    if (!this.selectedProfileId || !this.selectedCustomerCode) {
      return;
    }
    this.service.syncStock(this.selectedProfileId, this.selectedCustomerCode).subscribe(snapshot => {
      this.snapshot = snapshot;
      this.liveStates = snapshot.locations;
      this.startPolling();
    });
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
      this.service.loadStockDelta(this.selectedProfileId, this.selectedCustomerCode, this.snapshot.cursor).subscribe(delta => {
        this.mergeDelta(delta);
      });
    });
  }

  private stopPolling(): void {
    this.pollingSub?.unsubscribe();
    this.pollingSub = undefined;
    this.pollingActive = false;
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

    if (changedCodes.length) {
      window.setTimeout(() => {
        this.liveStates = this.liveStates.map(location =>
          changedCodes.includes(location.location) ? { ...location, blink: false } : location
        );
        if (this.snapshot) {
          this.snapshot = { ...this.snapshot, locations: this.liveStates };
        }
      }, 15000);
    }
  }
}
