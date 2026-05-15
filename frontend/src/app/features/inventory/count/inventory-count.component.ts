import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { ProgressBarModule } from 'primeng/progressbar';
import { TagModule } from 'primeng/tag';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';

@Component({
  selector: 'wms-inventory-count',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, InputNumberModule,
            DropdownModule, TableModule, ProgressBarModule, TagModule, PageHeaderComponent, BarcodeInputComponent],
  template: `
    <wms-page-header title="inventory.count_title" subtitle="inventory.count_subtitle" icon="pi pi-calculator">
      <button pButton label="New Count Plan" icon="pi pi-plus" class="p-button-sm" (click)="newCountPlan()"></button>
    </wms-page-header>

    <div class="count-layout">
      <!-- Active Count Plan -->
      @if (activePlan) {
        <div class="wms-card plan-card">
          <div class="plan-header">
            <div>
              <h3 class="plan-id">{{ activePlan.id }}</h3>
              <span class="plan-zone">Zone {{ activePlan.zone }} — {{ activePlan.totalLocations }} locations</span>
            </div>
            <p-tag [value]="activePlan.status" severity="info" [rounded]="true"></p-tag>
          </div>
          <p-progressBar [value]="countProgress" [showValue]="true" styleClass="mt-2"></p-progressBar>
          <div class="plan-stats">
            <div class="plan-stat"><span class="plan-stat-value text-accent">{{ countedLocations }}</span><span class="plan-stat-label">Counted</span></div>
            <div class="plan-stat"><span class="plan-stat-value">{{ activePlan.totalLocations - countedLocations }}</span><span class="plan-stat-label">Remaining</span></div>
            <div class="plan-stat"><span class="plan-stat-value text-danger">{{ discrepancies }}</span><span class="plan-stat-label">Discrepancies</span></div>
          </div>
        </div>

        <!-- Scan Location -->
        <div class="wms-card">
          <h3 class="section-title">Scan Location</h3>
          <wms-barcode-input placeholder="Scan location barcode..." (scan)="onScanLocation($event)"></wms-barcode-input>
        </div>

        @if (currentLocation) {
          <!-- Items to Count -->
          <div class="wms-card">
            <div class="location-header">
              <div class="location-badge">
                <i class="pi pi-map-marker" style="color:#1A005D"></i>
                <span class="location-code-large">{{ currentLocation.code }}</span>
              </div>
              <span class="items-count">{{ currentLocation.items.length }} items</span>
            </div>
            <div class="count-items">
              @for (item of currentLocation.items; track item.sku) {
                <div class="count-item" [class.counted]="item.counted" [class.discrepancy]="item.counted && item.actualQty !== item.expectedQty">
                  <div class="count-item-info">
                    <span class="count-item-sku">{{ item.sku }}</span>
                    <span class="count-item-name">{{ item.name }}</span>
                  </div>
                  <div class="count-item-qty">
                    <div class="expected-qty">
                      <span class="qty-label">Expected</span>
                      <span class="qty-value">{{ item.expectedQty }}</span>
                    </div>
                    <div class="actual-qty-input">
                      <span class="qty-label">Actual</span>
                      @if (!item.counted) {
                        <p-inputNumber [(ngModel)]="item.actualQty" [showButtons]="true" [min]="0" size="3"
                                       (onBlur)="item.counted = true; checkDiscrepancy(item)"></p-inputNumber>
                      } @else {
                        <span class="qty-value" [class.text-accent]="item.actualQty === item.expectedQty"
                              [class.text-danger]="item.actualQty !== item.expectedQty">
                          {{ item.actualQty }}
                          @if (item.actualQty !== item.expectedQty) {
                            <i class="pi pi-exclamation-triangle" style="font-size:12px;margin-left:4px"></i>
                          } @else {
                            <i class="pi pi-check" style="font-size:12px;margin-left:4px"></i>
                          }
                        </span>
                      }
                    </div>
                  </div>
                </div>
              }
            </div>
            <button pButton label="Complete Location" icon="pi pi-check" class="p-button-sm w-full mt-3"
                    [disabled]="!allItemsCounted()" (click)="completeLocation()"></button>
          </div>
        }
      } @else {
        <!-- Count Plans List -->
        <div class="wms-card">
          <h3 class="section-title">Count Plans</h3>
          <p-table [value]="countPlans" styleClass="p-datatable-sm p-datatable-gridlines">
            <ng-template pTemplate="header">
              <tr>
                <th>Plan ID</th><th>Zone</th><th>Locations</th><th>Status</th><th>Progress</th><th>Date</th><th></th>
              </tr>
            </ng-template>
            <ng-template pTemplate="body" let-plan>
              <tr>
                <td class="font-bold text-primary">{{ plan.id }}</td>
                <td>{{ plan.zone }}</td>
                <td>{{ plan.totalLocations }}</td>
                <td><p-tag [value]="plan.status" [severity]="getPlanSeverity(plan.status)" [rounded]="true"></p-tag></td>
                <td><p-progressBar [value]="plan.progress" [showValue]="true" styleClass="thin-bar"></p-progressBar></td>
                <td>{{ plan.date }}</td>
                <td>
                  <button pButton icon="pi pi-play" class="p-button-text p-button-sm p-button-rounded"
                          [disabled]="plan.status === 'Completed'" (click)="startPlan(plan)"></button>
                </td>
              </tr>
            </ng-template>
          </p-table>
        </div>
      }
    </div>
  `,
  styles: [`
    .count-layout { display: flex; flex-direction: column; gap: 16px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .plan-card { border-left: 4px solid #1A005D; }
    .plan-header { display: flex; justify-content: space-between; align-items: flex-start; }
    .plan-id { font-size: 18px; font-weight: 800; color: #1A005D; margin: 0; }
    .plan-zone { font-size: 13px; color: #6b7280; }
    .mt-2 { margin-top: 8px; }
    .plan-stats { display: flex; gap: 32px; margin-top: 12px; }
    .plan-stat { display: flex; flex-direction: column; }
    .plan-stat-value { font-size: 20px; font-weight: 800; color: #111827; }
    .plan-stat-label { font-size: 11px; color: #6b7280; }
    .text-accent { color: #8EC400 !important; }
    .text-danger { color: #FF585D !important; }
    .text-primary { color: #1A005D !important; }
    .location-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .location-badge { display: flex; align-items: center; gap: 8px; }
    .location-code-large { font-family: monospace; font-size: 20px; font-weight: 800; color: #1A005D; }
    .items-count { font-size: 13px; color: #6b7280; }
    .count-items { display: flex; flex-direction: column; gap: 8px; }
    .count-item { display: flex; align-items: center; justify-content: space-between; padding: 14px 16px; border: 1px solid #e5e7eb; border-radius: 10px; transition: all .15s; }
    .count-item.counted { background: #f0fdf4; border-color: #bbf7d0; }
    .count-item.discrepancy { background: #fef2f2; border-color: #fecaca; }
    .count-item-info { display: flex; flex-direction: column; gap: 2px; }
    .count-item-sku { font-family: monospace; font-weight: 700; font-size: 14px; color: #1A005D; }
    .count-item-name { font-size: 12px; color: #6b7280; }
    .count-item-qty { display: flex; gap: 20px; align-items: center; }
    .expected-qty, .actual-qty-input { display: flex; flex-direction: column; align-items: center; gap: 2px; }
    .qty-label { font-size: 10px; color: #9ca3af; text-transform: uppercase; letter-spacing: .5px; }
    .qty-value { font-size: 16px; font-weight: 700; color: #111827; }
    .font-bold { font-weight: 700; }
    .w-full { width: 100%; }
    .mt-3 { margin-top: 12px; }
    :host ::ng-deep .thin-bar .p-progressbar { height: 8px; }
  `]
})
export class InventoryCountComponent {
  activePlan: any = null;
  currentLocation: any = null;
  countedLocations = 0;
  discrepancies = 0;

  countPlans = [
    { id: 'CP-2024-001', zone: 'A', totalLocations: 48, status: 'In Progress', progress: 35, date: '2024-06-15' },
    { id: 'CP-2024-002', zone: 'B', totalLocations: 32, status: 'Planned', progress: 0, date: '2024-06-16' },
    { id: 'CP-2024-003', zone: 'C', totalLocations: 56, status: 'Completed', progress: 100, date: '2024-06-14' },
    { id: 'CP-2024-004', zone: 'D', totalLocations: 24, status: 'Planned', progress: 0, date: '2024-06-17' },
  ];

  get countProgress(): number {
    return this.activePlan ? Math.round((this.countedLocations / this.activePlan.totalLocations) * 100) : 0;
  }

  startPlan(plan: any): void {
    this.activePlan = plan;
    this.countedLocations = 0;
    this.discrepancies = 0;
    this.currentLocation = null;
  }

  newCountPlan(): void {
    console.log('New count plan');
  }

  onScanLocation(barcode: string): void {
    this.currentLocation = {
      code: barcode || 'A-01-02-03',
      items: [
        { sku: 'SKU-00123', name: 'Organic Cotton T-Shirt M', expectedQty: 500, actualQty: 0, counted: false },
        { sku: 'SKU-00456', name: 'Aroma Diffuser 200ml', expectedQty: 85, actualQty: 0, counted: false },
        { sku: 'SKU-01921', name: 'Ceramic Mug 300ml White', expectedQty: 340, actualQty: 0, counted: false },
      ]
    };
  }

  checkDiscrepancy(item: any): void {
    // just mark counted
  }

  allItemsCounted(): boolean {
    return this.currentLocation?.items?.every((i: any) => i.counted) ?? false;
  }

  completeLocation(): void {
    this.countedLocations++;
    this.currentLocation.items.forEach((item: any) => {
      if (item.actualQty !== item.expectedQty) this.discrepancies++;
    });
    this.currentLocation = null;
  }

  getPlanSeverity(status: string): 'success' | 'info' | 'warning' | 'danger' | 'secondary' | 'contrast' | undefined {
    const map: Record<string, 'success' | 'info' | 'warning' | 'danger' | 'secondary'> = { 'In Progress': 'info', Planned: 'warning', Completed: 'success' };
    return map[status] || 'info';
  }
}
