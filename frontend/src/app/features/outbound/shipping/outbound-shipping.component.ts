import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';

@Component({
  selector: 'wms-outbound-shipping',
  standalone: true,
  imports: [CommonModule, TranslateModule, ButtonModule, DropdownModule, FormsModule, PageHeaderComponent, StatusBadgeComponent, BarcodeInputComponent],
  template: `
    <wms-page-header title="outbound.shipping_title" subtitle="outbound.shipping_subtitle" icon="pi pi-truck">
      <button pButton label="Create Manifest" icon="pi pi-file" class="p-button-sm" (click)="createManifest()"></button>
    </wms-page-header>

    <div class="shipping-layout">
      <!-- Filters -->
      <div class="wms-card">
        <div class="filter-row">
          <div class="filter-item">
            <label class="filter-label">Carrier</label>
            <p-dropdown [options]="carriers" [(ngModel)]="selectedCarrier" placeholder="All carriers"
                        [showClear]="true" styleClass="w-full"></p-dropdown>
          </div>
          <div class="filter-item">
            <label class="filter-label">Status</label>
            <p-dropdown [options]="statuses" [(ngModel)]="selectedStatus" placeholder="All statuses"
                        [showClear]="true" styleClass="w-full"></p-dropdown>
          </div>
          <div class="filter-actions">
            <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
          </div>
        </div>
      </div>

      <!-- LPN Load Verification -->
      <div class="wms-card">
        <h3 class="section-title">LPN Load Verification</h3>
        <p class="section-desc">Scan outbound LPN barcodes to verify they match the manifest before loading.</p>
        <wms-barcode-input placeholder="Scan outbound LPN barcode..." (scan)="onScanLoadLpn($event)"></wms-barcode-input>

        @if (lpnVerifyError) {
          <div class="error-msg"><i class="pi pi-exclamation-triangle"></i> {{ lpnVerifyError }}</div>
        }

        @if (verifiedLpns.length > 0) {
          <div class="verified-list">
            @for (lpn of verifiedLpns; track lpn.lpnNumber) {
              <div class="verified-item" [class.match]="lpn.verified" [class.mismatch]="!lpn.verified">
                <i [class]="lpn.verified ? 'pi pi-check-circle' : 'pi pi-exclamation-circle'"
                   [style.color]="lpn.verified ? '#8EC400' : '#FF585D'"></i>
                <span class="verified-lpn">{{ lpn.lpnNumber }}</span>
                <span class="verified-shipment">{{ lpn.shipmentId }}</span>
                <span class="verified-status">{{ lpn.verified ? 'Verified' : 'Not on manifest' }}</span>
              </div>
            }
          </div>
          <div class="verify-summary">
            <span class="verify-count">{{ verifiedCount }} / {{ manifestLpns.length }} LPNs verified</span>
            @if (verifiedCount === manifestLpns.length) {
              <wms-status-badge variant="completed" label="All Verified"></wms-status-badge>
            }
          </div>
        }
      </div>

      <!-- Shipments Ready -->
      <div class="wms-card">
        <h3 class="section-title">Shipments Ready for Dispatch</h3>
        <div class="shipment-list">
          @for (s of shipments; track s.id) {
            <div class="shipment-card" [class.selected]="selectedShipments.has(s.id)" (click)="toggleSelect(s.id)">
              <div class="shipment-check">
                <i [class]="selectedShipments.has(s.id) ? 'pi pi-check-square' : 'pi pi-stop'"
                   [style.color]="selectedShipments.has(s.id) ? '#8EC400' : '#d1d5db'"></i>
              </div>
              <div class="shipment-info">
                <div class="shipment-header">
                  <span class="shipment-id">{{ s.id }}</span>
                  <wms-status-badge [variant]="$any(s.status)"></wms-status-badge>
                </div>
                <div class="shipment-meta">
                  <span><i class="pi pi-user"></i> {{ s.customer }}</span>
                  <span><i class="pi pi-box"></i> {{ s.boxes }} boxes</span>
                  <span><i class="pi pi-truck"></i> {{ s.carrier }}</span>
                  @if (s.lpnCount) {
                    <span><i class="pi pi-qrcode"></i> {{ s.lpnCount }} LPNs</span>
                  }
                </div>
                @if (s.trackingNo) {
                  <div class="tracking-no">
                    <i class="pi pi-qrcode"></i>
                    <span class="tracking-code">{{ s.trackingNo }}</span>
                  </div>
                }
              </div>
              <div class="shipment-weight">
                <span class="weight-value">{{ s.weight }}</span>
                <span class="weight-unit">kg</span>
              </div>
            </div>
          }
        </div>
      </div>

      <!-- Actions -->
      @if (selectedShipments.size > 0) {
        <div class="action-bar">
          <span class="selected-count">{{ selectedShipments.size }} shipment(s) selected</span>
          <div class="action-buttons">
            <button pButton label="Assign Carrier" icon="pi pi-truck" class="p-button-sm p-button-outlined"
                    (click)="assignCarrier()"></button>
            <button pButton label="Print Labels" icon="pi pi-print" class="p-button-sm p-button-outlined"
                    (click)="printLabels()"></button>
            <button pButton label="Dispatch" icon="pi pi-send" class="p-button-sm"
                    (click)="dispatch()"></button>
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    .shipping-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .section-desc { font-size: 13px; color: #6b7280; margin: -8px 0 16px; }
    .filter-row { display: flex; gap: 16px; align-items: flex-end; }
    .filter-item { flex: 1; display: flex; flex-direction: column; gap: 4px; }
    .filter-label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; align-items: flex-end; }
    .error-msg { display: flex; align-items: center; gap: 8px; margin-top: 12px; padding: 10px 14px; background: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; color: #dc2626; font-size: 13px; }
    .verified-list { margin-top: 16px; display: flex; flex-direction: column; gap: 6px; }
    .verified-item { display: flex; align-items: center; gap: 10px; padding: 10px 14px; border-radius: 8px; }
    .verified-item.match { background: #f0fdf4; }
    .verified-item.mismatch { background: #fef2f2; }
    .verified-lpn { font-family: monospace; font-weight: 700; color: #1A005D; }
    .verified-shipment { font-size: 12px; color: #6b7280; }
    .verified-status { margin-left: auto; font-size: 12px; font-weight: 600; }
    .verify-summary { display: flex; align-items: center; justify-content: space-between; margin-top: 12px; padding-top: 12px; border-top: 1px solid #e5e7eb; }
    .verify-count { font-size: 14px; font-weight: 600; color: #374151; }
    .shipment-list { display: flex; flex-direction: column; gap: 10px; }
    .shipment-card { display: flex; align-items: center; gap: 14px; padding: 16px; border: 1px solid #e5e7eb; border-radius: 10px;
                     cursor: pointer; transition: all .15s; }
    .shipment-card:hover { border-color: #c7d2fe; background: #fafbff; }
    .shipment-card.selected { border-color: #8EC400; background: #f7fee7; }
    .shipment-check { font-size: 20px; }
    .shipment-info { flex: 1; display: flex; flex-direction: column; gap: 6px; }
    .shipment-header { display: flex; align-items: center; gap: 10px; }
    .shipment-id { font-weight: 700; font-size: 14px; color: #1A005D; }
    .shipment-meta { display: flex; gap: 16px; font-size: 12px; color: #6b7280; }
    .shipment-meta i { margin-right: 4px; }
    .tracking-no { display: flex; align-items: center; gap: 6px; font-size: 12px; }
    .tracking-code { font-family: monospace; font-weight: 600; color: #374151; }
    .shipment-weight { text-align: right; }
    .weight-value { font-size: 18px; font-weight: 700; color: #111827; }
    .weight-unit { font-size: 12px; color: #6b7280; margin-left: 2px; }
    .action-bar { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px;
                  background: white; border-radius: 14px; border: 1px solid #e5e7eb; position: sticky; bottom: 16px;
                  box-shadow: 0 -4px 16px rgba(0,0,0,.08); }
    .selected-count { font-size: 14px; font-weight: 600; color: #1A005D; }
    .action-buttons { display: flex; gap: 8px; }
    .w-full { width: 100%; }
    @media (max-width: 768px) {
      .filter-row { flex-direction: column; }
      .action-bar { flex-direction: column; gap: 12px; }
    }
  `]
})
export class OutboundShippingComponent {
  carriers = [
    { label: 'Yamato Transport', value: 'yamato' },
    { label: 'Sagawa Express', value: 'sagawa' },
    { label: 'Japan Post', value: 'jp-post' }
  ];
  statuses = [
    { label: 'Packed', value: 'packed' },
    { label: 'Ready', value: 'ready' },
    { label: 'Dispatched', value: 'dispatched' }
  ];
  selectedCarrier: string | null = null;
  selectedStatus: string | null = null;
  selectedShipments = new Set<string>();

  shipments = [
    { id: 'SP-2024-1592', customer: 'MUJI Shibuya', boxes: 3, carrier: 'Yamato', weight: 12.5, status: 'completed', trackingNo: '4912-3456-7890', lpnCount: 3 },
    { id: 'SP-2024-1593', customer: 'MUJI Ginza', boxes: 1, carrier: 'Sagawa', weight: 4.2, status: 'confirmed', trackingNo: null, lpnCount: 1 },
    { id: 'SP-2024-1594', customer: 'MUJI Ikebukuro', boxes: 5, carrier: 'Yamato', weight: 22.8, status: 'confirmed', trackingNo: '4912-3456-7891', lpnCount: 5 },
    { id: 'SP-2024-1595', customer: 'MUJI Shinjuku', boxes: 2, carrier: 'JP Post', weight: 8.1, status: 'pending', trackingNo: null, lpnCount: 0 },
  ];

  // LPN load verification
  manifestLpns = [
    { lpnNumber: 'SP-20250316-000050', shipmentId: 'SP-2024-1592' },
    { lpnNumber: 'SP-20250316-000051', shipmentId: 'SP-2024-1592' },
    { lpnNumber: 'SP-20250316-000052', shipmentId: 'SP-2024-1592' },
    { lpnNumber: 'SP-20250316-000053', shipmentId: 'SP-2024-1594' },
    { lpnNumber: 'SP-20250316-000054', shipmentId: 'SP-2024-1594' },
  ];
  verifiedLpns: any[] = [];
  lpnVerifyError = '';

  get verifiedCount(): number {
    return this.verifiedLpns.filter(l => l.verified).length;
  }

  onScanLoadLpn(barcode: string): void {
    this.lpnVerifyError = '';

    if (this.verifiedLpns.some(l => l.lpnNumber === barcode)) {
      this.lpnVerifyError = `LPN ${barcode} already scanned`;
      return;
    }

    const match = this.manifestLpns.find(m => m.lpnNumber === barcode);
    this.verifiedLpns.push({
      lpnNumber: barcode,
      shipmentId: match?.shipmentId || 'Unknown',
      verified: !!match
    });

    if (!match) {
      this.lpnVerifyError = `LPN ${barcode} is not on the manifest`;
    }
  }

  toggleSelect(id: string): void {
    if (this.selectedShipments.has(id)) this.selectedShipments.delete(id);
    else this.selectedShipments.add(id);
  }

  search(): void { console.log('Search:', this.selectedCarrier, this.selectedStatus); }
  createManifest(): void { console.log('Create manifest'); }
  assignCarrier(): void { console.log('Assign carrier:', this.selectedShipments); }
  printLabels(): void { console.log('Print labels:', this.selectedShipments); }
  dispatch(): void { console.log('Dispatch:', this.selectedShipments); }
}
