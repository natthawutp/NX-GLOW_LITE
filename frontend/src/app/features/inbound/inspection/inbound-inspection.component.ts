import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableLazyLoadEvent } from 'primeng/table';
import { finalize } from 'rxjs/operators';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { DataTableComponent, WmsColumn } from '@shared/components/data-table/data-table.component';
import { BadgeVariant, StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';
import { ApiService } from '@core/services/api.service';

interface InspectionListRow {
  arrivalNumber: string;
  arrivalStatus: string;
  arrivalStatusLabel: string;
  scheduledDate: string;
  supplierName: string;
  poNumber: string;
  totalLines: number;
  inspectedLines: number;
  remarks: string;
  updTimestamp: string;
}

interface InspectionLine {
  lineNumber: number;
  lineStatus: string;
  productCode: string;
  productName: string;
  subInventoryCode: string;
  poNumber: string;
  lotNumber: string;
  planCsQty: number;
  planPcQty: number;
  planBlQty: number;
  planTotalQty: number;
  resultCsQty: number;
  resultPcQty: number;
  resultBlQty: number;
  resultTotalQty: number;
  piecesPerCase: number;
  piecesPerBulk: number;
  areaCode: string;
  rackCode: string;
  levelCode: string;
  positionCode: string;
  lpnNumber: string;
  lpnType: string;
  multiCargo: boolean;
  updTimestamp: string;
}

@Component({
  selector: 'wms-inbound-inspection',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    ButtonModule, CalendarModule, DropdownModule, InputTextModule, InputNumberModule,
    PageHeaderComponent, DataTableComponent, StatusBadgeComponent, BarcodeInputComponent
  ],
  template: `
    <wms-page-header title="inbound.inspection_title" subtitle="inbound.inspection_subtitle"
                     icon="pi pi-clipboard">
    </wms-page-header>

    <!-- ===== LIST VIEW ===== -->
    <ng-container *ngIf="view === 'list'">
      <!-- Search filters -->
      <div class="filter-panel">
        <div class="filter-row">
          <div class="filter-field">
            <label>Arrival No</label>
            <input pInputText [(ngModel)]="filters.arrivalNo" placeholder="AV-..." class="w-full">
          </div>
          <div class="filter-field">
            <label>Status</label>
            <p-dropdown [options]="statusOptions" [(ngModel)]="filters.status" placeholder="All"
                        [showClear]="true" [style]="{ width: '100%' }"
                        optionLabel="label" optionValue="value"></p-dropdown>
          </div>
          <div class="filter-field">
            <label>Date From</label>
            <p-calendar [(ngModel)]="filters.dateFrom" dateFormat="yy-mm-dd" [showIcon]="true"
                        [style]="{ width: '100%' }"></p-calendar>
          </div>
          <div class="filter-field">
            <label>Date To</label>
            <p-calendar [(ngModel)]="filters.dateTo" dateFormat="yy-mm-dd" [showIcon]="true"
                        [style]="{ width: '100%' }"></p-calendar>
          </div>
          <div class="filter-actions">
            <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
            <button pButton label="Clear" icon="pi pi-times" class="p-button-sm p-button-outlined" (click)="clearFilters()"></button>
          </div>
        </div>
      </div>

      <!-- Results table -->
      <wms-data-table
        [columns]="listColumns"
        [data]="listRows"
        [loading]="loading()"
        [totalRecords]="totalRecords"
        [pageSize]="20"
        (lazyLoad)="onLazyLoad($event)">

        <ng-template #bodyCell let-row let-col="col">
          <ng-container [ngSwitch]="col.field">
            <ng-container *ngSwitchCase="'arrivalStatusLabel'">
              <wms-status-badge [label]="row.arrivalStatusLabel"
                [variant]="statusVariant(row.arrivalStatus)"></wms-status-badge>
            </ng-container>
            <ng-container *ngSwitchCase="'progress'">
              <span class="progress-badge" [class.done]="row.inspectedLines >= row.totalLines">
                {{ row.inspectedLines }} / {{ row.totalLines }}
              </span>
            </ng-container>
            <ng-container *ngSwitchCase="'actions'">
              <button pButton label="Inspect" icon="pi pi-pencil" class="p-button-sm p-button-outlined"
                      (click)="openDetail(row)"></button>
            </ng-container>
            <ng-container *ngSwitchDefault>{{ row[col.field] }}</ng-container>
          </ng-container>
        </ng-template>
      </wms-data-table>
    </ng-container>

    <!-- ===== DETAIL / INSPECTION EDIT VIEW ===== -->
    <ng-container *ngIf="view === 'detail' && selectedArrival">
      <!-- Header info + back button -->
      <div class="detail-header">
        <button pButton icon="pi pi-arrow-left" label="Back to list" class="p-button-sm p-button-text"
                (click)="backToList()"></button>
        <div class="arrival-info">
          <span class="av-num">{{ selectedArrival.arrivalNumber }}</span>
          <wms-status-badge [label]="selectedArrival.arrivalStatusLabel"
            [variant]="statusVariant(selectedArrival.arrivalStatus)"></wms-status-badge>
          <span class="supplier">{{ selectedArrival.supplierName }}</span>
        </div>
      </div>

      <!-- Collective LPN panel -->
      <div class="wms-card lpn-panel">
        <div class="lpn-panel-header">
          <i class="pi pi-box"></i>
          <span>Collective LPN (optional — applies to all lines)</span>
          <button pButton icon="pi pi-qrcode" label="Scan LPN" class="p-button-sm p-button-outlined"
                  (click)="showLpnScan = !showLpnScan"></button>
        </div>
        <div class="lpn-inputs" *ngIf="showLpnScan">
          <wms-barcode-input placeholder="Scan or enter LPN barcode..."
                             (scan)="onScanCollectiveLpn($event)"></wms-barcode-input>
          <span class="lpn-value" *ngIf="collectiveLpnNumber">LPN: {{ collectiveLpnNumber }}</span>
        </div>
        <div class="lpn-type-row" *ngIf="showLpnScan">
          <label>LPN Type</label>
          <p-dropdown [options]="lpnTypeOptions" [(ngModel)]="collectiveLpnType"
                      optionLabel="label" optionValue="value" [style]="{ width: '160px' }"></p-dropdown>
          <button pButton icon="pi pi-times" class="p-button-sm p-button-text"
                  *ngIf="collectiveLpnNumber" (click)="clearCollectiveLpn()" label="Clear LPN"></button>
        </div>
      </div>

      <!-- Detail lines table -->
      <div class="wms-card">
        <h3 class="section-title">Inspection Lines ({{ detailLines.length }})</h3>

        <div class="lines-table">
          <!-- Table header -->
          <div class="line-row header-row">
            <div class="col-line">#</div>
            <div class="col-product">Product</div>
            <div class="col-plan">Planned Qty</div>
            <div class="col-result">Actual Qty <span class="required">*</span></div>
            <div class="col-location">Location</div>
            <div class="col-lpn">LPN</div>
          </div>

          <!-- Data rows -->
          @for (line of detailLines; track line.lineNumber) {
            <div class="line-row" [class.inspected]="line.resultTotalQty > 0"
                 [class.multi-cargo]="line.multiCargo">
              <!-- Line # -->
              <div class="col-line">
                <span class="line-num">{{ line.lineNumber }}</span>
                <span class="mc-badge" *ngIf="line.multiCargo" title="Multi-cargo">MC</span>
              </div>

              <!-- Product info -->
              <div class="col-product">
                <span class="prd-code">{{ line.productCode }}</span>
                <span class="prd-name">{{ line.productName }}</span>
                <span class="lot" *ngIf="line.lotNumber">Lot: {{ line.lotNumber }}</span>
              </div>

              <!-- Planned -->
              <div class="col-plan">
                <div class="qty-group">
                  <span class="qty-label">CS</span><span class="qty-val">{{ line.planCsQty || 0 }}</span>
                  <span class="qty-label">PC</span><span class="qty-val">{{ line.planPcQty || 0 }}</span>
                  <span class="qty-label">BL</span><span class="qty-val">{{ line.planBlQty || 0 }}</span>
                </div>
                <span class="total-qty">= {{ line.planTotalQty || 0 }}</span>
              </div>

              <!-- Actual (editable) -->
              <div class="col-result">
                <div class="result-inputs">
                  <span class="qty-label">CS</span>
                  <p-inputNumber [(ngModel)]="line.resultCsQty" [min]="0" [showButtons]="false"
                                 inputStyleClass="qty-input-sm" (ngModelChange)="calcTotal(line)"></p-inputNumber>
                  <span class="qty-label">PC</span>
                  <p-inputNumber [(ngModel)]="line.resultPcQty" [min]="0" [showButtons]="false"
                                 inputStyleClass="qty-input-sm" (ngModelChange)="calcTotal(line)"></p-inputNumber>
                  <span class="qty-label">BL</span>
                  <p-inputNumber [(ngModel)]="line.resultBlQty" [min]="0" [showButtons]="false"
                                 inputStyleClass="qty-input-sm" (ngModelChange)="calcTotal(line)"></p-inputNumber>
                </div>
                <span class="total-qty accent">= {{ line.resultTotalQty || 0 }}</span>
                <span class="variance" *ngIf="variance(line) !== 0"
                      [class.surplus]="variance(line) > 0" [class.shortage]="variance(line) < 0">
                  {{ variance(line) > 0 ? '+' : '' }}{{ variance(line) }}
                </span>
              </div>

              <!-- Location -->
              <div class="col-location">
                <div class="loc-inputs">
                  <input pInputText [(ngModel)]="line.areaCode"    placeholder="Area"  class="loc-input">
                  <input pInputText [(ngModel)]="line.rackCode"    placeholder="Rack"  class="loc-input">
                  <input pInputText [(ngModel)]="line.levelCode"   placeholder="Lvl"   class="loc-input-sm">
                  <input pInputText [(ngModel)]="line.positionCode" placeholder="Pos"  class="loc-input-sm">
                </div>
              </div>

              <!-- Per-line LPN (overrides collective) -->
              <div class="col-lpn">
                <input pInputText [(ngModel)]="line.lpnNumber" placeholder="LPN (auto)"
                       class="lpn-input" [class.has-lpn]="line.lpnNumber">
                <p-dropdown *ngIf="line.lpnNumber" [options]="lpnTypeOptions" [(ngModel)]="line.lpnType"
                            optionLabel="label" optionValue="value"
                            [style]="{ width: '80px', 'font-size': '11px' }"></p-dropdown>
              </div>
            </div>
          }
        </div>

        <!-- Action bar -->
        <div class="action-bar">
          <div class="action-info">
            <span>{{ inspectedCount }} / {{ detailLines.length }} lines with results</span>
            <span *ngIf="collectiveLpnNumber" class="lpn-chip">
              <i class="pi pi-box"></i> {{ collectiveLpnNumber }}
            </span>
          </div>
          <div class="action-buttons">
            <button pButton label="Cancel Inspection" icon="pi pi-ban"
                    class="p-button-sm p-button-outlined p-button-danger"
                    [disabled]="selectedArrival.arrivalStatus !== '205' || submitting()"
                    (click)="cancelInspection()"></button>
            <button pButton label="Register Inspection" icon="pi pi-check"
                    class="p-button-sm"
                    [disabled]="inspectedCount === 0 || submitting()"
                    [loading]="submitting()"
                    (click)="registerInspection()"></button>
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
    </ng-container>
  `,
  styles: [`
    /* Filter panel */
    .filter-panel { background: white; border-radius: 12px; border: 1px solid #e5e7eb; padding: 16px; margin-bottom: 16px; }
    .filter-row { display: flex; gap: 12px; flex-wrap: wrap; align-items: flex-end; }
    .filter-field { display: flex; flex-direction: column; gap: 4px; min-width: 160px; flex: 1; }
    .filter-field label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; gap: 8px; }

    /* Detail header */
    .detail-header { display: flex; align-items: center; gap: 16px; margin-bottom: 16px; }
    .arrival-info { display: flex; align-items: center; gap: 10px; }
    .av-num { font-family: monospace; font-size: 15px; font-weight: 700; color: #1A005D; }
    .supplier { font-size: 13px; color: #6b7280; }

    /* LPN collective panel */
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; margin-bottom: 16px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .lpn-panel { border-color: #d1fae5; background: #f0fdf4; }
    .lpn-panel-header { display: flex; align-items: center; gap: 10px; font-size: 13px; font-weight: 600; color: #065f46; }
    .lpn-inputs { margin-top: 12px; }
    .lpn-type-row { display: flex; align-items: center; gap: 10px; margin-top: 8px; font-size: 13px; font-weight: 600; }
    .lpn-value { font-family: monospace; font-size: 12px; color: #1A005D; font-weight: 700; margin-top: 4px; display: block; }

    /* Lines table */
    .lines-table { display: flex; flex-direction: column; gap: 2px; }
    .line-row { display: grid; grid-template-columns: 48px 220px 160px 220px 200px 180px; gap: 8px; align-items: start; padding: 10px 8px; border-radius: 8px; border: 1px solid transparent; }
    .header-row { background: #f3f4f6; font-size: 11px; font-weight: 700; color: #6b7280; text-transform: uppercase; letter-spacing: 0.05em; }
    .line-row:not(.header-row) { border-color: #f3f4f6; }
    .line-row.inspected { border-color: #bbf7d0; background: #f0fdf4; }
    .line-row.multi-cargo { border-left: 3px solid #f59e0b; }

    /* Columns */
    .col-line { display: flex; flex-direction: column; align-items: center; gap: 4px; padding-top: 6px; }
    .line-num { font-size: 13px; font-weight: 700; color: #1A005D; }
    .mc-badge { font-size: 9px; background: #fef3c7; color: #92400e; border-radius: 4px; padding: 1px 4px; font-weight: 700; }
    .col-product { display: flex; flex-direction: column; gap: 2px; padding-top: 6px; }
    .prd-code { font-family: monospace; font-size: 13px; font-weight: 600; color: #111827; }
    .prd-name { font-size: 12px; color: #6b7280; }
    .lot { font-size: 11px; color: #9ca3af; }

    /* Planned qty */
    .qty-group { display: flex; gap: 6px; flex-wrap: wrap; align-items: center; margin-bottom: 4px; }
    .qty-label { font-size: 10px; font-weight: 700; color: #9ca3af; }
    .qty-val { font-size: 13px; font-weight: 600; color: #374151; }
    .total-qty { font-size: 12px; font-weight: 700; color: #374151; }
    .total-qty.accent { color: #8EC400; }
    .variance { font-size: 11px; font-weight: 700; margin-left: 4px; }
    .variance.surplus { color: #059669; }
    .variance.shortage { color: #dc2626; }

    /* Result inputs */
    .result-inputs { display: flex; gap: 4px; align-items: center; flex-wrap: wrap; margin-bottom: 4px; }
    :host ::ng-deep .qty-input-sm { width: 52px !important; text-align: center; font-size: 12px; padding: 4px 6px; }

    /* Location inputs */
    .loc-inputs { display: flex; gap: 4px; flex-wrap: wrap; }
    .loc-input { width: 72px !important; font-size: 12px; padding: 4px 6px; }
    .loc-input-sm { width: 48px !important; font-size: 12px; padding: 4px 6px; }

    /* LPN column */
    .lpn-input { width: 120px !important; font-size: 12px; padding: 4px 6px; font-family: monospace; }
    .lpn-input.has-lpn { border-color: #6ee7b7; background: #ecfdf5; }

    /* Action bar */
    .action-bar { display: flex; justify-content: space-between; align-items: center; margin-top: 20px; padding-top: 16px; border-top: 1px solid #e5e7eb; }
    .action-info { display: flex; align-items: center; gap: 12px; font-size: 13px; font-weight: 600; color: #374151; }
    .action-buttons { display: flex; gap: 10px; }
    .lpn-chip { display: flex; align-items: center; gap: 4px; background: #ecfdf5; color: #065f46; border-radius: 20px; padding: 3px 10px; font-family: monospace; font-size: 12px; }

    /* Progress badge in list */
    .progress-badge { font-size: 12px; font-weight: 700; color: #374151; background: #f3f4f6; border-radius: 8px; padding: 3px 8px; }
    .progress-badge.done { background: #bbf7d0; color: #065f46; }

    /* Messages */
    .message { padding: 12px 16px; border-radius: 8px; margin-top: 16px; font-size: 13px; display: flex; align-items: center; gap: 8px; }
    .message.success { background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; }
    .message.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; }
    .required { color: #dc2626; }

    @media (max-width: 1200px) {
      .line-row { grid-template-columns: 48px 1fr 1fr; grid-template-rows: auto auto; }
      .col-location, .col-lpn { grid-column: span 1; }
    }
  `]
})
export class InboundInspectionComponent implements OnInit {

  view: 'list' | 'detail' = 'list';
  loading = signal(false);
  submitting = signal(false);

  listRows: InspectionListRow[] = [];
  totalRecords = 0;

  selectedArrival: InspectionListRow | null = null;
  detailLines: InspectionLine[] = [];

  collectiveLpnNumber = '';
  collectiveLpnType = 'PLT';
  showLpnScan = false;
  successMsg = '';
  errorMsg = '';

  filters = {
    arrivalNo: '',
    status: '',
    dateFrom: null as Date | null,
    dateTo: null as Date | null
  };

  statusOptions = [
    { label: 'Ready for Inspection', value: '200' },
    { label: 'Inspected', value: '205' },
    { label: 'Receiving', value: '300' }
  ];

  lpnTypeOptions = [
    { label: 'Pallet', value: 'PLT' },
    { label: 'Case', value: 'CSE' },
    { label: 'Tote', value: 'TOT' },
    { label: 'Other', value: 'OTH' }
  ];

  listColumns: WmsColumn[] = [
    { field: 'arrivalNumber',      header: 'Arrival No',    sortable: true },
    { field: 'arrivalStatusLabel', header: 'Status',        sortable: false },
    { field: 'scheduledDate',      header: 'Sched. Date',   sortable: true },
    { field: 'supplierName',       header: 'Supplier',      sortable: true },
    { field: 'poNumber',           header: 'PO No',         sortable: false },
    { field: 'progress',           header: 'Lines',         sortable: false },
    { field: 'actions',            header: '',              sortable: false }
  ];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.search();
  }

  // ---------------------------------------------------------------------------
  // List screen
  // ---------------------------------------------------------------------------

  search(): void {
    this.loading.set(true);
    const params: Record<string, string> = {};
    if (this.filters.arrivalNo)  params['arrivalNo'] = this.filters.arrivalNo;
    if (this.filters.status)     params['status']    = this.filters.status;
    if (this.filters.dateFrom)   params['dateFrom']  = this.toIsoDate(this.filters.dateFrom);
    if (this.filters.dateTo)     params['dateTo']    = this.toIsoDate(this.filters.dateTo);

    this.api.get<InspectionListRow[]>('/inbound/inspection', params)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (res) => {
          this.listRows = res.data ?? [];
          this.totalRecords = this.listRows.length;
        },
        error: () => {
          this.listRows = [];
          this.totalRecords = 0;
        }
      });
  }

  clearFilters(): void {
    this.filters = { arrivalNo: '', status: '', dateFrom: null, dateTo: null };
    this.search();
  }

  onLazyLoad(event: TableLazyLoadEvent): void {
    this.search();
  }

  // ---------------------------------------------------------------------------
  // Detail screen
  // ---------------------------------------------------------------------------

  openDetail(row: InspectionListRow): void {
    this.selectedArrival = row;
    this.detailLines = [];
    this.collectiveLpnNumber = '';
    this.successMsg = '';
    this.errorMsg = '';
    this.showLpnScan = false;
    this.view = 'detail';

    this.loading.set(true);
    this.api.get<InspectionLine[]>(`/inbound/inspection/${row.arrivalNumber}/lines`)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (res) => {
          this.detailLines = (res.data ?? []).map(line => ({
            ...line,
            resultCsQty: line.resultCsQty ?? 0,
            resultPcQty: line.resultPcQty ?? 0,
            resultBlQty: line.resultBlQty ?? 0,
            resultTotalQty: line.resultTotalQty ?? 0,
            lpnNumber: line.lpnNumber ?? '',
            lpnType: line.lpnType ?? 'PLT'
          }));
        },
        error: () => {
          this.errorMsg = 'Failed to load inspection lines';
        }
      });
  }

  backToList(): void {
    this.view = 'list';
    this.selectedArrival = null;
    this.detailLines = [];
    this.search();
  }

  // ---------------------------------------------------------------------------
  // Collective LPN
  // ---------------------------------------------------------------------------

  onScanCollectiveLpn(barcode: string): void {
    this.collectiveLpnNumber = barcode;
  }

  clearCollectiveLpn(): void {
    this.collectiveLpnNumber = '';
  }

  // ---------------------------------------------------------------------------
  // Quantity helpers
  // ---------------------------------------------------------------------------

  calcTotal(line: InspectionLine): void {
    const ppc = line.piecesPerCase || 1;
    const ppb = line.piecesPerBulk || 1;
    line.resultTotalQty =
      (ppc * (line.resultCsQty || 0)) + (line.resultPcQty || 0) + (ppb * (line.resultBlQty || 0));
  }

  variance(line: InspectionLine): number {
    return (line.resultTotalQty || 0) - (line.planTotalQty || 0);
  }

  get inspectedCount(): number {
    return this.detailLines.filter(l => l.resultTotalQty > 0).length;
  }

  // ---------------------------------------------------------------------------
  // Register inspection
  // ---------------------------------------------------------------------------

  registerInspection(): void {
    if (!this.selectedArrival) return;
    this.submitting.set(true);
    this.successMsg = '';
    this.errorMsg = '';

    const body = {
      arrivalNumber: this.selectedArrival.arrivalNumber,
      processMode: this.selectedArrival.arrivalStatus === '205' ? 'CHANGE' : 'NEW',
      collectiveLpnNumber: this.collectiveLpnNumber || null,
      collectiveLpnType: this.collectiveLpnType,
      updTimestamp: this.selectedArrival.updTimestamp,
      lines: this.detailLines
        .filter(l => l.resultTotalQty > 0)
        .map(l => ({
          lineNumber:     l.lineNumber,
          resultCsQty:    l.resultCsQty,
          resultPcQty:    l.resultPcQty,
          resultBlQty:    l.resultBlQty,
          areaCode:       l.areaCode,
          rackCode:       l.rackCode,
          levelCode:      l.levelCode,
          positionCode:   l.positionCode,
          lpnNumber:      l.lpnNumber || null,
          lpnType:        l.lpnType || null,
          lotNumber:      l.lotNumber,
          updTimestamp:   l.updTimestamp
        }))
    };

    this.api.post<any>('/inbound/inspection/register', body)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: (res) => {
          if (res.status === 'SUCCESS') {
            const lpns: string[] = res.data?.lpnNumbers ?? [];
            this.successMsg = `Inspection registered for ${res.data?.registeredLines} line(s).` +
              (lpns.length ? ` LPNs: ${lpns.join(', ')}` : '');
            if (this.selectedArrival) {
              this.selectedArrival.arrivalStatus = '205';
              this.selectedArrival.arrivalStatusLabel = 'Inspecting';
              this.selectedArrival.inspectedLines = res.data?.registeredLines ?? this.selectedArrival.inspectedLines;
            }
          } else {
            this.errorMsg = res.messages?.[0]?.message ?? 'Inspection registration failed';
          }
        },
        error: (err) => {
          this.errorMsg = err.error?.messages?.[0]?.message ?? 'Inspection registration failed';
        }
      });
  }

  // ---------------------------------------------------------------------------
  // Cancel inspection
  // ---------------------------------------------------------------------------

  cancelInspection(): void {
    if (!this.selectedArrival) return;
    if (!confirm(`Cancel inspection for ${this.selectedArrival.arrivalNumber}? This will revert all results.`)) return;

    this.submitting.set(true);
    this.successMsg = '';
    this.errorMsg = '';

    this.api.post<void>('/inbound/inspection/cancel', {
      arrivalNumber: this.selectedArrival.arrivalNumber,
      updTimestamp: this.selectedArrival.updTimestamp
    })
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: (res) => {
          if (res.status === 'SUCCESS') {
            this.successMsg = 'Inspection cancelled successfully';
            if (this.selectedArrival) {
              this.selectedArrival.arrivalStatus = '200';
              this.selectedArrival.arrivalStatusLabel = 'Located';
              this.selectedArrival.inspectedLines = 0;
            }
            // Clear result quantities on screen
            this.detailLines.forEach(l => {
              l.resultCsQty = 0;
              l.resultPcQty = 0;
              l.resultBlQty = 0;
              l.resultTotalQty = 0;
              l.lpnNumber = '';
            });
          } else {
            this.errorMsg = res.messages?.[0]?.message ?? 'Cancel failed';
          }
        },
        error: (err) => {
          this.errorMsg = err.error?.messages?.[0]?.message ?? 'Cancel failed';
        }
      });
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  statusVariant(status: string): BadgeVariant {
    switch (status) {
      case '100': return 'pending';
      case '200': return 'in-progress';
      case '205': return 'received';
      case '209': return 'completed';
      case '300': return 'in-progress';
      default:    return 'pending';
    }
  }

  private toIsoDate(d: Date | null): string {
    if (!d) return '';
    return d.toISOString().substring(0, 10);
  }
}
