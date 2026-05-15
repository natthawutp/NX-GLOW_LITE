import { Component, Output, EventEmitter, signal, computed, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { InputTextModule } from 'primeng/inputtext';
import { MenuItem } from 'primeng/api';
import { AuthService } from '@core/services/auth.service';
import { NotificationService } from '@core/services/notification.service';
import { environment } from '@env/environment';

@Component({
  selector: 'wms-topbar',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule, TranslateModule,
    OverlayPanelModule, BadgeModule, AvatarModule, MenuModule, InputTextModule
  ],
  template: `
    <header class="topbar">
      <!-- Left section -->
      <div class="topbar-left">
        <button class="hamburger-btn" (click)="menuToggle.emit()">
          <i class="pi pi-bars"></i>
        </button>

        <!-- Global search -->
        <div class="global-search">
          <i class="pi pi-search search-icon"></i>
          <input type="text"
                 class="search-input"
                 [placeholder]="'topbar.search_placeholder' | translate"
                 [(ngModel)]="searchQuery"
                 (keyup.enter)="onSearch()">
          <kbd class="search-shortcut" *ngIf="!searchQuery">⌘K</kbd>
        </div>
      </div>

      <!-- Right section -->
      <div class="topbar-right">
        <!-- Warehouse info -->
        <div class="warehouse-badge">
          <i class="pi pi-building"></i>
          <span class="warehouse-text">
            {{ currentTenant().warehouseCode || 'WHS01' }}
          </span>
        </div>

        <!-- Language selector -->
        <div class="lang-selector">
          <button class="icon-btn" (click)="langPanel.toggle($event)">
            <span class="lang-code">{{ currentLang() }}</span>
          </button>
          <p-overlayPanel #langPanel [style]="{ width: '160px' }">
            <div class="lang-list">
              @for (lang of languages; track lang.code) {
                <button class="lang-option"
                        [class.active]="currentLang() === lang.code"
                        (click)="switchLanguage(lang.code); langPanel.hide()">
                  <span class="lang-flag">{{ lang.flag }}</span>
                  <span>{{ lang.name }}</span>
                </button>
              }
            </div>
          </p-overlayPanel>
        </div>

        <!-- Notifications -->
        <button class="icon-btn notification-btn" (click)="notifPanel.toggle($event)">
          <i class="pi pi-bell"></i>
          <span class="notif-badge" *ngIf="unreadCount() > 0">{{ unreadCount() > 9 ? '9+' : unreadCount() }}</span>
        </button>
        <p-overlayPanel #notifPanel [style]="{ width: '360px' }">
          <div class="notif-header">
            <h4>{{ 'topbar.notifications' | translate }}</h4>
            <button class="text-btn" (click)="markAllRead()">{{ 'topbar.mark_all_read' | translate }}</button>
          </div>
          <div class="notif-list">
            @for (notif of notifications(); track notif.id) {
              <div class="notif-item" [class.unread]="!notif.read" (click)="onNotificationClick(notif.id)">
                <div class="notif-icon" [class]="'severity-' + notif.severity">
                  <i class="pi" [class]="getNotifIcon(notif.severity)"></i>
                </div>
                <div class="notif-content">
                  <div class="notif-title">{{ notif.summary }}</div>
                  <div class="notif-detail">{{ notif.detail }}</div>
                  <div class="notif-time">{{ notif.timestamp | date:'HH:mm' }}</div>
                </div>
              </div>
            } @empty {
              <div class="notif-empty">
                <i class="pi pi-check-circle"></i>
                <span>{{ 'topbar.no_notifications' | translate }}</span>
              </div>
            }
          </div>
        </p-overlayPanel>

        <!-- User menu -->
        <div class="user-menu" (click)="userPanel.toggle($event)">
          <p-avatar
            [label]="userInitial()"
            shape="circle"
            size="normal"
            [style]="{ 'background-color': '#8EC400', 'color': '#1A005D', 'font-weight': '700', 'font-size': '14px' }">
          </p-avatar>
          <div class="user-info" *ngIf="userName()">
            <span class="user-name">{{ userName() }}</span>
            <span class="user-role">{{ userRole() }}</span>
          </div>
          <i class="pi pi-chevron-down user-chevron"></i>
        </div>
        <p-overlayPanel #userPanel [style]="{ width: '220px' }">
          <div class="user-panel-header">
            <strong>{{ userName() }}</strong>
            <span class="user-email">{{ userEmail() }}</span>
          </div>
          <div class="user-panel-menu">
            <button class="user-panel-item" (click)="userPanel.hide()">
              <i class="pi pi-user"></i>
              <span>{{ 'topbar.profile' | translate }}</span>
            </button>
            <button class="user-panel-item" (click)="userPanel.hide()">
              <i class="pi pi-cog"></i>
              <span>{{ 'topbar.settings' | translate }}</span>
            </button>
            <hr class="divider">
            <button class="user-panel-item logout" (click)="onLogout(); userPanel.hide()">
              <i class="pi pi-sign-out"></i>
              <span>{{ 'topbar.logout' | translate }}</span>
            </button>
          </div>
        </p-overlayPanel>
      </div>
    </header>
  `,
  styles: [`
    .topbar {
      height: 64px;
      background: white;
      border-bottom: 1px solid #e5e7eb;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 24px;
      position: sticky;
      top: 0;
      z-index: 900;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
    }

    .topbar-left {
      display: flex;
      align-items: center;
      gap: 16px;
    }

    .hamburger-btn {
      display: none;
      width: 38px;
      height: 38px;
      border: none;
      background: transparent;
      border-radius: 8px;
      color: #374151;
      cursor: pointer;
      font-size: 18px;
      align-items: center;
      justify-content: center;
      transition: background 0.2s;
    }

    .hamburger-btn:hover {
      background: #f3f4f6;
    }

    @media (max-width: 1024px) {
      .hamburger-btn {
        display: flex;
      }
    }

    .global-search {
      position: relative;
      width: 360px;
    }

    .search-icon {
      position: absolute;
      left: 12px;
      top: 50%;
      transform: translateY(-50%);
      color: #9ca3af;
      font-size: 14px;
    }

    .search-input {
      width: 100%;
      height: 38px;
      padding: 0 40px 0 36px;
      border: 1px solid #e5e7eb;
      border-radius: 10px;
      background: #f9fafb;
      font-size: 13.5px;
      color: #374151;
      transition: all 0.2s;
      outline: none;
    }

    .search-input:focus {
      border-color: #1A005D;
      background: white;
      box-shadow: 0 0 0 3px rgba(26, 0, 93, 0.08);
    }

    .search-input::placeholder {
      color: #9ca3af;
    }

    .search-shortcut {
      position: absolute;
      right: 10px;
      top: 50%;
      transform: translateY(-50%);
      font-size: 11px;
      color: #9ca3af;
      background: #e5e7eb;
      padding: 2px 6px;
      border-radius: 4px;
      font-family: inherit;
      border: none;
    }

    @media (max-width: 768px) {
      .global-search { width: 200px; }
      .search-shortcut { display: none; }
    }

    @media (max-width: 480px) {
      .global-search { display: none; }
    }

    .topbar-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .warehouse-badge {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 6px 12px;
      background: #f0f9ff;
      border: 1px solid #bae6fd;
      border-radius: 8px;
      color: #0369a1;
      font-size: 12.5px;
      font-weight: 600;
    }

    .warehouse-badge i {
      font-size: 13px;
    }

    @media (max-width: 640px) {
      .warehouse-badge .warehouse-text { display: none; }
    }

    .icon-btn {
      width: 38px;
      height: 38px;
      border: none;
      background: transparent;
      border-radius: 8px;
      color: #6b7280;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 17px;
      transition: all 0.2s;
      position: relative;
    }

    .icon-btn:hover {
      background: #f3f4f6;
      color: #374151;
    }

    .lang-code {
      font-size: 12px;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .lang-list {
      display: flex;
      flex-direction: column;
      gap: 2px;
    }

    .lang-option {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 8px 12px;
      border: none;
      background: transparent;
      border-radius: 6px;
      cursor: pointer;
      font-size: 13.5px;
      color: #374151;
      width: 100%;
      text-align: left;
      transition: background 0.15s;
    }

    .lang-option:hover {
      background: #f3f4f6;
    }

    .lang-option.active {
      background: #eff6ff;
      color: #1A005D;
      font-weight: 600;
    }

    .lang-flag {
      font-size: 18px;
    }

    .notification-btn {
      position: relative;
    }

    .notif-badge {
      position: absolute;
      top: 4px;
      right: 4px;
      min-width: 16px;
      height: 16px;
      background: #FF585D;
      color: white;
      font-size: 10px;
      font-weight: 700;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 4px;
      line-height: 1;
    }

    .notif-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 12px;
      border-bottom: 1px solid #e5e7eb;
      margin-bottom: 8px;
    }

    .notif-header h4 {
      margin: 0;
      font-size: 14px;
      color: #111827;
    }

    .text-btn {
      border: none;
      background: transparent;
      color: #1A005D;
      font-size: 12px;
      font-weight: 500;
      cursor: pointer;
      padding: 0;
    }

    .text-btn:hover {
      text-decoration: underline;
    }

    .notif-list {
      max-height: 320px;
      overflow-y: auto;
    }

    .notif-item {
      display: flex;
      gap: 10px;
      padding: 10px 4px;
      border-radius: 6px;
      cursor: pointer;
      transition: background 0.15s;
    }

    .notif-item:hover {
      background: #f9fafb;
    }

    .notif-item.unread {
      background: #fefce8;
    }

    .notif-icon {
      width: 32px;
      height: 32px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      font-size: 14px;
    }

    .severity-success { background: #ecfccb; color: #4d7c0f; }
    .severity-info { background: #e0f2fe; color: #0369a1; }
    .severity-warn { background: #fef3c7; color: #b45309; }
    .severity-error { background: #fee2e2; color: #dc2626; }

    .notif-content {
      flex: 1;
      min-width: 0;
    }

    .notif-title {
      font-size: 13px;
      font-weight: 600;
      color: #111827;
      margin-bottom: 2px;
    }

    .notif-detail {
      font-size: 12px;
      color: #6b7280;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .notif-time {
      font-size: 11px;
      color: #9ca3af;
      margin-top: 2px;
    }

    .notif-empty {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      padding: 32px 16px;
      color: #9ca3af;
    }

    .notif-empty i {
      font-size: 28px;
    }

    .notif-empty span {
      font-size: 13px;
    }

    .user-menu {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 4px 8px 4px 4px;
      border-radius: 10px;
      cursor: pointer;
      transition: background 0.2s;
      margin-left: 4px;
    }

    .user-menu:hover {
      background: #f3f4f6;
    }

    .user-info {
      display: flex;
      flex-direction: column;
    }

    .user-name {
      font-size: 13px;
      font-weight: 600;
      color: #111827;
      line-height: 1.2;
    }

    .user-role {
      font-size: 11px;
      color: #9ca3af;
    }

    .user-chevron {
      font-size: 11px;
      color: #9ca3af;
    }

    @media (max-width: 640px) {
      .user-info { display: none; }
      .user-chevron { display: none; }
    }

    .user-panel-header {
      padding: 8px 4px 12px;
      border-bottom: 1px solid #e5e7eb;
      margin-bottom: 6px;
    }

    .user-panel-header strong {
      display: block;
      font-size: 14px;
      color: #111827;
    }

    .user-email {
      font-size: 12px;
      color: #9ca3af;
    }

    .user-panel-menu {
      display: flex;
      flex-direction: column;
      gap: 2px;
    }

    .user-panel-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 8px 8px;
      border: none;
      background: transparent;
      border-radius: 6px;
      cursor: pointer;
      font-size: 13px;
      color: #374151;
      width: 100%;
      text-align: left;
      transition: background 0.15s;
    }

    .user-panel-item:hover {
      background: #f3f4f6;
    }

    .user-panel-item.logout {
      color: #dc2626;
    }

    .user-panel-item.logout:hover {
      background: #fef2f2;
    }

    .user-panel-item i {
      font-size: 15px;
      width: 18px;
      text-align: center;
    }

    .divider {
      border: none;
      border-top: 1px solid #e5e7eb;
      margin: 4px 0;
    }
  `]
})
export class TopbarComponent {
  @Output() menuToggle = new EventEmitter<void>();

  searchQuery = '';

  languages = [
    { code: 'en', name: 'English', flag: '🇺🇸' },
    { code: 'ja', name: '日本語', flag: '🇯🇵' },
    { code: 'th', name: 'ไทย', flag: '🇹🇭' }
  ];

  currentLang = signal(localStorage.getItem('wms_locale') || environment.defaultLocale);

  notifications = this.notificationService.allNotifications;
  unreadCount = this.notificationService.unreadCount;

  currentTenant = computed(() => ({
    companyCode: this.authService.getCompanyCode(),
    warehouseCode: this.authService.getWarehouseCode(),
    customerCode: this.authService.getCustomerCode()
  }));

  userName = computed(() => this.authService.user()?.displayName || '');
  userEmail = computed(() => this.authService.user()?.email || '');
  userRole = computed(() => {
    const roles = this.authService.userRoles();
    return roles.length > 0 ? roles[0] : '';
  });
  userInitial = computed(() => {
    const name = this.userName();
    return name ? name.charAt(0).toUpperCase() : 'U';
  });

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private translate: TranslateService
  ) {}

  switchLanguage(lang: string): void {
    this.currentLang.set(lang);
    this.translate.use(lang);
    localStorage.setItem('wms_locale', lang);
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      console.log('Global search:', this.searchQuery);
      // TODO: implement global search navigation
    }
  }

  onNotificationClick(id: string): void {
    this.notificationService.markAsRead(id);
  }

  markAllRead(): void {
    this.notificationService.markAllRead();
  }

  getNotifIcon(severity: string): string {
    const icons: Record<string, string> = {
      success: 'pi-check-circle',
      info: 'pi-info-circle',
      warn: 'pi-exclamation-triangle',
      error: 'pi-times-circle'
    };
    return icons[severity] || 'pi-info-circle';
  }

  onLogout(): void {
    this.authService.logout();
  }
}
