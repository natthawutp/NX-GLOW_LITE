import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { TooltipModule } from 'primeng/tooltip';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';

@Component({
  selector: 'wms-master-products',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, DropdownModule,
            TableModule, TagModule, DialogModule, InputNumberModule, InputTextareaModule, TooltipModule, PageHeaderComponent],
  template: `
    <wms-page-header title="master.products_title" subtitle="master.products_subtitle" icon="pi pi-th-large">
      <button pButton label="Add Product" icon="pi pi-plus" class="p-button-sm" (click)="openDialog()"></button>
    </wms-page-header>

    <!-- Filters -->
    <div class="wms-card mb-3">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Product Code</label>
          <input pInputText [(ngModel)]="filters.code" placeholder="Search..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Product Name</label>
          <input pInputText [(ngModel)]="filters.name" placeholder="Search..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Category</label>
          <p-dropdown [options]="categories" [(ngModel)]="filters.category" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-item">
          <label class="filter-label">Status</label>
          <p-dropdown [options]="statusOptions" [(ngModel)]="filters.status" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-actions">
          <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="wms-card">
      <p-table [value]="products" [paginator]="true" [rows]="15" [rowsPerPageOptions]="[10,15,30,50]"
               [showCurrentPageReport]="true" currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
               styleClass="p-datatable-sm p-datatable-gridlines" [scrollable]="true" scrollHeight="520px" sortMode="multiple">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="code" style="width:130px">Code <p-sortIcon field="code"></p-sortIcon></th>
            <th pSortableColumn="name">Name <p-sortIcon field="name"></p-sortIcon></th>
            <th pSortableColumn="category" style="width:120px">Category <p-sortIcon field="category"></p-sortIcon></th>
            <th style="width:80px">UOM</th>
            <th pSortableColumn="weight" style="width:90px">Weight <p-sortIcon field="weight"></p-sortIcon></th>
            <th style="width:100px">Dimensions</th>
            <th pSortableColumn="minStock" style="width:90px">Min Stock <p-sortIcon field="minStock"></p-sortIcon></th>
            <th pSortableColumn="maxStock" style="width:90px">Max Stock <p-sortIcon field="maxStock"></p-sortIcon></th>
            <th style="width:80px">Status</th>
            <th style="width:90px">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-row>
          <tr>
            <td><span class="mono-code">{{ row.code }}</span></td>
            <td>{{ row.name }}</td>
            <td>{{ row.category }}</td>
            <td>{{ row.uom }}</td>
            <td>{{ row.weight ? row.weight + ' kg' : '—' }}</td>
            <td>{{ row.dimensions || '—' }}</td>
            <td class="text-right">{{ row.minStock | number }}</td>
            <td class="text-right">{{ row.maxStock | number }}</td>
            <td><p-tag [value]="row.status" [severity]="row.status === 'Active' ? 'success' : 'danger'" [rounded]="true"></p-tag></td>
            <td>
              <button pButton icon="pi pi-pencil" class="p-button-text p-button-sm p-button-rounded" pTooltip="Edit" (click)="editProduct(row)"></button>
              <button pButton icon="pi pi-trash" class="p-button-text p-button-sm p-button-rounded p-button-danger" pTooltip="Delete" (click)="deleteProduct(row)"></button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog -->
    <p-dialog [(visible)]="showDialog" [header]="editMode ? 'Edit Product' : 'Add Product'" [modal]="true"
              [style]="{ width: '640px' }" [draggable]="false" [resizable]="false">
      <div class="dialog-form">
        <div class="form-row">
          <div class="form-field"><label>Product Code</label><input pInputText [(ngModel)]="form.code" class="w-full" [disabled]="editMode" /></div>
          <div class="form-field"><label>Product Name</label><input pInputText [(ngModel)]="form.name" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Category</label><p-dropdown [options]="categories" [(ngModel)]="form.category" styleClass="w-full"></p-dropdown></div>
          <div class="form-field"><label>UOM</label><p-dropdown [options]="uoms" [(ngModel)]="form.uom" styleClass="w-full"></p-dropdown></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Weight (kg)</label><p-inputNumber [(ngModel)]="form.weight" [minFractionDigits]="2" styleClass="w-full"></p-inputNumber></div>
          <div class="form-field"><label>Dimensions (L×W×H cm)</label><input pInputText [(ngModel)]="form.dimensions" class="w-full" placeholder="30×20×15" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Min Stock</label><p-inputNumber [(ngModel)]="form.minStock" [showButtons]="true" [min]="0" styleClass="w-full"></p-inputNumber></div>
          <div class="form-field"><label>Max Stock</label><p-inputNumber [(ngModel)]="form.maxStock" [showButtons]="true" [min]="0" styleClass="w-full"></p-inputNumber></div>
        </div>
        <div class="form-field"><label>Description</label><textarea pInputTextarea [(ngModel)]="form.description" rows="3" class="w-full"></textarea></div>
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancel" icon="pi pi-times" class="p-button-text" (click)="showDialog = false"></button>
        <button pButton label="Save" icon="pi pi-check" (click)="saveProduct()"></button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .mb-3 { margin-bottom: 16px; }
    .filter-row { display: flex; gap: 14px; align-items: flex-end; flex-wrap: wrap; }
    .filter-item { flex: 1; min-width: 150px; display: flex; flex-direction: column; gap: 4px; }
    .filter-label { font-size: 12px; font-weight: 600; color: #374151; }
    .filter-actions { display: flex; align-items: flex-end; }
    .w-full { width: 100%; }
    .mono-code { font-family: monospace; font-weight: 700; color: #1A005D; }
    .text-right { text-align: right; }
    .dialog-form { display: flex; flex-direction: column; gap: 14px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field label { font-size: 12px; font-weight: 600; color: #374151; }
    @media (max-width: 768px) { .form-row { grid-template-columns: 1fr; } }
  `]
})
export class MasterProductsComponent implements OnInit {
  filters = { code: '', name: '', category: null as string | null, status: null as string | null };
  showDialog = false;
  editMode = false;
  form: any = {};

  categories = [
    { label: 'Apparel', value: 'Apparel' }, { label: 'Home & Living', value: 'Home & Living' },
    { label: 'Stationery', value: 'Stationery' }, { label: 'Food & Beverage', value: 'Food & Beverage' },
    { label: 'Health & Beauty', value: 'Health & Beauty' }
  ];
  statusOptions = [{ label: 'Active', value: 'Active' }, { label: 'Inactive', value: 'Inactive' }];
  uoms = [{ label: 'PCS', value: 'PCS' }, { label: 'BOX', value: 'BOX' }, { label: 'KG', value: 'KG' }, { label: 'SET', value: 'SET' }];

  products: any[] = [];

  ngOnInit(): void {
    this.products = [
      { code: 'SKU-00123', name: 'Organic Cotton T-Shirt M', category: 'Apparel', uom: 'PCS', weight: 0.22, dimensions: '30×25×3', minStock: 50, maxStock: 1000, status: 'Active' },
      { code: 'SKU-00456', name: 'Aroma Diffuser 200ml', category: 'Home & Living', uom: 'PCS', weight: 0.65, dimensions: '12×12×18', minStock: 20, maxStock: 300, status: 'Active' },
      { code: 'SKU-00789', name: 'Notebook A5 Recycled Paper', category: 'Stationery', uom: 'PCS', weight: 0.18, dimensions: '21×15×1', minStock: 100, maxStock: 2000, status: 'Active' },
      { code: 'SKU-01012', name: 'Stainless Steel Water Bottle', category: 'Home & Living', uom: 'PCS', weight: 0.35, dimensions: '7×7×24', minStock: 30, maxStock: 500, status: 'Inactive' },
      { code: 'SKU-01315', name: 'Green Tea Bags 100pk', category: 'Food & Beverage', uom: 'BOX', weight: 0.25, dimensions: '15×10×8', minStock: 50, maxStock: 800, status: 'Active' },
      { code: 'SKU-01618', name: 'Linen Cushion Cover 45cm', category: 'Home & Living', uom: 'PCS', weight: 0.30, dimensions: '46×46×2', minStock: 40, maxStock: 600, status: 'Active' },
      { code: 'SKU-01921', name: 'Ceramic Mug 300ml White', category: 'Home & Living', uom: 'PCS', weight: 0.42, dimensions: '10×8×10', minStock: 60, maxStock: 1200, status: 'Active' },
    ];
  }

  search(): void { console.log('Search products:', this.filters); }
  openDialog(): void { this.editMode = false; this.form = { minStock: 0, maxStock: 0 }; this.showDialog = true; }
  editProduct(row: any): void { this.editMode = true; this.form = { ...row }; this.showDialog = true; }
  deleteProduct(row: any): void { console.log('Delete:', row.code); }
  saveProduct(): void { console.log('Save:', this.form); this.showDialog = false; }
}
