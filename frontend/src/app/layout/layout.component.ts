import { Component, ViewChild, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopbarComponent } from './topbar/topbar.component';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';

@Component({
  selector: 'wms-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, TopbarComponent, BreadcrumbComponent],
  template: `
    <div class="layout-wrapper" [class.sidebar-collapsed]="sidebarCollapsed()">
      <wms-sidebar #sidebar (collapsedChange)="sidebarCollapsed.set($event)"></wms-sidebar>

      <div class="layout-main">
        <wms-topbar (menuToggle)="onMenuToggle()"></wms-topbar>

        <main class="layout-content">
          <wms-breadcrumb></wms-breadcrumb>

          <div class="content-area">
            <router-outlet></router-outlet>
          </div>
        </main>
      </div>
    </div>
  `,
  styles: [`
    .layout-wrapper {
      min-height: 100vh;
      display: flex;
    }

    .layout-main {
      flex: 1;
      margin-left: 260px;
      height: 100vh;
      display: flex;
      flex-direction: column;
      transition: margin-left 0.25s cubic-bezier(0.4, 0, 0.2, 1);
      background: #f5f6fa;
      overflow: hidden;
    }

    .sidebar-collapsed .layout-main {
      margin-left: 72px;
    }

    .layout-content {
      flex: 1;
      padding: 20px 28px 28px;
      overflow-y: auto;
    }

    .content-area {
      animation: fadeIn 0.2s ease;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(4px); }
      to { opacity: 1; transform: translateY(0); }
    }

    @media (max-width: 1024px) {
      .layout-main {
        margin-left: 0 !important;
      }

      .layout-content {
        padding: 16px 16px 16px;
      }
    }
  `]
})
export class LayoutComponent {
  @ViewChild('sidebar') sidebar!: SidebarComponent;
  sidebarCollapsed = signal(false);

  onMenuToggle(): void {
    if (window.innerWidth <= 1024) {
      this.sidebar.openMobile();
    } else {
      this.sidebar.toggleCollapse();
      // sidebarCollapsed is updated via (collapsedChange) output
    }
  }
}
