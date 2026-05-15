import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { InputSwitchModule } from 'primeng/inputswitch';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { LpnService, LpnConfigDto } from '@core/services/lpn.service';

@Component({
  selector: 'wms-master-lpn-config',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, DropdownModule,
            InputTextModule, InputSwitchModule, PageHeaderComponent],
  template: `
    <wms-page-header title="lpn.config_title" subtitle="lpn.config_subtitle" icon="pi pi-cog">
    </wms-page-header>

    <div class="config-layout" *ngIf="config">
      <div class="wms-card">
        <h3 class="section-title">LPN Settings</h3>
        <div class="config-grid">
          <div class="config-item">
            <div class="config-label">
              <span class="label-text">Enable LPN</span>
              <span class="label-desc">Turn on License Plate Number tracking for this customer</span>
            </div>
            <p-inputSwitch [(ngModel)]="config.lpnEnabled"></p-inputSwitch>
          </div>

          <div class="config-item">
            <div class="config-label">
              <span class="label-text">Auto-Generate LPN Numbers</span>
              <span class="label-desc">Automatically generate LPN barcodes during receiving</span>
            </div>
            <p-inputSwitch [(ngModel)]="config.autoGenerate"></p-inputSwitch>
          </div>

          <div class="config-item">
            <div class="config-label">
              <span class="label-text">Allow Mixed-SKU LPNs</span>
              <span class="label-desc">Allow multiple different products in a single LPN</span>
            </div>
            <p-inputSwitch [(ngModel)]="config.mixedSkuAllowed"></p-inputSwitch>
          </div>

          <div class="config-item">
            <div class="config-label">
              <span class="label-text">Enable LPN Hierarchy</span>
              <span class="label-desc">Allow parent-child LPN relationships (pallet of cases)</span>
            </div>
            <p-inputSwitch [(ngModel)]="config.hierarchyEnabled"></p-inputSwitch>
          </div>

          <div class="config-item">
            <div class="config-label">
              <span class="label-text">Enable Outbound LPN</span>
              <span class="label-desc">Use LPN tracking for outbound packing and shipping</span>
            </div>
            <p-inputSwitch [(ngModel)]="config.outboundLpnEnabled"></p-inputSwitch>
          </div>
        </div>
      </div>

      <div class="wms-card">
        <h3 class="section-title">Barcode Settings</h3>
        <div class="form-grid">
          <div class="form-field">
            <label>Barcode Format</label>
            <p-dropdown [options]="barcodeFormats" [(ngModel)]="config.barcodeFormat" optionLabel="label" optionValue="value"
                        [style]="{ width: '100%' }"></p-dropdown>
          </div>
          <div class="form-field">
            <label>Barcode Prefix</label>
            <input pInputText [(ngModel)]="config.barcodePrefix" placeholder="e.g. company prefix" />
          </div>
          <div class="form-field">
            <label>Label Print Template</label>
            <input pInputText [(ngModel)]="config.printTemplate" placeholder="Template name" />
          </div>
        </div>
      </div>

      <div class="actions">
        <button pButton label="Save Configuration" icon="pi pi-save" class="p-button-sm"
                (click)="save()" [loading]="saving"></button>
      </div>
    </div>

    <div class="message success" *ngIf="successMsg">
      <i class="pi pi-check-circle"></i> {{ successMsg }}
    </div>
    <div class="message error" *ngIf="errorMsg">
      <i class="pi pi-exclamation-triangle"></i> {{ errorMsg }}
    </div>
  `,
  styles: [`
    .config-layout { display: flex; flex-direction: column; gap: 16px; max-width: 700px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .config-grid { display: flex; flex-direction: column; gap: 16px; }
    .config-item { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #f3f4f6; }
    .config-item:last-child { border-bottom: none; }
    .config-label { display: flex; flex-direction: column; gap: 2px; }
    .label-text { font-size: 14px; font-weight: 600; color: #111827; }
    .label-desc { font-size: 12px; color: #6b7280; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-field { display: flex; flex-direction: column; gap: 5px; }
    .form-field label { font-size: 13px; font-weight: 600; color: #374151; }
    .actions { display: flex; justify-content: flex-end; }
    .message { padding: 12px 16px; border-radius: 8px; margin-top: 16px; font-size: 13px; display: flex; align-items: center; gap: 8px; max-width: 700px; }
    .message.success { background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; }
    .message.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; }
  `]
})
export class MasterLpnConfigComponent implements OnInit {
  config: LpnConfigDto | null = null;
  saving = false;
  successMsg = '';
  errorMsg = '';

  barcodeFormats = [
    { label: 'Custom (Warehouse-Date-Seq)', value: 'CUSTOM' },
    { label: 'GS1 SSCC', value: 'SSCC' },
    { label: 'GS1-128', value: 'GS128' }
  ];

  constructor(private lpnService: LpnService) {}

  ngOnInit(): void {
    this.lpnService.getConfig().subscribe({
      next: (res) => { this.config = res.data || null; },
      error: () => { this.errorMsg = 'Failed to load configuration'; }
    });
  }

  save(): void {
    if (!this.config) return;
    this.saving = true;
    this.successMsg = '';
    this.errorMsg = '';

    this.lpnService.saveConfig(this.config).subscribe({
      next: (res) => {
        this.saving = false;
        if (res.status === 'SUCCESS') {
          this.config = res.data || this.config;
          this.successMsg = 'LPN configuration saved successfully';
        } else {
          this.errorMsg = res.messages?.[0]?.message || 'Failed to save';
        }
      },
      error: (err) => {
        this.saving = false;
        this.errorMsg = err.error?.messages?.[0]?.message || 'Failed to save configuration';
      }
    });
  }
}
