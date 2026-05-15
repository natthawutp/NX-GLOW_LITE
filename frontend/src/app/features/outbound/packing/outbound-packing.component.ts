import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';

@Component({
  selector: 'wms-outbound-packing',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, PageHeaderComponent, BarcodeInputComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="outbound.packing_title" subtitle="outbound.packing_subtitle" icon="pi pi-box">
    </wms-page-header>

    <div class="packing-layout">
      <div class="wms-card">
        <h3 class="section-title">Scan Shipment to Pack</h3>
        <wms-barcode-input placeholder="Scan shipment number..." (scan)="onScanShipment($event)"></wms-barcode-input>
      </div>

      @if (shipment) {
        <div class="packing-grid">
          <div class="wms-card">
            <h3 class="section-title">Shipment Info</h3>
            <div class="info-grid">
              <div class="info-item"><span class="info-label">Shipment</span><span class="info-value">{{ shipment.id }}</span></div>
              <div class="info-item"><span class="info-label">Customer</span><span class="info-value">{{ shipment.customer }}</span></div>
              <div class="info-item"><span class="info-label">Items</span><span class="info-value">{{ shipment.totalItems }}</span></div>
              <div class="info-item"><span class="info-label">Packed</span><span class="info-value accent">{{ packedItems.length }}</span></div>
            </div>
          </div>

          <div class="wms-card">
            <h3 class="section-title">Scan Items into Box</h3>
            <wms-barcode-input placeholder="Scan item barcode..." (scan)="onScanItem($event)" [autoFocus]="false"></wms-barcode-input>
            <div class="packed-list">
              @for (item of packedItems; track item.barcode) {
                <div class="packed-item">
                  <i class="pi pi-check-circle" style="color: #8EC400"></i>
                  <span class="packed-code">{{ item.barcode }}</span>
                  <span class="packed-qty">× {{ item.qty }}</span>
                </div>
              }
            </div>
            <button pButton label="Complete Packing" icon="pi pi-check" class="p-button-sm w-full mt-3"
                    [disabled]="packedItems.length === 0" (click)="completePacking()"></button>
          </div>
        </div>

        <!-- Outbound LPN Generation -->
        <div class="wms-card">
          <h3 class="section-title">Outbound LPN (Shipping Carton)</h3>
          <p class="section-desc">Generate an outbound LPN for the packed carton to enable shipping verification by LPN scan.</p>

          @if (!outboundLpn) {
            <div class="lpn-gen-form">
              <div class="form-row">
                <div class="form-field">
                  <label class="field-label">Carton Weight (kg)</label>
                  <input pInputText [(ngModel)]="cartonWeight" placeholder="0.0" class="w-full" />
                </div>
                <div class="form-field">
                  <label class="field-label">Carton Dimensions (cm)</label>
                  <div class="dim-row">
                    <input pInputText [(ngModel)]="cartonLength" placeholder="L" class="dim-input" />
                    <span>×</span>
                    <input pInputText [(ngModel)]="cartonWidth" placeholder="W" class="dim-input" />
                    <span>×</span>
                    <input pInputText [(ngModel)]="cartonHeight" placeholder="H" class="dim-input" />
                  </div>
                </div>
              </div>
              <button pButton label="Generate Outbound LPN" icon="pi pi-qrcode" class="p-button-sm"
                      [disabled]="packedItems.length === 0" (click)="generateOutboundLpn()"></button>
            </div>
          } @else {
            <div class="lpn-generated">
              <div class="lpn-barcode-card">
                <i class="pi pi-qrcode lpn-icon"></i>
                <div class="lpn-details">
                  <span class="lpn-number">{{ outboundLpn.lpnNumber }}</span>
                  <span class="lpn-meta">{{ outboundLpn.itemCount }} items · {{ outboundLpn.weight }} kg</span>
                </div>
                <wms-status-badge variant="completed" label="Generated"></wms-status-badge>
              </div>
              <button pButton label="Print LPN Label" icon="pi pi-print" class="p-button-sm p-button-outlined"
                      (click)="printLpnLabel()"></button>
            </div>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    .packing-layout { display: flex; flex-direction: column; gap: 16px; }
    .packing-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .section-desc { font-size: 13px; color: #6b7280; margin: -8px 0 16px; }
    .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .info-item { display: flex; flex-direction: column; gap: 3px; }
    .info-label { font-size: 12px; color: #6b7280; }
    .info-value { font-size: 15px; font-weight: 600; color: #111827; }
    .info-value.accent { color: #8EC400; }
    .packed-list { margin-top: 16px; display: flex; flex-direction: column; gap: 6px; }
    .packed-item { display: flex; align-items: center; gap: 8px; padding: 8px 12px; background: #f0fdf4; border-radius: 8px; }
    .packed-code { font-family: monospace; font-size: 13px; font-weight: 600; flex: 1; }
    .packed-qty { font-size: 13px; color: #6b7280; }
    .w-full { width: 100%; }
    .mt-3 { margin-top: 12px; }
    .lpn-gen-form { display: flex; flex-direction: column; gap: 16px; }
    .form-row { display: flex; gap: 16px; }
    .form-field { flex: 1; display: flex; flex-direction: column; gap: 4px; }
    .field-label { font-size: 12px; font-weight: 600; color: #374151; }
    .dim-row { display: flex; align-items: center; gap: 6px; }
    .dim-input { width: 60px; text-align: center; }
    .lpn-generated { display: flex; flex-direction: column; gap: 12px; }
    .lpn-barcode-card { display: flex; align-items: center; gap: 14px; padding: 16px; background: #f0fdf4; border: 1px solid #bbf7d0; border-radius: 10px; }
    .lpn-icon { font-size: 28px; color: #1A005D; }
    .lpn-details { display: flex; flex-direction: column; flex: 1; }
    .lpn-number { font-family: monospace; font-size: 16px; font-weight: 800; color: #1A005D; }
    .lpn-meta { font-size: 12px; color: #6b7280; }
    @media (max-width: 768px) { .packing-grid { grid-template-columns: 1fr; } .form-row { flex-direction: column; } }
  `]
})
export class OutboundPackingComponent {
  shipment: any = null;
  packedItems: any[] = [];

  // Outbound LPN fields
  cartonWeight = '';
  cartonLength = '';
  cartonWidth = '';
  cartonHeight = '';
  outboundLpn: any = null;

  onScanShipment(barcode: string): void {
    this.shipment = { id: barcode || 'SP-2024-1592', customer: 'MUJI Shibuya', totalItems: 15 };
    this.packedItems = [];
    this.outboundLpn = null;
    this.cartonWeight = '';
    this.cartonLength = '';
    this.cartonWidth = '';
    this.cartonHeight = '';
  }

  onScanItem(barcode: string): void {
    this.packedItems.push({ barcode, qty: 1 });
  }

  generateOutboundLpn(): void {
    const seq = String(Math.floor(Math.random() * 999999)).padStart(6, '0');
    const today = new Date().toISOString().slice(0, 10).replace(/-/g, '');
    this.outboundLpn = {
      lpnNumber: `SP-${today}-${seq}`,
      itemCount: this.packedItems.length,
      weight: this.cartonWeight || '0',
      shipmentId: this.shipment?.id
    };
    console.log('Outbound LPN generated:', this.outboundLpn);
  }

  printLpnLabel(): void {
    console.log('Print LPN label:', this.outboundLpn?.lpnNumber);
  }

  completePacking(): void {
    console.log('Packing completed:', this.shipment, this.packedItems, this.outboundLpn);
    this.shipment = null;
    this.packedItems = [];
    this.outboundLpn = null;
  }
}
