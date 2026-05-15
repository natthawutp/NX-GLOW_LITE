import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule, TableLazyLoadEvent } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { CalendarModule } from 'primeng/calendar';
import { CheckboxModule } from 'primeng/checkbox';
import { PanelModule } from 'primeng/panel';
import { MessageModule } from 'primeng/message';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { ApiService } from '@core/services/api.service';
import { finalize } from 'rxjs/operators';

interface StockSearchResult {
  records: any[];
  totalRecords: number;
  totalPhysicalCases: number;
  totalPhysicalPieces: number;
  totalAvailableCases: number;
  totalAvailablePieces: number;
  totalPhysicalStock: number;
  totalAvailableStock: number;
  pieceModeFlag: boolean;
  warningCode: string | null;
  warningMessage: string | null;
}

interface StockFilters {
  productCode: string;
  originCode: string;
  areaCode: string;
  rackCode: string;
  positionCode: string;
  levelCode: string;
  subInventoryCode: string;
  arrivalNumber: string;
  arrivalLineNumber: string;
  arrivalSeqNumber: string;
  dateFrom: Date | null;
  dateTo: Date | null;
  pik1: string;
  pik2: string;
  pik3: string;
  pik4: string;
  pik5: string;
  pik6: string;
  pik7: string;
  damageFlag: string | null;
  lockFlag: string | null;
  bondFlag: string | null;
  physicalQtyFilter: string;
  availableQtyFilter: string;
  forwardSearch: boolean;
  tempLocationOnly: boolean;
  subDrop: string;
  areaDrop: string;
  rackDrop: string;
  posDrop: string;
  levDrop: string;
  dmgDrop: string;
  pik1Drop: string;
  pik2Drop: string;
  pik3Drop: string;
  pik4Drop: string;
  pik5Drop: string;
  maxDisplayResults: number | null;
}

@Component({
  selector: 'wms-inventory-stock',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    ButtonModule, InputTextModule, DropdownModule, TableModule,
    TooltipModule, CalendarModule, CheckboxModule, PanelModule, MessageModule,
    PageHeaderComponent
  ],
  template: `
    <wms-page-header title="inventory.stock_title" subtitle="inventory.stock_subtitle" icon="pi pi-database">
      <button pButton label="{{ 'common.export' | translate }}" icon="pi pi-download" class="p-button-sm p-button-outlined" (click)="exportStock()"></button>
    </wms-page-header>

    <!-- Summary Cards -->
    <div class="summary-row">
      @for (card of summaryCards; track card.label) {
        <div class="summary-card">
          <div class="summary-icon" [style.background]="card.bg">
            <i [class]="card.icon" [style.color]="card.color"></i>
          </div>
          <div class="summary-info">
            <span class="summary-value">{{ card.value | number }}</span>
            <span class="summary-label">{{ card.label | translate }}</span>
          </div>
        </div>
      }
    </div>

    <!-- Warning Message -->
    @if (warningMessage()) {
      <p-message [severity]="warningCode() === 'ERR0174' ? 'warn' : 'info'" [text]="warningMessage() ?? undefined" styleClass="mb-3 w-full"></p-message>
    }

    <!-- Basic Filters -->
    <div class="wms-card">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.product_code' | translate }}</label>
          <input pInputText [(ngModel)]="filters.productCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.origin' | translate }}</label>
          <input pInputText [(ngModel)]="filters.originCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.area' | translate }}</label>
          <input pInputText [(ngModel)]="filters.areaCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.rack' | translate }}</label>
          <input pInputText [(ngModel)]="filters.rackCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.position' | translate }}</label>
          <input pInputText [(ngModel)]="filters.positionCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.level' | translate }}</label>
          <input pInputText [(ngModel)]="filters.levelCode" class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">{{ 'inventory.sub_inventory' | translate }}</label>
          <input pInputText [(ngModel)]="filters.subInventoryCode" class="w-full" />
        </div>
        <div class="filter-actions">
          <button pButton label="{{ 'common.search' | translate }}" icon="pi pi-search" class="p-button-sm" (click)="onSearch()"></button>
          <button pButton label="{{ 'common.clear' | translate }}" icon="pi pi-times" class="p-button-sm p-button-text" (click)="clearFilters()"></button>
        </div>
      </div>

      <!-- Advanced Filters (collapsible) -->
      <p-panel header="{{ 'inventory.advanced_filters' | translate }}" [toggleable]="true" [collapsed]="true" styleClass="mt-3">
        <div class="filter-row">
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.arrival_number' | translate }}</label>
            <input pInputText [(ngModel)]="filters.arrivalNumber" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.arrival_line' | translate }}</label>
            <input pInputText [(ngModel)]="filters.arrivalLineNumber" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.arrival_seq' | translate }}</label>
            <input pInputText [(ngModel)]="filters.arrivalSeqNumber" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.date_from' | translate }}</label>
            <p-calendar [(ngModel)]="filters.dateFrom" dateFormat="yy-mm-dd" [showIcon]="true" [style]="{ width: '100%' }"></p-calendar>
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.date_to' | translate }}</label>
            <p-calendar [(ngModel)]="filters.dateTo" dateFormat="yy-mm-dd" [showIcon]="true" [style]="{ width: '100%' }"></p-calendar>
          </div>
        </div>
        <div class="filter-row mt-2">
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik1' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik1" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik2' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik2" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik3' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik3" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik4' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik4" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik5' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik5" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik6' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik6" class="w-full" />
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.pik7' | translate }}</label>
            <input pInputText [(ngModel)]="filters.pik7" class="w-full" />
          </div>
        </div>
        <div class="filter-row mt-2">
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.damage' | translate }}</label>
            <p-dropdown [options]="flagOptions" [(ngModel)]="filters.damageFlag" [showClear]="true" placeholder="All" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.lock' | translate }}</label>
            <p-dropdown [options]="flagOptions" [(ngModel)]="filters.lockFlag" [showClear]="true" placeholder="All" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.bond' | translate }}</label>
            <p-dropdown [options]="flagOptions" [(ngModel)]="filters.bondFlag" [showClear]="true" placeholder="All" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.physical_qty_filter' | translate }}</label>
            <p-dropdown [options]="qtyFilterOptions" [(ngModel)]="filters.physicalQtyFilter" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item">
            <label class="filter-label">{{ 'inventory.available_qty_filter' | translate }}</label>
            <p-dropdown [options]="qtyFilterOptions" [(ngModel)]="filters.availableQtyFilter" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="flex: 0 0 auto;">
            <label class="filter-label">&nbsp;</label>
            <div class="flex align-items-center gap-2">
              <p-checkbox [(ngModel)]="filters.forwardSearch" [binary]="true"></p-checkbox>
              <span class="filter-label">{{ 'inventory.forward_search' | translate }}</span>
            </div>
          </div>
          <div class="filter-item" style="flex: 0 0 auto;">
            <label class="filter-label">&nbsp;</label>
            <div class="flex align-items-center gap-2">
              <p-checkbox [(ngModel)]="filters.tempLocationOnly" [binary]="true"></p-checkbox>
              <span class="filter-label">{{ 'inventory.temp_location' | translate }}</span>
            </div>
          </div>
        </div>
      </p-panel>

      <!-- Aggregation Filters (collapsible) -->
      <p-panel header="{{ 'inventory.aggregation' | translate }}" [toggleable]="true" [collapsed]="true" styleClass="mt-3">
        <div class="filter-row">
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.sub_inventory' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.subDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.area' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.areaDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.rack' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.rackDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.position' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.posDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.level' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.levDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">{{ 'inventory.damage' | translate }}</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.dmgDrop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
        </div>
        <div class="filter-row mt-2">
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">PIK1</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.pik1Drop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">PIK2</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.pik2Drop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">PIK3</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.pik3Drop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">PIK4</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.pik4Drop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:120px">
            <label class="filter-label">PIK5</label>
            <p-dropdown [options]="dropOptions" [(ngModel)]="filters.pik5Drop" [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="filter-item" style="min-width:140px">
            <label class="filter-label">{{ 'inventory.max_display_results' | translate }}</label>
            <input pInputText type="number" [(ngModel)]="filters.maxDisplayResults" class="w-full" />
          </div>
        </div>
      </p-panel>
    </div>

    <!-- Data Table -->
    <div class="wms-card">
      <p-table [value]="stocks()" [lazy]="true" [paginator]="true" [rows]="pageSize"
               [totalRecords]="totalRecords()" [rowsPerPageOptions]="[10,20,50]"
               [showCurrentPageReport]="true" currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
               styleClass="p-datatable-sm p-datatable-gridlines"
               [scrollable]="true" scrollHeight="480px"
               [loading]="loading()"
               (onLazyLoad)="onLazyLoad($event)">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:130px">{{ 'inventory.product_code' | translate }}</th>
            <th style="width:200px">{{ 'inventory.product_name' | translate }}</th>
            <th style="width:90px">{{ 'inventory.origin' | translate }}</th>
            <th style="width:60px">PIK1</th>
            <th style="width:60px">PIK2</th>
            <th style="width:60px">PIK3</th>
            <th style="width:60px">PIK4</th>
            <th style="width:60px">PIK5</th>
            <th style="width:60px">PIK6</th>
            <th style="width:60px">PIK7</th>
            <th style="width:150px">{{ 'inventory.location' | translate }}</th>
            <th style="width:90px">{{ 'inventory.sub_inventory' | translate }}</th>
            <th style="width:140px">{{ 'inventory.sub_inventory_name' | translate }}</th>
            <th style="width:60px">{{ 'inventory.bond' | translate }}</th>
            <th style="width:60px">{{ 'inventory.damage' | translate }}</th>
            <th style="width:60px">{{ 'inventory.lock' | translate }}</th>
            <th style="width:110px;text-align:right">{{ 'inventory.physical_stock' | translate }}</th>
            <th style="width:110px;text-align:right">{{ 'inventory.available_stock' | translate }}</th>
            <th style="width:110px;text-align:right">{{ 'inventory.allocated_stock' | translate }}</th>
            @if (!pieceMode()) {
              <th style="width:90px;text-align:right">{{ 'inventory.physical_cases' | translate }}</th>
              <th style="width:90px;text-align:right">{{ 'inventory.physical_pieces' | translate }}</th>
              <th style="width:90px;text-align:right">{{ 'inventory.available_cases' | translate }}</th>
              <th style="width:90px;text-align:right">{{ 'inventory.available_pieces' | translate }}</th>
            }
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-row>
          <tr>
            <td><span class="product-code">{{ row.ST_PROD_COD }}</span></td>
            <td>{{ row.PROD_NAM1 }}</td>
            <td>{{ row.ST_ORGN_COD }}</td>
            <td>{{ row.ST_PIK1 }}</td>
            <td>{{ row.ST_PIK2 }}</td>
            <td>{{ row.ST_PIK3 }}</td>
            <td>{{ row.ST_PIK4 }}</td>
            <td>{{ row.ST_PIK5 }}</td>
            <td>{{ row.ST_PIK6 }}</td>
            <td>{{ row.ST_PIK7 }}</td>
            <td><span class="location-code">{{ row.LOCATION }}</span></td>
            <td>{{ row.ST_SBIV_COD }}</td>
            <td>{{ row.SBIV_NAM }}</td>
            <td class="text-center">{{ row.ST_BOND_FLG }}</td>
            <td class="text-center">{{ row.ST_DMG_FLG }}</td>
            <td class="text-center">{{ row.ST_LOCK_FLG }}</td>
            <td class="text-right font-bold">{{ row.ST_PYST_QTY | number }}</td>
            <td class="text-right font-bold" [class.text-accent]="row.ST_AVST_QTY > 0" [class.text-danger]="row.ST_AVST_QTY === 0">{{ row.ST_AVST_QTY | number }}</td>
            <td class="text-right" [class.text-warning]="row.ST_ALST_QTY > 0">{{ row.ST_ALST_QTY | number }}</td>
            @if (!pieceMode()) {
              <td class="text-right">{{ row.ST_PYCS_QTY | number }}</td>
              <td class="text-right">{{ row.ST_PYPC_QTY | number }}</td>
              <td class="text-right">{{ row.ST_AVCS_QTY | number }}</td>
              <td class="text-right">{{ row.ST_AVPC_QTY | number }}</td>
            }
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td [attr.colspan]="pieceMode() ? 19 : 23" class="text-center py-4 text-gray-400">{{ 'inventory.no_data_found' | translate }}</td></tr>
        </ng-template>
      </p-table>
    </div>
  `,
  styles: [`
    .summary-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 16px; }
    .summary-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 18px; display: flex; align-items: center; gap: 14px; }
    .summary-icon { width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 20px; }
    .summary-info { display: flex; flex-direction: column; }
    .summary-value { font-size: 22px; font-weight: 800; color: #111827; }
    .summary-label { font-size: 12px; color: #6b7280; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; margin-bottom: 16px; }
    .filter-row { display: flex; gap: 14px; align-items: flex-end; flex-wrap: wrap; }
    .filter-item { flex: 1; min-width: 130px; display: flex; flex-direction: column; gap: 4px; }
    .filter-label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; gap: 8px; align-items: flex-end; }
    .w-full { width: 100%; }
    .mt-2 { margin-top: 10px; }
    .mt-3 { margin-top: 14px; }
    .mb-3 { margin-bottom: 14px; }
    .flex { display: flex; }
    .align-items-center { align-items: center; }
    .gap-2 { gap: 8px; }
    .product-code { font-family: monospace; font-weight: 600; color: #1A005D; }
    .location-code { font-family: monospace; font-weight: 600; color: #374151; background: #f3f4f6; padding: 2px 8px; border-radius: 4px; }
    .text-right { text-align: right; }
    .text-center { text-align: center; }
    .font-bold { font-weight: 700; }
    .text-accent { color: #8EC400; }
    .text-warning { color: #FF9E1B; }
    .text-danger { color: #FF585D; }
    .py-4 { padding: 16px 0; }
    .text-gray-400 { color: #9ca3af; }
    @media (max-width: 768px) { .summary-row { grid-template-columns: repeat(2, 1fr); } }
  `]
})
export class InventoryStockComponent implements OnInit {
  stocks = signal<any[]>([]);
  totalRecords = signal(0);
  loading = signal(false);
  pieceMode = signal(false);
  warningCode = signal<string | null>(null);
  warningMessage = signal<string | null>(null);

  page = 0;
  pageSize = 20;

  filters: StockFilters = {
    productCode: '',
    originCode: '',
    areaCode: '',
    rackCode: '',
    positionCode: '',
    levelCode: '',
    subInventoryCode: '',
    arrivalNumber: '',
    arrivalLineNumber: '',
    arrivalSeqNumber: '',
    dateFrom: null,
    dateTo: null,
    pik1: '', pik2: '', pik3: '', pik4: '', pik5: '', pik6: '', pik7: '',
    damageFlag: null,
    lockFlag: null,
    bondFlag: null,
    physicalQtyFilter: '1',
    availableQtyFilter: '1',
    forwardSearch: false,
    tempLocationOnly: false,
    subDrop: '01',
    areaDrop: '01',
    rackDrop: '01',
    posDrop: '01',
    levDrop: '01',
    dmgDrop: '01',
    pik1Drop: '01', pik2Drop: '01', pik3Drop: '01', pik4Drop: '01', pik5Drop: '01',
    maxDisplayResults: null
  };

  flagOptions = [
    { label: 'Yes', value: 'Y' },
    { label: 'No', value: 'N' }
  ];

  qtyFilterOptions = [
    { label: 'All', value: '1' },
    { label: 'Non-Zero', value: '2' },
    { label: 'Zero Only', value: '3' }
  ];

  dropOptions = [
    { label: 'Distinct', value: '01' },
    { label: 'Aggregate', value: '02' }
  ];

  summaryCards = [
    { label: 'inventory.total_physical_stock', value: 0, icon: 'pi pi-database', color: '#1A005D', bg: '#ede9fe' },
    { label: 'inventory.total_available_stock', value: 0, icon: 'pi pi-check-circle', color: '#8EC400', bg: '#ecfccb' },
    { label: 'inventory.physical_cases', value: 0, icon: 'pi pi-box', color: '#FF9E1B', bg: '#fff7ed' },
    { label: 'inventory.physical_pieces', value: 0, icon: 'pi pi-th-large', color: '#5BC2E7', bg: '#e0f2fe' }
  ];

  constructor(private api: ApiService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.onSearch();
  }

  onLazyLoad(event: TableLazyLoadEvent): void {
    const rows = event.rows ?? this.pageSize;
    const first = event.first ?? 0;
    this.pageSize = rows;
    this.page = Math.floor(first / rows);
    this.onSearch();
  }

  onSearch(): void {
    this.loading.set(true);
    this.warningCode.set(null);
    this.warningMessage.set(null);

    const params: Record<string, any> = {
      productCode: this.filters.productCode,
      forwardSearch: this.filters.forwardSearch ? 'true' : '',
      originCode: this.filters.originCode,
      areaCode: this.filters.areaCode,
      rackCode: this.filters.rackCode,
      positionCode: this.filters.positionCode,
      levelCode: this.filters.levelCode,
      subInventoryCode: this.filters.subInventoryCode,
      arrivalNumber: this.filters.arrivalNumber,
      arrivalLineNumber: this.filters.arrivalLineNumber,
      arrivalSeqNumber: this.filters.arrivalSeqNumber,
      dateFrom: this.formatDate(this.filters.dateFrom),
      dateTo: this.formatDate(this.filters.dateTo),
      pik1: this.filters.pik1, pik2: this.filters.pik2, pik3: this.filters.pik3,
      pik4: this.filters.pik4, pik5: this.filters.pik5, pik6: this.filters.pik6, pik7: this.filters.pik7,
      damageFlag: this.filters.damageFlag,
      lockFlag: this.filters.lockFlag,
      bondFlag: this.filters.bondFlag,
      physicalQtyFilter: this.filters.physicalQtyFilter,
      availableQtyFilter: this.filters.availableQtyFilter,
      tempLocationOnly: this.filters.tempLocationOnly ? 'true' : '',
      subDrop: this.filters.subDrop,
      areaDrop: this.filters.areaDrop,
      rackDrop: this.filters.rackDrop,
      posDrop: this.filters.posDrop,
      levDrop: this.filters.levDrop,
      dmgDrop: this.filters.dmgDrop,
      pik1Drop: this.filters.pik1Drop, pik2Drop: this.filters.pik2Drop,
      pik3Drop: this.filters.pik3Drop, pik4Drop: this.filters.pik4Drop, pik5Drop: this.filters.pik5Drop,
      maxDisplayResults: this.filters.maxDisplayResults,
      page: this.page,
      size: this.pageSize
    };

    this.api.get<StockSearchResult>('/inventory/stocks', params).pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: (response) => {
        const data = response.data;
        if (data) {
          this.stocks.set(data.records || []);
          this.totalRecords.set(data.totalRecords || 0);
          this.pieceMode.set(data.pieceModeFlag || false);
          this.updateSummaryCards(data);

          if (data.warningCode) {
            this.warningCode.set(data.warningCode);
            this.warningMessage.set(data.warningMessage);
          }
        } else {
          this.stocks.set([]);
          this.totalRecords.set(0);
        }
      },
      error: () => {
        this.stocks.set([]);
        this.totalRecords.set(0);
      }
    });
  }

  clearFilters(): void {
    this.filters = {
      productCode: '', originCode: '',
      areaCode: '', rackCode: '', positionCode: '', levelCode: '',
      subInventoryCode: '',
      arrivalNumber: '', arrivalLineNumber: '', arrivalSeqNumber: '',
      dateFrom: null, dateTo: null,
      pik1: '', pik2: '', pik3: '', pik4: '', pik5: '', pik6: '', pik7: '',
      damageFlag: null, lockFlag: null, bondFlag: null,
      physicalQtyFilter: '1', availableQtyFilter: '1',
      forwardSearch: false, tempLocationOnly: false,
      subDrop: '01', areaDrop: '01', rackDrop: '01', posDrop: '01', levDrop: '01', dmgDrop: '01',
      pik1Drop: '01', pik2Drop: '01', pik3Drop: '01', pik4Drop: '01', pik5Drop: '01',
      maxDisplayResults: null
    } as StockFilters;
    this.page = 0;
    this.onSearch();
  }

  exportStock(): void {
    // TODO: Implement export
  }

  private updateSummaryCards(data: StockSearchResult): void {
    this.summaryCards = [
      { label: 'inventory.total_physical_stock', value: data.totalPhysicalStock || 0, icon: 'pi pi-database', color: '#1A005D', bg: '#ede9fe' },
      { label: 'inventory.total_available_stock', value: data.totalAvailableStock || 0, icon: 'pi pi-check-circle', color: '#8EC400', bg: '#ecfccb' },
      { label: 'inventory.physical_cases', value: data.totalPhysicalCases || 0, icon: 'pi pi-box', color: '#FF9E1B', bg: '#fff7ed' },
      { label: 'inventory.physical_pieces', value: data.totalPhysicalPieces || 0, icon: 'pi pi-th-large', color: '#5BC2E7', bg: '#e0f2fe' }
    ];
  }

  private formatDate(date: Date | null): string {
    if (!date) { return ''; }
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  }
}
