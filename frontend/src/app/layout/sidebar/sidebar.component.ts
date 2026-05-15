import { Component, signal, computed, OnInit, OnDestroy, HostListener, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter, Subscription } from 'rxjs';
import { AuthService } from '@core/services/auth.service';

export interface MenuItem {
  label: string;
  icon: string;
  routerLink?: string;
  children?: MenuItem[];
  badge?: number;
  roles?: string[];
  expanded?: boolean;
}

@Component({
  selector: 'wms-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <aside class="sidebar" [class.collapsed]="collapsed()" [class.mobile-open]="mobileOpen()">
      <!-- Logo -->
      <div class="sidebar-header">
        <div class="logo-container" (click)="navigateHome()">
          <div class="logo-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <rect width="32" height="32" rx="8" fill="#1A005D"/>
              <path d="M8 12L16 8L24 12V20L16 24L8 20V12Z" stroke="#8EC400" stroke-width="2" fill="none"/>
              <path d="M16 8V24M8 12L24 20M24 12L8 20" stroke="#8EC400" stroke-width="1.5" stroke-opacity="0.5"/>
              <circle cx="16" cy="16" r="3" fill="#8EC400"/>
            </svg>
          </div>
          <div class="logo-text" *ngIf="!collapsed()">
            <span class="logo-name">NX GLOW</span>
            <span class="logo-sub" style="color: #8EC400">Lite</span>
          </div>
        </div>
        <button class="collapse-btn" (click)="toggleCollapse()" *ngIf="!isMobile">
          <i class="pi" [class.pi-chevron-left]="!collapsed()" [class.pi-chevron-right]="collapsed()"></i>
        </button>
      </div>

      <!-- Navigation -->
      <nav class="sidebar-nav">
        <ul class="menu-list">
          @for (item of visibleMenuItems(); track item.label) {
            <li class="menu-item" [class.has-children]="item.children?.length"
                [class.expanded]="item.expanded">
              @if (item.children?.length) {
                <a class="menu-link parent-link"
                   [class.active]="isGroupActive(item)"
                   (click)="toggleGroup(item)"
                   [title]="collapsed() ? (item.label | translate) : ''">
                  <i class="menu-icon" [class]="item.icon"></i>
                  <span class="menu-label" *ngIf="!collapsed()">{{ item.label | translate }}</span>
                  <i class="expand-icon pi" *ngIf="!collapsed()"
                     [class.pi-chevron-down]="item.expanded"
                     [class.pi-chevron-right]="!item.expanded"></i>
                </a>
                <ul class="submenu" *ngIf="item.expanded && !collapsed()">
                  @for (child of item.children; track child.label) {
                    <li class="submenu-item">
                      <a class="menu-link child-link"
                         [routerLink]="child.routerLink"
                         routerLinkActive="active"
                         [routerLinkActiveOptions]="{ exact: false }"
                         (click)="closeMobile()">
                        <span class="submenu-dot"></span>
                        <span class="menu-label">{{ child.label | translate }}</span>
                        <span class="badge" *ngIf="child.badge">{{ child.badge }}</span>
                      </a>
                    </li>
                  }
                </ul>
              } @else {
                <a class="menu-link"
                   [routerLink]="item.routerLink"
                   routerLinkActive="active"
                   [routerLinkActiveOptions]="{ exact: item.routerLink === '/dashboard' }"
                   [title]="collapsed() ? (item.label | translate) : ''"
                   (click)="closeMobile()">
                  <i class="menu-icon" [class]="item.icon"></i>
                  <span class="menu-label" *ngIf="!collapsed()">{{ item.label | translate }}</span>
                  <span class="badge" *ngIf="item.badge && !collapsed()">{{ item.badge }}</span>
                </a>
              }
            </li>
          }
        </ul>
      </nav>

      <!-- Footer -->
      <div class="sidebar-footer" *ngIf="!collapsed()">
        <div class="version-info">
          <span>v1.0.0</span>
        </div>
      </div>
    </aside>

    <!-- Mobile overlay -->
    <div class="sidebar-overlay" *ngIf="mobileOpen()" (click)="closeMobile()"></div>
  `,
  styles: [`
    .sidebar {
      position: fixed;
      left: 0;
      top: 0;
      bottom: 0;
      width: 260px;
      background: linear-gradient(180deg, #0f0035 0%, #1A005D 40%, #200068 100%);
      color: white;
      display: flex;
      flex-direction: column;
      z-index: 1000;
      transition: width 0.25s cubic-bezier(0.4, 0, 0.2, 1);
      overflow: hidden;
      box-shadow: 2px 0 12px rgba(0, 0, 0, 0.15);
    }

    .sidebar.collapsed {
      width: 72px;
    }

    .sidebar-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.08);
      min-height: 64px;
    }

    .logo-container {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      transition: opacity 0.2s;
    }

    .logo-container:hover {
      opacity: 0.85;
    }

    .logo-icon {
      flex-shrink: 0;
    }

    .logo-text {
      display: flex;
      flex-direction: column;
      white-space: nowrap;
    }

    .logo-name {
      font-size: 18px;
      font-weight: 800;
      letter-spacing: -0.5px;
      line-height: 1.1;
    }

    .logo-sub {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.5);
      font-weight: 400;
    }

    .collapse-btn {
      width: 28px;
      height: 28px;
      border: none;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 6px;
      color: rgba(255, 255, 255, 0.5);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;
      flex-shrink: 0;
    }

    .collapse-btn:hover {
      background: rgba(255, 255, 255, 0.15);
      color: white;
    }

    .sidebar-nav {
      flex: 1;
      overflow-y: auto;
      overflow-x: hidden;
      padding: 12px 8px;
    }

    .sidebar-nav::-webkit-scrollbar {
      width: 4px;
    }

    .sidebar-nav::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.15);
      border-radius: 4px;
    }

    .menu-list {
      list-style: none;
      margin: 0;
      padding: 0;
    }

    .menu-item {
      margin-bottom: 2px;
    }

    .menu-link {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 10px 12px;
      border-radius: 8px;
      color: rgba(255, 255, 255, 0.65);
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s ease;
      font-size: 13.5px;
      font-weight: 500;
      position: relative;
      white-space: nowrap;
      user-select: none;
    }

    .menu-link:hover {
      color: white;
      background: rgba(255, 255, 255, 0.08);
    }

    .menu-link.active {
      color: white;
      background: rgba(142, 196, 0, 0.18);
    }

    .menu-link.active::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 3px;
      height: 20px;
      background: #8EC400;
      border-radius: 0 3px 3px 0;
    }

    .menu-icon {
      font-size: 18px;
      width: 22px;
      text-align: center;
      flex-shrink: 0;
    }

    .menu-label {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .expand-icon {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.35);
      transition: transform 0.2s ease;
    }

    .badge {
      background: #FF585D;
      color: white;
      font-size: 11px;
      font-weight: 600;
      padding: 1px 7px;
      border-radius: 10px;
      min-width: 18px;
      text-align: center;
    }

    .submenu {
      list-style: none;
      margin: 2px 0 4px 0;
      padding: 0 0 0 20px;
    }

    .submenu-item {
      margin-bottom: 1px;
    }

    .child-link {
      padding: 8px 12px;
      font-size: 13px;
      gap: 10px;
    }

    .submenu-dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      flex-shrink: 0;
      transition: background 0.2s;
    }

    .child-link.active .submenu-dot {
      background: #8EC400;
    }

    .child-link:hover .submenu-dot {
      background: rgba(255, 255, 255, 0.5);
    }

    .sidebar-footer {
      padding: 12px 16px;
      border-top: 1px solid rgba(255, 255, 255, 0.08);
    }

    .version-info {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.3);
      text-align: center;
    }

    .sidebar-overlay {
      display: none;
    }

    /* Collapsed state tooltip on hover */
    .collapsed .menu-link {
      justify-content: center;
      padding: 12px;
    }

    .collapsed .menu-link.active::before {
      left: 0;
    }

    @media (max-width: 1024px) {
      .sidebar {
        transform: translateX(-100%);
        width: 280px;
      }

      .sidebar.mobile-open {
        transform: translateX(0);
      }

      .sidebar-overlay {
        display: block;
        position: fixed;
        inset: 0;
        background: rgba(0, 0, 0, 0.5);
        z-index: 999;
        animation: fadeIn 0.2s ease;
      }

      @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
      }
    }
  `]
})
export class SidebarComponent implements OnInit, OnDestroy {
  collapsed = signal(false);
  mobileOpen = signal(false);
  isMobile = false;

  private routerSub!: Subscription;

  menuItems: MenuItem[] = [
    {
      label: 'nav.dashboard',
      icon: 'pi pi-th-large',
      routerLink: '/dashboard'
    },
    {
      label: 'nav.inbound',
      icon: 'pi pi-download',
      children: [
        { label: 'nav.inbound_orders', icon: '', routerLink: '/inbound/orders' },
        { label: 'nav.inbound_receive', icon: '', routerLink: '/inbound/receive' },
        { label: 'nav.inbound_confirm', icon: '', routerLink: '/inbound/confirm' },
        { label: 'nav.inbound_putaway', icon: '', routerLink: '/inbound/putaway' }
      ],
      roles: ['ADMIN', 'INBOUND', 'OPERATOR']
    },
    {
      label: 'nav.outbound',
      icon: 'pi pi-upload',
      children: [
        { label: 'nav.outbound_orders', icon: '', routerLink: '/outbound/orders' },
        { label: 'nav.outbound_picking', icon: '', routerLink: '/outbound/picking' },
        { label: 'nav.outbound_packing', icon: '', routerLink: '/outbound/packing' },
        { label: 'nav.outbound_shipping', icon: '', routerLink: '/outbound/shipping' }
      ],
      roles: ['ADMIN', 'OUTBOUND', 'OPERATOR']
    },
    {
      label: 'nav.inventory',
      icon: 'pi pi-box',
      children: [
        { label: 'nav.inventory_stock', icon: '', routerLink: '/inventory/stock' },
        { label: 'nav.inventory_lpn', icon: '', routerLink: '/inventory/lpn' },
        { label: 'nav.inventory_adjustment', icon: '', routerLink: '/inventory/adjustment' },
        { label: 'nav.inventory_transfer', icon: '', routerLink: '/inventory/transfer' },
        { label: 'nav.inventory_count', icon: '', routerLink: '/inventory/count' }
      ],
      roles: ['ADMIN', 'INVENTORY', 'OPERATOR']
    },
    {
      label: 'nav.master',
      icon: 'pi pi-database',
      children: [
        { label: 'nav.master_products', icon: '', routerLink: '/master/products' },
        { label: 'nav.master_locations', icon: '', routerLink: '/master/locations' },
        { label: 'nav.master_customers', icon: '', routerLink: '/master/customers' },
        { label: 'nav.master_warehouses', icon: '', routerLink: '/master/warehouses' },
        { label: 'nav.master_users', icon: '', routerLink: '/master/users', roles: ['ADMIN'] },
        { label: 'nav.master_lpn_config', icon: '', routerLink: '/master/lpn-config' }
      ],
      roles: ['ADMIN', 'MASTER']
    },
    {
      label: 'nav.reports',
      icon: 'pi pi-chart-bar',
      routerLink: '/reports',
      roles: ['ADMIN', 'REPORTS']
    }
  ];

  visibleMenuItems = computed(() => {
    return this.menuItems.filter(item => {
      if (!item.roles || item.roles.length === 0) return true;
      return this.authService.hasAnyRole(item.roles);
    });
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.checkViewport();
    // Auto-expand active group on init
    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.expandActiveGroup();
    });
    this.expandActiveGroup();
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }

  @HostListener('window:resize')
  onResize(): void {
    this.checkViewport();
  }

  private checkViewport(): void {
    this.isMobile = window.innerWidth <= 1024;
    if (this.isMobile) {
      this.collapsed.set(false);
    }
  }

  @Output() collapsedChange = new EventEmitter<boolean>();

  toggleCollapse(): void {
    this.collapsed.update(v => !v);
    this.collapsedChange.emit(this.collapsed());
    if (this.collapsed()) {
      this.menuItems.forEach(item => item.expanded = false);
    } else {
      this.expandActiveGroup();
    }
  }

  toggleGroup(item: MenuItem): void {
    if (this.collapsed()) {
      this.collapsed.set(false);
      setTimeout(() => { item.expanded = true; }, 260);
      return;
    }
    item.expanded = !item.expanded;
  }

  isGroupActive(item: MenuItem): boolean {
    if (!item.children) return false;
    return item.children.some(child =>
      child.routerLink && this.router.url.startsWith(child.routerLink)
    );
  }

  navigateHome(): void {
    this.router.navigate(['/dashboard']);
    this.closeMobile();
  }

  openMobile(): void {
    this.mobileOpen.set(true);
  }

  closeMobile(): void {
    this.mobileOpen.set(false);
  }

  private expandActiveGroup(): void {
    this.menuItems.forEach(item => {
      if (item.children) {
        item.expanded = this.isGroupActive(item);
      }
    });
  }
}
