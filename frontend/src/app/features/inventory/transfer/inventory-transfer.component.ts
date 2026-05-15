import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';

@Component({
  selector: 'wms-inventory-transfer',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, DropdownModule, InputNumberModule, PageHeaderComponent, BarcodeInputComponent],
  template: `
    <wms-page-header title="inventory.transfer_title" subtitle="inventory.transfer_subtitle" icon="pi pi-arrows-h">
    </wms-page-header>

    <div class="transfer-layout">
      <!-- Scan Product -->
      <div class="wms-card">
        <h3 class="section-title">Scan Product to Transfer</h3>
        <wms-barcode-input placeholder="Scan product barcode..." (scan)="onScanProduct($event)"></wms-barcode-input>
      </div>

      @if (product) {
        <!-- Transfer Visual -->
        <div class="transfer-visual">
          <!-- From -->
          <div class="wms-card transfer-card from-card">
            <div class="card-badge from-badge">FROM</div>
            <div class="location-icon"><i class="pi pi-map-marker" style="font-size:24px;color:#FF9E1B"></i></div>
            <span class="transfer-location">{{ product.fromLocation }}</span>
            <span class="transfer-zone">Zone {{ product.fromZone }}</span>
            <div class="transfer-stock">
              <span class="transfer-stock-label">Current Stock</span>
              <span class="transfer-stock-value">{{ product.currentQty }}</span>
            </div>
          </div>

          <!-- Arrow -->
          <div class="transfer-arrow">
            <div class="arrow-line"></div>
            <div class="arrow-qty-badge">
              <span>{{ transferQty }}</span>
              <span class="arrow-uom">{{ product.uom }}</span>
            </div>
            <i class="pi pi-arrow-right" style="font-size:24px;color:#1A005D"></i>
          </div>

          <!-- To -->
          <div class="wms-card transfer-card to-card">
            <div class="card-badge to-badge">TO</div>
            <div class="location-icon"><i class="pi pi-map-marker" style="font-size:24px;color:#8EC400"></i></div>
            @if (toLocation) {
              <span class="transfer-location">{{ toLocation }}</span>
              <span class="transfer-zone">Scanned</span>
            } @else {
              <wms-barcode-input placeholder="Scan target location..." (scan)="onScanTarget($event)" [autoFocus]="false"></wms-barcode-input>
            }
          </div>
        </div>

        <!-- Product & Qty -->
        <div class="wms-card">
          <h3 class="section-title">Transfer Details</h3>
          <div class="detail-grid">
            <div class="detail-item"><label class="detail-label">Product</label><span class="product-code">{{ product.code }}</span></div>
            <div class="detail-item"><label class="detail-label">Name</label><span>{{ product.name }}</span></div>
            <div class="detail-item">
              <label class="detail-label">Transfer Qty</label>
              <p-inputNumber [(ngModel)]="transferQty" [showButtons]="true" [min]="1" [max]="product.currentQty" styleClass="w-full"></p-inputNumber>
            </div>
            <div class="detail-item">
              <label class="detail-label">Reason</label>
              <p-dropdown [options]="reasons" [(ngModel)]="transferReason" placeholder="Select reason" styleClass="w-full"></p-dropdown>
            </div>
          </div>
          <button pButton label="Execute Transfer" icon="pi pi-check" class="p-button-sm w-full mt-3"
                  [disabled]="!toLocation || transferQty < 1 || !transferReason" (click)="executeTransfer()"></button>
        </div>

        <!-- Recent Transfers -->
        <div class="wms-card">
          <h3 class="section-title">Completed Transfers</h3>
          @for (t of completedTransfers; track $index) {
            <div class="transfer-row">
              <i class="pi pi-check-circle" style="color:#8EC400"></i>
              <span class="product-code">{{ t.product }}</span>
              <span class="transfer-path">{{ t.from }} → {{ t.to }}</span>
              <span class="transfer-qty-badge">× {{ t.qty }}</span>
              <span class="transfer-time">{{ t.time }}</span>
            </div>
          }
          @if (completedTransfers.length === 0) {
            <div class="empty-text">No transfers completed in this session</div>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    .transfer-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .transfer-visual { display: grid; grid-template-columns: 1fr auto 1fr; gap: 20px; align-items: center; }
    .transfer-card { text-align: center; position: relative; display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 28px 20px; }
    .card-badge { position: absolute; top: 12px; left: 12px; font-size: 10px; font-weight: 800; padding: 2px 8px; border-radius: 6px; letter-spacing: 1px; }
    .from-badge { background: #fff7ed; color: #FF9E1B; }
    .to-badge { background: #ecfccb; color: #8EC400; }
    .location-icon { width: 52px; height: 52px; border-radius: 14px; background: #f3f4f6; display: flex; align-items: center; justify-content: center; }
    .transfer-location { font-family: monospace; font-size: 18px; font-weight: 800; color: #1A005D; }
    .transfer-zone { font-size: 12px; color: #6b7280; }
    .transfer-stock { margin-top: 8px; display: flex; flex-direction: column; }
    .transfer-stock-label { font-size: 11px; color: #6b7280; }
    .transfer-stock-value { font-size: 22px; font-weight: 800; color: #111827; }
    .transfer-arrow { display: flex; flex-direction: column; align-items: center; gap: 8px; }
    .arrow-line { width: 2px; height: 20px; background: #d1d5db; }
    .arrow-qty-badge { background: #1A005D; color: white; border-radius: 20px; padding: 6px 14px; font-weight: 700; font-size: 15px; display: flex; align-items: center; gap: 4px; }
    .arrow-uom { font-size: 10px; font-weight: 400; opacity: .7; }
    .detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .detail-item { display: flex; flex-direction: column; gap: 4px; }
    .detail-label { font-size: 12px; font-weight: 600; color: #374151; }
    .product-code { font-family: monospace; font-weight: 700; color: #1A005D; }
    .w-full { width: 100%; }
    .mt-3 { margin-top: 12px; }
    .transfer-row { display: flex; align-items: center; gap: 10px; padding: 10px 0; border-bottom: 1px solid #f3f4f6; }
    .transfer-row:last-child { border-bottom: none; }
    .transfer-path { font-family: monospace; font-size: 13px; color: #374151; flex: 1; }
    .transfer-qty-badge { font-size: 13px; font-weight: 600; color: #1A005D; }
    .transfer-time { font-size: 12px; color: #9ca3af; }
    .empty-text { font-size: 13px; color: #9ca3af; text-align: center; padding: 16px; }
    @media (max-width: 768px) {
      .transfer-visual { grid-template-columns: 1fr; }
      .transfer-arrow { transform: rotate(90deg); }
      .detail-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class InventoryTransferComponent {
  product: any = null;
  toLocation: string | null = null;
  transferQty = 1;
  transferReason: string | null = null;
  completedTransfers: any[] = [];

  reasons = [
    { label: 'Replenishment', value: 'Replenishment' },
    { label: 'Consolidation', value: 'Consolidation' },
    { label: 'Zone Optimization', value: 'Zone Optimization' },
    { label: 'Damage Relocation', value: 'Damage Relocation' },
    { label: 'Other', value: 'Other' }
  ];

  onScanProduct(barcode: string): void {
    this.product = { code: barcode || 'SKU-00123', name: 'Organic Cotton T-Shirt M', fromLocation: 'A-01-02-03', fromZone: 'A', currentQty: 500, uom: 'PCS' };
    this.toLocation = null;
    this.transferQty = 1;
    this.transferReason = null;
  }

  onScanTarget(barcode: string): void {
    this.toLocation = barcode || 'B-02-01-04';
  }

  executeTransfer(): void {
    this.completedTransfers.unshift({
      product: this.product.code,
      from: this.product.fromLocation,
      to: this.toLocation,
      qty: this.transferQty,
      time: new Date().toLocaleTimeString()
    });
    this.product = null;
    this.toLocation = null;
  }
}
