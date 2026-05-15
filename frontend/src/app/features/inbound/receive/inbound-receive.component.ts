import { Component, signal } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';
import { LpnService, LpnReceiveRequest } from '@core/services/lpn.service';
import { ApiService } from '@core/services/api.service';

interface ReceiveArrivalDto {
  arrivalNumber: string;
  arrivalStatus: string;
  arrivalStatusLabel: string;
  scheduledDate: string | null;
  arrivalDate: string | null;
  supplierCode: string;
  supplierName: string;
  poNumber: string;
  remarks: string;
  totalPlannedQty: number;
  totalReceivedQty: number;
  lines: InspectionLineDto[];
}

interface InspectionLineDto {
  lineNumber: number;
  productCode: string;
  productName: string;
  lotNumber: string;
  planTotalQty: number;
  resultTotalQty: number;
  areaCode: string;
  rackCode: string;
  levelCode: string;
  positionCode: string;
  lpnNumber: string;
}

interface ScanItemResponse {
  arrivalNumber: string;
  lineNumber: number;
  productCode: string;
  productName: string;
  resultTotalQty: number;
  planTotalQty: number;
  variance: number;
  lpnNumber: string;
  headerStatus: string;
  totalPlannedQty: number;
  totalReceivedQty: number;
  receiveStatus: string;       // "OK" | "SHORT" | "OVER"
  receiveWarning: string | null;
  arrival: ReceiveArrivalDto | null;
}

interface ScannedItem {
  barcode: string;
  productName: string;
  qty: number;
  lineNumber: number;
}

interface CompletedLpn {
  lpnNumber: string;
  rfBarcode?: string;
  typeLabel: string;
  itemCount: number;
  totalQty: number;
}

@Component({
  selector: 'wms-inbound-receive',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, DropdownModule, InputNumberModule,
            PageHeaderComponent, BarcodeInputComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="inbound.receive_title" subtitle="inbound.receive_subtitle" icon="pi pi-qrcode">
    </wms-page-header>

    <!-- Mode Toggle -->
    <div class="mode-toggle">
      <button pButton [class]="receiveMode === 'item' ? 'p-button-sm' : 'p-button-sm p-button-outlined'"
              label="By Item" icon="pi pi-barcode" (click)="receiveMode = 'item'"></button>
      <button pButton [class]="receiveMode === 'lpn' ? 'p-button-sm' : 'p-button-sm p-button-outlined'"
              label="By LPN" icon="pi pi-box" (click)="receiveMode = 'lpn'"></button>
    </div>

    <div class="receive-layout">
      <!-- Scan Arrival Order (shared) -->
      <div class="scan-panel">
        <div class="wms-card">
          <h3 class="section-title">Scan Arrival Order</h3>
          <wms-barcode-input placeholder="Scan arrival number..." (scan)="onScanArrival($event)"></wms-barcode-input>
        </div>

        <div class="wms-card" *ngIf="arrival">
          <h3 class="section-title">Order Details</h3>
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-label">Arrival No</span>
              <span class="detail-value">{{ arrival.arrivalNumber }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Supplier</span>
              <span class="detail-value">{{ arrival.supplierName || arrival.supplierCode }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Status</span>
              <wms-status-badge [variant]="'in-progress'" [label]="arrival.arrivalStatusLabel"></wms-status-badge>
            </div>
            <div class="detail-item">
              <span class="detail-label">PO Number</span>
              <span class="detail-value">{{ arrival.poNumber }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Expected Qty</span>
              <span class="detail-value">{{ arrival.totalPlannedQty }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Received Qty</span>
              <span class="detail-value accent">{{ arrival.totalReceivedQty }}</span>
            </div>
            <div class="detail-item" *ngIf="receiveMode === 'lpn'">
              <span class="detail-label">LPNs Created</span>
              <span class="detail-value accent">{{ lpnCount }}</span>
            </div>
          </div>

          <!-- Progress bar -->
          <div class="progress-wrap" *ngIf="arrival.totalPlannedQty > 0">
            <div class="progress-bar">
              <div class="progress-fill" [style.width.%]="progressPct"></div>
            </div>
            <span class="progress-label">{{ arrival.totalReceivedQty }} / {{ arrival.totalPlannedQty }} ({{ progressPct }}%)</span>
          </div>
        </div>

        <!-- Line Progress Panel -->
        <div class="wms-card" *ngIf="arrival && arrival.lines.length > 0">
          <h3 class="section-title">Line Details</h3>
          <table class="lines-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Plan</th>
                <th>Received</th>
              </tr>
            </thead>
            <tbody>
              @for (line of arrival.lines; track line.lineNumber) {
                <tr [class.line-complete]="line.resultTotalQty >= line.planTotalQty">
                  <td class="prod-code">{{ line.productCode }}</td>
                  <td class="qty-cell">{{ line.planTotalQty }}</td>
                  <td class="qty-cell">
                    <span [class.qty-ok]="line.resultTotalQty >= line.planTotalQty"
                          [class.qty-partial]="line.resultTotalQty > 0 && line.resultTotalQty < line.planTotalQty">
                      {{ line.resultTotalQty }}
                    </span>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </div>

      <!-- Item Mode Panel -->
      <div class="items-panel" *ngIf="arrival && receiveMode === 'item'">
        <div class="wms-card">
          <h3 class="section-title">Scan Items</h3>
          <wms-barcode-input placeholder="Scan item barcode..." (scan)="onScanItem($event)"></wms-barcode-input>

          <div class="scanned-items">
            @for (item of scannedItems; track item.barcode + '_' + item.lineNumber) {
              <div class="scanned-item">
                <div class="item-info">
                  <span class="item-code">{{ item.barcode }}</span>
                  <span class="item-name">{{ item.productName }}</span>
                </div>
                <div class="item-qty">
                  <span>Qty: {{ item.qty }}</span>
                </div>
                <wms-status-badge variant="received" label="OK" [showIcon]="false"></wms-status-badge>
              </div>
            }
          </div>
        </div>
      </div>

      <!-- LPN Mode Panel -->
      <div class="items-panel" *ngIf="arrival && receiveMode === 'lpn'">
        <div class="wms-card">
          <h3 class="section-title">
            <i class="pi pi-box"></i> Create LPN
          </h3>

          <!-- LPN Type -->
          <div class="form-field">
            <label>LPN Type</label>
            <p-dropdown [options]="lpnTypes" [(ngModel)]="currentLpnType" optionLabel="label" optionValue="value"
                        [style]="{ width: '100%' }"></p-dropdown>
          </div>

          <!-- LPN Barcode (optional) -->
          <div class="form-field">
            <label>LPN Barcode (leave blank to auto-generate)</label>
            <wms-barcode-input placeholder="Scan or enter LPN barcode..." [showHint]="false" [autoFocus]="false"
                               (scan)="onScanLpnBarcode($event)"></wms-barcode-input>
            <span class="lpn-preview" *ngIf="currentLpnBarcode">LPN: {{ currentLpnBarcode }}</span>
          </div>

          <!-- Scan items INTO the LPN -->
          <h4 class="sub-title">Scan Items into LPN</h4>
          <wms-barcode-input placeholder="Scan item barcode..." (scan)="onScanItemIntoLpn($event)"></wms-barcode-input>

          <div class="scanned-items">
            @for (item of lpnItems; track item.barcode) {
              <div class="scanned-item">
                <div class="item-info">
                  <span class="item-code">{{ item.barcode }}</span>
                  <span class="item-name">{{ item.productName }}</span>
                </div>
                <div class="item-qty-edit">
                  <label>Qty:</label>
                  <p-inputNumber [(ngModel)]="item.qty" [showButtons]="true" [min]="1" [size]="3"
                                 buttonLayout="horizontal" inputStyleClass="qty-input"></p-inputNumber>
                </div>
                <button pButton icon="pi pi-times" class="p-button-sm p-button-text p-button-danger"
                        (click)="removeLpnItem(item)"></button>
              </div>
            }
          </div>

          <div class="lpn-actions" *ngIf="lpnItems.length > 0">
            <div class="lpn-summary">
              <span>{{ lpnItems.length }} item(s), Total Qty: {{ lpnTotalQty }}</span>
            </div>
            <button pButton label="Confirm LPN" icon="pi pi-check" class="p-button-sm"
                    (click)="confirmLpn()" [loading]="submitting"></button>
          </div>
        </div>

        <!-- Completed LPNs -->
        <div class="wms-card" *ngIf="completedLpns.length > 0">
          <h3 class="section-title">Received LPNs</h3>
          <div class="completed-list">
            @for (lpn of completedLpns; track lpn.lpnNumber) {
              <div class="completed-item lpn-completed">
                <wms-status-badge variant="completed" label="OK" [showIcon]="true"></wms-status-badge>
                <div class="lpn-info">
                  <span class="lpn-number">{{ lpn.lpnNumber }}</span>
                  <span class="lpn-rf" *ngIf="lpn.rfBarcode">RF: {{ lpn.rfBarcode }}</span>
                  <span class="lpn-meta">{{ lpn.typeLabel }} · {{ lpn.itemCount }} items · Qty {{ lpn.totalQty }}</span>
                </div>
              </div>
            }
          </div>
        </div>
      </div>
    </div>

    <!-- Messages -->
    <div class="message success" *ngIf="successMsg">
      <i class="pi pi-check-circle"></i> {{ successMsg }}
    </div>
    <div class="message error" *ngIf="errorMsg">
      <i class="pi pi-exclamation-triangle"></i> {{ errorMsg }}
    </div>
  `,
  styles: [`
    .mode-toggle { display: flex; gap: 8px; margin-bottom: 16px; }
    .receive-layout { display: grid; grid-template-columns: 1fr 1fr; gap: 18px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; margin-bottom: 16px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; display: flex; align-items: center; gap: 8px; }
    .sub-title { font-size: 13px; font-weight: 600; color: #374151; margin: 16px 0 8px; }
    .detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .detail-item { display: flex; flex-direction: column; gap: 3px; }
    .detail-label { font-size: 12px; color: #6b7280; font-weight: 500; }
    .detail-value { font-size: 14px; color: #111827; font-weight: 600; }
    .detail-value.accent { color: #8EC400; }
    .progress-wrap { margin-top: 14px; }
    .progress-bar { height: 8px; background: #e5e7eb; border-radius: 4px; overflow: hidden; }
    .progress-fill { height: 100%; background: #8EC400; border-radius: 4px; transition: width 0.3s; }
    .progress-label { font-size: 12px; color: #6b7280; margin-top: 4px; display: block; }
    .lines-table { width: 100%; border-collapse: collapse; font-size: 13px; }
    .lines-table th { text-align: left; padding: 6px 8px; border-bottom: 2px solid #e5e7eb; color: #6b7280; font-weight: 600; }
    .lines-table td { padding: 6px 8px; border-bottom: 1px solid #f3f4f6; }
    .lines-table tr.line-complete { background: #f0fdf4; }
    .prod-code { font-family: monospace; color: #1A005D; font-weight: 600; }
    .qty-cell { text-align: right; }
    .qty-ok { color: #166534; font-weight: 700; }
    .qty-partial { color: #92400e; font-weight: 600; }
    .form-field { display: flex; flex-direction: column; gap: 5px; margin-bottom: 12px; }
    .form-field label { font-size: 13px; font-weight: 600; color: #374151; }
    .lpn-preview { font-size: 12px; color: #1A005D; font-weight: 600; font-family: monospace; }
    .scanned-items { margin-top: 12px; display: flex; flex-direction: column; gap: 8px; }
    .scanned-item { display: flex; align-items: center; gap: 12px; padding: 10px 12px; background: #f9fafb; border-radius: 8px; }
    .item-info { display: flex; flex-direction: column; flex: 1; }
    .item-code { font-size: 13px; font-weight: 600; color: #111827; font-family: monospace; }
    .item-name { font-size: 12px; color: #6b7280; }
    .item-qty { font-size: 13px; font-weight: 600; color: #374151; }
    .item-qty-edit { display: flex; align-items: center; gap: 6px; font-size: 13px; font-weight: 600; }
    :host ::ng-deep .qty-input { width: 60px !important; text-align: center; }
    .lpn-actions { display: flex; justify-content: space-between; align-items: center; margin-top: 16px; padding-top: 12px; border-top: 1px solid #e5e7eb; }
    .lpn-summary { font-size: 13px; font-weight: 600; color: #374151; }
    .completed-list { display: flex; flex-direction: column; gap: 6px; }
    .completed-item { display: flex; align-items: center; gap: 10px; padding: 8px 12px; background: #f9fafb; border-radius: 8px; }
    .lpn-info { display: flex; flex-direction: column; }
    .lpn-number { font-family: monospace; font-size: 13px; font-weight: 600; color: #1A005D; }
    .lpn-rf { font-size: 11px; color: #1A005D; font-family: monospace; font-weight: 600; }
    .lpn-meta { font-size: 11px; color: #6b7280; }
    .message { padding: 12px 16px; border-radius: 8px; margin-top: 16px; font-size: 13px; display: flex; align-items: center; gap: 8px; }
    .message.success { background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; }
    .message.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; }
    @media (max-width: 768px) { .receive-layout { grid-template-columns: 1fr; } }
  `]
})
export class InboundReceiveComponent {
  receiveMode: 'item' | 'lpn' = 'item';
  arrival: ReceiveArrivalDto | null = null;
  scannedItems: ScannedItem[] = [];

  loadingArrival = signal(false);
  scanning = signal(false);

  // LPN mode state
  currentLpnBarcode = '';
  currentLpnType = 'PLT';
  lpnItems: any[] = [];
  completedLpns: CompletedLpn[] = [];
  lpnCount = 0;
  submitting = false;
  successMsg = '';
  errorMsg = '';

  lpnTypes = [
    { label: 'Pallet', value: 'PLT' },
    { label: 'Case / Carton', value: 'CSE' },
    { label: 'Tote', value: 'TOT' },
    { label: 'Other', value: 'OTH' }
  ];

  constructor(private lpnService: LpnService, private api: ApiService) {}

  get lpnTotalQty(): number {
    return this.lpnItems.reduce((sum: number, item: any) => sum + (item.qty || 0), 0);
  }

  get progressPct(): number {
    if (!this.arrival || this.arrival.totalPlannedQty === 0) return 0;
    return Math.min(100, Math.round((this.arrival.totalReceivedQty / this.arrival.totalPlannedQty) * 100));
  }

  // ---------------------------------------------------------------------------
  // Scan arrival number → load header + lines from backend
  // ---------------------------------------------------------------------------

  onScanArrival(barcode: string): void {
    if (!barcode || !barcode.trim()) return;
    this.loadingArrival.set(true);
    this.successMsg = '';
    this.errorMsg = '';
    this.arrival = null;
    this.scannedItems = [];
    this.lpnItems = [];
    this.completedLpns = [];
    this.lpnCount = 0;

    this.api.get<ReceiveArrivalDto>(`/inbound/receive/${encodeURIComponent(barcode.trim())}`)
      .subscribe({
        next: (res) => {
          this.loadingArrival.set(false);
          if (res.status === 'SUCCESS' && res.data) {
            this.arrival = res.data;
          } else {
            this.errorMsg = res.messages?.[0]?.message || 'Arrival not found';
          }
        },
        error: (err) => {
          this.loadingArrival.set(false);
          this.errorMsg = err.error?.messages?.[0]?.message || 'Failed to load arrival';
        }
      });
  }

  // ---------------------------------------------------------------------------
  // Item mode: each scan immediately persists AVR
  // ---------------------------------------------------------------------------

  onScanItem(barcode: string): void {
    if (!barcode || !barcode.trim() || !this.arrival) return;
    this.scanning.set(true);
    this.successMsg = '';
    this.errorMsg = '';

    this.api.post<ScanItemResponse>('/inbound/receive/scan', {
      arrivalNumber: this.arrival.arrivalNumber,
      productCode: barcode.trim(),
      quantity: 1
    }).subscribe({
      next: (res) => {
        this.scanning.set(false);
        if (res.status === 'SUCCESS' && res.data) {
          const r = res.data;
          this.scannedItems.unshift({
            barcode: r.productCode,
            productName: r.productName,
            qty: 1,
            lineNumber: r.lineNumber
          });
          // Sync full arrival data from backend
          if (r.arrival) {
            this.arrival = r.arrival;
          }
          // Show warning for SHORT or OVER receive
          if ((r.receiveStatus === 'SHORT' || r.receiveStatus === 'OVER') && r.receiveWarning) {
            this.errorMsg = r.receiveWarning;
          }
        } else {
          this.errorMsg = res.messages?.[0]?.message || 'Scan failed';
        }
      },
      error: (err) => {
        this.scanning.set(false);
        this.errorMsg = err.error?.messages?.[0]?.message || 'Scan failed';
      }
    });
  }

  // ---------------------------------------------------------------------------
  // LPN mode
  // ---------------------------------------------------------------------------

  onScanLpnBarcode(barcode: string): void {
    if (!barcode || !barcode.trim()) {
      this.currentLpnBarcode = '';
      return;
    }
    this.errorMsg = '';
    this.lpnService.validateBarcode(barcode.trim()).subscribe({
      next: (res) => {
        if (res.status === 'SUCCESS') {
          this.currentLpnBarcode = barcode.trim();
        } else {
          this.errorMsg = res.messages?.[0]?.message || 'LPN Number already scanned.';
          this.currentLpnBarcode = '';
        }
      },
      error: (err) => {
        this.errorMsg = err.error?.messages?.[0]?.message || 'LPN Number already scanned.';
        this.currentLpnBarcode = '';
      }
    });
  }

  onScanItemIntoLpn(barcode: string): void {
    if (!barcode || !barcode.trim() || !this.arrival) return;
    this.errorMsg = '';
    const matchedLine = this.arrival.lines.find(
      line => line.productCode.trim().toLowerCase() === barcode.trim().toLowerCase()
    );
    if (!matchedLine) {
      this.errorMsg = 'Product [' + barcode + '] is not on this arrival';
      return;
    }
    const existing = this.lpnItems.find((i: any) => i.barcode === barcode);
    if (existing) {
      existing.qty++;
    } else {
      this.lpnItems.push({ barcode, productName: matchedLine.productName || barcode, qty: 1, lineNumber: matchedLine.lineNumber });
    }
  }

  removeLpnItem(item: any): void {
    this.lpnItems = this.lpnItems.filter((i: any) => i !== item);
  }

  confirmLpn(): void {
    if (!this.arrival || this.lpnItems.length === 0) return;
    this.submitting = true;
    this.successMsg = '';
    this.errorMsg = '';

    const request: LpnReceiveRequest = {
      arrivalNumber: this.arrival.arrivalNumber,
      lpnRfNumber: this.currentLpnBarcode || undefined,
      lpnType: this.currentLpnType,
      items: this.lpnItems.map((item: any) => ({
        productCode: item.barcode,
        quantity: item.qty
      }))
    };

    this.lpnService.receive(request).subscribe({
      next: (res) => {
        if (res.status === 'SUCCESS' && res.data) {
          const lpn = res.data;
          const confirmedLpn = lpn.lpnNumber;
          const itemsSnapshot = [...this.lpnItems];
          const totalQty = this.lpnTotalQty;
          const typeLabel = lpn.lpnTypeLabel || this.currentLpnType;

          const scanRequests = itemsSnapshot.map((item: any) =>
            this.api.post<ScanItemResponse>('/inbound/receive/scan', {
              arrivalNumber: this.arrival!.arrivalNumber,
              productCode: item.barcode,
              quantity: item.qty,
              lpnNumber: confirmedLpn,
              lpnType: this.currentLpnType,
              lineNumber: item.lineNumber
            }).pipe(catchError(() => of(null)))
          );

          forkJoin(scanRequests).subscribe(results => {
            this.submitting = false;
            const failed = results.filter(r => r === null).length;
            // Sync full arrival data from the last successful scan response
            for (let i = results.length - 1; i >= 0; i--) {
              const r = results[i];
              if (r && (r as any).status === 'SUCCESS' && (r as any).data?.arrival) {
                this.arrival = (r as any).data.arrival;
                break;
              }
            }
            if (failed === 0) {
              this.successMsg = 'LPN ' + confirmedLpn + ' received successfully (' + itemsSnapshot.length + ' items)';
              this.completedLpns.unshift({ lpnNumber: confirmedLpn, rfBarcode: lpn.lpnRfNumber || undefined, typeLabel, itemCount: itemsSnapshot.length, totalQty });
              this.lpnCount++;
            } else {
              this.errorMsg = 'LPN received but ' + failed + ' item(s) failed to record. Please check.';
            }
            this.lpnItems = [];
            this.currentLpnBarcode = '';
          });
        } else {
          this.submitting = false;
          this.errorMsg = res.messages?.[0]?.message || 'Failed to receive LPN';
        }
      },
      error: (err) => {
        this.submitting = false;
        this.errorMsg = err.error?.messages?.[0]?.message || 'Failed to receive LPN';
      }
    });
  }
}
