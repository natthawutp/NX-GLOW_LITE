import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export type BadgeVariant = 'new' | 'pending' | 'in-progress' | 'completed' | 'shipped' | 'cancelled' | 'draft' | 'confirmed' | 'received' | 'allocated';

const VARIANT_CONFIG: Record<BadgeVariant, { bg: string; color: string; icon: string }> = {
  'new':         { bg: '#eff6ff', color: '#1d4ed8', icon: 'pi pi-plus-circle' },
  'pending':     { bg: '#fef3c7', color: '#b45309', icon: 'pi pi-clock' },
  'in-progress': { bg: '#dbeafe', color: '#1e40af', icon: 'pi pi-spin pi-spinner' },
  'completed':   { bg: '#ecfccb', color: '#4d7c0f', icon: 'pi pi-check-circle' },
  'shipped':     { bg: '#e0f2fe', color: '#0369a1', icon: 'pi pi-truck' },
  'cancelled':   { bg: '#fee2e2', color: '#dc2626', icon: 'pi pi-times-circle' },
  'draft':       { bg: '#f3f4f6', color: '#6b7280', icon: 'pi pi-file' },
  'confirmed':   { bg: '#d1fae5', color: '#059669', icon: 'pi pi-check' },
  'received':    { bg: '#ecfccb', color: '#65a30d', icon: 'pi pi-download' },
  'allocated':   { bg: '#ede9fe', color: '#7c3aed', icon: 'pi pi-link' }
};

@Component({
  selector: 'wms-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="status-badge"
          [style.background]="config.bg"
          [style.color]="config.color">
      <i [class]="config.icon" *ngIf="showIcon"></i>
      <span>{{ label || variant }}</span>
    </span>
  `,
  styles: [`
    .status-badge {
      display: inline-flex;
      align-items: center;
      gap: 5px;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 12px;
      font-weight: 600;
      white-space: nowrap;
      text-transform: capitalize;
    }

    .status-badge i {
      font-size: 12px;
    }
  `]
})
export class StatusBadgeComponent {
  @Input() variant: BadgeVariant = 'pending';
  @Input() label = '';
  @Input() showIcon = true;

  get config() {
    return VARIANT_CONFIG[this.variant] || VARIANT_CONFIG['pending'];
  }
}
