import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { finalize } from 'rxjs/operators';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { DataTableComponent, WmsColumn } from '@shared/components/data-table/data-table.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';
import { ApiService } from '@core/services/api.service';

@Component({
  selector: 'wms-inbound-confirm',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    ButtonModule,
    InputTextModule,
    MessageModule,
    PageHeaderComponent,
    DataTableComponent,
    StatusBadgeComponent
  ],
  template: `
    <wms-page-header title="inbound.confirm_title" subtitle="inbound.confirm_subtitle" icon="pi pi-check-circle">
      <button pButton label="{{ 'inbound.confirm_selected' | translate }}" icon="pi pi-check" class="p-button-sm"
              [disabled]="!selectedOrders.length || confirming()" [loading]="confirming()" (click)="confirmSelected()"></button>
    </wms-page-header>

    @if (confirmFeedback()) {
      <p-message
        [severity]="confirmFeedback()!.type === 'success' ? 'success' : 'error'"
        [text]="confirmFeedback()!.text"
        styleClass="mb-3 w-full"
        (click)="confirmFeedback.set(null)">
      </p-message>
    }

    <div class="filter-panel">
      <div class="filter-row">
        <div class="filter-field">
          <label>{{ 'inbound.arrival_no' | translate }}</label>
          <input pInputText [(ngModel)]="arrivalNoFilter" placeholder="AV-..." class="w-full apcnf-arrival-filter">
        </div>
        <div class="filter-actions">
          <button pButton label="{{ 'common.search' | translate }}" icon="pi pi-search" class="p-button-sm apcnf-search-btn"
                  [disabled]="loading()" [loading]="loading()" (click)="loadOrders()"></button>
        </div>
      </div>
    </div>

    <wms-data-table [data]="orders()" [columns]="columns" [totalRecords]="orders().length" [loading]="loading()"
                    [lazy]="false" dataKey="arrivalNo" selectionMode="multiple" [(selection)]="selectedOrders">
      <ng-template #customCell let-row let-col="column">
        @if (col.field === 'status') {
          <wms-status-badge [variant]="getStatusVariant(row.status)" [label]="row.statusLabel || row.status"></wms-status-badge>
        }
      </ng-template>
      <ng-template #actionCell let-row>
        <button pButton icon="pi pi-check" class="p-button-text p-button-success p-button-sm p-button-rounded"
                pTooltip="Confirm" (click)="confirmOrder(row)"></button>
      </ng-template>
    </wms-data-table>
  `,
  styles: [
    `.action-buttons { display: flex; gap: 2px; justify-content: center; }
     .filter-panel {
      background: white;
      border-radius: 14px;
      border: 1px solid #e5e7eb;
      padding: 14px 16px;
      margin-bottom: 14px;
    }
    .filter-row {
      display: flex;
      gap: 12px;
      align-items: flex-end;
      flex-wrap: wrap;
    }
    .filter-field {
      display: flex;
      flex-direction: column;
      gap: 4px;
      min-width: 240px;
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
    .w-full { width: 100%; }`
  ]
})
export class InboundConfirmComponent implements OnInit {
  selectedOrders: any[] = [];
  orders = signal<any[]>([]);
  loading = signal(false);
  confirming = signal(false);
  arrivalNoFilter = '';
  confirmFeedback = signal<{ type: 'success' | 'error'; text: string } | null>(null);

  columns: WmsColumn[] = [
    { field: 'arrivalNo', header: 'inbound.arrival_no', sortable: true, width: '140px' },
    { field: 'status', header: 'common.status', width: '130px', type: 'custom' },
    { field: 'supplier', header: 'inbound.supplier', sortable: true },
    { field: 'receivedQty', header: 'common.received_qty', width: '120px', type: 'number', align: 'right' },
    { field: 'expectedQty', header: 'common.expected_qty', width: '120px', type: 'number', align: 'right' },
    { field: 'receivedDate', header: 'common.date', width: '120px', type: 'date' }
  ];

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.api.get<any[]>('/inbound/orders', {
      page: 0,
      size: 100,
      arrivalNo: this.arrivalNoFilter
    }).pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: response => {
        if (response.status !== 'SUCCESS') {
          this.orders.set([]);
          return;
        }

        const incoming = response.data || [];
        this.orders.set(incoming.map(item => ({
          arrivalNo: item.AVH_AV_NUM,
          status: item.AVH_AV_STS,
          statusLabel: item.AVH_AV_STS_LBL,
          supplier: item.AVH_SUPLR_COD,
          receivedQty: item.AVH_TTL_QTY,
          expectedQty: item.AVH_TTL_QTY,
          receivedDate: item.AVH_AV_YMD,
          raw: item
        })));
      },
      error: () => {
        this.orders.set([]);
      }
    });
  }

  confirmOrder(row: any): void {
    if (!row?.arrivalNo) {
      return;
    }
    this.submitConfirm([row.arrivalNo]);
  }

  confirmSelected(): void {
    const arrivalNumbers = this.selectedOrders
      .map(item => item?.arrivalNo)
      .filter((value): value is string => Boolean(value));

    if (!arrivalNumbers.length) {
      return;
    }

    this.submitConfirm(arrivalNumbers);
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

  private mapStatusLabel(code: string): string {
    const map: Record<string, string> = {
      '100': 'Pre',
      '200': 'Located',
      '205': 'Inspecting',
      '209': 'Inspected',
      '300': 'Receiving',
      '305': 'Locating',
      '306': 'Located',
      '500': 'Putaway',
      '605': 'Putaway Done',
      '609': 'Stored',
      '700': 'Closed',
      '809': 'Confirmed',
      '999': 'Cancelled'
    };
    return map[code] || code;
  }

  private submitConfirm(arrivalNumbers: string[]): void {
    this.confirming.set(true);
    this.confirmFeedback.set(null);
    this.api.post('/inbound/apcnf/confirm', {
      arrivalNumbers,
      arrivalDate: new Date().toISOString().slice(0, 10)
    }).pipe(
      finalize(() => this.confirming.set(false))
    ).subscribe({
      next: (response: any) => {
        const confirmedCount = response?.data?.confirmedCount ?? arrivalNumbers.length;
        const appliedStatus = response?.data?.appliedStatus || '809';
        const statusLabel = this.mapStatusLabel(appliedStatus);
        this.confirmFeedback.set({
          type: 'success',
          text: `Successfully confirmed ${confirmedCount} arrival(s) to status ${statusLabel}.`
        });
        this.selectedOrders = [];
        // Optimistically update the confirmed rows' status badge immediately
        const confirmedSet = new Set(arrivalNumbers);
        this.orders.update(orders =>
          orders.map(o =>
            confirmedSet.has(o.arrivalNo) ? { ...o, status: appliedStatus, statusLabel: this.mapStatusLabel(appliedStatus) } : o
          )
        );
        this.loadOrders();
      },
      error: (err: any) => {
        const apiMessages: any[] = err?.error?.messages;
        const firstMsg = Array.isArray(apiMessages) && apiMessages.length > 0
          ? (apiMessages[0]?.message || apiMessages[0]?.text)
          : null;
        const message = firstMsg
          || err?.error?.message
          || 'Confirmation failed. Please verify that all selected arrivals have been inspected and quantities match.';
        this.confirmFeedback.set({ type: 'error', text: message });
      }
    });
  }
}
