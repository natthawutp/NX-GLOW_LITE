import { CommonModule } from '@angular/common';
import { Component, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseLayoutMapComponent } from './shared/warehouse-layout-map.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import { SlottingAssignment, WarehouseLayout } from './shared/warehouse-optimize.models';
import { formatNumber, normalizeLayout } from './shared/warehouse-optimize-layout.utils';

@Component({
  selector: 'wms-warehouse-optimize-view2d',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    TableModule,
    PageHeaderComponent,
    WarehouseLayoutMapComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="2D Result" subtitle="Read-only slotting result mapped onto the selected shared warehouse profile" icon="pi pi-map">
      <button pButton label="Reload Assignments" icon="pi pi-refresh" class="p-button-outlined p-button-sm" (click)="loadAssignments()"></button>
    </wms-page-header>

    <wms-warehouse-optimize-nav></wms-warehouse-optimize-nav>

    <section class="toolbar panel">
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
      <div class="summary">
        <div><span>Assignments</span><strong>{{ assignments.length }}</strong></div>
        <div><span>A-class</span><strong>{{ velocitySummary.A }}</strong></div>
        <div><span>B-class</span><strong>{{ velocitySummary.B }}</strong></div>
        <div><span>C-class</span><strong>{{ velocitySummary.C }}</strong></div>
      </div>
    </section>

    <section class="content-grid">
      <section class="panel map-panel">
        <div class="panel-head">
          <h3>{{ profileName }}</h3>
          <span class="meta">{{ formatNumber(layout?.warehouseWidth) }}m x {{ formatNumber(layout?.warehouseHeight) }}m</span>
        </div>
        <wms-warehouse-layout-map [dataLayout]="layout" [assignments]="assignments" [showLabels]="showLabels"></wms-warehouse-layout-map>
      </section>

      <section class="panel table-panel">
        <div class="panel-head">
          <h3>Assignments</h3>
          <button pButton label="Toggle Labels" icon="pi pi-tag" class="p-button-text p-button-sm" (click)="showLabels = !showLabels"></button>
        </div>
        <p-table [value]="assignments" [scrollable]="true" scrollHeight="640px" styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Location</th>
              <th>SKU</th>
              <th>Category</th>
              <th>Velocity</th>
              <th>Score</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-row>
            <tr>
              <td>{{ row.location }}</td>
              <td>{{ row.productSku }}</td>
              <td>{{ row.productCategory || '-' }}</td>
              <td>{{ row.velocityClass || '-' }}</td>
              <td>{{ row.assignmentScore ?? '-' }}</td>
            </tr>
          </ng-template>
        </p-table>
      </section>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .toolbar { display: grid; grid-template-columns: 320px 1fr; gap: 18px; align-items: end; margin-bottom: 18px; }
    .field { display: flex; flex-direction: column; gap: 6px; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .summary { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 12px; }
    .summary div { border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px; background: #f8fafc; display: flex; flex-direction: column; gap: 4px; }
    .summary span { font-size: 12px; color: #64748b; }
    .summary strong { font-size: 18px; color: #0f172a; }
    .content-grid { display: grid; grid-template-columns: minmax(0, 1.1fr) minmax(420px, 0.9fr); gap: 18px; }
    .map-panel { min-height: 760px; }
    .table-panel { min-height: 760px; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .meta { font-size: 12px; color: #64748b; }
    .w-full { width: 100%; }
    @media (max-width: 1200px) {
      .toolbar, .content-grid { grid-template-columns: 1fr; }
      .summary { grid-template-columns: repeat(2, minmax(0, 1fr)); }
    }
    @media (max-width: 640px) {
      .summary { grid-template-columns: 1fr; }
    }
  `]
})
export class WarehouseOptimizeView2dComponent {
  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);

  selectedProfileId: number | null = null;
  profileName = 'No profile selected';
  layout: WarehouseLayout | null = null;
  assignments: SlottingAssignment[] = [];
  showLabels = false;
  velocitySummary = { A: 0, B: 0, C: 0 };

  get profileOptions(): Array<{ label: string; value: number }> {
    return this.state.profiles().map(profile => ({
      label: `${profile.profileName} (${profile.warehouseCode})`,
      value: profile.id
    }));
  }

  readonly formatNumber = formatNumber;

  constructor() {
    this.state.initialize();

    effect(() => {
      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }
      this.selectedProfileId = profile.id;
      this.profileName = profile.profileName;
      this.layout = normalizeLayout(JSON.parse(profile.layoutData));
      this.loadAssignments();
    });
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.state.selectProfile(profileId);
  }

  loadAssignments(): void {
    if (!this.selectedProfileId) {
      return;
    }
    this.service.getAssignments(this.selectedProfileId).subscribe(response => {
      this.assignments = response.assignments;
      this.velocitySummary = {
        A: response.summary['a_class'] ?? 0,
        B: response.summary['b_class'] ?? 0,
        C: response.summary['c_class'] ?? 0
      };
    });
  }
}
