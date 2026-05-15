import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { StatusBadgeComponent, BadgeVariant } from '@shared/components/status-badge/status-badge.component';
import { LpnService, LpnDto } from '@core/services/lpn.service';

@Component({
  selector: 'wms-lpn-inquiry',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, ConfirmDialogModule,
            DropdownModule, InputTextModule, TableModule, ToastModule,
            PageHeaderComponent, StatusBadgeComponent],
  providers: [ConfirmationService, MessageService],
  template: `
    <wms-page-header title="lpn.inquiry_title" subtitle="lpn.inquiry_subtitle" icon="pi pi-box">
    </wms-page-header>

    <p-toast></p-toast>
    <p-confirmDialog></p-confirmDialog>

    <!-- Filters -->
    <div class="wms-card filter-card">
      <div class="filter-grid">
        <div class="filter-field">
          <label>LPN Number</label>
          <input pInputText [(ngModel)]="filters.lpnNumber" placeholder="Search LPN..." />
        </div>
        <div class="filter-field">
          <label>LPN Refer Number</label>
          <input pInputText [(ngModel)]="filters.lpnRfNumber" placeholder="Search RF number..." />
        </div>
        <div class="filter-field">
          <label>Status</label>
          <p-dropdown [options]="statusOptions" [(ngModel)]="filters.status" optionLabel="label" optionValue="value"
                      placeholder="All" [showClear]="true" [style]="{ width: '100%' }"></p-dropdown>
        </div>
        <div class="filter-field">
          <label>Type</label>
          <p-dropdown [options]="typeOptions" [(ngModel)]="filters.lpnType" optionLabel="label" optionValue="value"
                      placeholder="All" [showClear]="true" [style]="{ width: '100%' }"></p-dropdown>
        </div>
        <div class="filter-field">
          <label>Location</label>
          <input pInputText [(ngModel)]="filters.locationCode" placeholder="Location code..." />
        </div>
        <div class="filter-actions">
          <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
          <button pButton label="Clear" icon="pi pi-times" class="p-button-sm p-button-outlined" (click)="clearFilters()"></button>
        </div>
      </div>
    </div>

    <!-- Results -->
    <div class="wms-card">
      <p-table [value]="lpns" [loading]="loading" [paginator]="true" [rows]="20"
               [totalRecords]="totalRecords" [lazy]="true" (onLazyLoad)="onPageChange($event)"
               styleClass="p-datatable-sm" [rowHover]="true">
        <ng-template pTemplate="header">
          <tr>
            <th style="width: 180px">LPN Number</th>
            <th style="width: 160px">LPN Refer No</th>
            <th style="width: 80px">Type</th>
            <th style="width: 100px">Status</th>
            <th>Location</th>
            <th>Arrival No</th>
            <th style="width: 80px; text-align: right">Qty</th>
            <th style="width: 100px">Received</th>
            <th style="width: 100px">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-lpn>
          <tr>
            <td><span class="lpn-number">{{ lpn.lpnNumber }}</span></td>
            <td><span class="lpn-number">{{ lpn.lpnRfNumber || '-' }}</span></td>
            <td>{{ lpn.lpnTypeLabel }}</td>
            <td><wms-status-badge [variant]="statusVariant(lpn.lpnStatus)" [label]="lpn.lpnStatusLabel"></wms-status-badge></td>
            <td><span class="location-code">{{ lpn.locationCode || '-' }}</span></td>
            <td>{{ lpn.arrivalNumber || '-' }}</td>
            <td style="text-align: right; font-weight: 600">{{ lpn.totalQuantity }}</td>
            <td>{{ lpn.receiveDate || '-' }}</td>
            <td>
              <button pButton icon="pi pi-eye" class="p-button-sm p-button-text"
                      (click)="viewDetail(lpn.lpnNumber)"></button>
              <button pButton icon="pi pi-trash" class="p-button-sm p-button-text p-button-danger"
                      (click)="removeLpn(lpn)"></button>
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="9" class="empty-text">No LPNs found</td></tr>
        </ng-template>
      </p-table>
    </div>
  `,
  styles: [`
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; margin-bottom: 16px; }
    .filter-card { margin-bottom: 16px; }
    .filter-grid { display: grid; grid-template-columns: repeat(5, 1fr) auto; gap: 12px; align-items: end; }
    .filter-field { display: flex; flex-direction: column; gap: 4px; }
    .filter-field label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; gap: 8px; align-items: end; }
    .lpn-number { font-family: monospace; font-weight: 600; color: #1A005D; cursor: pointer; }
    .location-code { font-family: monospace; font-size: 13px; }
    .empty-text { text-align: center; color: #9ca3af; padding: 20px; }
    @media (max-width: 768px) { .filter-grid { grid-template-columns: 1fr 1fr; } }
  `]
})
export class LpnInquiryComponent implements OnInit {
  lpns: LpnDto[] = [];
  loading = false;
  totalRecords = 0;

  filters = {
    lpnNumber: '',
    lpnRfNumber: '',
    status: '',
    lpnType: '',
    locationCode: ''
  };

  statusOptions = [
    { label: 'Pre', value: '100' },
    { label: 'Ready for Receiving', value: '200' },
    { label: 'Inspecting', value: '205' },
    { label: 'Inspected', value: '209' },
    { label: 'Receiving', value: '300' },
    { label: 'Locating', value: '305' },
    { label: 'Located', value: '306' },
    { label: 'Putaway', value: '500' },
    { label: 'Stored', value: '609' },
    { label: 'Confirmed', value: '809' }
  ];

  typeOptions = [
    { label: 'Pallet', value: 'PLT' },
    { label: 'Case', value: 'CSE' },
    { label: 'Tote', value: 'TOT' },
    { label: 'Other', value: 'OTH' }
  ];

  constructor(
    private lpnService: LpnService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.search();
  }

  search(page = 0): void {
    this.loading = true;
    this.lpnService.search({ ...this.filters, page, size: 20 }).subscribe({
      next: (res) => {
        this.loading = false;
        this.lpns = res.data || [];
        this.totalRecords = res.totalRecords || 0;
      },
      error: () => { this.loading = false; }
    });
  }

  clearFilters(): void {
    this.filters = { lpnNumber: '', lpnRfNumber: '', status: '', lpnType: '', locationCode: '' };
    this.search();
  }

  onPageChange(event: any): void {
    this.search(event.first / event.rows);
  }

  viewDetail(lpnNumber: string): void {
    this.router.navigate(['/inventory/lpn', lpnNumber]);
  }

  removeLpn(lpn: LpnDto): void {
    if (lpn.lpnStatus === '809') {
      this.messageService.add({
        severity: 'error',
        summary: 'Cannot Remove',
        detail: 'LPN status is Confirmed (809). Remove is not allowed.'
      });
      return;
    }
    const rfDisplay = lpn.lpnRfNumber ? ` LPN refer number: ${lpn.lpnRfNumber}` : '';
    this.confirmationService.confirm({
      message: `Confirm to remove LPN number: ${lpn.lpnNumber}${rfDisplay}?`,
      header: 'Confirm Remove',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.lpnService.remove(lpn.lpnNumber).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Removed', detail: 'LPN removed successfully.' });
            this.search(0);
          },
          error: (err) => {
            const msg = err?.error?.message || 'Failed to remove LPN.';
            this.messageService.add({ severity: 'error', summary: 'Error', detail: msg });
          }
        });
      }
    });
  }

  statusVariant(status: string): BadgeVariant {
    switch (status) {
      case '100': return 'draft';
      case '200': return 'pending';
      case '205': return 'in-progress';
      case '209': return 'received';
      case '300': return 'in-progress';
      case '305': case '306': return 'allocated';
      case '500': return 'in-progress';
      case '609': return 'completed';
      case '809': return 'confirmed';
      default: return 'pending';
    }
  }
}
