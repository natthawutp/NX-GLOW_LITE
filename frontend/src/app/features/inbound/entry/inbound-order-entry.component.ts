import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { ApiService } from '@core/services/api.service';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TabViewModule } from 'primeng/tabview';

@Component({
  selector: 'wms-inbound-order-entry',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    PageHeaderComponent,
    ButtonModule,
    CalendarModule,
    InputTextModule,
    InputNumberModule,
    TabViewModule
  ],
  template: `
    <wms-page-header title="New Inbound Order" subtitle="APPRE migration - tab based registration" icon="pi pi-plus-circle">
      <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined" (click)="goBack()"></button>
      <button pButton label="Submit" icon="pi pi-check" class="p-button-sm p-button-success" [disabled]="saving()" (click)="submit()"></button>
    </wms-page-header>

    @if (errorMessage()) {
      <div class="message error"><i class="pi pi-times-circle"></i> {{ errorMessage() }}</div>
    }
    @if (successMessage()) {
      <div class="message success"><i class="pi pi-check-circle"></i> {{ successMessage() }}</div>
    }

    <form [formGroup]="form" class="entry-form">
      <p-tabView>

        <!-- ── Header Tab ──────────────────────────────────────── -->
        <p-tabPanel header="Header" leftIcon="pi pi-info-circle">
          <table class="form-table">
            <tbody>
              <tr>
                <th>Scheduled Date <span class="req">*</span></th>
                <td><p-calendar formControlName="scheduledDate" dateFormat="yy-mm-dd" [showIcon]="true" [style]="{ width: '100%' }"></p-calendar></td>
                <th>Transaction Kind</th>
                <td><input pInputText formControlName="transactionKind" class="w-full"></td>
              </tr>
              <tr>
                <th>Supplier Code <span class="req">*</span></th>
                <td><input pInputText formControlName="supplierCode" class="w-full"></td>
                <th>Supplier Name</th>
                <td><input pInputText formControlName="supplierName" class="w-full"></td>
              </tr>
              <tr>
                <th>PO Number</th>
                <td><input pInputText formControlName="poNumber" class="w-full"></td>
                <th>Reference Number</th>
                <td><input pInputText formControlName="referenceNumber" class="w-full"></td>
              </tr>
              <tr>
                <th>Status</th>
                <td><input pInputText formControlName="arrivalStatus" class="w-full"></td>
                <th>Weight</th>
                <td><p-inputNumber formControlName="weight" mode="decimal" [minFractionDigits]="0" [maxFractionDigits]="3" [style]="{ width: '100%' }"></p-inputNumber></td>
              </tr>
              <tr>
                <th>Volume (M3)</th>
                <td><p-inputNumber formControlName="volumeM3" mode="decimal" [minFractionDigits]="0" [maxFractionDigits]="3" [style]="{ width: '100%' }"></p-inputNumber></td>
                <th>Remarks</th>
                <td><input pInputText formControlName="remarks" class="w-full"></td>
              </tr>
            </tbody>
          </table>
        </p-tabPanel>

        <!-- ── Detail Lines Tab ───────────────────────────────── -->
        <p-tabPanel header="Detail Lines" leftIcon="pi pi-list">
          <div class="lines-toolbar">
            <button pButton type="button" label="Add Line" icon="pi pi-plus" class="p-button-sm p-button-outlined" (click)="addLine()"></button>
            <span class="line-count">{{ lineControls.length }} line(s)</span>
          </div>
          <div class="table-scroll">
            <table class="lines-table">
              <thead>
                <tr>
                  <th class="col-no">#</th>
                  <th class="col-prod">Product Code <span class="req">*</span></th>
                  <th class="col-md">Product Name</th>
                  <th class="col-sm">Origin</th>
                  <th class="col-sm">Shipper Code</th>
                  <th class="col-md">Shipper Name</th>
                  <th class="col-num">PCS Qty</th>
                  <th class="col-num">CS Qty</th>
                  <th class="col-num">Weight</th>
                  <th class="col-num">M3</th>
                  <th class="col-sm">Sub Inv</th>
                  <th class="col-pik">PIK1</th>
                  <th class="col-pik">PIK2</th>
                  <th class="col-pik">PIK3</th>
                  <th class="col-pik">PIK4</th>
                  <th class="col-pik">PIK5</th>
                  <th class="col-pik">PIK6</th>
                  <th class="col-pik">PIK7</th>
                  <th class="col-rmk">Remarks</th>
                  <th class="col-act"></th>
                </tr>
              </thead>
              <tbody formArrayName="lines">
                @for (lineCtrl of lineControls.controls; track idx; let idx = $index) {
                  <tr [formGroupName]="idx">
                    <td class="td-no">{{ idx + 1 }}</td>
                    <td><input pInputText formControlName="productCode" class="tbl-input"></td>
                    <td><input pInputText formControlName="productName" class="tbl-input"></td>
                    <td><input pInputText formControlName="originCode" class="tbl-input"></td>
                    <td><input pInputText formControlName="shipperCode" class="tbl-input"></td>
                    <td><input pInputText formControlName="shipperName" class="tbl-input"></td>
                    <td><p-inputNumber formControlName="plannedPieceQuantity" [style]="{ width: '100%' }" [inputStyle]="{ height: '28px', fontSize: '12px', padding: '2px 6px' }"></p-inputNumber></td>
                    <td><p-inputNumber formControlName="plannedCaseQuantity" [style]="{ width: '100%' }" [inputStyle]="{ height: '28px', fontSize: '12px', padding: '2px 6px' }"></p-inputNumber></td>
                    <td><p-inputNumber formControlName="weight" mode="decimal" [maxFractionDigits]="3" [style]="{ width: '100%' }" [inputStyle]="{ height: '28px', fontSize: '12px', padding: '2px 6px' }"></p-inputNumber></td>
                    <td><p-inputNumber formControlName="volumeM3" mode="decimal" [maxFractionDigits]="3" [style]="{ width: '100%' }" [inputStyle]="{ height: '28px', fontSize: '12px', padding: '2px 6px' }"></p-inputNumber></td>
                    <td><input pInputText formControlName="subInventoryCode" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik1" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik2" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik3" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik4" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik5" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik6" class="tbl-input"></td>
                    <td><input pInputText formControlName="pik7" class="tbl-input"></td>
                    <td><input pInputText formControlName="remarks" class="tbl-input"></td>
                    <td class="td-act">
                      <button pButton type="button" icon="pi pi-trash" class="p-button-text p-button-danger p-button-sm" (click)="removeLine(idx)" [disabled]="lineControls.length <= 1"></button>
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        </p-tabPanel>

        <!-- ── Advanced Fields Tab ────────────────────────────── -->
        <p-tabPanel header="Advanced Fields" leftIcon="pi pi-cog">
          <table class="form-table">
            <tbody>
              <tr>
                <th>Header CLI <small>(comma separated, up to 15)</small></th>
                <td colspan="3"><input pInputText formControlName="headerCliRaw" class="w-full"></td>
              </tr>
              <tr>
                <th>Header CFG Flags <small>(comma separated, up to 15)</small></th>
                <td colspan="3"><input pInputText formControlName="headerCfgRaw" class="w-full"></td>
              </tr>
              <tr>
                <th>Header CNI Numbers <small>(comma separated, up to 15)</small></th>
                <td colspan="3"><input pInputText formControlName="headerCniRaw" class="w-full"></td>
              </tr>
            </tbody>
          </table>
        </p-tabPanel>

        <!-- ── Review Tab ─────────────────────────────────────── -->
        <p-tabPanel header="Review" leftIcon="pi pi-check-square">
          <table class="form-table review-table">
            <tbody>
              <tr>
                <th>Supplier Code</th>
                <td>{{ form.value.supplierCode || '—' }}</td>
                <th>Supplier Name</th>
                <td>{{ form.value.supplierName || '—' }}</td>
              </tr>
              <tr>
                <th>Scheduled Date</th>
                <td>{{ formatDate(form.value.scheduledDate) }}</td>
                <th>Status</th>
                <td>{{ form.value.arrivalStatus || '100' }}</td>
              </tr>
              <tr>
                <th>PO Number</th>
                <td>{{ form.value.poNumber || '—' }}</td>
                <th>Reference Number</th>
                <td>{{ form.value.referenceNumber || '—' }}</td>
              </tr>
              <tr>
                <th>Total Lines</th>
                <td><strong>{{ lineControls.length }}</strong></td>
                <th>Weight / M3</th>
                <td>{{ form.value.weight || 0 }} / {{ form.value.volumeM3 || 0 }}</td>
              </tr>
              <tr>
                <th>Remarks</th>
                <td colspan="3">{{ form.value.remarks || '—' }}</td>
              </tr>
            </tbody>
          </table>
        </p-tabPanel>

      </p-tabView>
    </form>
  `,
  styles: [`
    .entry-form { display: flex; flex-direction: column; gap: 14px; }

    /* ── Messages ─────────────────────────────────────────────── */
    .message { display: flex; align-items: center; gap: 8px; border-radius: 6px; padding: 8px 14px; margin-bottom: 10px; font-size: 13px; }
    .message.error   { background: #fff0f0; color: #c0392b; border: 1px solid #e74c3c; }
    .message.success { background: #f0fff4; color: #27ae60; border: 1px solid #2ecc71; }
    .req { color: #e74c3c; margin-left: 2px; }

    /* ── Shared form table (Header / Advanced / Review tabs) ──── */
    .form-table { width: 100%; border-collapse: collapse; font-size: 13px; }
    .form-table th,
    .form-table td { padding: 7px 10px; border: 1px solid var(--surface-border); vertical-align: middle; }
    .form-table th { width: 180px; background: var(--surface-ground); font-weight: 600; color: var(--text-color); white-space: nowrap; }
    .form-table th small { font-weight: 400; font-size: 11px; color: var(--text-color-secondary); margin-left: 4px; }
    .form-table td { background: var(--surface-card); }
    .w-full { width: 100%; }

    /* ── Detail Lines table ───────────────────────────────────── */
    .lines-toolbar { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
    .line-count { font-size: 12px; color: var(--text-color-secondary); }
    .table-scroll {
      overflow-x: auto;
      border: 1px solid var(--surface-border);
      border-radius: 6px;
      padding-bottom: 4px;
    }
    .table-scroll::-webkit-scrollbar {
      height: 10px;
    }
    .table-scroll::-webkit-scrollbar-track {
      background: var(--surface-ground);
      border-radius: 0 0 6px 6px;
      margin: 0 2px;
    }
    .table-scroll::-webkit-scrollbar-thumb {
      background: rgba(0, 0, 0, 0.25);
      border-radius: 10px;
      border: 2px solid var(--surface-ground);
    }
    .table-scroll::-webkit-scrollbar-thumb:hover {
      background: rgba(0, 0, 0, 0.45);
    }
    .lines-table { width: max-content; min-width: 100%; border-collapse: collapse; font-size: 12px; }
    .lines-table thead tr { background: var(--surface-ground); position: sticky; top: 0; z-index: 1; }
    .lines-table th {
      padding: 7px 8px;
      text-align: left;
      font-size: 11px;
      font-weight: 700;
      white-space: nowrap;
      border-bottom: 2px solid var(--surface-border);
      border-right: 1px solid var(--surface-border);
      color: var(--text-color);
    }
    .lines-table td {
      padding: 2px 3px;
      border-bottom: 1px solid var(--surface-border);
      border-right: 1px solid var(--surface-border);
      vertical-align: middle;
      background: var(--surface-card);
    }
    .lines-table tbody tr:hover td { background: var(--surface-hover); }
    .tbl-input {
      width: 100%;
      height: 28px;
      font-size: 12px;
      padding: 2px 6px !important;
      border-radius: 4px !important;
      border: 1px solid var(--surface-border) !important;
      background: transparent !important;
    }
    .tbl-input:focus { outline: none; border-color: var(--primary-color) !important; box-shadow: 0 0 0 2px color-mix(in srgb, var(--primary-color) 20%, transparent) !important; }
    .col-no  { width: 36px; }
    .col-prod { min-width: 110px; }
    .col-sm  { min-width: 90px; }
    .col-md  { min-width: 120px; }
    .col-num { min-width: 84px; }
    .col-pik { min-width: 72px; }
    .col-rmk { min-width: 130px; }
    .col-act { width: 38px; }
    .td-no   { text-align: center; font-weight: 600; color: var(--text-color-secondary); font-size: 11px; }
    .td-act  { text-align: center; }
  `]
})
export class InboundOrderEntryComponent {
  saving = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  form = this.fb.group({
    scheduledDate: [new Date(), Validators.required],
    transactionKind: ['001'],
    arrivalStatus: ['100'],
    supplierCode: ['', Validators.required],
    supplierName: [''],
    poNumber: [''],
    referenceNumber: [''],
    remarks: [''],
    weight: [null as number | null],
    volumeM3: [null as number | null],
    headerCliRaw: [''],
    headerCfgRaw: [''],
    headerCniRaw: [''],
    lines: this.fb.array([this.createLineGroup()])
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly api: ApiService,
    private readonly router: Router
  ) {}

  get lineControls(): FormArray<FormGroup> {
    return this.form.get('lines') as FormArray<FormGroup>;
  }

  addLine(): void {
    this.lineControls.push(this.createLineGroup());
  }

  removeLine(index: number): void {
    if (this.lineControls.length <= 1) {
      return;
    }
    this.lineControls.removeAt(index);
  }

  submit(): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage.set('Please complete required fields before submit.');
      return;
    }

    const payload = this.buildPayload();
    this.saving.set(true);
    this.api.post<any>('/inbound/orders', payload)
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (response) => {
          if (response.status === 'SUCCESS') {
            const arrivalNo = response.data?.AVH_AV_NUM || '';
            this.successMessage.set(`Inbound order ${arrivalNo} registered successfully.`);
            this.router.navigate(['/inbound/orders']);
            return;
          }
          this.errorMessage.set(response.messages?.[0]?.message || 'Registration failed.');
          if (response.messages && response.messages.length > 0) {
            this.errorMessage.set(response.messages.map(item => `${item.code}: ${item.message}`).join(' | '));
          }
        },
        error: (error) => {
          const errorMessages = error?.error?.messages;
          if (errorMessages && errorMessages.length > 0) {
            this.errorMessage.set(errorMessages.map((item: any) => `${item.code}: ${item.message}`).join(' | '));
            return;
          }
          this.errorMessage.set('Registration failed.');
        }
      });
  }

  goBack(): void {
    this.router.navigate(['/inbound/orders']);
  }

  formatDate(value: Date | string | null | undefined): string {
    if (!value) {
      return '-';
    }
    if (value instanceof Date) {
      const year = value.getFullYear();
      const month = String(value.getMonth() + 1).padStart(2, '0');
      const day = String(value.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
    return value.toString();
  }

  private createLineGroup(): FormGroup {
    return this.fb.group({
      productCode: ['', Validators.required],
      productName: [''],
      originCode: [''],
      shipperCode: [''],
      shipperName: [''],
      plannedPieceQuantity: [null as number | null],
      plannedCaseQuantity: [null as number | null],
      weight: [null as number | null],
      volumeM3: [null as number | null],
      subInventoryCode: [''],
      pik1: [''],
      pik2: [''],
      pik3: [''],
      pik4: [''],
      pik5: [''],
      pik6: [''],
      pik7: [''],
      remarks: ['']
    });
  }

  private buildPayload(): any {
    const value = this.form.value;
    return {
      header: {
        AVH_SCDL_YMD: this.formatDate(value.scheduledDate as Date),
        AVH_TRN_KND: value.transactionKind,
        AVH_AV_STS: value.arrivalStatus,
        AVH_SPL_COD: value.supplierCode,
        AVH_SPL_NAM1: value.supplierName,
        AVH_PO_NUM: value.poNumber,
        AVH_RF_NUM: value.referenceNumber,
        AVH_RMKS: value.remarks,
        AVH_WGT: value.weight,
        AVH_M3: value.volumeM3,
        AVH_CLI: this.parseStringArray(value.headerCliRaw as string),
        AVH_CFG: this.parseStringArray(value.headerCfgRaw as string),
        AVH_CNI: this.parseNumberArray(value.headerCniRaw as string)
      },
      lines: this.lineControls.controls.map((lineCtrl, index) => {
        const lineValue = lineCtrl.value;
        return {
          AVD_AVLN_NUM: index + 1,
          AVD_AV_STS: value.arrivalStatus || '100',
          AVD_INSP_STS: '100',
          AVD_PROD_COD: lineValue.productCode,
          AVD_PROD_NAM: lineValue.productName,
          AVD_ORGN_COD: lineValue.originCode,
          AVD_SPPR_COD: lineValue.shipperCode,
          AVD_SPPR_NAM: lineValue.shipperName,
          AVD_PPCS_QTY: lineValue.plannedPieceQuantity,
          AVD_SCS_QTY: lineValue.plannedCaseQuantity,
          AVD_WGT: lineValue.weight,
          AVD_M3: lineValue.volumeM3,
          AVD_SBIV_COD: lineValue.subInventoryCode,
          AVD_PIK1: lineValue.pik1,
          AVD_PIK2: lineValue.pik2,
          AVD_PIK3: lineValue.pik3,
          AVD_PIK4: lineValue.pik4,
          AVD_PIK5: lineValue.pik5,
          AVD_PIK6: lineValue.pik6,
          AVD_PIK7: lineValue.pik7,
          AVD_RMKS: lineValue.remarks,
          AVD_CLI: [],
          AVD_CFG: [],
          AVD_CNI: []
        };
      })
    };
  }

  private parseStringArray(value: string | null | undefined): string[] {
    if (!value) {
      return [];
    }
    return value.split(',').map(item => item.trim()).filter(item => !!item).slice(0, 15);
  }

  private parseNumberArray(value: string | null | undefined): number[] {
    if (!value) {
      return [];
    }
    return value
      .split(',')
      .map(item => item.trim())
      .filter(item => !!item)
      .slice(0, 15)
      .map(item => Number(item))
      .filter(item => !Number.isNaN(item));
  }
}
