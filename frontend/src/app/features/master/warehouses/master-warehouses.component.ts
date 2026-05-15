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
  selector: 'wms-master-warehouses',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, DropdownModule,
            TableModule, TagModule, DialogModule, InputNumberModule, InputTextareaModule, TooltipModule, PageHeaderComponent],
  template: `
    <wms-page-header title="master.warehouses_title" subtitle="master.warehouses_subtitle" icon="pi pi-building">
      <button pButton label="Add Warehouse" icon="pi pi-plus" class="p-button-sm" (click)="openDialog()"></button>
    </wms-page-header>

    <!-- Warehouse Cards -->
    <div class="warehouse-grid">
      @for (wh of warehouses; track wh.code) {
        <div class="warehouse-card" [class.inactive]="wh.status !== 'Active'">
          <div class="wh-header">
            <div class="wh-icon" [style.background]="wh.status === 'Active' ? '#ede9fe' : '#f3f4f6'">
              <i class="pi pi-building" [style.color]="wh.status === 'Active' ? '#1A005D' : '#9ca3af'"></i>
            </div>
            <div class="wh-title">
              <span class="wh-code">{{ wh.code }}</span>
              <span class="wh-name">{{ wh.name }}</span>
            </div>
            <p-tag [value]="wh.status" [severity]="wh.status === 'Active' ? 'success' : 'danger'" [rounded]="true"></p-tag>
          </div>

          <div class="wh-stats">
            <div class="wh-stat">
              <span class="wh-stat-label">Zones</span>
              <span class="wh-stat-value">{{ wh.zones }}</span>
            </div>
            <div class="wh-stat">
              <span class="wh-stat-label">Locations</span>
              <span class="wh-stat-value">{{ wh.locations }}</span>
            </div>
            <div class="wh-stat">
              <span class="wh-stat-label">Area (m²)</span>
              <span class="wh-stat-value">{{ wh.area | number }}</span>
            </div>
          </div>

          <div class="wh-utilization">
            <div class="util-header">
              <span class="util-label">Utilization</span>
              <span class="util-value">{{ wh.utilization }}%</span>
            </div>
            <div class="util-bar">
              <div class="util-fill" [style.width.%]="wh.utilization"
                   [style.background]="wh.utilization > 90 ? '#FF585D' : wh.utilization > 70 ? '#FF9E1B' : '#8EC400'"></div>
            </div>
          </div>

          <div class="wh-meta">
            <span><i class="pi pi-map-marker"></i> {{ wh.address }}</span>
            <span><i class="pi pi-phone"></i> {{ wh.phone }}</span>
          </div>

          <div class="wh-actions">
            <button pButton icon="pi pi-pencil" label="Edit" class="p-button-text p-button-sm" (click)="editWarehouse(wh)"></button>
            <button pButton icon="pi pi-eye" label="Details" class="p-button-text p-button-sm" (click)="viewDetails(wh)"></button>
          </div>
        </div>
      }
    </div>

    <!-- Dialog -->
    <p-dialog [(visible)]="showDialog" [header]="editMode ? 'Edit Warehouse' : 'Add Warehouse'" [modal]="true"
              [style]="{ width: '600px' }" [draggable]="false">
      <div class="dialog-form">
        <div class="form-row">
          <div class="form-field"><label>Warehouse Code</label><input pInputText [(ngModel)]="form.code" class="w-full" [disabled]="editMode" /></div>
          <div class="form-field"><label>Warehouse Name</label><input pInputText [(ngModel)]="form.name" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Zones</label><p-inputNumber [(ngModel)]="form.zones" [min]="1" [showButtons]="true" styleClass="w-full"></p-inputNumber></div>
          <div class="form-field"><label>Area (m²)</label><p-inputNumber [(ngModel)]="form.area" [min]="0" styleClass="w-full"></p-inputNumber></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Phone</label><input pInputText [(ngModel)]="form.phone" class="w-full" /></div>
          <div class="form-field"><label>Manager</label><input pInputText [(ngModel)]="form.manager" class="w-full" /></div>
        </div>
        <div class="form-field"><label>Address</label><textarea pInputTextarea [(ngModel)]="form.address" rows="2" class="w-full"></textarea></div>
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancel" class="p-button-text" (click)="showDialog = false"></button>
        <button pButton label="Save" icon="pi pi-check" (click)="saveWarehouse()"></button>
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .warehouse-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(360px, 1fr)); gap: 16px; }
    .warehouse-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; display: flex; flex-direction: column; gap: 16px; transition: all .15s; }
    .warehouse-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,.06); }
    .warehouse-card.inactive { opacity: .6; }
    .wh-header { display: flex; align-items: center; gap: 12px; }
    .wh-icon { width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 20px; }
    .wh-title { flex: 1; display: flex; flex-direction: column; }
    .wh-code { font-family: monospace; font-weight: 700; font-size: 14px; color: #1A005D; }
    .wh-name { font-size: 13px; color: #374151; }
    .wh-stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; }
    .wh-stat { display: flex; flex-direction: column; align-items: center; padding: 10px; background: #f9fafb; border-radius: 8px; }
    .wh-stat-label { font-size: 11px; color: #6b7280; }
    .wh-stat-value { font-size: 18px; font-weight: 800; color: #111827; }
    .wh-utilization { }
    .util-header { display: flex; justify-content: space-between; margin-bottom: 6px; }
    .util-label { font-size: 12px; color: #6b7280; }
    .util-value { font-size: 13px; font-weight: 700; color: #111827; }
    .util-bar { height: 8px; background: #e5e7eb; border-radius: 4px; overflow: hidden; }
    .util-fill { height: 100%; border-radius: 4px; transition: width .3s; }
    .wh-meta { display: flex; flex-direction: column; gap: 4px; font-size: 12px; color: #6b7280; }
    .wh-meta i { margin-right: 6px; width: 14px; }
    .wh-actions { display: flex; gap: 8px; justify-content: flex-end; border-top: 1px solid #f3f4f6; padding-top: 12px; }
    .dialog-form { display: flex; flex-direction: column; gap: 14px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field label { font-size: 12px; font-weight: 600; color: #374151; }
    .w-full { width: 100%; }
  `]
})
export class MasterWarehousesComponent implements OnInit {
  showDialog = false;
  editMode = false;
  form: any = {};
  warehouses: any[] = [];

  ngOnInit(): void {
    this.warehouses = [
      { code: 'WH-TKY01', name: 'Tokyo Central Warehouse', zones: 4, locations: 1248, area: 8500, utilization: 78, address: 'Koto-ku, Tokyo 135-0064', phone: '03-1234-5678', manager: 'Tanaka Hiroshi', status: 'Active' },
      { code: 'WH-OSK01', name: 'Osaka Distribution Center', zones: 3, locations: 864, area: 6200, utilization: 62, address: 'Suminoe-ku, Osaka 559-0031', phone: '06-2345-6789', manager: 'Suzuki Yuma', status: 'Active' },
      { code: 'WH-NGY01', name: 'Nagoya Fulfillment Hub', zones: 2, locations: 512, area: 3800, utilization: 91, address: 'Minato-ku, Nagoya 455-0033', phone: '052-3456-7890', manager: 'Yamamoto Sota', status: 'Active' },
      { code: 'WH-FKO01', name: 'Fukuoka Cold Storage', zones: 1, locations: 128, area: 1200, utilization: 45, address: 'Hakata-ku, Fukuoka 812-0011', phone: '092-4567-8901', manager: 'Nakamura Riku', status: 'Inactive' },
    ];
  }

  openDialog(): void { this.editMode = false; this.form = { zones: 1, area: 0 }; this.showDialog = true; }
  editWarehouse(wh: any): void { this.editMode = true; this.form = { ...wh }; this.showDialog = true; }
  viewDetails(wh: any): void { console.log('View details:', wh.code); }
  saveWarehouse(): void { console.log('Save:', this.form); this.showDialog = false; }
}
