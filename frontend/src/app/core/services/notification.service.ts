import { Injectable, signal, computed } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { environment } from '@env/environment';

export interface NotificationItem {
  id: string;
  severity: 'success' | 'info' | 'warn' | 'error';
  summary: string;
  detail: string;
  timestamp: Date;
  read: boolean;
  module?: string;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private notifications = signal<NotificationItem[]>([]);

  readonly allNotifications = this.notifications.asReadonly();
  readonly unreadCount = computed(() =>
    this.notifications().filter(n => !n.read).length
  );

  addNotification(notification: Omit<NotificationItem, 'id' | 'timestamp' | 'read'>): void {
    const item: NotificationItem = {
      ...notification,
      id: crypto.randomUUID(),
      timestamp: new Date(),
      read: false
    };
    this.notifications.update(list => [item, ...list].slice(0, 50));
  }

  markAsRead(id: string): void {
    this.notifications.update(list =>
      list.map(n => n.id === id ? { ...n, read: true } : n)
    );
  }

  markAllRead(): void {
    this.notifications.update(list =>
      list.map(n => ({ ...n, read: true }))
    );
  }

  clearAll(): void {
    this.notifications.set([]);
  }

  removeNotification(id: string): void {
    this.notifications.update(list => list.filter(n => n.id !== id));
  }
}
