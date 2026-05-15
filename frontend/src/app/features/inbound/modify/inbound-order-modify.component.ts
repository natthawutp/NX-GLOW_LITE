import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
  selector: 'wms-inbound-order-modify',
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
    <wms-page-header title="Modify Arrival Order" subtitle="Edit arrival order header and detail lines" icon="pi pi-pencil">
      <button pButton label="Back" icon="pi pi-arrow-left" class="p-button-sm p-button-outlined" (click)="goBack()"></button>
      <button pButton label="Save" icon="pi pi-check" class="p-button-sm p-button-success" [disabled]="saving() || loadingData()" (click)="submit()"></button>
    </wms-page-header>

    @if (errorMessage()) {
      <div class="message error"><i class="pi pi-times-circle"></i> {{ errorMessage() }}</div>
    }
    @if (successMessage()) {
      <div class="message success"><i class="pi pi-check-circle"></i> {{ successMessage() }}</div>
    }

    @if (loadingData()) {
      <div class="wms-card"><p style="color: #6b7280; padding: 20px; text-align: center;">Loading order data...</p></div>
    } @else {
      <form [formGroup]="form" class="entry-form">
        <p-tabView>

          <!-- Header Tab -->
          <p-tabPanel header="Header" leftIcon="pi pi-info-circle">
            <table class="form-table">
              <tbody>
                <tr>
                  <th>Arrival Number</th>
                  <td><span class="readonly-value">{{ arrivalNumber }}</span></td>
                  <th>Status</th>
                  <td><span class="readonly-value">{{ currentStatusLabel }}</span></td>
                </tr>
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
                  <th>Weight</th>
                  <td><p-inputNumber formControlName="weight" mode="decimal" [minFractionDigits]="0" [maxFractionDigits]="3" [style]="{ width: '100%' }"></p-inputNumber></td>
                  <th>Volume (M3)</th>
                  <td><p-inputNumber formControlName="volumeM3" mode="decimal" [minFractionDigits]="0" [maxFractionDigits]="3" [style]="{ width: '100%' }"></p-inputNumber></td>
                </tr>
                <tr>
                  <th>Remarks</th>
                  <td colspan="3"><input pInputText formControlName="remarks" class="w-full"></td>
                </tr>
              </tbody>
            </table>
          </p-tabPanel>

          <!-- Detail Lines Tab -->
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

        </p-tabView>
      </form>
    }
  `,
  styles: [`
    .entry-form { display: flex; flex-direction: column; gap: 14px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }

    .message { display: flex; align-items: center; gap: 8px; border-radius: 6px; padding: 8px 14px; margin-bottom: 10px; font-size: 13px; }
    .message.error   { background: #fff0f0; color: #c0392b; border: 1px solid #e74c3c; }
    .message.success { background: #f0fff4; color: #27ae60; border: 1px solid #2ecc71; }
    .req { color: #e74c3c; margin-left: 2px; }

    .form-table { width: 100%; border-collapse: collapse; font-size: 13px; }
    .form-table th,
    .form-table td { padding: 7px 10px; border: 1px solid var(--surface-border); vertical-align: middle; }
    .form-table th { width: 180px; background: var(--surface-ground); font-weight: 600; color: var(--text-color); white-space: nowrap; }
    .form-table td { background: var(--surface-card); }
    .w-full { width: 100%; }
    .readonly-value { font-weight: 600; color: #1A005D; font-family: monospace; }

    .lines-toolbar { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
    .line-count { font-size: 12px; color: var(--text-color-secondary); }
    .table-scroll {
      overflow-x: auto;
      border: 1px solid var(--surface-border);
      border-radius: 6px;
      padding-bottom: 4px;
    }
    .table-scroll::-webkit-scrollbar { height: 10px; }
    .table-scroll::-webkit-scrollbar-track { background: var(--surface-ground); border-radius: 0 0 6px 6px; margin: 0 2px; }
    .table-scroll::-webkit-scrollbar-thumb { background: rgba(0,0,0,0.25); border-radius: 10px; border: 2px solid var(--surface-ground); }
    .table-scroll::-webkit-scrollbar-thumb:hover { background: rgba(0,0,0,0.45); }

    .lines-table { width: max-content; min-width: 100%; border-collapse: collapse; font-size: 12px; }
    .lines-table thead tr { background: var(--surface-ground); position: sticky; top: 0; z-index: 1; }
    .lines-table th {
      padding: 7px 8px; text-align: left; font-size: 11px; font-weight: 700; white-space: nowrap;
      border-bottom: 2px solid var(--surface-border); border-right: 1px solid var(--surface-border); color: var(--text-color);
    }
    .lines-table td {
      padding: 2px 3px; border-bottom: 1px solid var(--surface-border); border-right: 1px solid var(--surface-border);
      vertical-align: middle; background: var(--surface-card);
    }
    .lines-table tbody tr:hover td { background: var(--surface-hover); }
    .tbl-input {
      width: 100%; height: 28px; font-size: 12px; padding: 2px 6px !important;
      border-radius: 4px !important; border: 1px solid var(--surface-border) !important; background: transparent !important;
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
export class InboundOrderModifyComponent implements OnInit {
  saving = signal(false);
  loadingData = signal(true);
  errorMessage = signal('');
  successMessage = signal('');
  arrivalNumber = '';
  currentStatusLabel = '';
  private updTimestamp = '';

  form: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly api: ApiService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      scheduledDate: [null as Date | null, Validators.required],
      transactionKind: [''],
      supplierCode: ['', Validators.required],
      supplierName: [''],
      poNumber: [''],
      referenceNumber: [''],
      remarks: [''],
      weight: [null as number | null],
      volumeM3: [null as number | null],
      lines: this.fb.array([] as FormGroup[])
    });
  }

  get lineControls(): FormArray<FormGroup> {
    return this.form.get('lines') as FormArray<FormGroup>;
  }

  ngOnInit(): void {
    this.arrivalNumber = this.route.snapshot.paramMap.get('arrivalNo') || '';
    if (this.arrivalNumber) {
      this.api.get<any>(`/inbound/orders/${this.arrivalNumber}`).subscribe({
        next: (res) => {
          this.loadingData.set(false);
          if (res.status === 'SUCCESS' && res.data) {
            this.patchForm(res.data);
          } else {
            this.errorMessage.set('Order not found.');
          }
        },
        error: () => {
          this.loadingData.set(false);
          this.errorMessage.set('Failed to load order data.');
        }
      });
    } else {
      this.loadingData.set(false);
      this.errorMessage.set('No arrival number specified.');
    }
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
      this.errorMessage.set('Please complete required fields before saving.');
      return;
    }

    const payload = this.buildPayload();
    this.saving.set(true);
    this.api.put<any>(`/inbound/orders/${this.arrivalNumber}`, payload)
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (response) => {
          if (response.status === 'SUCCESS') {
            this.successMessage.set('Order updated successfully.');
            setTimeout(() => this.router.navigate(['/inbound/orders', this.arrivalNumber]), 1500);
            return;
          }
          if (response.messages && response.messages.length > 0) {
            this.errorMessage.set(response.messages.map((m: any) => `${m.code}: ${m.message}`).join(' | '));
          } else {
            this.errorMessage.set('Update failed.');
          }
        },
        error: (error) => {
          const errorMessages = error?.error?.messages;
          if (errorMessages && errorMessages.length > 0) {
            this.errorMessage.set(errorMessages.map((item: any) => `${item.code}: ${item.message}`).join(' | '));
            return;
          }
          this.errorMessage.set('Update failed.');
        }
      });
  }

  goBack(): void {
    this.router.navigate(['/inbound/orders', this.arrivalNumber]);
  }

  private patchForm(data: any): void {
    this.updTimestamp = data.UPD_YMDHMS || '';
    this.currentStatusLabel = data.AVH_AV_STS_LBL || data.AVH_AV_STS || '';

    this.form.patchValue({
      scheduledDate: data.AVH_SCDL_YMD ? new Date(data.AVH_SCDL_YMD) : null,
      transactionKind: data.AVH_TRN_KND || '',
      supplierCode: data.AVH_SPL_COD || '',
      supplierName: data.AVH_SPL_NAM1 || '',
      poNumber: data.AVH_PO_NUM || '',
      referenceNumber: data.AVH_RF_NUM || '',
      remarks: data.AVH_RMKS || '',
      weight: data.AVH_WGT,
      volumeM3: data.AVH_M3
    });

    const linesArray = this.lineControls;
    linesArray.clear();
    for (const line of (data.lines || [])) {
      linesArray.push(this.createLineGroup(line));
    }
    if (linesArray.length === 0) {
      linesArray.push(this.createLineGroup());
    }
  }

  private createLineGroup(line?: any): FormGroup {
    return this.fb.group({
      productCode: [line?.AVD_PROD_COD || '', Validators.required],
      productName: [line?.AVD_PROD_NAM || ''],
      originCode: [line?.AVD_ORGN_COD || ''],
      shipperCode: [line?.AVD_SPPR_COD || ''],
      shipperName: [line?.AVD_SPPR_NAM || ''],
      plannedPieceQuantity: [line?.AVD_PPCS_QTY ?? null],
      plannedCaseQuantity: [line?.AVD_SCS_QTY ?? null],
      weight: [line?.AVD_WGT ?? null],
      volumeM3: [line?.AVD_M3 ?? null],
      subInventoryCode: [line?.AVD_SBIV_COD || ''],
      pik1: [line?.AVD_PIK1 || ''],
      pik2: [line?.AVD_PIK2 || ''],
      pik3: [line?.AVD_PIK3 || ''],
      pik4: [line?.AVD_PIK4 || ''],
      pik5: [line?.AVD_PIK5 || ''],
      pik6: [line?.AVD_PIK6 || ''],
      pik7: [line?.AVD_PIK7 || ''],
      remarks: [line?.AVD_RMKS || '']
    });
  }

  private buildPayload(): any {
    const value = this.form.value;
    return {
      AVH_AV_NUM: this.arrivalNumber,
      UPD_YMDHMS: this.updTimestamp,
      header: {
        AVH_SCDL_YMD: this.formatDate(value.scheduledDate),
        AVH_TRN_KND: value.transactionKind,
        AVH_PO_NUM: value.poNumber,
        AVH_RF_NUM: value.referenceNumber,
        AVH_RMKS: value.remarks,
        AVH_SPL_COD: value.supplierCode,
        AVH_SPL_NAM1: value.supplierName,
        AVH_WGT: value.weight,
        AVH_M3: value.volumeM3,
        AVH_CLI: [],
        AVH_CFG: [],
        AVH_CNI: []
      },
      lines: this.lineControls.controls.map((lineCtrl, index) => {
        const lv = lineCtrl.value;
        return {
          AVD_AVLN_NUM: index + 1,
          AVD_AV_STS: '100',
          AVD_INSP_STS: '100',
          AVD_PROD_COD: lv.productCode,
          AVD_PROD_NAM: lv.productName,
          AVD_ORGN_COD: lv.originCode,
          AVD_SPPR_COD: lv.shipperCode,
          AVD_SPPR_NAM: lv.shipperName,
          AVD_PPCS_QTY: lv.plannedPieceQuantity,
          AVD_SCS_QTY: lv.plannedCaseQuantity,
          AVD_WGT: lv.weight,
          AVD_M3: lv.volumeM3,
          AVD_SBIV_COD: lv.subInventoryCode,
          AVD_PIK1: lv.pik1,
          AVD_PIK2: lv.pik2,
          AVD_PIK3: lv.pik3,
          AVD_PIK4: lv.pik4,
          AVD_PIK5: lv.pik5,
          AVD_PIK6: lv.pik6,
          AVD_PIK7: lv.pik7,
          AVD_RMKS: lv.remarks,
          AVD_CLI: [],
          AVD_CFG: [],
          AVD_CNI: []
        };
      })
    };
  }

  private formatDate(value: Date | string | null | undefined): string | null {
    if (!value) {
      return null;
    }
    if (value instanceof Date) {
      const year = value.getFullYear();
      const month = String(value.getMonth() + 1).padStart(2, '0');
      const day = String(value.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
    return value.toString();
  }
}
