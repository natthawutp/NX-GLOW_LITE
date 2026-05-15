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
import { InputTextareaModule } from 'primeng/inputtextarea';
import { TooltipModule } from 'primeng/tooltip';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';

@Component({
  selector: 'wms-master-customers',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, DropdownModule,
            TableModule, TagModule, DialogModule, InputTextareaModule, TooltipModule, PageHeaderComponent],
  template: `
    <wms-page-header title="master.customers_title" subtitle="master.customers_subtitle" icon="pi pi-users">
      <button pButton label="Add Customer" icon="pi pi-plus" class="p-button-sm" (click)="openDialog()"></button>
    </wms-page-header>

    <!-- Filters -->
    <div class="wms-card mb-3">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Customer Code</label>
          <input pInputText [(ngModel)]="filterCode" placeholder="Search..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Customer Name</label>
          <input pInputText [(ngModel)]="filterName" placeholder="Search..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Type</label>
          <p-dropdown [options]="customerTypes" [(ngModel)]="filterType" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-actions">
          <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="wms-card">
      <p-table [value]="customers" [paginator]="true" [rows]="15" [rowsPerPageOptions]="[10,15,30]"
               styleClass="p-datatable-sm p-datatable-gridlines" [scrollable]="true" scrollHeight="480px" sortMode="multiple">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="code" style="width:120px">Code <p-sortIcon field="code"></p-sortIcon></th>
            <th pSortableColumn="name">Name <p-sortIcon field="name"></p-sortIcon></th>
            <th style="width:100px">Type</th>
            <th>Contact</th>
            <th style="width:130px">Phone</th>
            <th>Email</th>
            <th style="width:80px">Status</th>
            <th style="width:80px">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-c>
          <tr>
            <td><span class="mono-code">{{ c.code }}</span></td>
            <td><span class="font-semibold">{{ c.name }}</span></td>
            <td><p-tag [value]="c.type" [severity]="c.type === 'Shipper' ? 'info' : 'warning'" [rounded]="true"></p-tag></td>
            <td>{{ c.contact }}</td>
            <td>{{ c.phone }}</td>
            <td><a [href]="'mailto:' + c.email" class="email-link">{{ c.email }}</a></td>
            <td><p-tag [value]="c.status" [severity]="c.status === 'Active' ? 'success' : 'danger'" [rounded]="true"></p-tag></td>
            <td>
              <button pButton icon="pi pi-pencil" class="p-button-text p-button-sm p-button-rounded" pTooltip="Edit" (click)="editCustomer(c)"></button>
              <button pButton icon="pi pi-trash" class="p-button-text p-button-sm p-button-rounded p-button-danger" pTooltip="Delete"></button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog -->
    <p-dialog [(visible)]="showDialog" [header]="editMode ? 'Edit Customer' : 'Add Customer'" [modal]="true"
              [style]="{ width: '600px' }" [draggable]="false">
      <div class="dialog-form">
        <div class="form-row">
          <div class="form-field"><label>Customer Code</label><input pInputText [(ngModel)]="form.code" class="w-full" [disabled]="editMode" /></div>
          <div class="form-field"><label>Customer Name</label><input pInputText [(ngModel)]="form.name" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Type</label><p-dropdown [options]="customerTypes" [(ngModel)]="form.type" styleClass="w-full"></p-dropdown></div>
          <div class="form-field"><label>Contact Person</label><input pInputText [(ngModel)]="form.contact" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Phone</label><input pInputText [(ngModel)]="form.phone" class="w-full" /></div>
          <div class="form-field"><label>Email</label><input pInputText [(ngModel)]="form.email" class="w-full" /></div>
        </div>
        <div class="form-field"><label>Address</label><textarea pInputTextarea [(ngModel)]="form.address" rows="3" class="w-full"></textarea></div>
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancel" class="p-button-text" (click)="showDialog = false"></button>
        <button pButton label="Save" icon="pi pi-check" (click)="saveCustomer()"></button>
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
    .font-semibold { font-weight: 600; }
    .email-link { color: #5BC2E7; text-decoration: none; font-size: 13px; }
    .email-link:hover { text-decoration: underline; }
    .dialog-form { display: flex; flex-direction: column; gap: 14px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field label { font-size: 12px; font-weight: 600; color: #374151; }
  `]
})
export class MasterCustomersComponent implements OnInit {
  filterCode = '';
  filterName = '';
  filterType: string | null = null;
  showDialog = false;
  editMode = false;
  form: any = {};

  customerTypes = [{ label: 'Shipper', value: 'Shipper' }, { label: 'Consignee', value: 'Consignee' }];

  customers: any[] = [];

  ngOnInit(): void {
    this.customers = [
      { code: 'C-MUJI01', name: 'MUJI Retail Co., Ltd.', type: 'Shipper', contact: 'Tanaka Yuki', phone: '03-1234-5678', email: 'tanaka@muji.co.jp', status: 'Active' },
      { code: 'C-MUJI02', name: 'MUJI Shibuya Store', type: 'Consignee', contact: 'Sato Kenji', phone: '03-2345-6789', email: 'sato@muji.co.jp', status: 'Active' },
      { code: 'C-MUJI03', name: 'MUJI Ginza Flagship', type: 'Consignee', contact: 'Yamada Haruto', phone: '03-3456-7890', email: 'yamada@muji.co.jp', status: 'Active' },
      { code: 'C-GGA101', name: 'GGA1 Distribution', type: 'Shipper', contact: 'Watanabe Aoi', phone: '06-4567-8901', email: 'watanabe@gga1.co.jp', status: 'Active' },
      { code: 'C-GGA102', name: 'GGA1 Osaka Outlet', type: 'Consignee', contact: 'Ito Rin', phone: '06-5678-9012', email: 'ito@gga1.co.jp', status: 'Inactive' },
    ];
  }

  search(): void { console.log('Search customers'); }
  openDialog(): void { this.editMode = false; this.form = {}; this.showDialog = true; }
  editCustomer(c: any): void { this.editMode = true; this.form = { ...c }; this.showDialog = true; }
  saveCustomer(): void { console.log('Save:', this.form); this.showDialog = false; }
}
