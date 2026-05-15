import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { StepsModule } from 'primeng/steps';
import { TableModule } from 'primeng/table';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';

@Component({
  selector: 'wms-inventory-adjustment',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, InputNumberModule, DropdownModule,
            InputTextareaModule, StepsModule, TableModule, PageHeaderComponent, BarcodeInputComponent],
  template: `
    <wms-page-header title="inventory.adjustment_title" subtitle="inventory.adjustment_subtitle" icon="pi pi-sliders-h">
    </wms-page-header>

    <div class="adjustment-layout">
      <!-- Step Indicator -->
      <div class="wms-card">
        <p-steps [model]="steps" [activeIndex]="activeStep" [readonly]="false" (activeIndexChange)="activeStep = $event"></p-steps>
      </div>

      <!-- Step 1: Select Product -->
      @if (activeStep === 0) {
        <div class="wms-card">
          <h3 class="section-title">Scan or Search Product</h3>
          <wms-barcode-input placeholder="Scan product barcode..." (scan)="onScanProduct($event)"></wms-barcode-input>
          @if (selectedProduct) {
            <div class="product-detail-card">
              <div class="product-header">
                <span class="product-code">{{ selectedProduct.code }}</span>
                <span class="product-name">{{ selectedProduct.name }}</span>
              </div>
              <div class="stock-grid">
                <div class="stock-item">
                  <span class="stock-label">Location</span>
                  <span class="stock-value">{{ selectedProduct.location }}</span>
                </div>
                <div class="stock-item">
                  <span class="stock-label">Current Qty</span>
                  <span class="stock-value highlight">{{ selectedProduct.currentQty }}</span>
                </div>
                <div class="stock-item">
                  <span class="stock-label">Lot No</span>
                  <span class="stock-value">{{ selectedProduct.lotNo || '—' }}</span>
                </div>
                <div class="stock-item">
                  <span class="stock-label">UOM</span>
                  <span class="stock-value">{{ selectedProduct.uom }}</span>
                </div>
              </div>
              <button pButton label="Next: Enter Adjustment" icon="pi pi-arrow-right" iconPos="right"
                      class="p-button-sm mt-3" (click)="activeStep = 1"></button>
            </div>
          }
        </div>
      }

      <!-- Step 2: Enter Adjustment -->
      @if (activeStep === 1 && selectedProduct) {
        <div class="wms-card">
          <h3 class="section-title">Adjustment Details</h3>
          <div class="form-grid">
            <div class="form-field">
              <label class="form-label">Adjustment Type</label>
              <p-dropdown [options]="adjustmentTypes" [(ngModel)]="adjustmentType" placeholder="Select type" styleClass="w-full"></p-dropdown>
            </div>
            <div class="form-field">
              <label class="form-label">Quantity</label>
              <p-inputNumber [(ngModel)]="adjustmentQty" [showButtons]="true" [min]="1" styleClass="w-full"></p-inputNumber>
            </div>
            <div class="form-field span-2">
              <label class="form-label">Reason</label>
              <p-dropdown [options]="reasons" [(ngModel)]="adjustmentReason" placeholder="Select reason" styleClass="w-full"></p-dropdown>
            </div>
            <div class="form-field span-2">
              <label class="form-label">Remarks</label>
              <textarea pInputTextarea [(ngModel)]="remarks" rows="3" class="w-full" placeholder="Additional notes..."></textarea>
            </div>
          </div>

          <!-- Preview -->
          <div class="preview-card">
            <div class="preview-row">
              <span>Current Qty</span><span class="font-bold">{{ selectedProduct.currentQty }}</span>
            </div>
            <div class="preview-row">
              <span>Adjustment</span>
              <span [class]="adjustmentType === 'increase' ? 'text-accent font-bold' : 'text-danger font-bold'">
                {{ adjustmentType === 'increase' ? '+' : '-' }}{{ adjustmentQty }}
              </span>
            </div>
            <div class="preview-divider"></div>
            <div class="preview-row">
              <span class="font-bold">New Qty</span>
              <span class="preview-result">{{ adjustmentType === 'increase' ? selectedProduct.currentQty + adjustmentQty : selectedProduct.currentQty - adjustmentQty }}</span>
            </div>
          </div>

          <div class="button-row mt-3">
            <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined" (click)="activeStep = 0"></button>
            <button pButton label="Next: Confirm" icon="pi pi-arrow-right" iconPos="right" class="p-button-sm"
                    [disabled]="!adjustmentType || adjustmentQty < 1 || !adjustmentReason" (click)="activeStep = 2"></button>
          </div>
        </div>
      }

      <!-- Step 3: Confirm -->
      @if (activeStep === 2 && selectedProduct) {
        <div class="wms-card">
          <h3 class="section-title">Confirm Adjustment</h3>
          <div class="confirm-summary">
            <div class="confirm-item"><span class="confirm-label">Product</span><span>{{ selectedProduct.code }} — {{ selectedProduct.name }}</span></div>
            <div class="confirm-item"><span class="confirm-label">Location</span><span>{{ selectedProduct.location }}</span></div>
            <div class="confirm-item"><span class="confirm-label">Type</span><span class="capitalize">{{ adjustmentType }}</span></div>
            <div class="confirm-item"><span class="confirm-label">Quantity</span><span>{{ adjustmentType === 'increase' ? '+' : '-' }}{{ adjustmentQty }}</span></div>
            <div class="confirm-item"><span class="confirm-label">Reason</span><span>{{ adjustmentReason }}</span></div>
            @if (remarks) {
              <div class="confirm-item"><span class="confirm-label">Remarks</span><span>{{ remarks }}</span></div>
            }
          </div>
          <div class="button-row mt-3">
            <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined" (click)="activeStep = 1"></button>
            <button pButton label="Submit Adjustment" icon="pi pi-check" class="p-button-sm p-button-success" (click)="submitAdjustment()"></button>
          </div>
        </div>
      }

      <!-- Recent Adjustments -->
      <div class="wms-card">
        <h3 class="section-title">Recent Adjustments</h3>
        <p-table [value]="recentAdjustments" styleClass="p-datatable-sm" [rows]="5">
          <ng-template pTemplate="header">
            <tr>
              <th>Date</th><th>Product</th><th>Location</th><th>Type</th><th>Qty</th><th>Reason</th><th>By</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-r>
            <tr>
              <td>{{ r.date }}</td>
              <td><span class="product-code">{{ r.product }}</span></td>
              <td>{{ r.location }}</td>
              <td><span [class]="r.type === 'Increase' ? 'text-accent' : 'text-danger'">{{ r.type }}</span></td>
              <td class="font-bold">{{ r.type === 'Increase' ? '+' : '-' }}{{ r.qty }}</td>
              <td>{{ r.reason }}</td>
              <td>{{ r.user }}</td>
            </tr>
          </ng-template>
        </p-table>
      </div>
    </div>
  `,
  styles: [`
    .adjustment-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .product-detail-card { margin-top: 16px; padding: 16px; border: 1px solid #e5e7eb; border-radius: 10px; background: #fafbff; }
    .product-header { display: flex; gap: 10px; align-items: center; margin-bottom: 12px; }
    .product-code { font-family: monospace; font-weight: 700; color: #1A005D; font-size: 14px; }
    .product-name { color: #374151; }
    .stock-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }
    .stock-item { display: flex; flex-direction: column; gap: 2px; }
    .stock-label { font-size: 11px; color: #6b7280; }
    .stock-value { font-size: 14px; font-weight: 600; color: #111827; }
    .stock-value.highlight { color: #1A005D; font-size: 18px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field.span-2 { grid-column: span 2; }
    .form-label { font-size: 12px; font-weight: 600; color: #374151; }
    .w-full { width: 100%; }
    .preview-card { margin-top: 16px; padding: 16px; background: #f9fafb; border-radius: 10px; border: 1px solid #e5e7eb; }
    .preview-row { display: flex; justify-content: space-between; padding: 6px 0; font-size: 14px; }
    .preview-divider { height: 1px; background: #e5e7eb; margin: 6px 0; }
    .preview-result { font-size: 20px; font-weight: 800; color: #1A005D; }
    .font-bold { font-weight: 700; }
    .text-accent { color: #8EC400; }
    .text-danger { color: #FF585D; }
    .capitalize { text-transform: capitalize; }
    .button-row { display: flex; justify-content: space-between; }
    .mt-3 { margin-top: 12px; }
    .confirm-summary { display: flex; flex-direction: column; gap: 10px; }
    .confirm-item { display: flex; gap: 16px; font-size: 14px; }
    .confirm-label { font-weight: 600; color: #6b7280; min-width: 100px; }
    @media (max-width: 768px) {
      .form-grid { grid-template-columns: 1fr; }
      .form-field.span-2 { grid-column: span 1; }
      .stock-grid { grid-template-columns: repeat(2, 1fr); }
    }
  `]
})
export class InventoryAdjustmentComponent {
  activeStep = 0;
  steps = [
    { label: 'Select Product' },
    { label: 'Enter Adjustment' },
    { label: 'Confirm' }
  ];

  selectedProduct: any = null;
  adjustmentType: string | null = null;
  adjustmentQty = 1;
  adjustmentReason: string | null = null;
  remarks = '';

  adjustmentTypes = [
    { label: 'Increase (Add Stock)', value: 'increase' },
    { label: 'Decrease (Remove Stock)', value: 'decrease' }
  ];

  reasons = [
    { label: 'Physical Count Discrepancy', value: 'Physical Count Discrepancy' },
    { label: 'Damaged Goods', value: 'Damaged Goods' },
    { label: 'Returned Goods', value: 'Returned Goods' },
    { label: 'Quality Issue', value: 'Quality Issue' },
    { label: 'System Error Correction', value: 'System Error Correction' },
    { label: 'Other', value: 'Other' }
  ];

  recentAdjustments = [
    { date: '2024-06-15 14:32', product: 'SKU-00123', location: 'A-01-02-03', type: 'Decrease', qty: 5, reason: 'Damaged Goods', user: 'T. Tanaka' },
    { date: '2024-06-15 11:15', product: 'SKU-00789', location: 'C-02-04-01', type: 'Increase', qty: 100, reason: 'Returned Goods', user: 'S. Suzuki' },
    { date: '2024-06-14 16:48', product: 'SKU-01618', location: 'A-02-03-02', type: 'Decrease', qty: 20, reason: 'Physical Count Discrepancy', user: 'T. Tanaka' },
  ];

  onScanProduct(barcode: string): void {
    this.selectedProduct = {
      code: barcode || 'SKU-00123',
      name: 'Organic Cotton T-Shirt M',
      location: 'A-01-02-03',
      currentQty: 500,
      lotNo: 'L20240115',
      uom: 'PCS'
    };
  }

  submitAdjustment(): void {
    console.log('Submit adjustment:', this.selectedProduct, this.adjustmentType, this.adjustmentQty, this.adjustmentReason, this.remarks);
    this.activeStep = 0;
    this.selectedProduct = null;
    this.adjustmentType = null;
    this.adjustmentQty = 1;
    this.adjustmentReason = null;
    this.remarks = '';
  }
}
