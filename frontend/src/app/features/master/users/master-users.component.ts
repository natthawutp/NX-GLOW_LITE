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
import { MultiSelectModule } from 'primeng/multiselect';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { TooltipModule } from 'primeng/tooltip';
import { AvatarModule } from 'primeng/avatar';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';

@Component({
  selector: 'wms-master-users',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, InputTextModule, DropdownModule,
            TableModule, TagModule, DialogModule, MultiSelectModule, PasswordModule, CheckboxModule,
            TooltipModule, AvatarModule, PageHeaderComponent],
  template: `
    <wms-page-header title="master.users_title" subtitle="master.users_subtitle" icon="pi pi-id-card">
      <button pButton label="Add User" icon="pi pi-user-plus" class="p-button-sm" (click)="openDialog()"></button>
    </wms-page-header>

    <!-- Filters -->
    <div class="wms-card mb-3">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Username</label>
          <input pInputText [(ngModel)]="filterUsername" placeholder="Search..." class="w-full" />
        </div>
        <div class="filter-item">
          <label class="filter-label">Role</label>
          <p-dropdown [options]="roleOptions" [(ngModel)]="filterRole" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-item">
          <label class="filter-label">Warehouse</label>
          <p-dropdown [options]="warehouseOptions" [(ngModel)]="filterWarehouse" placeholder="All" [showClear]="true" styleClass="w-full"></p-dropdown>
        </div>
        <div class="filter-actions">
          <button pButton label="Search" icon="pi pi-search" class="p-button-sm" (click)="search()"></button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="wms-card">
      <p-table [value]="users" [paginator]="true" [rows]="15" [rowsPerPageOptions]="[10,15,30]"
               styleClass="p-datatable-sm p-datatable-gridlines" [scrollable]="true" scrollHeight="480px" sortMode="multiple">
        <ng-template pTemplate="header">
          <tr>
            <th style="width:60px"></th>
            <th pSortableColumn="username" style="width:130px">Username <p-sortIcon field="username"></p-sortIcon></th>
            <th pSortableColumn="fullName">Full Name <p-sortIcon field="fullName"></p-sortIcon></th>
            <th>Email</th>
            <th style="width:120px">Warehouse</th>
            <th>Roles</th>
            <th pSortableColumn="lastLogin" style="width:140px">Last Login <p-sortIcon field="lastLogin"></p-sortIcon></th>
            <th style="width:80px">Status</th>
            <th style="width:90px">Actions</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-u>
          <tr>
            <td>
              <p-avatar [label]="u.initials" [style]="{ background: u.avatarColor, color: 'white' }" shape="circle" size="normal"></p-avatar>
            </td>
            <td><span class="mono-code">{{ u.username }}</span></td>
            <td class="font-semibold">{{ u.fullName }}</td>
            <td><span class="email-text">{{ u.email }}</span></td>
            <td>{{ u.warehouse }}</td>
            <td>
              @for (role of u.roles; track role) {
                <span class="role-chip">{{ role }}</span>
              }
            </td>
            <td>{{ u.lastLogin }}</td>
            <td><p-tag [value]="u.status" [severity]="u.status === 'Active' ? 'success' : u.status === 'Locked' ? 'danger' : 'warning'" [rounded]="true"></p-tag></td>
            <td>
              <button pButton icon="pi pi-pencil" class="p-button-text p-button-sm p-button-rounded" pTooltip="Edit" (click)="editUser(u)"></button>
              <button pButton icon="pi pi-lock" class="p-button-text p-button-sm p-button-rounded"
                      [pTooltip]="u.status === 'Locked' ? 'Unlock' : 'Lock'" (click)="toggleLock(u)"></button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog -->
    <p-dialog [(visible)]="showDialog" [header]="editMode ? 'Edit User' : 'Add User'" [modal]="true"
              [style]="{ width: '600px' }" [draggable]="false">
      <div class="dialog-form">
        <div class="form-row">
          <div class="form-field"><label>Username</label><input pInputText [(ngModel)]="form.username" class="w-full" [disabled]="editMode" /></div>
          <div class="form-field"><label>Full Name</label><input pInputText [(ngModel)]="form.fullName" class="w-full" /></div>
        </div>
        <div class="form-row">
          <div class="form-field"><label>Email</label><input pInputText [(ngModel)]="form.email" class="w-full" type="email" /></div>
          <div class="form-field"><label>Warehouse</label><p-dropdown [options]="warehouseOptions" [(ngModel)]="form.warehouse" styleClass="w-full"></p-dropdown></div>
        </div>
        @if (!editMode) {
          <div class="form-row">
            <div class="form-field"><label>Password</label><p-password [(ngModel)]="form.password" [feedback]="true" styleClass="w-full" inputStyleClass="w-full"></p-password></div>
            <div class="form-field"><label>Confirm Password</label><p-password [(ngModel)]="form.confirmPassword" [feedback]="false" styleClass="w-full" inputStyleClass="w-full"></p-password></div>
          </div>
        }
        <div class="form-field">
          <label>Roles</label>
          <p-multiSelect [options]="roleOptions" [(ngModel)]="form.roles" placeholder="Select roles" styleClass="w-full" display="chip"></p-multiSelect>
        </div>
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancel" class="p-button-text" (click)="showDialog = false"></button>
        <button pButton label="Save" icon="pi pi-check" (click)="saveUser()"></button>
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
    .email-text { font-size: 13px; color: #6b7280; }
    .role-chip { display: inline-block; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 6px;
                 background: #ede9fe; color: #1A005D; margin-right: 4px; margin-bottom: 2px; }
    .dialog-form { display: flex; flex-direction: column; gap: 14px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
    .form-field { display: flex; flex-direction: column; gap: 4px; }
    .form-field label { font-size: 12px; font-weight: 600; color: #374151; }
  `]
})
export class MasterUsersComponent implements OnInit {
  filterUsername = '';
  filterRole: string | null = null;
  filterWarehouse: string | null = null;
  showDialog = false;
  editMode = false;
  form: any = {};

  roleOptions = [
    { label: 'Admin', value: 'ADMIN' }, { label: 'Inbound', value: 'INBOUND' },
    { label: 'Outbound', value: 'OUTBOUND' }, { label: 'Inventory', value: 'INVENTORY' },
    { label: 'Master', value: 'MASTER' }, { label: 'Reports', value: 'REPORTS' },
    { label: 'Operator', value: 'OPERATOR' }
  ];
  warehouseOptions = [
    { label: 'WH-TKY01', value: 'WH-TKY01' }, { label: 'WH-OSK01', value: 'WH-OSK01' },
    { label: 'WH-NGY01', value: 'WH-NGY01' }, { label: 'WH-FKO01', value: 'WH-FKO01' }
  ];

  users: any[] = [];

  ngOnInit(): void {
    const colors = ['#1A005D', '#5BC2E7', '#8EC400', '#FF9E1B', '#FF585D'];
    this.users = [
      { username: 'admin', fullName: 'System Administrator', email: 'admin@gwh.co.jp', warehouse: 'WH-TKY01', roles: ['ADMIN'], lastLogin: '2024-06-15 09:12', status: 'Active', initials: 'SA', avatarColor: colors[0] },
      { username: 't.tanaka', fullName: 'Tanaka Hiroshi', email: 'tanaka@gwh.co.jp', warehouse: 'WH-TKY01', roles: ['INBOUND', 'OUTBOUND'], lastLogin: '2024-06-15 08:45', status: 'Active', initials: 'TH', avatarColor: colors[1] },
      { username: 's.suzuki', fullName: 'Suzuki Yuma', email: 'suzuki@gwh.co.jp', warehouse: 'WH-OSK01', roles: ['INVENTORY', 'REPORTS'], lastLogin: '2024-06-14 17:30', status: 'Active', initials: 'SY', avatarColor: colors[2] },
      { username: 'y.yamamoto', fullName: 'Yamamoto Sota', email: 'yamamoto@gwh.co.jp', warehouse: 'WH-NGY01', roles: ['OPERATOR'], lastLogin: '2024-06-15 07:00', status: 'Active', initials: 'YS', avatarColor: colors[3] },
      { username: 'n.nakamura', fullName: 'Nakamura Riku', email: 'nakamura@gwh.co.jp', warehouse: 'WH-FKO01', roles: ['MASTER'], lastLogin: '2024-06-10 15:22', status: 'Locked', initials: 'NR', avatarColor: colors[4] },
      { username: 'k.kobayashi', fullName: 'Kobayashi Mei', email: 'kobayashi@gwh.co.jp', warehouse: 'WH-TKY01', roles: ['OUTBOUND', 'OPERATOR'], lastLogin: null, status: 'Inactive', initials: 'KM', avatarColor: colors[0] },
    ];
  }

  search(): void { console.log('Search users'); }
  openDialog(): void { this.editMode = false; this.form = { roles: [] }; this.showDialog = true; }
  editUser(u: any): void { this.editMode = true; this.form = { ...u, roles: [...u.roles] }; this.showDialog = true; }
  toggleLock(u: any): void { u.status = u.status === 'Locked' ? 'Active' : 'Locked'; }
  saveUser(): void { console.log('Save:', this.form); this.showDialog = false; }
}
