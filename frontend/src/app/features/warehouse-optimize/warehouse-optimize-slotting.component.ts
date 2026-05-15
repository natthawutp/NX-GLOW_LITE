import { CommonModule } from '@angular/common';
import { Component, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import { ProductRecord, SlottingAssignment } from './shared/warehouse-optimize.models';

@Component({
  selector: 'wms-warehouse-optimize-slotting',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    InputNumberModule,
    TableModule,
    PageHeaderComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="Slotting" subtitle="Generate products, optimize slot assignments, and keep customer overlays separated on a shared layout" icon="pi pi-sitemap">
      <button pButton label="Refresh" icon="pi pi-refresh" class="p-button-outlined p-button-sm" (click)="reloadAll()"></button>
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
      <div class="field compact">
        <label>Generate products</label>
        <div class="inline-actions">
          <p-inputNumber [(ngModel)]="generateCount" [min]="10" [max]="2000" [showButtons]="true"></p-inputNumber>
          <button pButton label="Generate" icon="pi pi-plus" class="p-button-sm" (click)="generateProducts()"></button>
        </div>
      </div>
      <div class="field compact">
        <label>Algorithm</label>
        <div class="inline-actions">
          <p-dropdown [options]="algorithmOptions" [(ngModel)]="algorithm" optionLabel="label" optionValue="value"></p-dropdown>
          <button pButton label="Optimize" icon="pi pi-play" class="p-button-sm" (click)="optimizeSlotting()"></button>
          <button pButton label="Clear" icon="pi pi-trash" class="p-button-outlined p-button-sm" (click)="clearAssignments()"></button>
        </div>
      </div>
      <div class="field compact">
        <label>Import products</label>
        <div class="inline-actions">
          <input #productsInput type="file" accept=".json,.csv" hidden (change)="uploadProducts($event)" />
          <button pButton label="Upload File" icon="pi pi-upload" class="p-button-outlined p-button-sm" (click)="productsInput.click()"></button>
        </div>
      </div>
    </section>

    <section class="summary-grid">
      <div class="summary-card">
        <span>Products</span>
        <strong>{{ products.length }}</strong>
      </div>
      <div class="summary-card">
        <span>Assignments</span>
        <strong>{{ assignments.length }}</strong>
      </div>
      <div class="summary-card">
        <span>A-class</span>
        <strong>{{ assignmentSummary.A }}</strong>
      </div>
      <div class="summary-card">
        <span>B-class</span>
        <strong>{{ assignmentSummary.B }}</strong>
      </div>
      <div class="summary-card">
        <span>C-class</span>
        <strong>{{ assignmentSummary.C }}</strong>
      </div>
    </section>

    <section class="content-grid">
      <section class="panel">
        <div class="panel-head">
          <h3>Products</h3>
          <span>{{ products.length }} records</span>
        </div>
        <p-table [value]="products" [scrollable]="true" scrollHeight="560px" styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>SKU</th>
              <th>Name</th>
              <th>Category</th>
              <th>Velocity</th>
              <th>Demand</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-row>
            <tr>
              <td>{{ row.sku }}</td>
              <td>{{ row.name }}</td>
              <td>{{ row.category || '-' }}</td>
              <td>{{ row.velocityClass || '-' }}</td>
              <td>{{ row.demandFrequency ?? '-' }}</td>
            </tr>
          </ng-template>
        </p-table>
      </section>

      <section class="panel">
        <div class="panel-head">
          <h3>Assignments</h3>
          <span>{{ algorithm }}</span>
        </div>
        <p-table [value]="assignments" [scrollable]="true" scrollHeight="560px" styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Location</th>
              <th>SKU</th>
              <th>Velocity</th>
              <th>Category</th>
              <th>Reason</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-row>
            <tr>
              <td>{{ row.location }}</td>
              <td>{{ row.productSku }}</td>
              <td>{{ row.velocityClass || '-' }}</td>
              <td>{{ row.productCategory || '-' }}</td>
              <td>{{ row.assignmentReason || '-' }}</td>
            </tr>
          </ng-template>
        </p-table>
      </section>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .toolbar { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 14px; margin-bottom: 18px; }
    .field { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .inline-actions { display: flex; gap: 8px; align-items: center; flex-wrap: wrap; }
    .summary-grid { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 12px; margin-bottom: 18px; }
    .summary-card { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 14px; display: flex; flex-direction: column; gap: 4px; }
    .summary-card span { font-size: 12px; color: #64748b; }
    .summary-card strong { font-size: 20px; color: #0f172a; }
    .content-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 18px; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .w-full { width: 100%; }
    @media (max-width: 1200px) {
      .toolbar, .summary-grid, .content-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class WarehouseOptimizeSlottingComponent {
  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);
  private readonly messageService = inject(MessageService);

  selectedProfileId: number | null = null;
  generateCount = 200;
  algorithm = 'combined';
  products: ProductRecord[] = [];
  assignments: SlottingAssignment[] = [];
  assignmentSummary = { A: 0, B: 0, C: 0 };

  readonly algorithmOptions = [
    { label: 'Combined', value: 'combined' },
    { label: 'ABC Ranking', value: 'abc' },
    { label: 'Golden Zone', value: 'golden-zone' }
  ];

  get profileOptions(): Array<{ label: string; value: number }> {
    return this.state.profiles().map(profile => ({
      label: `${profile.profileName} (${profile.warehouseCode})`,
      value: profile.id
    }));
  }

  constructor() {
    this.state.initialize();
    this.service.initializeSlotting().subscribe();

    effect(() => {
      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }
      this.selectedProfileId = profile.id;
      this.reloadAll();
    });
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.state.selectProfile(profileId);
  }

  reloadAll(): void {
    this.loadProducts();
    this.loadAssignments();
  }

  loadProducts(): void {
    this.service.getProducts().subscribe(products => {
      this.products = products;
    });
  }

  loadAssignments(): void {
    if (!this.selectedProfileId) {
      this.assignments = [];
      return;
    }
    this.service.getAssignments(this.selectedProfileId).subscribe(response => {
      this.assignments = response.assignments;
      this.assignmentSummary = {
        A: response.summary['a_class'] ?? 0,
        B: response.summary['b_class'] ?? 0,
        C: response.summary['c_class'] ?? 0
      };
    });
  }

  generateProducts(): void {
    this.service.generateProducts(this.generateCount).subscribe(result => {
      this.loadProducts();
      this.messageService.add({
        severity: 'success',
        summary: 'Products generated',
        detail: `${result.count} product records generated for the active customer.`
      });
    });
  }

  optimizeSlotting(): void {
    if (!this.selectedProfileId) {
      this.messageService.add({ severity: 'warn', summary: 'Select a profile', detail: 'Choose a warehouse profile before optimizing slotting.' });
      return;
    }
    this.service.optimizeSlotting(this.selectedProfileId, this.algorithm).subscribe(response => {
      this.assignments = response.assignments;
      this.assignmentSummary = {
        A: response.summary['a_class'] ?? 0,
        B: response.summary['b_class'] ?? 0,
        C: response.summary['c_class'] ?? 0
      };
      this.messageService.add({
        severity: 'success',
        summary: 'Slotting complete',
        detail: `${response.count} assignments created for ${this.algorithm}.`
      });
    });
  }

  clearAssignments(): void {
    if (!this.selectedProfileId) {
      return;
    }
    this.service.clearAssignments(this.selectedProfileId).subscribe(() => {
      this.assignments = [];
      this.assignmentSummary = { A: 0, B: 0, C: 0 };
      this.messageService.add({ severity: 'success', summary: 'Assignments cleared', detail: 'Customer-specific slot assignments were removed.' });
    });
  }

  uploadProducts(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }
    void file.text().then(text => {
      const records = file.name.toLowerCase().endsWith('.json') ? parseJsonArray(text) : parseCsv(text);
      this.service.uploadProducts(records).subscribe(result => {
        this.loadProducts();
        this.messageService.add({
          severity: 'success',
          summary: 'Products uploaded',
          detail: `${result.count} product records imported successfully.`
        });
      });
      input.value = '';
    });
  }
}

function parseJsonArray(text: string): Record<string, unknown>[] {
  const payload = JSON.parse(text) as unknown;
  if (Array.isArray(payload)) {
    return payload as Record<string, unknown>[];
  }
  if (payload && typeof payload === 'object' && Array.isArray((payload as { products?: unknown[] }).products)) {
    return (payload as { products: Record<string, unknown>[] }).products;
  }
  return [];
}

function parseCsv(text: string): Record<string, unknown>[] {
  const [headerLine, ...rows] = text.split(/\r?\n/).filter(Boolean);
  const headers = headerLine.split(',').map(item => item.trim());
  return rows.map(row => {
    const values = row.split(',').map(item => item.trim());
    const record: Record<string, unknown> = {};
    headers.forEach((header, index) => {
      record[header] = values[index] ?? '';
    });
    return record;
  });
}
