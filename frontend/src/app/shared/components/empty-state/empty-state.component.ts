import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'wms-empty-state',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="empty-state">
      <div class="empty-icon">
        <i [class]="icon"></i>
      </div>
      <h3 class="empty-title">{{ title | translate }}</h3>
      <p class="empty-description" *ngIf="description">{{ description | translate }}</p>
      <div class="empty-actions">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 48px 24px;
      text-align: center;
    }

    .empty-icon {
      width: 72px;
      height: 72px;
      border-radius: 50%;
      background: #f3f4f6;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;
    }

    .empty-icon i {
      font-size: 28px;
      color: #9ca3af;
    }

    .empty-title {
      font-size: 16px;
      font-weight: 700;
      color: #374151;
      margin: 0 0 6px;
    }

    .empty-description {
      font-size: 13.5px;
      color: #9ca3af;
      margin: 0 0 20px;
      max-width: 340px;
    }

    .empty-actions {
      display: flex;
      gap: 8px;
    }
  `]
})
export class EmptyStateComponent {
  @Input() icon = 'pi pi-inbox';
  @Input() title = 'common.no_data';
  @Input() description = '';
}
