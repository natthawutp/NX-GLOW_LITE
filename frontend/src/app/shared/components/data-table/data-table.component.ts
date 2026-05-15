import { Component, Input, Output, EventEmitter, ContentChild, TemplateRef, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TableModule, TableLazyLoadEvent } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { SkeletonModule } from 'primeng/skeleton';
import { TooltipModule } from 'primeng/tooltip';
import { EmptyStateComponent } from '../empty-state/empty-state.component';

export interface WmsColumn {
  field: string;
  header: string;
  sortable?: boolean;
  width?: string;
  align?: 'left' | 'center' | 'right';
  type?: 'text' | 'date' | 'number' | 'status' | 'custom';
  frozen?: boolean;
}

@Component({
  selector: 'wms-data-table',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    TableModule, InputTextModule, ButtonModule, ToolbarModule,
    SkeletonModule, TooltipModule, EmptyStateComponent
  ],
  template: `
    <div class="wms-table-container">
      <!-- Toolbar -->
      <div class="table-toolbar" *ngIf="showToolbar">
        <div class="toolbar-left">
          <span class="p-input-icon-left" *ngIf="showSearch">
            <i class="pi pi-search"></i>
            <input pInputText
                   type="text"
                   [(ngModel)]="globalFilter"
                   (input)="onGlobalFilter()"
                   [placeholder]="'common.search' | translate"
                   class="table-search">
          </span>
          <ng-content select="[toolbar-left]"></ng-content>
        </div>
        <div class="toolbar-right">
          <ng-content select="[toolbar-right]"></ng-content>
          <button pButton
                  icon="pi pi-refresh"
                  class="p-button-text p-button-sm"
                  [pTooltip]="'common.refresh' | translate"
                  (click)="onRefresh()">
          </button>
          <button pButton
                  icon="pi pi-download"
                  class="p-button-text p-button-sm"
                  [pTooltip]="'common.export' | translate"
                  (click)="onExport()"
                  *ngIf="showExport">
          </button>
        </div>
      </div>

      <!-- Data table -->
      <p-table
        #dt
        [value]="data"
        [columns]="columns"
        [lazy]="lazy"
        [paginator]="paginator"
        [rows]="pageSize"
        [totalRecords]="totalRecords"
        [loading]="loading"
        [rowHover]="true"
        [showCurrentPageReport]="true"
        [currentPageReportTemplate]="'Showing {first} to {last} of {totalRecords}'"
        [rowsPerPageOptions]="[10, 20, 50, 100]"
        [scrollable]="scrollable"
        [scrollHeight]="scrollHeight"
        [selectionMode]="selectionMode"
        [(selection)]="selection"
        [globalFilterFields]="globalFilterFields"
        [sortField]="sortField"
        [sortOrder]="sortOrder"
        [resizableColumns]="true"
        [reorderableColumns]="false"
        styleClass="p-datatable-sm"
        (onLazyLoad)="onLazyLoad($event)"
        (onRowSelect)="rowSelect.emit($event)"
        (onRowUnselect)="rowUnselect.emit($event)"
        [dataKey]="dataKey">

        <ng-template pTemplate="header">
          <tr>
            <th *ngIf="selectionMode === 'multiple'" style="width: 3rem">
              <p-tableHeaderCheckbox></p-tableHeaderCheckbox>
            </th>
            @for (col of columns; track col.field) {
              <th [pSortableColumn]="col.sortable ? col.field : undefined"
                  [style.width]="col.width"
                  [style.text-align]="col.align || 'left'">
                {{ col.header | translate }}
                <p-sortIcon [field]="col.field" *ngIf="col.sortable"></p-sortIcon>
              </th>
            }
            <th *ngIf="showActions" style="width: 100px; text-align: center">
              {{ 'common.actions' | translate }}
            </th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-rowData let-rowIndex="rowIndex">
          <tr>
            <td *ngIf="selectionMode === 'multiple'">
              <p-tableCheckbox [value]="rowData"></p-tableCheckbox>
            </td>
            @for (col of columns; track col.field) {
              <td [style.text-align]="col.align || 'left'">
                <ng-container [ngSwitch]="col.type">
                  <span *ngSwitchCase="'date'">{{ rowData[col.field] | date:'yyyy-MM-dd' }}</span>
                  <span *ngSwitchCase="'number'" class="number-cell">{{ rowData[col.field] | number }}</span>
                  <ng-container *ngSwitchCase="'custom'">
                    <ng-container *ngTemplateOutlet="customCellTemplate; context: { $implicit: rowData, column: col, index: rowIndex }">
                    </ng-container>
                  </ng-container>
                  <span *ngSwitchDefault>{{ rowData[col.field] }}</span>
                </ng-container>
              </td>
            }
            <td *ngIf="showActions" style="text-align: center">
              <ng-container *ngTemplateOutlet="actionTemplate; context: { $implicit: rowData, index: rowIndex }">
              </ng-container>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="emptymessage">
          <tr>
            <td [attr.colspan]="columns.length + (selectionMode === 'multiple' ? 1 : 0) + (showActions ? 1 : 0)">
              <wms-empty-state
                [title]="emptyTitle"
                [description]="emptyDescription"
                [icon]="emptyIcon">
              </wms-empty-state>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="loadingbody">
          <tr *ngFor="let i of skeletonRows">
            <td *ngIf="selectionMode === 'multiple'"><p-skeleton width="20px" height="20px"></p-skeleton></td>
            <td *ngFor="let col of columns"><p-skeleton></p-skeleton></td>
            <td *ngIf="showActions"><p-skeleton width="60px"></p-skeleton></td>
          </tr>
        </ng-template>
      </p-table>
    </div>
  `,
  styles: [`
    .wms-table-container {
      background: white;
      border-radius: 14px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }

    .table-toolbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 14px 18px;
      border-bottom: 1px solid #f3f4f6;
      gap: 12px;
      flex-wrap: wrap;
    }

    .toolbar-left, .toolbar-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .p-input-icon-left {
      position: relative;
      display: inline-flex;
      align-items: center;
    }

    .p-input-icon-left > i {
      position: absolute;
      left: 10px;
      top: 50%;
      transform: translateY(-50%);
      color: #9ca3af;
      z-index: 1;
      pointer-events: none;
    }

    .table-search {
      width: 260px;
      font-size: 13px;
      padding-left: 2.2rem !important;
    }

    .number-cell {
      font-variant-numeric: tabular-nums;
    }

    /* Header alignment fix: ensure header content + sort icon are inline */
    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th .p-column-header-content {
      display: flex !important;
      flex-direction: row !important;
      align-items: center !important;
      gap: 0.35rem;
      flex-wrap: nowrap;
    }

    @media (max-width: 640px) {
      .table-search { width: 180px; }
      .table-toolbar { padding: 10px 12px; }
    }
  `]
})
export class DataTableComponent {
  @Input() data: any[] = [];
  @Input() columns: WmsColumn[] = [];
  @Input() totalRecords = 0;
  @Input() pageSize = 20;
  @Input() loading = false;
  @Input() lazy = true;
  @Input() paginator = true;
  @Input() scrollable = true;
  @Input() scrollHeight = 'flex';
  @Input() selectionMode: 'single' | 'multiple' | null = null;
  @Input() selection: any = null;
  @Input() dataKey = 'id';
  @Input() sortField = '';
  @Input() sortOrder = 1;
  @Input() globalFilterFields: string[] = [];
  @Input() showToolbar = true;
  @Input() showSearch = true;
  @Input() showExport = true;
  @Input() showActions = true;
  @Input() emptyTitle = 'common.no_data';
  @Input() emptyDescription = 'common.no_data_description';
  @Input() emptyIcon = 'pi pi-inbox';

  @Output() lazyLoad = new EventEmitter<TableLazyLoadEvent>();
  @Output() rowSelect = new EventEmitter<any>();
  @Output() rowUnselect = new EventEmitter<any>();
  @Output() refresh = new EventEmitter<void>();
  @Output() export = new EventEmitter<void>();
  @Output() selectionChange = new EventEmitter<any>();

  @ContentChild('customCell') customCellTemplate!: TemplateRef<any>;
  @ContentChild('actionCell') actionTemplate!: TemplateRef<any>;

  globalFilter = '';
  skeletonRows = Array(5).fill(0);

  onLazyLoad(event: TableLazyLoadEvent): void {
    this.lazyLoad.emit(event);
  }

  onGlobalFilter(): void {
    // Handled by PrimeNG Table globalFilter internally
  }

  onRefresh(): void {
    this.refresh.emit();
  }

  onExport(): void {
    this.export.emit();
  }
}
