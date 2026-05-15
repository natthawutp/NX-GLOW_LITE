import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'wms-page-header',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  template: `
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i [class]="icon" *ngIf="icon"></i>
          {{ title | translate }}
        </h1>
        <p class="page-subtitle" *ngIf="subtitle">{{ subtitle | translate }}</p>
      </div>
      <div class="header-actions">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .page-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 24px;
      gap: 16px;
      flex-wrap: wrap;
    }

    .page-title {
      font-size: 22px;
      font-weight: 800;
      color: #111827;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 10px;
      letter-spacing: -0.3px;
    }

    .page-title i {
      font-size: 20px;
      color: #1A005D;
    }

    .page-subtitle {
      font-size: 13.5px;
      color: #6b7280;
      margin: 4px 0 0;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 8px;
      flex-wrap: wrap;
    }

    @media (max-width: 640px) {
      .page-header {
        flex-direction: column;
      }
      .header-actions {
        width: 100%;
      }
    }
  `]
})
export class PageHeaderComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() icon = '';
}
