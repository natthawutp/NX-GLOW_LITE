import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';
import { StatusBadgeComponent, BadgeVariant } from '@shared/components/status-badge/status-badge.component';
import { LpnService, LpnPutawayRequest, LpnDto } from '@core/services/lpn.service';

@Component({
  selector: 'wms-inbound-putaway',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule, ButtonModule, DropdownModule, PageHeaderComponent, BarcodeInputComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="inbound.putaway_title" subtitle="inbound.putaway_subtitle" icon="pi pi-arrow-right-arrow-left">
    </wms-page-header>

    <!-- Mode Toggle -->
    <div class="mode-toggle">
      <button pButton [class]="putawayMode === 'item' ? 'p-button-sm' : 'p-button-sm p-button-outlined'"
              label="By Item" icon="pi pi-barcode" (click)="putawayMode = 'item'"></button>
      <button pButton [class]="putawayMode === 'lpn' ? 'p-button-sm' : 'p-button-sm p-button-outlined'"
              label="By LPN" icon="pi pi-box" (click)="putawayMode = 'lpn'"></button>
    </div>

    <div class="putaway-layout">
      <!-- Item Mode -->
      <ng-container *ngIf="putawayMode === 'item'">
        <div class="wms-card">
          <h3 class="section-title">Scan Item for Put Away</h3>
          <wms-barcode-input placeholder="Scan item barcode..." (scan)="onScanItem($event)"></wms-barcode-input>
        </div>

        <div class="wms-card" *ngIf="currentItem">
          <h3 class="section-title">Put Away Assignment</h3>
          <div class="putaway-detail">
            <div class="detail-row">
              <span class="detail-label">Item</span>
              <span class="detail-value">{{ currentItem.code }} - {{ currentItem.name }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Quantity</span>
              <span class="detail-value">{{ currentItem.qty }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Suggested Location</span>
              <span class="detail-value highlight">{{ currentItem.suggestedLocation }}</span>
            </div>
            <div class="form-field">
              <label>Target Location</label>
              <wms-barcode-input placeholder="Scan location..." [showHint]="false" [autoFocus]="false"
                                 (scan)="onScanLocation($event)"></wms-barcode-input>
            </div>
            <button pButton label="Confirm Put Away" icon="pi pi-check" class="p-button-sm confirm-btn"
                    (click)="confirmPutaway()" [disabled]="!targetLocation"></button>
          </div>
        </div>
      </ng-container>

      <!-- LPN Mode -->
      <ng-container *ngIf="putawayMode === 'lpn'">
        <div class="wms-card">
          <h3 class="section-title"><i class="pi pi-box"></i> Scan LPN for Put Away</h3>
          <wms-barcode-input placeholder="Scan LPN barcode..." (scan)="onScanLpn($event)"></wms-barcode-input>
        </div>

        <div class="wms-card" *ngIf="currentLpn">
          <h3 class="section-title">LPN Details</h3>
          <div class="putaway-detail">
            <div class="detail-row">
              <span class="detail-label">LPN Number</span>
              <span class="detail-value lpn-highlight">{{ currentLpn.lpnNumber }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Type</span>
              <span class="detail-value">{{ currentLpn.lpnTypeLabel }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Status</span>
              <wms-status-badge [variant]="lpnStatusVariant(currentLpn.lpnStatus)" [label]="currentLpn.lpnStatusLabel"></wms-status-badge>
            </div>
            <div class="detail-row">
              <span class="detail-label">Total Qty</span>
              <span class="detail-value">{{ currentLpn.totalQuantity }}</span>
            </div>
            <div class="detail-row" *ngIf="currentLpn.arrivalNumber">
              <span class="detail-label">Arrival No</span>
              <span class="detail-value">{{ currentLpn.arrivalNumber }}</span>
            </div>

            <!-- LPN Contents -->
            <div class="lpn-contents" *ngIf="currentLpn.contents?.length">
              <h4 class="sub-title">Contents ({{ currentLpn.contents?.length }} items)</h4>
              @for (content of currentLpn.contents; track content.lineNumber) {
                <div class="content-row">
                  <span class="content-product">{{ content.productCode }}</span>
                  <span class="content-qty">Qty: {{ content.quantity }}</span>
                  <span class="content-lot" *ngIf="content.lotNumber">Lot: {{ content.lotNumber }}</span>
                </div>
              }
            </div>

            <!-- Suggested Location -->
            <div class="detail-row">
              <span class="detail-label">Suggested Location</span>
              <span class="detail-value highlight">{{ suggestedLocation }}</span>
            </div>

            <div class="form-field">
              <label>Target Location</label>
              <wms-barcode-input placeholder="Scan location..." [showHint]="false" [autoFocus]="false"
                                 (scan)="onScanLpnLocation($event)"></wms-barcode-input>
            </div>
            <button pButton label="Confirm LPN Put Away" icon="pi pi-check" class="p-button-sm confirm-btn"
                    (click)="confirmLpnPutaway()" [disabled]="!lpnTargetLocation" [loading]="submitting"></button>
          </div>
        </div>
      </ng-container>

      <!-- Completed Put-Aways (shared) -->
      <div class="wms-card">
        <h3 class="section-title">Completed Put-Aways</h3>
        <div class="completed-list">
          @for (item of completedItems; track item.code) {
            <div class="completed-item">
              <wms-status-badge variant="completed" label="Done" [showIcon]="true"></wms-status-badge>
              <span class="item-code">{{ item.code }}</span>
              <span class="item-type-badge" *ngIf="item.isLpn">LPN</span>
              <span class="arrow">&rarr;</span>
              <span class="location">{{ item.location }}</span>
              <span class="item-qty-badge" *ngIf="item.totalQty">{{ item.totalQty }} pcs</span>
            </div>
          } @empty {
            <div class="empty-text">No items put away yet</div>
          }
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
    .putaway-layout { display: flex; flex-direction: column; gap: 16px; max-width: 700px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; display: flex; align-items: center; gap: 8px; }
    .sub-title { font-size: 13px; font-weight: 600; color: #374151; margin: 12px 0 8px; }
    .putaway-detail { display: flex; flex-direction: column; gap: 12px; }
    .detail-row { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #f3f4f6; }
    .detail-label { font-size: 13px; color: #6b7280; }
    .detail-value { font-size: 14px; font-weight: 600; color: #111827; }
    .detail-value.highlight { color: #1A005D; font-family: monospace; font-size: 15px; }
    .detail-value.lpn-highlight { color: #1A005D; font-family: monospace; font-size: 16px; letter-spacing: 0.5px; }
    .form-field { display: flex; flex-direction: column; gap: 5px; margin-top: 4px; }
    .form-field label { font-size: 13px; font-weight: 600; color: #374151; }
    .confirm-btn { width: 100%; margin-top: 8px; }
    .lpn-contents { margin: 8px 0; }
    .content-row { display: flex; gap: 16px; padding: 6px 10px; background: #f0f4ff; border-radius: 6px; margin-bottom: 4px; font-size: 13px; }
    .content-product { font-weight: 600; font-family: monospace; color: #111827; }
    .content-qty { color: #374151; font-weight: 500; }
    .content-lot { color: #6b7280; font-size: 12px; }
    .completed-list { display: flex; flex-direction: column; gap: 6px; }
    .completed-item { display: flex; align-items: center; gap: 10px; padding: 8px 12px; background: #f9fafb; border-radius: 8px; }
    .item-code { font-family: monospace; font-size: 13px; font-weight: 600; }
    .item-type-badge { font-size: 10px; font-weight: 700; color: #1A005D; background: #ede9fe; padding: 2px 6px; border-radius: 4px; }
    .item-qty-badge { font-size: 11px; color: #6b7280; margin-left: auto; }
    .arrow { color: #9ca3af; }
    .location { font-family: monospace; font-size: 13px; color: #1A005D; font-weight: 600; }
    .empty-text { font-size: 13px; color: #9ca3af; text-align: center; padding: 20px; }
    .message { padding: 12px 16px; border-radius: 8px; margin-top: 16px; font-size: 13px; display: flex; align-items: center; gap: 8px; }
    .message.success { background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; }
    .message.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; }
  `]
})
export class InboundPutawayComponent {
  putawayMode: 'item' | 'lpn' = 'item';

  // Item mode
  currentItem: any = null;
  targetLocation = '';

  // LPN mode
  currentLpn: LpnDto | null = null;
  lpnTargetLocation = '';
  suggestedLocation = '';
  submitting = false;
  successMsg = '';
  errorMsg = '';

  // Shared
  completedItems: any[] = [];

  constructor(private lpnService: LpnService) {}

  // --- Item mode ---
  onScanItem(barcode: string): void {
    this.currentItem = {
      code: barcode,
      name: 'Product Item',
      qty: Math.floor(Math.random() * 50) + 1,
      suggestedLocation: `A-${Math.floor(Math.random() * 10) + 1}-${Math.floor(Math.random() * 5) + 1}-${Math.floor(Math.random() * 3) + 1}`
    };
    this.targetLocation = '';
  }

  onScanLocation(location: string): void {
    this.targetLocation = location;
  }

  confirmPutaway(): void {
    if (this.currentItem && this.targetLocation) {
      this.completedItems.unshift({ code: this.currentItem.code, location: this.targetLocation, isLpn: false });
      this.currentItem = null;
      this.targetLocation = '';
    }
  }

  // --- LPN mode ---
  onScanLpn(barcode: string): void {
    this.successMsg = '';
    this.errorMsg = '';
    this.lpnTargetLocation = '';
    this.suggestedLocation = `A-${Math.floor(Math.random() * 10) + 1}-${Math.floor(Math.random() * 5) + 1}-${Math.floor(Math.random() * 3) + 1}`;

    this.lpnService.getDetail(barcode).subscribe({
      next: (res) => {
        if (res.status === 'SUCCESS' && res.data) {
          this.currentLpn = res.data;
        } else {
          this.currentLpn = null;
          this.errorMsg = `LPN '${barcode}' not found`;
        }
      },
      error: () => {
        this.currentLpn = null;
        this.errorMsg = `LPN '${barcode}' not found`;
      }
    });
  }

  onScanLpnLocation(location: string): void {
    this.lpnTargetLocation = location;
  }

  confirmLpnPutaway(): void {
    if (!this.currentLpn || !this.lpnTargetLocation) return;
    this.submitting = true;
    this.successMsg = '';
    this.errorMsg = '';

    const request: LpnPutawayRequest = {
      lpnNumber: this.currentLpn.lpnNumber,
      targetLocationCode: this.lpnTargetLocation
    };

    this.lpnService.putaway(request).subscribe({
      next: (res) => {
        this.submitting = false;
        if (res.status === 'SUCCESS') {
          this.completedItems.unshift({
            code: this.currentLpn!.lpnNumber,
            location: this.lpnTargetLocation,
            isLpn: true,
            totalQty: this.currentLpn!.totalQuantity
          });
          this.successMsg = `LPN ${this.currentLpn!.lpnNumber} put away to ${this.lpnTargetLocation}`;
          this.currentLpn = null;
          this.lpnTargetLocation = '';
        } else {
          this.errorMsg = res.messages?.[0]?.message || 'Failed to putaway LPN';
        }
      },
      error: (err) => {
        this.submitting = false;
        this.errorMsg = err.error?.messages?.[0]?.message || 'Failed to putaway LPN';
      }
    });
  }

  lpnStatusVariant(status: string): BadgeVariant {
    switch (status) {
      case '100': return 'pending';
      case '200': return 'in-progress';
      case '300': case '400': return 'completed';
      case '500': case '600': return 'in-progress';
      case '900': return 'completed';
      case '999': return 'cancelled';
      default: return 'pending';
    }
  }
}
