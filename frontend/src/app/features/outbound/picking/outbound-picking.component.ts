import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { ProgressBarModule } from 'primeng/progressbar';
import { SelectButtonModule } from 'primeng/selectbutton';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { BarcodeInputComponent } from '@shared/components/barcode-input/barcode-input.component';
import { StatusBadgeComponent } from '@shared/components/status-badge/status-badge.component';

@Component({
  selector: 'wms-outbound-picking',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, ButtonModule, ProgressBarModule, SelectButtonModule, PageHeaderComponent, BarcodeInputComponent, StatusBadgeComponent],
  template: `
    <wms-page-header title="outbound.picking_title" subtitle="outbound.picking_subtitle" icon="pi pi-list">
    </wms-page-header>

    <div class="picking-layout">
      <div class="wms-card">
        <div class="mode-header">
          <h3 class="section-title">Active Pick Wave</h3>
          <p-selectButton [options]="pickModes" [(ngModel)]="pickMode" optionLabel="label" optionValue="value"
                          [allowEmpty]="false" styleClass="mode-toggle"></p-selectButton>
        </div>

        @if (pickWave) {
          <div class="wave-header">
            <div class="wave-info">
              <span class="wave-id">{{ pickWave.waveId }}</span>
              <wms-status-badge variant="in-progress" label="In Progress"></wms-status-badge>
              @if (pickMode === 'lpn') {
                <span class="lpn-badge">LPN Mode</span>
              }
            </div>
            <div class="wave-progress">
              <span class="progress-text">{{ pickWave.pickedItems }} / {{ pickWave.totalItems }} items</span>
              <p-progressBar [value]="pickWave.progress" [showValue]="false" [style]="{ height: '8px' }"></p-progressBar>
            </div>
          </div>
        } @else {
          <div class="no-wave">
            <p>No active pick wave. Scan a wave ID to start.</p>
            <wms-barcode-input placeholder="Scan wave ID..." (scan)="startWave($event)"></wms-barcode-input>
          </div>
        }
      </div>

      @if (pickWave && pickMode === 'item') {
        <div class="wms-card">
          <h3 class="section-title">Current Pick Task</h3>
          @if (currentTask) {
            <div class="pick-task">
              <div class="task-location">
                <span class="loc-label">GO TO</span>
                <span class="loc-value">{{ currentTask.location }}</span>
              </div>
              <div class="task-details">
                <div class="task-row"><span class="t-label">Product</span><span class="t-value">{{ currentTask.product }}</span></div>
                <div class="task-row"><span class="t-label">Pick Qty</span><span class="t-value qty">{{ currentTask.qty }}</span></div>
                <div class="task-row"><span class="t-label">UOM</span><span class="t-value">{{ currentTask.uom }}</span></div>
              </div>
              <wms-barcode-input placeholder="Scan picked item..." (scan)="confirmPick($event)"></wms-barcode-input>
            </div>
          } @else {
            <div class="all-done">
              <i class="pi pi-check-circle"></i>
              <p>All tasks completed for this wave!</p>
              <button pButton label="Complete Wave" icon="pi pi-flag-fill" class="p-button-sm" (click)="completeWave()"></button>
            </div>
          }
        </div>
      }

      @if (pickWave && pickMode === 'lpn') {
        <div class="wms-card">
          <h3 class="section-title">LPN Full-Pallet Pick</h3>
          @if (currentLpnTask) {
            <div class="pick-task">
              <div class="task-location">
                <span class="loc-label">GO TO LOCATION</span>
                <span class="loc-value">{{ currentLpnTask.location }}</span>
              </div>
              <div class="task-details">
                <div class="task-row"><span class="t-label">LPN</span><span class="t-value lpn-code">{{ currentLpnTask.lpnNumber }}</span></div>
                <div class="task-row"><span class="t-label">Type</span><span class="t-value">{{ currentLpnTask.lpnType }}</span></div>
                <div class="task-row"><span class="t-label">Items</span><span class="t-value">{{ currentLpnTask.itemCount }} SKUs</span></div>
                <div class="task-row"><span class="t-label">Total Qty</span><span class="t-value qty">{{ currentLpnTask.totalQty }}</span></div>
              </div>
              <wms-barcode-input placeholder="Scan LPN barcode to confirm pick..." (scan)="confirmLpnPick($event)"></wms-barcode-input>
              @if (lpnPickError) {
                <div class="error-msg"><i class="pi pi-exclamation-triangle"></i> {{ lpnPickError }}</div>
              }
            </div>
          } @else {
            <div class="all-done">
              <i class="pi pi-check-circle"></i>
              <p>All LPN picks completed for this wave!</p>
              <button pButton label="Complete Wave" icon="pi pi-flag-fill" class="p-button-sm" (click)="completeWave()"></button>
            </div>
          }
        </div>

        @if (pickedLpns.length > 0) {
          <div class="wms-card">
            <h3 class="section-title">Picked LPNs ({{ pickedLpns.length }})</h3>
            <div class="picked-lpn-list">
              @for (lpn of pickedLpns; track lpn.lpnNumber) {
                <div class="picked-lpn-item">
                  <i class="pi pi-check-circle" style="color: #8EC400"></i>
                  <span class="lpn-code">{{ lpn.lpnNumber }}</span>
                  <span class="lpn-meta">{{ lpn.location }} · {{ lpn.totalQty }} pcs</span>
                </div>
              }
            </div>
          </div>
        }
      }
    </div>
  `,
  styles: [`
    .picking-layout { display: flex; flex-direction: column; gap: 16px; max-width: 700px; }
    .wms-card { background: white; border-radius: 14px; border: 1px solid #e5e7eb; padding: 20px; }
    .section-title { font-size: 15px; font-weight: 700; color: #111827; margin: 0 0 16px; }
    .mode-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .mode-header .section-title { margin: 0; }
    .lpn-badge { font-size: 11px; font-weight: 600; color: #1A005D; background: #ede9fe; padding: 2px 10px; border-radius: 12px; }
    .wave-header { display: flex; flex-direction: column; gap: 12px; }
    .wave-info { display: flex; align-items: center; gap: 12px; }
    .wave-id { font-size: 18px; font-weight: 700; color: #1A005D; font-family: monospace; }
    .wave-progress { display: flex; flex-direction: column; gap: 4px; }
    .progress-text { font-size: 13px; color: #6b7280; }
    .no-wave { text-align: center; }
    .no-wave p { color: #9ca3af; margin-bottom: 16px; }
    .pick-task { display: flex; flex-direction: column; gap: 16px; }
    .task-location { background: linear-gradient(135deg, #1A005D, #2d0080); color: white; border-radius: 12px; padding: 16px; text-align: center; }
    .loc-label { display: block; font-size: 12px; color: rgba(255,255,255,0.6); margin-bottom: 4px; text-transform: uppercase; letter-spacing: 1px; }
    .loc-value { font-size: 28px; font-weight: 800; font-family: monospace; letter-spacing: 2px; }
    .task-details { display: flex; flex-direction: column; gap: 8px; }
    .task-row { display: flex; justify-content: space-between; padding: 6px 0; border-bottom: 1px solid #f3f4f6; }
    .t-label { font-size: 13px; color: #6b7280; }
    .t-value { font-size: 14px; font-weight: 600; color: #111827; }
    .t-value.qty { color: #8EC400; font-size: 18px; }
    .t-value.lpn-code { font-family: monospace; color: #1A005D; }
    .all-done { text-align: center; padding: 24px; }
    .all-done i { font-size: 40px; color: #8EC400; }
    .all-done p { color: #374151; margin: 12px 0; }
    .error-msg { display: flex; align-items: center; gap: 8px; padding: 10px 14px; background: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; color: #dc2626; font-size: 13px; }
    .picked-lpn-list { display: flex; flex-direction: column; gap: 6px; }
    .picked-lpn-item { display: flex; align-items: center; gap: 10px; padding: 10px 14px; background: #f0fdf4; border-radius: 8px; }
    .lpn-code { font-family: monospace; font-weight: 700; color: #1A005D; }
    .lpn-meta { font-size: 12px; color: #6b7280; margin-left: auto; }
  `]
})
export class OutboundPickingComponent {
  pickMode = 'item';
  pickModes = [
    { label: 'By Item', value: 'item' },
    { label: 'By LPN', value: 'lpn' }
  ];

  pickWave: any = null;
  currentTask: any = null;
  private taskIndex = 0;
  private tasks = [
    { location: 'A-3-2-1', product: 'SKU-PRD-4421', qty: 5, uom: 'PCS' },
    { location: 'B-1-4-2', product: 'SKU-PRD-8821', qty: 12, uom: 'PCS' },
    { location: 'C-5-1-3', product: 'SKU-PRD-1122', qty: 3, uom: 'BOX' }
  ];

  // LPN pick mode
  currentLpnTask: any = null;
  private lpnTaskIndex = 0;
  private lpnTasks = [
    { lpnNumber: 'SP-20250316-000042', location: 'A-1-1-1', lpnType: 'Pallet', itemCount: 3, totalQty: 120 },
    { lpnNumber: 'SP-20250316-000043', location: 'B-2-3-1', lpnType: 'Case', itemCount: 1, totalQty: 48 }
  ];
  pickedLpns: any[] = [];
  lpnPickError = '';

  startWave(waveId: string): void {
    const totalItems = this.pickMode === 'lpn' ? this.lpnTasks.length : this.tasks.length;
    this.pickWave = { waveId: waveId || 'WV-0123', totalItems, pickedItems: 0, progress: 0 };
    if (this.pickMode === 'item') {
      this.taskIndex = 0;
      this.currentTask = this.tasks[0];
    } else {
      this.lpnTaskIndex = 0;
      this.currentLpnTask = this.lpnTasks[0];
      this.pickedLpns = [];
    }
  }

  confirmPick(barcode: string): void {
    if (this.pickWave) {
      this.pickWave.pickedItems++;
      this.pickWave.progress = Math.round((this.pickWave.pickedItems / this.pickWave.totalItems) * 100);
      this.taskIndex++;
      this.currentTask = this.taskIndex < this.tasks.length ? this.tasks[this.taskIndex] : null;
    }
  }

  confirmLpnPick(barcode: string): void {
    if (!this.currentLpnTask || !this.pickWave) return;
    this.lpnPickError = '';

    if (barcode !== this.currentLpnTask.lpnNumber) {
      this.lpnPickError = `Expected LPN ${this.currentLpnTask.lpnNumber}, scanned ${barcode}`;
      return;
    }

    this.pickedLpns.push({ ...this.currentLpnTask });
    this.pickWave.pickedItems++;
    this.pickWave.progress = Math.round((this.pickWave.pickedItems / this.pickWave.totalItems) * 100);
    this.lpnTaskIndex++;
    this.currentLpnTask = this.lpnTaskIndex < this.lpnTasks.length ? this.lpnTasks[this.lpnTaskIndex] : null;
  }

  completeWave(): void {
    this.pickWave = null;
    this.currentTask = null;
    this.currentLpnTask = null;
    this.pickedLpns = [];
  }
}
