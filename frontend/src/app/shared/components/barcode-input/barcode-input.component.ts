import { Component, Input, Output, EventEmitter, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'wms-barcode-input',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, TooltipModule],
  template: `
    <div class="barcode-input-wrapper">
      <div class="barcode-field">
        <i class="pi pi-qrcode barcode-icon"></i>
        <input #barcodeInput
               pInputText
               type="text"
               [(ngModel)]="barcode"
               [placeholder]="placeholder"
               class="barcode-text-input"
               (keyup.enter)="onScan()"
               (focus)="isFocused = true"
               (blur)="isFocused = false"
               [class.focused]="isFocused"
               autocomplete="off"
               spellcheck="false">
        <button pButton
                icon="pi pi-search"
                class="p-button-sm scan-btn"
                (click)="onScan()"
                [disabled]="!barcode"
                pTooltip="Scan">
        </button>
      </div>
      <div class="scan-hint" *ngIf="showHint">
        <i class="pi pi-info-circle"></i>
        <span>{{ hint }}</span>
      </div>
    </div>
  `,
  styles: [`
    .barcode-input-wrapper {
      width: 100%;
    }

    .barcode-field {
      display: flex;
      align-items: center;
      gap: 0;
      position: relative;
    }

    .barcode-icon {
      position: absolute;
      left: 12px;
      color: #9ca3af;
      font-size: 17px;
      z-index: 1;
    }

    .barcode-text-input {
      flex: 1;
      padding-left: 38px !important;
      padding-right: 8px !important;
      height: 40px;
      font-size: 14px;
      font-family: 'JetBrains Mono', 'Fira Code', monospace;
      letter-spacing: 0.5px;
      border-radius: 8px 0 0 8px !important;
    }

    .barcode-text-input.focused {
      border-color: #8EC400 !important;
      box-shadow: 0 0 0 3px rgba(142, 196, 0, 0.15) !important;
    }

    .scan-btn {
      border-radius: 0 8px 8px 0 !important;
      height: 40px;
      width: 42px;
    }

    .scan-hint {
      display: flex;
      align-items: center;
      gap: 5px;
      margin-top: 4px;
      font-size: 11.5px;
      color: #9ca3af;
    }

    .scan-hint i {
      font-size: 12px;
    }
  `]
})
export class BarcodeInputComponent implements AfterViewInit {
  @Input() placeholder = 'Scan or enter barcode...';
  @Input() hint = 'Scan barcode or enter manually and press Enter';
  @Input() showHint = true;
  @Input() autoFocus = true;

  @Output() scan = new EventEmitter<string>();

  @ViewChild('barcodeInput') inputRef!: ElementRef<HTMLInputElement>;

  barcode = '';
  isFocused = false;

  ngAfterViewInit(): void {
    if (this.autoFocus) {
      setTimeout(() => this.inputRef.nativeElement.focus(), 100);
    }
  }

  onScan(): void {
    if (this.barcode.trim()) {
      this.scan.emit(this.barcode.trim());
      this.barcode = '';
      this.inputRef.nativeElement.focus();
    }
  }

  focus(): void {
    this.inputRef.nativeElement.focus();
  }
}
