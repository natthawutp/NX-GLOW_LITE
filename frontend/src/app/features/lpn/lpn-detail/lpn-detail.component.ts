import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { StatusBadgeComponent, BadgeVariant } from '@shared/components/status-badge/status-badge.component';
import { LpnService, LpnDto } from '@core/services/lpn.service';

@Component({
  selector: 'wms-lpn-detail',
  standalone: true,
  imports: [CommonModule, TranslateModule, ButtonModule, TableModule, PageHeaderComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="lpn.detail_title" subtitle="lpn.detail_subtitle" icon="pi pi-box">
      <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined"
              (click)="goBack()"></button>
    </wms-page-header>

    <div class="detail-layout" *ngIf="lpn">
      <!-- Header Info -->
      <div class="wms-card">
        <h3 class="section-title">LPN Information</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">LPN Number</span>
            <span class="info-value lpn-number">{{ lpn.lpnNumber }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">LPN Refer Number</span>
            <span class="info-value lpn-number">{{ lpn.lpnRfNumber || '-' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Type</span>
            <span class="info-value">{{ lpn.lpnTypeLabel }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Status</span>
            <wms-status-badge [variant]="statusVariant(lpn.lpnStatus)" [label]="lpn.lpnStatusLabel"></wms-status-badge>
          </div>
          <div class="info-item">
            <span class="info-label">Location</span>
            <span class="info-value location">{{ lpn.locationCode || 'Not assigned' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Total Quantity</span>
            <span class="info-value">{{ lpn.totalQuantity }}</span>
          </div>
          <div class="info-item" *ngIf="lpn.arrivalNumber">
            <span class="info-label">Arrival Number</span>
            <span class="info-value">{{ lpn.arrivalNumber }}</span>
          </div>
          <div class="info-item" *ngIf="lpn.shipmentNumber">
            <span class="info-label">Shipment Number</span>
            <span class="info-value">{{ lpn.shipmentNumber }}</span>
          </div>
          <div class="info-item" *ngIf="lpn.parentLpnNumber">
            <span class="info-label">Parent LPN</span>
            <span class="info-value lpn-number">{{ lpn.parentLpnNumber }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Barcode Format</span>
            <span class="info-value">{{ lpn.barcodeFormat }}</span>
          </div>
          <div class="info-item" *ngIf="lpn.receiveDate">
            <span class="info-label">Received Date</span>
            <span class="info-value">{{ lpn.receiveDate }}</span>
          </div>
          <div class="info-item" *ngIf="lpn.remarks">
            <span class="info-label">Remarks</span>
            <span class="info-value">{{ lpn.remarks }}</span>
          </div>
        </div>
      </div>

      <!-- Contents -->
      <div class="wms-card">
        <h3 class="section-title">Contents</h3>
        <p-table [value]="lpn.contents || []" styleClass="p-datatable-sm" [rowHover]="true">
          <ng-template pTemplate="header">
            <tr>
              <th style="width: 50px">#</th>
              <th>Product Code</th>
              <th>Lot Number</th>
              <th style="text-align: right">Quantity</th>
              <th style="text-align: right">Allocated</th>
              <th style="text-align: right">Available</th>
              <th>Sub Inventory</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-content>
            <tr>
              <td>{{ content.lineNumber }}</td>
              <td><span class="product-code">{{ content.productCode }}</span></td>
              <td>{{ content.lotNumber || '-' }}</td>
              <td style="text-align: right; font-weight: 600">{{ content.quantity }}</td>
              <td style="text-align: right">{{ content.allocatedQuantity || 0 }}</td>
              <td style="text-align: right; color: #8EC400; font-weight: 600">{{ content.availableQuantity || content.quantity }}</td>
              <td>{{ content.subInventoryCode || '-' }}</td>
            </tr>
          </ng-template>
          <ng-template pTemplate="emptymessage">
            <tr><td colspan="7" class="empty-text">No contents</td></tr>
          </ng-template>
        </p-table>
      </div>
    </div>

    <div class="wms-card" *ngIf="!lpn && !loading">
      <div class="empty-text">LPN not found</div>
    </div>
  `,
  styles: [`
    .detail-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .info-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
    .info-item { display: flex; flex-direction: column; gap: 4px; }
    .info-label { font-size: 12px; color: #6b7280; font-weight: 500; }
    .info-value { font-size: 14px; color: #111827; font-weight: 600; }
    .info-value.lpn-number { color: #1A005D; font-family: monospace; font-size: 16px; }
    .info-value.location { color: #1A005D; font-family: monospace; }
    .product-code { font-family: monospace; font-weight: 600; }
    .empty-text { text-align: center; color: #9ca3af; padding: 20px; }
    @media (max-width: 768px) { .info-grid { grid-template-columns: 1fr 1fr; } }
  `]
})
export class LpnDetailComponent implements OnInit {
  lpn: LpnDto | null = null;
  loading = true;

  constructor(
    private lpnService: LpnService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const lpnNumber = this.route.snapshot.paramMap.get('lpnNumber');
    if (lpnNumber) {
      this.lpnService.getDetail(lpnNumber).subscribe({
        next: (res) => {
          this.loading = false;
          this.lpn = res.data || null;
        },
        error: () => { this.loading = false; }
      });
    } else {
      this.loading = false;
    }
  }

  goBack(): void {
    this.router.navigate(['/inventory/lpn']);
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
