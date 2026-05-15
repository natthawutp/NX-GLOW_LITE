import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { StatusBadgeComponent, BadgeVariant } from '@shared/components/status-badge/status-badge.component';
import { ApiService } from '@core/services/api.service';

@Component({
  selector: 'wms-inbound-order-detail',
  standalone: true,
  imports: [CommonModule, TranslateModule, ButtonModule, TableModule, TooltipModule, PageHeaderComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="Arrival Order Detail" subtitle="View arrival order header and detail lines" icon="pi pi-eye">
      <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined"
              (click)="goBack()"></button>
      @if (order()?.editable) {
        <button pButton label="Edit" icon="pi pi-pencil" class="p-button-sm edit-btn"
                (click)="goEdit()"></button>
      }
    </wms-page-header>

    @if (loading()) {
      <div class="wms-card"><p style="color: #6b7280; padding: 20px; text-align: center;">Loading...</p></div>
    }

    @if (order(); as o) {
      <div class="detail-layout">
        <!-- Header Card -->
        <div class="wms-card">
          <h3 class="section-title">Header Information</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">Arrival Number</span>
              <span class="info-value av-number">{{ o.AVH_AV_NUM }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Status</span>
              <wms-status-badge [variant]="getStatusVariant(o.AVH_AV_STS)" [label]="getStatusLabel(o.AVH_AV_STS, o.AVH_AV_STS_LBL)"></wms-status-badge>
            </div>
            <div class="info-item">
              <span class="info-label">Transaction Kind</span>
              <span class="info-value">{{ o.AVH_TRN_KND_LBL || o.AVH_TRN_KND || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Supplier Code</span>
              <span class="info-value">{{ o.AVH_SPL_COD || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Supplier Name</span>
              <span class="info-value">{{ o.AVH_SPL_NAM1 || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">PO Number</span>
              <span class="info-value">{{ o.AVH_PO_NUM || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Reference Number</span>
              <span class="info-value">{{ o.AVH_RF_NUM || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Scheduled Date</span>
              <span class="info-value">{{ o.AVH_SCDL_YMD || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Actual Date</span>
              <span class="info-value">{{ o.AVH_ARV_YMD || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Weight</span>
              <span class="info-value">{{ o.AVH_WGT ?? 0 }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Volume (M3)</span>
              <span class="info-value">{{ o.AVH_M3 ?? 0 }}</span>
            </div>
            <div class="info-item" *ngIf="o.AVH_RMKS">
              <span class="info-label">Remarks</span>
              <span class="info-value">{{ o.AVH_RMKS }}</span>
            </div>
          </div>
        </div>

        <!-- Detail Lines Card -->
        <div class="wms-card">
          <h3 class="section-title">Detail Lines ({{ o.lines?.length || 0 }})</h3>
          <div class="table-scroll">
            <p-table [value]="o.lines || []" styleClass="p-datatable-sm" [rowHover]="true">
              <ng-template pTemplate="header">
                <tr>
                  <th style="width: 50px">#</th>
                  <th>Status</th>
                  <th>Product Code</th>
                  <th>Product Name</th>
                  <th style="text-align: right">Case Qty</th>
                  <th style="text-align: right">Piece Qty</th>
                  <th style="text-align: right">Total Pcs</th>
                  <th>Sub Inventory</th>
                  <th>Origin</th>
                  <th>Shipper</th>
                  <th style="text-align: right">Weight</th>
                  <th style="text-align: right">M3</th>
                  <th>PIK1</th>
                  <th>PIK2</th>
                  <th>PIK3</th>
                  <th>Remarks</th>
                </tr>
              </ng-template>
              <ng-template pTemplate="body" let-line>
                <tr>
                  <td>{{ line.AVD_AVLN_NUM }}</td>
                  <td>
                    <wms-status-badge [variant]="getStatusVariant(line.AVD_AV_STS)"
                          [label]="getStatusLabel(line.AVD_AV_STS, line.AVD_AV_STS_LBL)"></wms-status-badge>
                  </td>
                  <td><span class="product-code">{{ line.AVD_PROD_COD }}</span></td>
                  <td>{{ line.AVD_PROD_NAM || '-' }}</td>
                  <td style="text-align: right">{{ line.AVD_SCS_QTY ?? 0 }}</td>
                  <td style="text-align: right">{{ line.AVD_PPCS_QTY ?? 0 }}</td>
                  <td style="text-align: right; font-weight: 600">{{ line.AVD_STPC_QTY ?? 0 }}</td>
                  <td>{{ line.AVD_SBIV_COD || '-' }}</td>
                  <td>{{ line.AVD_ORGN_COD || '-' }}</td>
                  <td>{{ line.AVD_SPPR_NAM || line.AVD_SPPR_COD || '-' }}</td>
                  <td style="text-align: right">{{ line.AVD_WGT ?? 0 }}</td>
                  <td style="text-align: right">{{ line.AVD_M3 ?? 0 }}</td>
                  <td>{{ line.AVD_PIK1 || '-' }}</td>
                  <td>{{ line.AVD_PIK2 || '-' }}</td>
                  <td>{{ line.AVD_PIK3 || '-' }}</td>
                  <td>{{ line.AVD_RMKS || '-' }}</td>
                </tr>
              </ng-template>
              <ng-template pTemplate="emptymessage">
                <tr><td colspan="16" class="empty-text">No detail lines</td></tr>
              </ng-template>
            </p-table>
          </div>
        </div>

        <!-- Arrival Results (Inspection/Receiving) Card -->
        @if (o.arrivalResults?.length > 0) {
          <div class="wms-card">
            <h3 class="section-title">Arrival Results — Inspection / Receiving ({{ o.arrivalResults.length }})</h3>
            <div class="table-scroll">
              <p-table [value]="o.arrivalResults" styleClass="p-datatable-sm" [rowHover]="true">
                <ng-template pTemplate="header">
                  <tr>
                    <th style="width: 50px">Line</th>
                    <th style="width: 50px">Seq</th>
                    <th>Status</th>
                    <th>Product Code</th>
                    <th style="text-align: right">Case Qty</th>
                    <th style="text-align: right">Piece Qty</th>
                    <th style="text-align: right; font-weight: 700">Total Pcs</th>
                    <th>Location</th>
                    <th style="width: 60px">Damage</th>
                    <th>Remarks</th>
                  </tr>
                </ng-template>
                <ng-template pTemplate="body" let-r>
                  <tr>
                    <td>{{ r.AVR_AVLN_NUM }}</td>
                    <td>{{ r.AVR_AVSQ_NUM }}</td>
                    <td>
                      <wms-status-badge [variant]="getStatusVariant(r.AVR_AVSP_STS)"
                            [label]="getStatusLabel(r.AVR_AVSP_STS, r.AVR_AVSP_STS_LBL)"></wms-status-badge>
                    </td>
                    <td><span class="product-code">{{ r.AVR_PROD_COD }}</span></td>
                    <td style="text-align: right">{{ r.AVR_RCS_QTY ?? 0 }}</td>
                    <td style="text-align: right">{{ r.AVR_RPC_QTY ?? 0 }}</td>
                    <td style="text-align: right; font-weight: 600">{{ r.AVR_RTPC_QTY ?? 0 }}</td>
                    <td><span class="location-code">{{ r.AVR_LOCATION || '-' }}</span></td>
                    <td style="text-align: center">
                      @if (r.AVR_DMG_FLG === '1') {
                        <span class="dmg-flag">DMG</span>
                      } @else {
                        <span style="color: #9ca3af">-</span>
                      }
                    </td>
                    <td>{{ r.AVR_RMKS || '-' }}</td>
                  </tr>
                </ng-template>
                <ng-template pTemplate="emptymessage">
                  <tr><td colspan="10" class="empty-text">No arrival results</td></tr>
                </ng-template>
              </p-table>
            </div>
          </div>
        }

        <!-- LPN Data Card -->
        @if (o.lpns?.length > 0) {
          <div class="wms-card">
            <h3 class="section-title">LPN — License Plate Numbers ({{ o.lpns.length }})</h3>
            <div class="table-scroll">
              <p-table [value]="o.lpns" styleClass="p-datatable-sm" [rowHover]="true">
                <ng-template pTemplate="header">
                  <tr>
                    <th>LPN Number</th>
                    <th style="width: 80px">Type</th>
                    <th style="width: 80px">Status</th>
                    <th>Location</th>
                    <th style="text-align: right">Total Qty</th>
                    <th style="text-align: right">Weight</th>
                    <th style="text-align: right">Volume</th>
                    <th>Receive Date</th>
                    <th>Remarks</th>
                  </tr>
                </ng-template>
                <ng-template pTemplate="body" let-lpn>
                  <tr>
                    <td><span class="product-code">{{ lpn.LPN_NUM }}</span></td>
                    <td>{{ lpn.LPN_TYPE || '-' }}</td>
                    <td>{{ lpn.LPN_STS || '-' }}</td>
                    <td><span class="location-code">{{ lpn.LPN_LOC_COD || '-' }}</span></td>
                    <td style="text-align: right; font-weight: 600">{{ lpn.LPN_TTL_QTY ?? 0 }}</td>
                    <td style="text-align: right">{{ lpn.LPN_TTL_WGT ?? 0 }}</td>
                    <td style="text-align: right">{{ lpn.LPN_TTL_VOL ?? 0 }}</td>
                    <td>{{ lpn.LPN_RCV_YMD || '-' }}</td>
                    <td>{{ lpn.LPN_RMK || '-' }}</td>
                  </tr>
                </ng-template>
                <ng-template pTemplate="emptymessage">
                  <tr><td colspan="9" class="empty-text">No LPN data</td></tr>
                </ng-template>
              </p-table>
            </div>
          </div>
        }
      </div>
    }

    @if (!order() && !loading()) {
      <div class="wms-card"><div class="empty-text">Arrival order not found</div></div>
    }
  `,
  styles: [`
    .detail-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .info-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
    .info-item { display: flex; flex-direction: column; gap: 4px; }
    .info-label { font-size: 12px; color: #6b7280; font-weight: 500; }
    .info-value { font-size: 14px; color: #111827; font-weight: 600; }
    .info-value.av-number { color: #1A005D; font-family: monospace; font-size: 16px; }
    .product-code { font-family: monospace; font-weight: 600; }
    .location-code { font-family: monospace; font-size: 13px; color: #1A005D; }
    .empty-text { text-align: center; color: #9ca3af; padding: 20px; }

    .dmg-flag {
      background: #fee2e2;
      color: #dc2626;
      font-size: 10px;
      font-weight: 700;
      padding: 2px 6px;
      border-radius: 4px;
    }

    .edit-btn {
      background: rgb(142, 196, 0) !important;
      border-color: rgb(142, 196, 0) !important;
      color: #1f2937 !important;
      font-weight: 600;
    }
    .edit-btn:hover {
      background: rgb(124, 176, 0) !important;
      border-color: rgb(124, 176, 0) !important;
    }

    .table-scroll {
      overflow-x: auto;
      border: 1px solid #e5e7eb;
      border-radius: 6px;
    }
    .table-scroll::-webkit-scrollbar { height: 10px; }
    .table-scroll::-webkit-scrollbar-track { background: #f3f4f6; border-radius: 0 0 6px 6px; }
    .table-scroll::-webkit-scrollbar-thumb { background: rgba(0,0,0,0.25); border-radius: 10px; border: 2px solid #f3f4f6; }

    @media (max-width: 768px) { .info-grid { grid-template-columns: 1fr 1fr; } }
  `]
})
export class InboundOrderDetailComponent implements OnInit {
  order = signal<any>(null);
  loading = signal(true);

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const arrivalNo = this.route.snapshot.paramMap.get('arrivalNo');
    if (arrivalNo) {
      this.api.get<any>(`/inbound/orders/${arrivalNo}`).subscribe({
        next: (res) => {
          this.loading.set(false);
          this.order.set(res.status === 'SUCCESS' ? res.data : null);
        },
        error: () => { this.loading.set(false); }
      });
    } else {
      this.loading.set(false);
    }
  }

  goBack(): void {
    this.router.navigate(['/inbound/orders']);
  }

  goEdit(): void {
    const arrivalNo = this.route.snapshot.paramMap.get('arrivalNo');
    this.router.navigate(['/inbound/orders', arrivalNo, 'edit']);
  }

  getStatusVariant(status: string): BadgeVariant {
    const map: Record<string, BadgeVariant> = {
      '100': 'pending',
      '200': 'pending',
      '205': 'in-progress',
      '209': 'confirmed',
      '300': 'in-progress',
      '305': 'in-progress',
      '306': 'in-progress',
      '500': 'in-progress',
      '605': 'confirmed',
      '609': 'confirmed',
      '700': 'confirmed',
      '809': 'completed',
      '999': 'cancelled'
    };
    return map[(status || '').trim()] || 'pending';
  }

  getStatusLabel(status: string, backendLabel?: string): string {
    if (backendLabel) return backendLabel;
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
    return map[(status || '').trim()] || status || '-';
  }
}
