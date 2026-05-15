import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, ActivatedRoute, NavigationEnd } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { filter, map, startWith } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

interface BreadcrumbItem {
  label: string;
  icon?: string;
  routerLink?: string;
}

@Component({
  selector: 'wms-breadcrumb',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <nav class="breadcrumb-bar" *ngIf="breadcrumbs().length > 0">
      <ol class="breadcrumb-list">
        <li class="breadcrumb-item home">
          <a routerLink="/dashboard" class="breadcrumb-link">
            <i class="pi pi-home"></i>
          </a>
        </li>
        @for (item of breadcrumbs(); track item.label; let last = $last) {
          <li class="breadcrumb-separator">
            <i class="pi pi-chevron-right"></i>
          </li>
          <li class="breadcrumb-item" [class.active]="last">
            @if (last) {
              <span class="breadcrumb-current">
                <i [class]="item.icon" *ngIf="item.icon"></i>
                {{ item.label | translate }}
              </span>
            } @else {
              <a [routerLink]="item.routerLink" class="breadcrumb-link">
                {{ item.label | translate }}
              </a>
            }
          </li>
        }
      </ol>
    </nav>
  `,
  styles: [`
    .breadcrumb-bar {
      padding: 12px 0;
    }

    .breadcrumb-list {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
      gap: 0;
      flex-wrap: wrap;
    }

    .breadcrumb-item {
      display: flex;
      align-items: center;
    }

    .breadcrumb-separator {
      display: flex;
      align-items: center;
      padding: 0 8px;
      color: #d1d5db;
    }

    .breadcrumb-separator i {
      font-size: 10px;
    }

    .breadcrumb-link {
      color: #6b7280;
      text-decoration: none;
      font-size: 13px;
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 4px 6px;
      border-radius: 4px;
      transition: all 0.15s;
    }

    .breadcrumb-link:hover {
      color: #1A005D;
      background: rgba(26, 0, 93, 0.04);
    }

    .breadcrumb-link i {
      font-size: 14px;
    }

    .breadcrumb-current {
      color: #111827;
      font-size: 13px;
      font-weight: 600;
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .breadcrumb-current i {
      font-size: 14px;
      color: #6b7280;
    }
  `]
})
export class BreadcrumbComponent {
  private routeData$ = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    startWith(null),
    map(() => this.buildBreadcrumbs(this.route.root))
  );

  breadcrumbs = toSignal(this.routeData$, { initialValue: [] as BreadcrumbItem[] });

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  private buildBreadcrumbs(route: ActivatedRoute, url: string = '', breadcrumbs: BreadcrumbItem[] = []): BreadcrumbItem[] {
    const children = route.children;
    if (children.length === 0) {
      return breadcrumbs;
    }

    for (const child of children) {
      const snapshot = child?.snapshot;
      if (!snapshot) {
        continue;
      }

      const routeURL = (snapshot.url ?? []).map(segment => segment.path).join('/');
      if (routeURL !== '') {
        url += `/${routeURL}`;
      }

      const label = snapshot.data['breadcrumb'];
      const icon = snapshot.data['icon'];

      if (label) {
        breadcrumbs.push({
          label,
          icon,
          routerLink: url
        });
      }

      return this.buildBreadcrumbs(child, url, breadcrumbs);
    }

    return breadcrumbs;
  }
}
