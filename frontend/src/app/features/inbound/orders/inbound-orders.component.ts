import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TableLazyLoadEvent } from 'primeng/table';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { DataTableComponent, WmsColumn } from '@shared/components/data-table/data-table.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';
import { ApiService } from '@core/services/api.service';

@Component({
  selector: 'wms-inbound-orders',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    ButtonModule, CalendarModule, DropdownModule, InputTextModule,
    TagModule,
    PageHeaderComponent, DataTableComponent, StatusBadgeComponent
  ],
  template: `
    <wms-page-header title="inbound.orders_title" subtitle="inbound.orders_subtitle" icon="pi pi-download">
      <button pButton label="{{ 'common.new' | translate }}" icon="pi pi-plus" class="p-button-sm new-order-btn"
              (click)="openNewOrderEntry()"></button>
    </wms-page-header>

    <!-- Search Filters -->
    <div class="filter-panel">
      <div class="filter-row">
        <div class="filter-field">
          <label>{{ 'inbound.arrival_no' | translate }}</label>
          <input pInputText [(ngModel)]="filters.arrivalNo" placeholder="AV-..." class="w-full">
        </div>
        <div class="filter-field">
          <label>{{ 'common.status' | translate }}</label>
          <p-dropdown [options]="statusOptions" [(ngModel)]="filters.status" placeholder="All" [showClear]="true"
                      [style]="{ width: '100%' }" optionLabel="label" optionValue="value"></p-dropdown>
        </div>
        <div class="filter-field">
          <label>{{ 'inbound.supplier' | translate }}</label>
          <input pInputText [(ngModel)]="filters.supplier" placeholder="Supplier name" class="w-full">
        </div>
        <div class="filter-field">
          <label>{{ 'common.date_from' | translate }}</label>
          <p-calendar [(ngModel)]="filters.dateFrom" dateFormat="yy-mm-dd" [showIcon]="true"
                      [style]="{ width: '100%' }"></p-calendar>
        </div>
        <div class="filter-field">
          <label>{{ 'common.date_to' | translate }}</label>
          <p-calendar [(ngModel)]="filters.dateTo" dateFormat="yy-mm-dd" [showIcon]="true"
                      [style]="{ width: '100%' }"></p-calendar>
        </div>
        <div class="filter-actions">
          <button pButton label="{{ 'common.search' | translate }}" icon="pi pi-search" class="p-button-sm"
                  (click)="onSearch()"></button>
          <button pButton label="{{ 'common.clear' | translate }}" icon="pi pi-times" class="p-button-sm p-button-outlined"
                  (click)="onClearFilters()"></button>
        </div>
      </div>
    </div>

    <!-- Data Table -->
    <wms-data-table
      [data]="orders()"
      [columns]="columns"
      [totalRecords]="totalRecords()"
      [loading]="loading()"
      [pageSize]="20"
      dataKey="AVH_AV_NUM"
      (lazyLoad)="onLazyLoad($event)"
      (refresh)="onSearch()">

      <ng-template #customCell let-row let-col="column">
        @if (col.field === 'AVH_AV_STS') {
          <wms-status-badge [variant]="getStatusVariant(row.AVH_AV_STS)" [label]="row.AVH_AV_STS_LBL || row.AVH_AV_STS"></wms-status-badge>
        }
      </ng-template>

      <ng-template #actionCell let-row>
        <div class="action-buttons">
          <button pButton icon="pi pi-eye" class="p-button-text p-button-sm p-button-rounded"
                  (click)="viewOrder(row)" pTooltip="View"></button>
        </div>
      </ng-template>
    </wms-data-table>

  `,
  styles: [`
    .filter-panel {
      background: white;
      border-radius: 14px;
      border: 1px solid #e5e7eb;
      padding: 18px 20px;
      margin-bottom: 18px;
    }
    .filter-row {
      display: flex;
      gap: 14px;
      align-items: flex-end;
      flex-wrap: wrap;
    }
    .filter-field {
      display: flex;
      flex-direction: column;
      gap: 5px;
      min-width: 160px;
      flex: 1;
    }
    .filter-field label {
      font-size: 12px;
      font-weight: 600;
      color: #6b7280;
    }
    .filter-actions {
      display: flex;
      gap: 8px;
      padding-bottom: 1px;
    }
    .w-full { width: 100%; }
    .action-buttons { display: flex; gap: 2px; justify-content: center; }

    .new-order-btn {
      background: rgb(142, 196, 0) !important;
      border-color: rgb(142, 196, 0) !important;
      color: #1f2937 !important;
      font-weight: 600;
    }

    .new-order-btn:hover {
      background: rgb(124, 176, 0) !important;
      border-color: rgb(124, 176, 0) !important;
    }

    .new-order-btn:focus {
      box-shadow: 0 0 0 3px rgba(142, 196, 0, 0.28) !important;
    }
  `]
})
export class InboundOrdersComponent implements OnInit {
  orders = signal<any[]>([]);
  totalRecords = signal(0);
  loading = signal(false);
  page = 0;
  size = 20;

  filters = {
    arrivalNo: '',
    status: null as string | null,
    supplier: '',
    dateFrom: null as Date | null,
    dateTo: null as Date | null
  };

  statusOptions = [
    { label: 'All Statuses', value: null },
    { label: '100 - Created', value: '100' },
    { label: '200 - Located', value: '200' },
    { label: '205 - Inspected', value: '205' },
    { label: '209 - Confirmed', value: '209' },
    { label: '300 - Receiving', value: '300' },
    { label: '500 - Putaway', value: '500' },
    { label: '605 - Putaway Done', value: '605' },
    { label: '609 - Stored', value: '609' },
    { label: '700 - Closed', value: '700' },
    { label: '809 - Completed', value: '809' },
    { label: '999 - Cancelled', value: '999' }
  ];

  columns: WmsColumn[] = [
    { field: 'AVH_AV_NUM', header: 'inbound.arrival_no', sortable: true, width: '140px', frozen: true },
    { field: 'AVH_AV_STS', header: 'common.status', sortable: true, width: '130px', type: 'custom' },
    { field: 'AVH_SUPLR_COD', header: 'inbound.supplier', sortable: true, width: '180px' },
    { field: 'AVH_PO_NUM', header: 'inbound.po_number', sortable: true, width: '140px' },
    { field: 'AVH_SCDL_YMD', header: 'inbound.expected_date', sortable: true, width: '120px', type: 'date' },
    { field: 'AVH_AV_YMD', header: 'inbound.actual_date', sortable: true, width: '120px', type: 'date' },
    { field: 'AVH_TTL_QTY', header: 'common.total_qty', sortable: true, width: '100px', type: 'number', align: 'right' },
    { field: 'AVH_RMK', header: 'common.remarks', width: '200px' }
  ];

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.onSearch();
  }

  onLazyLoad(event: TableLazyLoadEvent): void {
    const rows = event.rows ?? this.size;
    const first = event.first ?? 0;
    this.size = rows;
    this.page = Math.floor(first / rows);
    this.onSearch();
  }

  onSearch(): void {
    this.loading.set(true);
    this.api.get<any[]>('/inbound/orders', {
      arrivalNo: this.filters.arrivalNo,
      status: this.filters.status,
      supplier: this.filters.supplier,
      dateFrom: this.formatDate(this.filters.dateFrom),
      dateTo: this.formatDate(this.filters.dateTo),
      page: this.page,
      size: this.size
    }).pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: (response) => {
        if (response.status === 'SUCCESS') {
          this.orders.set(response.data || []);
          this.totalRecords.set(response.totalRecords ?? (response.data || []).length);
          return;
        }
        this.orders.set([]);
        this.totalRecords.set(0);
      },
      error: () => {
        this.orders.set([]);
        this.totalRecords.set(0);
      }
    });
  }

  onClearFilters(): void {
    this.filters = { arrivalNo: '', status: null, supplier: '', dateFrom: null, dateTo: null };
    this.page = 0;
    this.onSearch();
  }

  getStatusVariant(status: string): any {
    const map: Record<string, string> = {
      '100': 'pending',
      '200': 'pending',
      '205': 'in-progress',
      '209': 'confirmed',
      '300': 'in-progress',
      '500': 'in-progress',
      '605': 'confirmed',
      '609': 'confirmed',
      '700': 'confirmed',
      '809': 'completed',
      '999': 'cancelled',
      PENDING: 'pending',
      RECEIVING: 'in-progress',
      CONFIRMED: 'confirmed',
      COMPLETED: 'completed',
      CANCELLED: 'cancelled'
    };
    return map[(status || '').toUpperCase()] || map[status] || 'pending';
  }

  viewOrder(row: any): void {
    this.router.navigate(['/inbound/orders', row.AVH_AV_NUM]);
  }

  openNewOrderEntry(): void {
    this.router.navigate(['/inbound/orders/new']);
  }

  private formatDate(value: Date | null): string | null {
    if (!value) {
      return null;
    }
    const year = value.getFullYear();
    const month = String(value.getMonth() + 1).padStart(2, '0');
    const day = String(value.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
