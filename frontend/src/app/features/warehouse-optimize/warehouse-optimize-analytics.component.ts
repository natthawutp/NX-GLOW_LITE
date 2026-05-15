import { CommonModule } from '@angular/common';
import { Component, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import { WarehouseOptimizeAnalytics } from './shared/warehouse-optimize.models';

@Component({
  selector: 'wms-warehouse-optimize-analytics',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    TableModule,
    PageHeaderComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="Analytics" subtitle="Review slotting distribution, route performance, and export the current analytics view" icon="pi pi-chart-line">
      <button pButton label="Export CSV" icon="pi pi-download" class="p-button-sm" [disabled]="!selectedProfileId" (click)="exportCsv()"></button>
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
      <div class="actions">
        <button pButton label="Refresh Analytics" icon="pi pi-refresh" class="p-button-outlined p-button-sm" (click)="loadAnalytics()"></button>
      </div>
    </section>

    <section class="summary-grid" *ngIf="analytics">
      <div class="summary-card">
        <span>Total assignments</span>
        <strong>{{ totalAssignments }}</strong>
      </div>
      <div class="summary-card">
        <span>Total orders</span>
        <strong>{{ analytics.picking.totalOrders }}</strong>
      </div>
      <div class="summary-card">
        <span>Optimized orders</span>
        <strong>{{ analytics.picking.optimizedOrders }}</strong>
      </div>
      <div class="summary-card">
        <span>Best avg improvement</span>
        <strong>{{ bestImprovement }}%</strong>
      </div>
    </section>

    <section class="content-grid" *ngIf="analytics">
      <section class="panel">
        <div class="panel-head">
          <h3>Velocity Distribution</h3>
        </div>
        <div class="bar-list">
          <div class="bar-row" *ngFor="let item of analytics.slotting.velocityDistribution">
            <div class="bar-label">{{ item.velocityClass }}</div>
            <div class="bar-track"><div class="bar-fill velocity" [style.width.%]="barWidth(item.count, maxVelocityCount)"></div></div>
            <div class="bar-value">{{ item.count }}</div>
          </div>
        </div>
        <p-table [value]="analytics.slotting.velocityDistribution" styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Velocity</th>
              <th>Count</th>
              <th>Avg Score</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-row>
            <tr>
              <td>{{ row.velocityClass }}</td>
              <td>{{ row.count }}</td>
              <td>{{ row.avgScore ?? '-' }}</td>
            </tr>
          </ng-template>
        </p-table>
      </section>

      <section class="panel">
        <div class="panel-head">
          <h3>Zone Distribution</h3>
        </div>
        <div class="bar-list">
          <div class="bar-row" *ngFor="let item of analytics.slotting.zoneDistribution">
            <div class="bar-label">{{ item.zone }}</div>
            <div class="bar-track"><div class="bar-fill zone" [style.width.%]="barWidth(item.count, maxZoneCount)"></div></div>
            <div class="bar-value">{{ item.count }}</div>
          </div>
        </div>
      </section>
    </section>

    <section class="panel" *ngIf="analytics">
      <div class="panel-head">
        <h3>Route Algorithms</h3>
      </div>
      <p-table [value]="analytics.picking.algorithmComparison" styleClass="p-datatable-sm">
        <ng-template pTemplate="header">
          <tr>
            <th>Algorithm</th>
            <th>Orders</th>
            <th>Avg Original</th>
            <th>Avg Optimized</th>
            <th>Avg Improvement %</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-row>
          <tr>
            <td>{{ row.algorithm }}</td>
            <td>{{ row.orderCount }}</td>
            <td>{{ row.avgOriginalDistance ?? '-' }}</td>
            <td>{{ row.avgOptimizedDistance ?? '-' }}</td>
            <td>{{ row.avgImprovement ?? '-' }}</td>
          </tr>
        </ng-template>
      </p-table>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .toolbar { display: grid; grid-template-columns: 320px 1fr; gap: 14px; margin-bottom: 18px; align-items: end; }
    .field { display: flex; flex-direction: column; gap: 6px; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .actions { display: flex; justify-content: flex-end; }
    .summary-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 12px; margin-bottom: 18px; }
    .summary-card { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 14px; display: flex; flex-direction: column; gap: 4px; }
    .summary-card span { font-size: 12px; color: #64748b; }
    .summary-card strong { font-size: 20px; color: #0f172a; }
    .content-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 18px; margin-bottom: 18px; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .bar-list { display: flex; flex-direction: column; gap: 10px; margin-bottom: 16px; }
    .bar-row { display: grid; grid-template-columns: 96px 1fr 48px; gap: 10px; align-items: center; }
    .bar-label, .bar-value { font-size: 12px; color: #334155; }
    .bar-track { height: 10px; border-radius: 999px; background: #e2e8f0; overflow: hidden; }
    .bar-fill { height: 100%; border-radius: 999px; }
    .bar-fill.velocity { background: linear-gradient(90deg, #ef4444, #f59e0b); }
    .bar-fill.zone { background: linear-gradient(90deg, #2563eb, #14b8a6); }
    .w-full { width: 100%; }
    @media (max-width: 1200px) {
      .toolbar, .summary-grid, .content-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class WarehouseOptimizeAnalyticsComponent {
  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);
  private lastLoadedProfileId: number | null = null;

  selectedProfileId: number | null = null;
  analytics: WarehouseOptimizeAnalytics | null = null;

  get profileOptions(): Array<{ label: string; value: number }> {
    return this.state.profiles().map(profile => ({
      label: `${profile.profileName} (${profile.warehouseCode})`,
      value: profile.id
    }));
  }

  get totalAssignments(): number {
    return this.analytics?.slotting.velocityDistribution.reduce((sum, item) => sum + item.count, 0) ?? 0;
  }

  get maxVelocityCount(): number {
    return Math.max(...(this.analytics?.slotting.velocityDistribution.map(item => item.count) ?? [1]));
  }

  get maxZoneCount(): number {
    return Math.max(...(this.analytics?.slotting.zoneDistribution.map(item => item.count) ?? [1]));
  }

  get bestImprovement(): number {
    return Math.max(...(this.analytics?.picking.algorithmComparison.map(item => item.avgImprovement ?? 0) ?? [0]));
  }

  constructor() {
    this.state.initialize();

    effect(() => {
      const profile = this.state.selectedProfile();
      const nextProfileId = profile?.id ?? this.state.selectedProfileId();
      if (!nextProfileId) {
        return;
      }

      this.selectedProfileId = nextProfileId;
      if (this.lastLoadedProfileId === nextProfileId) {
        return;
      }

      this.analytics = null;
      this.lastLoadedProfileId = nextProfileId;
      this.loadAnalytics();
    });
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.lastLoadedProfileId = null;
    this.state.selectProfile(profileId);
  }

  loadAnalytics(): void {
    if (!this.selectedProfileId) {
      return;
    }
    this.service.getAnalytics(this.selectedProfileId).subscribe(analytics => {
      this.analytics = analytics;
    });
  }

  exportCsv(): void {
    if (!this.selectedProfileId) {
      return;
    }
    this.service.exportAnalytics(this.selectedProfileId).subscribe(csv => {
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `warehouse-optimize-${this.selectedProfileId}-analytics.csv`;
      link.click();
      URL.revokeObjectURL(url);
    });
  }

  barWidth(value: number, max: number): number {
    if (!max) {
      return 0;
    }
    return Math.max(8, (value / max) * 100);
  }
}
