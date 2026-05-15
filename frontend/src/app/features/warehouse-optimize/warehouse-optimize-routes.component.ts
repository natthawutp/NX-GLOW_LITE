import { CommonModule } from '@angular/common';
import { Component, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseLayoutMapComponent } from './shared/warehouse-layout-map.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import {
  PickOrder,
  RouteOptimizationResult,
  WarehouseLayout
} from './shared/warehouse-optimize.models';
import { normalizeLayout } from './shared/warehouse-optimize-layout.utils';

@Component({
  selector: 'wms-warehouse-optimize-routes',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    InputNumberModule,
    TableModule,
    PageHeaderComponent,
    WarehouseLayoutMapComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header title="Routes" subtitle="Generate pick orders, compare route methods, and visualize the best path on the shared layout" icon="pi pi-directions">
      <button pButton label="Refresh Orders" icon="pi pi-refresh" class="p-button-outlined p-button-sm" (click)="loadOrders()"></button>
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
        <label>Generate sample orders</label>
        <div class="inline-actions">
          <p-inputNumber [(ngModel)]="generateOrderCount" [min]="1" [max]="500" [showButtons]="true"></p-inputNumber>
          <button pButton label="Generate" icon="pi pi-plus" class="p-button-sm" (click)="generateOrders()"></button>
        </div>
      </div>
      <div class="field compact">
        <label>Import orders</label>
        <div class="inline-actions">
          <input #ordersInput type="file" accept=".json,.csv" hidden (change)="uploadOrders($event)" />
          <button pButton label="Upload File" icon="pi pi-upload" class="p-button-outlined p-button-sm" (click)="ordersInput.click()"></button>
        </div>
      </div>
      <div class="field compact">
        <label>Optimize selected order</label>
        <div class="inline-actions">
          <button pButton label="Optimize Route" icon="pi pi-play" class="p-button-sm" [disabled]="!selectedOrder" (click)="optimizeRoute()"></button>
        </div>
      </div>
    </section>

    <section class="content-grid">
      <section class="panel">
        <div class="panel-head">
          <h3>Orders</h3>
          <span>{{ orders.length }} records</span>
        </div>
        <p-table [value]="orders" [scrollable]="true" scrollHeight="320px" styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Order</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Lines</th>
              <th>Qty</th>
              <th></th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-row>
            <tr [class.selected-row]="selectedOrder?.id === row.id">
              <td>{{ row.orderNumber }}</td>
              <td>{{ row.priority }}</td>
              <td>{{ row.status }}</td>
              <td>{{ row.linesCount }}</td>
              <td>{{ row.totalQuantity }}</td>
              <td><button pButton icon="pi pi-eye" class="p-button-text p-button-sm" (click)="selectOrder(row)"></button></td>
            </tr>
          </ng-template>
        </p-table>

        <div class="detail-card" *ngIf="selectedOrder">
          <div class="detail-head">
            <h4>{{ selectedOrder.orderNumber }}</h4>
            <span>{{ selectedOrder.lines.length }} lines</span>
          </div>
          <ul class="line-list">
            <li *ngFor="let line of selectedOrder.lines">
              <span>{{ line.location }}</span>
              <strong>{{ line.sku }}</strong>
              <em>x{{ line.quantity }}</em>
            </li>
          </ul>
        </div>
      </section>

      <section class="panel map-panel">
        <div class="panel-head">
          <h3>Route Map</h3>
          <span>{{ routeResult?.best?.method || 'Awaiting optimization' }}</span>
        </div>
        <wms-warehouse-layout-map
          [dataLayout]="layout"
          [highlightLocations]="highlightLocations"
          [showLabels]="true">
        </wms-warehouse-layout-map>
      </section>
    </section>

    <section class="panel results-panel" *ngIf="routeResult">
      <div class="panel-head">
        <h3>Optimization Results</h3>
        <span>Best route saves {{ routeResult.best.percent ?? 0 }}%</span>
      </div>
      <div class="results-grid">
        <div class="result-card original">
          <span class="result-kicker">Original</span>
          <strong>{{ routeResult.original.distance }}</strong>
          <small>{{ routeResult.original.method }}</small>
        </div>
        <div class="result-card best">
          <span class="result-kicker">Best</span>
          <strong>{{ routeResult.best.distance }}</strong>
          <small>{{ routeResult.best.method }}</small>
        </div>
        <div class="result-card">
          <span class="result-kicker">Stops</span>
          <strong>{{ routeResult.best.route.length }}</strong>
          <small>locations</small>
        </div>
      </div>
      <p-table [value]="routeResult.optimized" styleClass="p-datatable-sm">
        <ng-template pTemplate="header">
          <tr>
            <th>Method</th>
            <th>Distance</th>
            <th>Improvement %</th>
            <th>Route</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-row>
          <tr>
            <td>{{ row.method }}</td>
            <td>{{ row.distance }}</td>
            <td>{{ row.percent ?? '-' }}</td>
            <td>{{ row.route.join(' → ') }}</td>
          </tr>
        </ng-template>
      </p-table>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .panel { border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; padding: 16px; }
    .toolbar { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 14px; margin-bottom: 18px; }
    .field { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
    .field label { font-size: 12px; font-weight: 600; color: #334155; }
    .inline-actions { display: flex; gap: 8px; align-items: center; flex-wrap: wrap; }
    .content-grid { display: grid; grid-template-columns: minmax(420px, 0.9fr) minmax(0, 1.1fr); gap: 18px; margin-bottom: 18px; }
    .map-panel { min-height: 520px; }
    .panel-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 14px; }
    .panel-head h3 { margin: 0; font-size: 15px; font-weight: 700; color: #0f172a; }
    .selected-row { background: #f8fbff; }
    .detail-card { margin-top: 14px; border-top: 1px solid #e2e8f0; padding-top: 14px; }
    .detail-head { display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; }
    .detail-head h4 { margin: 0; font-size: 14px; font-weight: 700; color: #0f172a; }
    .line-list { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 8px; }
    .line-list li { display: grid; grid-template-columns: 1fr auto auto; gap: 8px; padding: 8px 10px; background: #f8fafc; border-radius: 8px; border: 1px solid #e2e8f0; }
    .line-list span { color: #334155; }
    .line-list strong { color: #0f172a; }
    .line-list em { color: #64748b; font-style: normal; }
    .results-panel { margin-bottom: 18px; }
    .results-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; margin-bottom: 16px; }
    .result-card { border: 1px solid #e2e8f0; border-radius: 8px; background: #f8fafc; padding: 14px; display: flex; flex-direction: column; gap: 4px; }
    .result-card.best { background: #f7fee7; border-color: #bef264; }
    .result-card.original { background: #eff6ff; border-color: #bfdbfe; }
    .result-kicker { font-size: 12px; color: #64748b; }
    .result-card strong { font-size: 20px; color: #0f172a; }
    .result-card small { color: #475569; }
    .w-full { width: 100%; }
    @media (max-width: 1200px) {
      .toolbar, .content-grid, .results-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class WarehouseOptimizeRoutesComponent {
  private readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);
  private readonly messageService = inject(MessageService);

  selectedProfileId: number | null = null;
  layout: WarehouseLayout | null = null;
  orders: PickOrder[] = [];
  selectedOrder: PickOrder | null = null;
  routeResult: RouteOptimizationResult | null = null;
  generateOrderCount = 24;

  get profileOptions(): Array<{ label: string; value: number }> {
    return this.state.profiles().map(profile => ({
      label: `${profile.profileName} (${profile.warehouseCode})`,
      value: profile.id
    }));
  }

  get highlightLocations(): string[] {
    if (this.routeResult?.best?.route?.length) {
      return this.routeResult.best.route;
    }
    return this.selectedOrder?.lines.map(line => line.location) ?? [];
  }

  constructor() {
    this.state.initialize();

    effect(() => {
      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }
      this.selectedProfileId = profile.id;
      this.layout = normalizeLayout(JSON.parse(profile.layoutData));
      this.selectedOrder = null;
      this.routeResult = null;
      this.loadOrders();
    });
  }

  selectProfile(profileId: number): void {
    this.selectedProfileId = profileId;
    this.state.selectProfile(profileId);
  }

  loadOrders(): void {
    if (!this.selectedProfileId) {
      this.orders = [];
      return;
    }
    this.service.getOrders(this.selectedProfileId).subscribe(orders => {
      this.orders = orders;
    });
  }

  selectOrder(order: PickOrder): void {
    this.service.getOrderDetail(order.id).subscribe(detail => {
      this.selectedOrder = detail;
      this.routeResult = null;
    });
  }

  generateOrders(): void {
    if (!this.selectedProfileId) {
      this.messageService.add({ severity: 'warn', summary: 'Select a profile', detail: 'Choose a warehouse profile first.' });
      return;
    }
    this.service.generateOrders(this.selectedProfileId, this.generateOrderCount).subscribe(result => {
      this.loadOrders();
      this.messageService.add({
        severity: 'success',
        summary: 'Orders generated',
        detail: `${result.ordersCreated} pick orders created for route optimization.`
      });
    });
  }

  optimizeRoute(): void {
    if (!this.selectedOrder) {
      return;
    }
    this.service.optimizeRoute(this.selectedOrder.id).subscribe(result => {
      this.routeResult = result;
      this.selectOrder(this.selectedOrder as PickOrder);
      this.messageService.add({
        severity: 'success',
        summary: 'Route optimized',
        detail: `${result.best.method} is the current best path for ${this.selectedOrder?.orderNumber}.`
      });
    });
  }

  uploadOrders(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file || !this.selectedProfileId) {
      return;
    }

    void file.text().then(text => {
      const orders = file.name.toLowerCase().endsWith('.json') ? parseOrdersJson(text) : parseOrdersCsv(text);
      this.service.uploadOrders(this.selectedProfileId as number, orders).subscribe(result => {
        this.loadOrders();
        this.messageService.add({
          severity: 'success',
          summary: 'Orders uploaded',
          detail: `${result.ordersCreated} orders imported successfully.`
        });
      });
      input.value = '';
    });
  }
}

function parseOrdersJson(text: string): Record<string, unknown>[] {
  const payload = JSON.parse(text) as unknown;
  if (Array.isArray(payload)) {
    return payload as Record<string, unknown>[];
  }
  if (payload && typeof payload === 'object' && Array.isArray((payload as { orders?: unknown[] }).orders)) {
    return (payload as { orders: Record<string, unknown>[] }).orders;
  }
  return [];
}

function parseOrdersCsv(text: string): Record<string, unknown>[] {
  const [headerLine, ...rows] = text.split(/\r?\n/).filter(Boolean);
  const headers = headerLine.split(',').map(item => item.trim());
  const grouped = new Map<string, { orderNumber: string; priority: number; lines: Array<Record<string, unknown>> }>();

  for (const row of rows) {
    const values = row.split(',').map(item => item.trim());
    const record: Record<string, string> = {};
    headers.forEach((header, index) => {
      record[header] = values[index] ?? '';
    });
    const orderNumber = record['orderNumber'] || record['order_no'] || record['order'];
    if (!orderNumber) {
      continue;
    }
    const priority = Number(record['priority'] || 5);
    const group = grouped.get(orderNumber) || {
      orderNumber,
      priority,
      lines: []
    };
    group.lines.push({
      sku: record['sku'],
      location: record['location'],
      quantity: Number(record['quantity'] || 1)
    });
    grouped.set(orderNumber, group);
  }

  return Array.from(grouped.values());
}
