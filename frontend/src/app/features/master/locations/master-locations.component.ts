import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { TooltipModule } from 'primeng/tooltip';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';

@Component({
  selector: 'wms-master-locations',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, DropdownModule,
            TableModule, TagModule, DialogModule, InputNumberModule, TooltipModule, PageHeaderComponent],
  template: `
    <wms-page-header title="master.locations_title" subtitle="master.locations_subtitle" icon="pi pi-map">
      <button pButton label="Add Location" icon="pi pi-plus" class="p-button-sm" (click)="openDialog()"></button>
    </wms-page-header>

    <!-- Summary -->
    <div class="summary-row">
      @for (s of summaryCards; track s.label) {
        <div class="summary-card" [style.border-left-color]="s.color">
          <span class="summary-value">{{ s.value }}</span>
          <span class="summary-label">{{ s.label }}</span>
        </div>
      }
    </div>

    <!-- Filters -->
    <div class="wms-card mb-3">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Location Code</label>
          <input pInputText [(ngModel)]="filterCode" placeholder="A-01-..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Zone</label>
          <p-dropdown [options]="zones" [(ngModel)]="filterZone" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-item">
          <label class="filter-label">Type</label>
          <p-dropdown [options]="types" [(ngModel)]="filterType" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-actions">
          <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="wms-card">
      <p-table [value]="locations" [paginator]="true" [rows]="15" [rowsPerPageOptions]="[10,15,30]"
               styleClass="p-datatable-sm p-datatable-gridlines" [scrollable]="true" scrollHeight="480px" sortMode="multiple">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="code" style="width:140px">Location <p-sortIcon field="code"></p-sortIcon></th>
            <th pSortableColumn="zone" style="width:80px">Zone <p-sortIcon field="zone"></p-sortIcon></th>
            <th style="width:80px">Aisle</th>
            <th style="width:70px">Rack</th>
            <th style="width:70px">Level</th>
            <th pSortableColumn="type" style="width:100px">Type <p-sortIcon field="type"></p-sortIcon></th>
            <th style="width:110px">Max Weight</th>
            <th style="width:100px">Capacity</th>
            <th style="width:90px">Occupancy</th>
            <th style="width:80px">Status</th>
            <th style="width:80px">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-loc>
          <tr>
            <td><span class="mono-code">{{ loc.code }}</span></td>
            <td><span class="zone-badge" [style.background]="getZoneColor(loc.zone)">{{ loc.zone }}</span></td>
            <td>{{ loc.aisle }}</td>
            <td>{{ loc.rack }}</td>
            <td>{{ loc.level }}</td>
            <td>{{ loc.type }}</td>
            <td>{{ loc.maxWeight }} kg</td>
            <td>{{ loc.capacity }} {{ loc.capacityUom }}</td>
            <td>
              <div class="occupancy-bar">
                <div class="occupancy-fill" [style.width.%]="loc.occupancy"
                     [style.background]="loc.occupancy > 90 ? '#FF585D' : loc.occupancy > 70 ? '#FF9E1B' : '#8EC400'"></div>
              </div>
              <span class="occupancy-text">{{ loc.occupancy }}%</span>
            </td>
            <td><p-tag [value]="loc.status" [severity]="loc.status === 'Active' ? 'success' : 'danger'" [rounded]="true"></p-tag></td>
            <td>
              <button pButton icon="pi pi-pencil" class="p-button-text p-button-sm p-button-rounded" pTooltip="Edit" (click)="editLocation(loc)"></button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog -->
    <p-dialog [(visible)]="showDialog" [header]="editMode ? 'Edit Location' : 'Add Location'" [modal]="true"
              [style]="{ width: '560px' }" [draggable]="false">
      <div class="dialog-form">
        <div class="form-row">
          <div class="form-field"><label>Zone</label><p-dropdown [options]="zones" [(ngModel)]="form.zone" styleClass="w-full"></p-dropdown></div>
          <div class="form-field"><label>Aisle</label><input pInputText [(ngModel)]="form.aisle" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Rack</label><input pInputText [(ngModel)]="form.rack" class="w-full" /></div>
          <div class="form-field"><label>Level</label><input pInputText [(ngModel)]="form.level" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Type</label><p-dropdown [options]="types" [(ngModel)]="form.type" styleClass="w-full"></p-dropdown></div>
          <div class="form-field"><label>Max Weight (kg)</label><p-inputNumber [(ngModel)]="form.maxWeight" [min]="0" styleClass="w-full"></p-inputNumber></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Capacity</label><p-inputNumber [(ngModel)]="form.capacity" [min]="0" styleClass="w-full"></p-inputNumber></div>
          <div class="form-field"><label>Capacity UOM</label><p-dropdown [options]="capacityUoms" [(ngModel)]="form.capacityUom" styleClass="w-full"></p-dropdown></div>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancel" class="p-button-text" (click)="showDialog = false"></button>
        <button pButton label="Save" icon="pi pi-check" (click)="saveLocation()"></button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .summary-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 16px; }
    .summary-card { background: white; border-radius: 12px; border: 1px solid #e5e7eb; border-left: 4px solid; padding: 16px; display: flex; flex-direction: column; }
    .summary-value { font-size: 22px; font-weight: 800; color: #111827; }
    .summary-label { font-size: 12px; color: #6b7280; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .mb-3 { margin-bottom: 16px; }
    .filter-row { display: flex; gap: 14px; align-items: flex-end; flex-wrap: wrap; }
    .filter-item { flex: 1; min-width: 150px; display: flex; flex-direction: column; gap: 4px; }
    .filter-label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; align-items: flex-end; }
    .w-full { width: 100%; }
    .mono-code { font-family: monospace; font-weight: 700; color: #1A005D; }
    .zone-badge { color: white; font-weight: 700; font-size: 12px; padding: 3px 10px; border-radius: 6px; }
    .occupancy-bar { width: 60px; height: 6px; background: #e5e7eb; border-radius: 3px; display: inline-block; margin-right: 6px; vertical-align: middle; }
    .occupancy-fill { height: 100%; border-radius: 3px; transition: width .3s; }
    .occupancy-text { font-size: 12px; font-weight: 600; }
    .dialog-form { display: flex; flex-direction: column; gap: 14px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field label { font-size: 12px; font-weight: 600; color: #374151; }
  `]
})
export class MasterLocationsComponent implements OnInit {
  filterCode = '';
  filterZone: string | null = null;
  filterType: string | null = null;
  showDialog = false;
  editMode = false;
  form: any = {};

  zones = [
    { label: 'Zone A', value: 'A' }, { label: 'Zone B', value: 'B' },
    { label: 'Zone C', value: 'C' }, { label: 'Zone D', value: 'D' }
  ];
  types = [
    { label: 'Rack', value: 'Rack' }, { label: 'Floor', value: 'Floor' },
    { label: 'Bulk', value: 'Bulk' }, { label: 'Cold Storage', value: 'Cold' }
  ];
  capacityUoms = [{ label: 'Pallets', value: 'PLT' }, { label: 'Cases', value: 'CS' }, { label: 'Units', value: 'PCS' }];

  summaryCards = [
    { label: 'Total Locations', value: 1248, color: '#1A005D' },
    { label: 'Active', value: 1195, color: '#8EC400' },
    { label: 'Occupied (>50%)', value: 843, color: '#FF9E1B' },
    { label: 'Full (>90%)', value: 127, color: '#FF585D' }
  ];

  locations: any[] = [];

  ngOnInit(): void {
    this.locations = [
      { code: 'A-01-01-01', zone: 'A', aisle: '01', rack: '01', level: '01', type: 'Rack', maxWeight: 500, capacity: 4, capacityUom: 'PLT', occupancy: 75, status: 'Active' },
      { code: 'A-01-02-03', zone: 'A', aisle: '01', rack: '02', level: '03', type: 'Rack', maxWeight: 500, capacity: 4, capacityUom: 'PLT', occupancy: 95, status: 'Active' },
      { code: 'B-03-01-02', zone: 'B', aisle: '03', rack: '01', level: '02', type: 'Rack', maxWeight: 300, capacity: 6, capacityUom: 'CS', occupancy: 40, status: 'Active' },
      { code: 'C-02-04-01', zone: 'C', aisle: '02', rack: '04', level: '01', type: 'Floor', maxWeight: 1000, capacity: 8, capacityUom: 'PLT', occupancy: 20, status: 'Active' },
      { code: 'D-01-01-01', zone: 'D', aisle: '01', rack: '01', level: '01', type: 'Cold', maxWeight: 200, capacity: 2, capacityUom: 'PLT', occupancy: 60, status: 'Active' },
      { code: 'A-05-03-02', zone: 'A', aisle: '05', rack: '03', level: '02', type: 'Bulk', maxWeight: 2000, capacity: 20, capacityUom: 'PLT', occupancy: 0, status: 'Inactive' },
    ];
  }

  getZoneColor(zone: string): string {
    const map: Record<string, string> = { A: '#1A005D', B: '#5BC2E7', C: '#8EC400', D: '#FF9E1B' };
    return map[zone] || '#6b7280';
  }

  search(): void { console.log('Search locations'); }
  openDialog(): void { this.editMode = false; this.form = {}; this.showDialog = true; }
  editLocation(loc: any): void { this.editMode = true; this.form = { ...loc }; this.showDialog = true; }
  saveLocation(): void { console.log('Save:', this.form); this.showDialog = false; }
}
