import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { DataTableComponent, WmsColumn } from '@shared/components/data-table/data-table.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';

@Component({
  selector: 'wms-outbound-orders',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    ButtonModule, CalendarModule, DropdownModule, InputTextModule, TagModule,
    PageHeaderComponent, DataTableComponent, StatusBadgeComponent
  ],
  template: `
    <wms-page-header title="outbound.orders_title" subtitle="outbound.orders_subtitle" icon="pi pi-upload">
      <button pButton label="{{ 'common.new' | translate }}" icon="pi pi-plus" class="p-button-sm"></button>
    </wms-page-header>

    <div class="filter-panel">
      <div class="filter-row">
        <div class="filter-field">
          <label>{{ 'outbound.shipment_no' | translate }}</label>
          <input pInputText [(ngModel)]="filters.shipmentNo" placeholder="SP-..." class="w-full">
        </div>
        <div class="filter-field">
          <label>{{ 'common.status' | translate }}</label>
          <p-dropdown [options]="statusOptions" [(ngModel)]="filters.status" placeholder="All" [showClear]="true"
                      [style]="{ width: '100%' }" optionLabel="label" optionValue="value"></p-dropdown>
        </div>
        <div class="filter-field">
          <label>{{ 'outbound.customer' | translate }}</label>
          <input pInputText [(ngModel)]="filters.customer" class="w-full">
        </div>
        <div class="filter-field">
          <label>{{ 'common.date_from' | translate }}</label>
          <p-calendar [(ngModel)]="filters.dateFrom" dateFormat="yy-mm-dd" [showIcon]="true" [style]="{ width: '100%' }"></p-calendar>
        </div>
        <div class="filter-actions">
          <button pButton label="{{ 'common.search' | translate }}" icon="pi pi-search" class="p-button-sm" (click)="onSearch()"></button>
          <button pButton label="{{ 'common.clear' | translate }}" icon="pi pi-times" class="p-button-sm p-button-outlined" (click)="onClear()"></button>
        </div>
      </div>
    </div>

    <wms-data-table [data]="orders()" [columns]="columns" [totalRecords]="orders().length" [loading]="loading()"
                    [lazy]="false" dataKey="SPH_SP_NUM" (refresh)="onSearch()">
      <ng-template #customCell let-row let-col="column">
        @if (col.field === 'SPH_SP_STS') {
          <wms-status-badge [variant]="getStatusVariant(row.SPH_SP_STS)" [label]="row.SPH_SP_STS"></wms-status-badge>
        }
      </ng-template>
      <ng-template #actionCell let-row>
        <div style="display:flex;gap:2px;justify-content:center">
          <button pButton icon="pi pi-eye" class="p-button-text p-button-sm p-button-rounded" pTooltip="View"></button>
          <button pButton icon="pi pi-pencil" class="p-button-text p-button-sm p-button-rounded" pTooltip="Edit"></button>
        </div>
      </ng-template>
    </wms-data-table>
  `,
  styles: [`
    .filter-panel { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 18px 20px; margin-bottom: 18px; }
    .filter-row { display: flex; gap: 14px; align-items: flex-end; flex-wrap: wrap; }
    .filter-field { display: flex; flex-direction: column; gap: 5px; min-width: 160px; flex: 1; }
    .filter-field label { font-size: 12px; font-weight: 600; color: #6b7280; }
    .filter-actions { display: flex; gap: 8px; padding-bottom: 1px; }
    .w-full { width: 100%; }
  `]
})
export class OutboundOrdersComponent implements OnInit {
  orders = signal<any[]>([]);
  loading = signal(false);
  filters = { shipmentNo: '', status: null as string | null, customer: '', dateFrom: null as Date | null };

  statusOptions = [
    { label: 'New', value: 'NEW' }, { label: 'Allocated', value: 'ALLOCATED' },
    { label: 'Picking', value: 'PICKING' }, { label: 'Packed', value: 'PACKED' },
    { label: 'Shipped', value: 'SHIPPED' }, { label: 'Cancelled', value: 'CANCELLED' }
  ];

  columns: WmsColumn[] = [
    { field: 'SPH_SP_NUM', header: 'outbound.shipment_no', sortable: true, width: '150px', frozen: true },
    { field: 'SPH_SP_STS', header: 'common.status', width: '130px', type: 'custom' },
    { field: 'SPH_CUST_NM', header: 'outbound.customer', sortable: true, width: '180px' },
    { field: 'SPH_DLV_NM', header: 'outbound.delivery_to', sortable: true, width: '200px' },
    { field: 'SPH_SCDL_YMD', header: 'outbound.ship_date', sortable: true, width: '120px', type: 'date' },
    { field: 'SPH_IV_NUM', header: 'outbound.invoice_no', sortable: true, width: '140px' },
    { field: 'SPH_TTL_QTY', header: 'common.total_qty', width: '100px', type: 'number', align: 'right' },
    { field: 'SPH_RTE_COD', header: 'outbound.route', width: '100px' }
  ];

  ngOnInit(): void { this.loadDemoData(); }

  onSearch(): void { this.loading.set(true); setTimeout(() => { this.loadDemoData(); this.loading.set(false); }, 400); }
  onClear(): void { this.filters = { shipmentNo: '', status: null, customer: '', dateFrom: null }; this.onSearch(); }

  getStatusVariant(status: string): any {
    const map: Record<string, string> = { NEW: 'new', ALLOCATED: 'allocated', PICKING: 'in-progress', PACKED: 'confirmed', SHIPPED: 'shipped', CANCELLED: 'cancelled' };
    return map[status] || 'pending';
  }

  private loadDemoData(): void {
    const statuses = ['NEW', 'ALLOCATED', 'PICKING', 'PACKED', 'SHIPPED'];
    const customers = ['MUJI Shibuya', 'MUJI Ginza', 'GGA1 Tokyo', 'GGA1 Osaka', 'MUJI Yokohama'];
    this.orders.set(Array.from({ length: 40 }, (_, i) => ({
      SPH_SP_NUM: `SP-2024-${String(1500 + i).padStart(4, '0')}`,
      SPH_SP_STS: statuses[Math.floor(Math.random() * statuses.length)],
      SPH_CUST_NM: customers[Math.floor(Math.random() * customers.length)],
      SPH_DLV_NM: `Delivery Address ${i + 1}, Tokyo`,
      SPH_SCDL_YMD: new Date(2024, 11, Math.floor(Math.random() * 28) + 1),
      SPH_IV_NUM: `INV-${String(2000 + i).padStart(5, '0')}`,
      SPH_TTL_QTY: Math.floor(Math.random() * 300) + 5,
      SPH_RTE_COD: `RT-${String(Math.floor(Math.random() * 10) + 1).padStart(2, '0')}`
    })));
  }
}
