import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'wms-stat-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="stat-card" [class]="'variant-' + variant">
      <div class="stat-header">
        <div class="stat-icon-wrap" [style.background]="iconBg">
          <i [class]="icon" [style.color]="iconColor"></i>
        </div>
        <div class="stat-trend" *ngIf="trend !== null"
             [class.positive]="trend >= 0"
             [class.negative]="trend < 0">
          <i class="pi" [class.pi-arrow-up]="trend >= 0" [class.pi-arrow-down]="trend < 0"></i>
          <span>{{ trend >= 0 ? '+' : '' }}{{ trend }}%</span>
        </div>
      </div>
      <div class="stat-value">{{ value }}</div>
      <div class="stat-label">{{ label }}</div>
      <div class="stat-sub" *ngIf="subText">{{ subText }}</div>
    </div>
  `,
  styles: [`
    .stat-card {
      background: white;
      border-radius: 14px;
      padding: 20px;
      border: 1px solid #e5e7eb;
      transition: transform 0.2s, box-shadow 0.2s;
      position: relative;
      overflow: hidden;
    }

    .stat-card::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 3px;
      border-radius: 14px 14px 0 0;
    }

    .variant-primary::after { background: #1A005D; }
    .variant-accent::after { background: #8EC400; }
    .variant-secondary::after { background: #5BC2E7; }
    .variant-warning::after { background: #FF9E1B; }
    .variant-danger::after { background: #FF585D; }

    .stat-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
    }

    .stat-header {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      margin-bottom: 14px;
    }

    .stat-icon-wrap {
      width: 42px;
      height: 42px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .stat-icon-wrap i {
      font-size: 19px;
    }

    .stat-trend {
      display: flex;
      align-items: center;
      gap: 3px;
      font-size: 12px;
      font-weight: 600;
      padding: 3px 8px;
      border-radius: 6px;
    }

    .stat-trend.positive {
      background: #ecfccb;
      color: #4d7c0f;
    }

    .stat-trend.negative {
      background: #fee2e2;
      color: #dc2626;
    }

    .stat-trend i {
      font-size: 11px;
    }

    .stat-value {
      font-size: 28px;
      font-weight: 800;
      color: #111827;
      letter-spacing: -0.5px;
      line-height: 1;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 13px;
      color: #6b7280;
      font-weight: 500;
    }

    .stat-sub {
      font-size: 11.5px;
      color: #9ca3af;
      margin-top: 6px;
    }
  `]
})
export class StatCardComponent {
  @Input() icon = 'pi pi-chart-bar';
  @Input() iconBg = 'rgba(26, 0, 93, 0.08)';
  @Input() iconColor = '#1A005D';
  @Input() value: string | number = '0';
  @Input() label = '';
  @Input() subText = '';
  @Input() trend: number | null = null;
  @Input() variant: 'primary' | 'accent' | 'secondary' | 'warning' | 'danger' = 'primary';
}
