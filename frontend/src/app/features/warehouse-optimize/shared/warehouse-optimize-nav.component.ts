import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'wms-warehouse-optimize-nav',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="module-nav">
      <a
        *ngFor="let item of items"
        [routerLink]="item.link"
        routerLinkActive="active"
        [routerLinkActiveOptions]="{ exact: true }">
        <i [class]="item.icon"></i>
        <span>{{ item.label }}</span>
      </a>
    </nav>
  `,
  styles: [`
    .module-nav {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 18px;
    }

    .module-nav a {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 8px 12px;
      border-radius: 8px;
      border: 1px solid #dbe2ea;
      background: #fff;
      color: #475569;
      text-decoration: none;
      font-size: 13px;
      font-weight: 600;
      transition: all 0.16s ease;
    }

    .module-nav a:hover {
      border-color: #93c5fd;
      color: #0f172a;
      background: #f8fbff;
    }

    .module-nav a.active {
      border-color: #8ec400;
      background: #f7fee7;
      color: #1f2937;
    }
  `]
})
export class WarehouseOptimizeNavComponent {
  readonly items = [
    { label: 'Design', link: '/warehouse-optimize/design', icon: 'pi pi-pencil' },
    { label: '2D Result', link: '/warehouse-optimize/view-2d', icon: 'pi pi-map' },
    { label: '3D Live', link: '/warehouse-optimize/view-3d', icon: 'pi pi-box' },
    { label: 'Slotting', link: '/warehouse-optimize/slotting', icon: 'pi pi-sitemap' },
    { label: 'Routes', link: '/warehouse-optimize/routes', icon: 'pi pi-directions' },
    { label: 'Analytics', link: '/warehouse-optimize/analytics', icon: 'pi pi-chart-line' }
  ];
}
