import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { StatCardComponent } from '@shared/components/stat-card/stat-card.component';
import { ApiService } from '@core/services/api.service';

interface DashboardSummary {
  kpiCards: KpiCard[];
  inboundByStatus: StatusCount[];
  outboundByStatus: StatusCount[];
  recentActivities: ActivityItem[];
  warehouseUtilization: WarehouseUtil;
  inboundProductivity: ProductivitySummary;
  alerts: AlertItem[];
}

interface ApiResponse<T> {
  status: string;
  data: T;
}

interface KpiCard {
  title: string;
  value: number;
  unit: string;
  trend: number;
  icon: string;
}

interface StatusCount {
  status: string;
  count: number;
  color: string;
}

interface ActivityItem {
  description: string;
  user: string;
  module: string;
  timestamp: string;
}

interface WarehouseUtil {
  totalLocations: number;
  usedLocations: number;
  utilizationPercent: number;
}

interface AlertItem {
  severity: string;
  message: string;
  module: string;
  timestamp: string;
}

interface ProductivitySummary {
  milestoneCapturedToday: number;
  productivityCapturedToday: number;
  averageLinesPerEvent: number;
  operationBreakdown: StatusCount[];
}

interface ProductivityTrend {
  dateFrom: string;
  dateTo: string;
  trendPoints: ProductivityTrendPoint[];
}

interface ProductivityTrendPoint {
  date: string;
  milestones: number;
  productivityEvents: number;
  processedLines: number;
}

@Component({
  selector: 'wms-dashboard',
  standalone: true,
  imports: [
    CommonModule, TranslateModule, ChartModule,
    SkeletonModule, ButtonModule, TagModule,
    PageHeaderComponent, StatCardComponent
  ],
  template: `
    <wms-page-header
      title="dashboard.title"
      subtitle="dashboard.subtitle"
      icon="pi pi-th-large">
      <button pButton
              label="{{ 'dashboard.refresh' | translate }}"
              icon="pi pi-refresh"
              class="p-button-outlined p-button-sm"
              (click)="loadDashboard()">
      </button>
    </wms-page-header>

    <!-- KPI Cards -->
    <div class="kpi-grid">
      <wms-stat-card
        icon="pi pi-download"
        [iconBg]="'rgba(91, 194, 231, 0.12)'"
        [iconColor]="'#0369a1'"
        [value]="data()?.kpiCards?.[0]?.value || 0"
        label="{{ 'dashboard.today_inbound' | translate }}"
        [trend]="data()?.kpiCards?.[0]?.trend ?? null"
        variant="secondary">
      </wms-stat-card>

      <wms-stat-card
        icon="pi pi-upload"
        [iconBg]="'rgba(26, 0, 93, 0.08)'"
        [iconColor]="'#1A005D'"
        [value]="data()?.kpiCards?.[1]?.value || 0"
        label="{{ 'dashboard.today_outbound' | translate }}"
        [trend]="data()?.kpiCards?.[1]?.trend ?? null"
        variant="primary">
      </wms-stat-card>

      <wms-stat-card
        icon="pi pi-box"
        [iconBg]="'rgba(142, 196, 0, 0.12)'"
        [iconColor]="'#4d7c0f'"
        [value]="data()?.kpiCards?.[2]?.value || 0"
        label="{{ 'dashboard.total_sku' | translate }}"
        [trend]="data()?.kpiCards?.[2]?.trend ?? null"
        variant="accent">
      </wms-stat-card>

      <wms-stat-card
        icon="pi pi-exclamation-triangle"
        [iconBg]="'rgba(255, 88, 93, 0.1)'"
        [iconColor]="'#dc2626'"
        [value]="data()?.alerts?.length || 0"
        label="{{ 'dashboard.active_alerts' | translate }}"
        variant="danger">
      </wms-stat-card>
    </div>

    <!-- Charts Row -->
    <div class="charts-row">
      <div class="chart-card">
        <div class="card-header">
          <h3>{{ 'dashboard.inbound_status' | translate }}</h3>
        </div>
        <div class="chart-body">
          <p-chart type="doughnut" [data]="inboundChartData()" [options]="doughnutOptions" height="260px"></p-chart>
        </div>
      </div>

      <div class="chart-card">
        <div class="card-header">
          <h3>{{ 'dashboard.outbound_status' | translate }}</h3>
        </div>
        <div class="chart-body">
          <p-chart type="doughnut" [data]="outboundChartData()" [options]="doughnutOptions" height="260px"></p-chart>
        </div>
      </div>

      <div class="chart-card utilization-card">
        <div class="card-header">
          <h3>{{ 'dashboard.warehouse_utilization' | translate }}</h3>
        </div>
        <div class="utilization-body">
          <div class="utilization-gauge">
            <svg viewBox="0 0 120 120" class="gauge-svg">
              <circle cx="60" cy="60" r="52" fill="none" stroke="#e5e7eb" stroke-width="10"/>
              <circle cx="60" cy="60" r="52" fill="none" stroke="#8EC400" stroke-width="10"
                      [attr.stroke-dasharray]="gaugeDash()"
                      stroke-dashoffset="0"
                      transform="rotate(-90 60 60)"
                      stroke-linecap="round"/>
            </svg>
            <div class="gauge-label">
              <span class="gauge-value">{{ data()?.warehouseUtilization?.utilizationPercent || 0 }}%</span>
              <span class="gauge-sub">Used</span>
            </div>
          </div>
          <div class="util-stats">
            <div class="util-stat">
              <span class="util-num">{{ data()?.warehouseUtilization?.usedLocations || 0 }}</span>
              <span class="util-lbl">Locations Used</span>
            </div>
            <div class="util-stat">
              <span class="util-num">{{ data()?.warehouseUtilization?.totalLocations || 0 }}</span>
              <span class="util-lbl">Total Locations</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Inbound Productivity Analysis -->
    <div class="productivity-card">
      <div class="card-header">
        <h3>Inbound Productivity Analysis</h3>
      </div>
      <div class="productivity-body">
        <div class="productivity-metrics">
          <div class="prod-metric">
            <span class="prod-label">Milestones Captured (Today)</span>
            <span class="prod-value">{{ data()?.inboundProductivity?.milestoneCapturedToday || 0 }}</span>
          </div>
          <div class="prod-metric">
            <span class="prod-label">Productivity Captured (Today)</span>
            <span class="prod-value">{{ data()?.inboundProductivity?.productivityCapturedToday || 0 }}</span>
          </div>
          <div class="prod-metric">
            <span class="prod-label">Avg Lines / Event</span>
            <span class="prod-value">{{ data()?.inboundProductivity?.averageLinesPerEvent || 0 }}</span>
          </div>
        </div>
        <div class="prod-breakdown">
          @for (item of data()?.inboundProductivity?.operationBreakdown || []; track item.status) {
            <div class="prod-breakdown-item">
              <span class="prod-dot" [style.background]="item.color"></span>
              <span class="prod-status">{{ item.status }}</span>
              <span class="prod-count">{{ item.count }}</span>
            </div>
          }
        </div>
      </div>
    </div>

    <div class="chart-card trend-card">
      <div class="card-header">
        <h3>Inbound Productivity Trend (Last 7 Days)</h3>
      </div>
      <div class="chart-body">
        <p-chart type="bar" [data]="productivityTrendChartData()" [options]="trendChartOptions" height="280px"></p-chart>
      </div>
    </div>

    <!-- Bottom Row: Activities & Alerts -->
    <div class="bottom-row">
      <div class="activity-card">
        <div class="card-header">
          <h3>{{ 'dashboard.recent_activity' | translate }}</h3>
          <button pButton label="View All" class="p-button-text p-button-sm"></button>
        </div>
        <div class="activity-list">
          @for (activity of data()?.recentActivities || []; track activity.timestamp; let i = $index) {
            <div class="activity-item" [class.fade-in]="true" [style.animation-delay]="i * 50 + 'ms'">
              <div class="activity-dot" [class]="'module-' + activity.module"></div>
              <div class="activity-content">
                <p class="activity-desc">{{ activity.description }}</p>
                <div class="activity-meta">
                  <span class="activity-user">
                    <i class="pi pi-user"></i> {{ activity.user }}
                  </span>
                  <span class="activity-time">{{ activity.timestamp | date:'HH:mm' }}</span>
                </div>
              </div>
            </div>
          }
          @empty {
            <div class="empty-activity">No recent activity</div>
          }
        </div>
      </div>

      <div class="alerts-card">
        <div class="card-header">
          <h3>{{ 'dashboard.alerts' | translate }}</h3>
          <p-tag [value]="(data()?.alerts?.length || 0) + ' active'" severity="danger" *ngIf="data()?.alerts?.length"></p-tag>
        </div>
        <div class="alerts-list">
          @for (alert of data()?.alerts || []; track alert.timestamp; let i = $index) {
            <div class="alert-item" [class]="'severity-' + alert.severity">
              <div class="alert-icon">
                <i class="pi" [class]="getAlertIcon(alert.severity)"></i>
              </div>
              <div class="alert-content">
                <p class="alert-msg">{{ alert.message }}</p>
                <span class="alert-meta">{{ alert.module }} &middot; {{ alert.timestamp | date:'HH:mm' }}</span>
              </div>
            </div>
          }
          @empty {
            <div class="no-alerts">
              <i class="pi pi-check-circle"></i>
              <span>All systems operational</span>
            </div>
          }
        </div>
      </div>
    </div>
  `,
  styles: [`
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 18px;
      margin-bottom: 24px;
    }

    @media (max-width: 1200px) {
      .kpi-grid { grid-template-columns: repeat(2, 1fr); }
    }

    @media (max-width: 640px) {
      .kpi-grid { grid-template-columns: 1fr; }
    }

    .charts-row {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 18px;
      margin-bottom: 24px;
    }

    @media (max-width: 1200px) {
      .charts-row {
        grid-template-columns: 1fr 1fr;
      }
      .utilization-card {
        grid-column: span 2;
      }
    }

    @media (max-width: 768px) {
      .charts-row { grid-template-columns: 1fr; }
      .utilization-card { grid-column: span 1; }
    }

    .chart-card, .activity-card, .alerts-card {
      background: white;
      border-radius: 14px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }

    .trend-card {
      margin-bottom: 24px;
    }

    .productivity-card {
      background: white;
      border-radius: 14px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
      margin-bottom: 24px;
    }

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16px 20px;
      border-bottom: 1px solid #f3f4f6;
    }

    .card-header h3 {
      margin: 0;
      font-size: 15px;
      font-weight: 700;
      color: #111827;
    }

    .chart-body {
      padding: 20px;
      display: flex;
      justify-content: center;
    }

    /* Utilization card */
    .utilization-body {
      padding: 20px;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 20px;
    }

    .utilization-gauge {
      position: relative;
      width: 140px;
      height: 140px;
    }

    .gauge-svg {
      width: 100%;
      height: 100%;
    }

    .gauge-label {
      position: absolute;
      inset: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }

    .gauge-value {
      font-size: 28px;
      font-weight: 800;
      color: #111827;
    }

    .gauge-sub {
      font-size: 12px;
      color: #9ca3af;
    }

    .util-stats {
      display: flex;
      gap: 32px;
    }

    .util-stat {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .util-num {
      font-size: 20px;
      font-weight: 700;
      color: #111827;
    }

    .util-lbl {
      font-size: 12px;
      color: #6b7280;
    }

    /* Bottom row */
    .bottom-row {
      display: grid;
      grid-template-columns: 3fr 2fr;
      gap: 18px;
    }

    @media (max-width: 1024px) {
      .bottom-row { grid-template-columns: 1fr; }
    }

    .activity-list {
      padding: 8px 16px;
      max-height: 360px;
      overflow-y: auto;
    }

    .activity-item {
      display: flex;
      gap: 12px;
      padding: 12px 4px;
      border-bottom: 1px solid #f9fafb;
      animation: fadeIn 0.3s ease both;
    }

    .activity-item:last-child {
      border-bottom: none;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateX(-8px); }
      to { opacity: 1; transform: translateX(0); }
    }

    .activity-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-top: 6px;
      flex-shrink: 0;
    }

    .module-INBOUND { background: #5BC2E7; }
    .module-OUTBOUND { background: #1A005D; }
    .module-INVENTORY { background: #8EC400; }
    .module-SYSTEM { background: #FF9E1B; }

    .activity-content {
      flex: 1;
      min-width: 0;
    }

    .activity-desc {
      margin: 0;
      font-size: 13px;
      color: #374151;
      line-height: 1.4;
    }

    .activity-meta {
      display: flex;
      gap: 12px;
      margin-top: 4px;
      font-size: 12px;
      color: #9ca3af;
    }

    .activity-user {
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .activity-user i {
      font-size: 11px;
    }

    .empty-activity {
      padding: 32px;
      text-align: center;
      font-size: 13px;
      color: #9ca3af;
    }

    /* Alerts */
    .alerts-list {
      padding: 8px 16px;
      max-height: 360px;
      overflow-y: auto;
    }

    .alert-item {
      display: flex;
      gap: 12px;
      padding: 12px 8px;
      border-radius: 8px;
      margin-bottom: 6px;
    }

    .alert-item.severity-ERROR { background: #fef2f2; }
    .alert-item.severity-WARN { background: #fffbeb; }
    .alert-item.severity-INFO { background: #eff6ff; }

    .alert-icon {
      width: 32px;
      height: 32px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .severity-ERROR .alert-icon { background: #fee2e2; color: #dc2626; }
    .severity-WARN .alert-icon { background: #fef3c7; color: #b45309; }
    .severity-INFO .alert-icon { background: #dbeafe; color: #1d4ed8; }

    .alert-content {
      flex: 1;
    }

    .alert-msg {
      margin: 0;
      font-size: 13px;
      color: #374151;
      font-weight: 500;
    }

    .alert-meta {
      font-size: 11.5px;
      color: #9ca3af;
      margin-top: 2px;
    }

    .no-alerts {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      padding: 40px;
      color: #9ca3af;
    }

    .no-alerts i {
      font-size: 32px;
      color: #8EC400;
    }

    .no-alerts span {
      font-size: 13px;
    }

    .productivity-body {
      padding: 16px 20px 18px;
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
    }

    @media (max-width: 1024px) {
      .productivity-body {
        grid-template-columns: 1fr;
      }
    }

    .productivity-metrics {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 10px;
    }

    @media (max-width: 768px) {
      .productivity-metrics {
        grid-template-columns: 1fr;
      }
    }

    .prod-metric {
      background: #f9fafb;
      border: 1px solid #f3f4f6;
      border-radius: 10px;
      padding: 12px;
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .prod-label {
      font-size: 12px;
      color: #6b7280;
    }

    .prod-value {
      font-size: 22px;
      font-weight: 700;
      color: #111827;
      line-height: 1;
    }

    .prod-breakdown {
      border: 1px solid #f3f4f6;
      border-radius: 10px;
      overflow: hidden;
      align-self: start;
    }

    .prod-breakdown-item {
      display: grid;
      grid-template-columns: auto 1fr auto;
      align-items: center;
      gap: 10px;
      padding: 10px 12px;
      border-bottom: 1px solid #f9fafb;
      font-size: 13px;
    }

    .prod-breakdown-item:last-child {
      border-bottom: none;
    }

    .prod-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
    }

    .prod-status {
      color: #374151;
    }

    .prod-count {
      color: #111827;
      font-weight: 700;
    }
  `]
})
export class DashboardComponent implements OnInit {
  data = signal<DashboardSummary | null>(null);
  trend = signal<ProductivityTrend | null>(null);
  loading = signal(true);

  doughnutOptions = {
    cutout: '70%',
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 16,
          usePointStyle: true,
          pointStyle: 'circle',
          font: { size: 12, family: 'Inter' }
        }
      }
    }
  };

  trendChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 14,
          usePointStyle: true,
          pointStyle: 'circle',
          font: { size: 12, family: 'Inter' }
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: '#6b7280'
        },
        grid: {
          color: '#f3f4f6'
        }
      },
      y: {
        beginAtZero: true,
        ticks: {
          color: '#6b7280'
        },
        grid: {
          color: '#f3f4f6'
        }
      }
    }
  };

  inboundChartData = computed(() => {
    const statuses = this.data()?.inboundByStatus || [];
    return {
      labels: statuses.map(s => s.status),
      datasets: [{
        data: statuses.map(s => s.count),
        backgroundColor: statuses.map(s => s.color),
        borderWidth: 0,
        hoverOffset: 6
      }]
    };
  });

  outboundChartData = computed(() => {
    const statuses = this.data()?.outboundByStatus || [];
    return {
      labels: statuses.map(s => s.status),
      datasets: [{
        data: statuses.map(s => s.count),
        backgroundColor: statuses.map(s => s.color),
        borderWidth: 0,
        hoverOffset: 6
      }]
    };
  });

  productivityTrendChartData = computed(() => {
    const points = this.trend()?.trendPoints || [];
    return {
      labels: points.map(p => p.date.slice(5)),
      datasets: [
        {
          type: 'line',
          label: 'Processed Lines',
          data: points.map(p => p.processedLines),
          borderColor: '#1A005D',
          backgroundColor: 'rgba(26, 0, 93, 0.12)',
          borderWidth: 2,
          tension: 0.35,
          yAxisID: 'y'
        },
        {
          type: 'bar',
          label: 'Milestones',
          data: points.map(p => p.milestones),
          backgroundColor: '#5BC2E7',
          borderRadius: 6,
          yAxisID: 'y'
        },
        {
          type: 'bar',
          label: 'Productivity Events',
          data: points.map(p => p.productivityEvents),
          backgroundColor: '#8EC400',
          borderRadius: 6,
          yAxisID: 'y'
        }
      ]
    };
  });

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading.set(true);
    this.api.get<DashboardSummary>('/dashboard/summary').subscribe({
      next: (response) => {
        if (response.status === 'SUCCESS') {
          this.data.set(response.data);
        }
        this.loading.set(false);
      },
      error: () => {
        // Load demo data for development
        this.data.set(this.getDemoData());
        this.loading.set(false);
      }
    });

    const to = new Date();
    const from = new Date(to.getTime() - 6 * 24 * 60 * 60 * 1000);
    const dateFrom = from.toISOString().slice(0, 10);
    const dateTo = to.toISOString().slice(0, 10);
    this.api.get<ProductivityTrend>(`/dashboard/productivity-trend?dateFrom=${dateFrom}&dateTo=${dateTo}`).subscribe({
      next: (response: ApiResponse<ProductivityTrend>) => {
        if (response.status === 'SUCCESS') {
          this.trend.set(response.data);
        }
      },
      error: () => {
        this.trend.set(this.getDemoTrendData());
      }
    });
  }

  gaugeDash(): string {
    const pct = this.data()?.warehouseUtilization?.utilizationPercent || 0;
    const circumference = 2 * Math.PI * 52;
    const filled = (pct / 100) * circumference;
    return `${filled} ${circumference}`;
  }

  getAlertIcon(severity: string): string {
    const map: Record<string, string> = {
      ERROR: 'pi-times-circle',
      WARN: 'pi-exclamation-triangle',
      INFO: 'pi-info-circle'
    };
    return map[severity] || 'pi-info-circle';
  }

  private getDemoData(): DashboardSummary {
    return {
      kpiCards: [
        { title: 'Today Inbound', value: 47, unit: 'orders', trend: 12.5, icon: 'pi-download' },
        { title: 'Today Outbound', value: 83, unit: 'orders', trend: -3.2, icon: 'pi-upload' },
        { title: 'Total SKU', value: 12450, unit: 'items', trend: 2.1, icon: 'pi-box' },
        { title: 'Active Alerts', value: 3, unit: '', trend: 0, icon: 'pi-exclamation-triangle' }
      ],
      inboundByStatus: [
        { status: 'Pending', count: 12, color: '#FF9E1B' },
        { status: 'Receiving', count: 8, color: '#5BC2E7' },
        { status: 'Confirmed', count: 18, color: '#8EC400' },
        { status: 'Put Away', count: 9, color: '#1A005D' }
      ],
      outboundByStatus: [
        { status: 'New', count: 15, color: '#5BC2E7' },
        { status: 'Picking', count: 22, color: '#FF9E1B' },
        { status: 'Packing', count: 18, color: '#1A005D' },
        { status: 'Shipped', count: 28, color: '#8EC400' }
      ],
      recentActivities: [
        { description: 'Inbound order #AV-2024-0847 received at Dock 3', user: 'Tanaka K.', module: 'INBOUND', timestamp: new Date().toISOString() },
        { description: 'Picking wave #WV-0123 completed - 45 items', user: 'Sato M.', module: 'OUTBOUND', timestamp: new Date(Date.now() - 300000).toISOString() },
        { description: 'Inventory adjustment: SKU PRD-8821 +15 units', user: 'Yamada T.', module: 'INVENTORY', timestamp: new Date(Date.now() - 900000).toISOString() },
        { description: 'Shipment #SP-2024-1592 dispatched to MUJI Shibuya', user: 'Suzuki H.', module: 'OUTBOUND', timestamp: new Date(Date.now() - 1800000).toISOString() },
        { description: 'System: Warehouse utilization alert threshold (85%)', user: 'System', module: 'SYSTEM', timestamp: new Date(Date.now() - 3600000).toISOString() }
      ],
      warehouseUtilization: {
        totalLocations: 2400,
        usedLocations: 1980,
        utilizationPercent: 82.5
      },
      inboundProductivity: {
        milestoneCapturedToday: 48,
        productivityCapturedToday: 12,
        averageLinesPerEvent: 4,
        operationBreakdown: [
          { status: 'Operation 10', count: 12, color: '#5BC2E7' },
          { status: 'Operation 20', count: 6, color: '#1A005D' },
          { status: 'Operation 60', count: 2, color: '#8EC400' }
        ]
      },
      alerts: [
        { severity: 'ERROR', message: 'Low stock alert: SKU PRD-4421 below minimum threshold (5 units)', module: 'Inventory', timestamp: new Date(Date.now() - 600000).toISOString() },
        { severity: 'WARN', message: '3 inbound orders pending confirmation for over 24 hours', module: 'Inbound', timestamp: new Date(Date.now() - 1200000).toISOString() },
        { severity: 'INFO', message: 'Scheduled maintenance at 23:00 - picking system update', module: 'System', timestamp: new Date(Date.now() - 7200000).toISOString() }
      ]
    };
  }

  private getDemoTrendData(): ProductivityTrend {
    const points: ProductivityTrendPoint[] = [];
    for (let i = 6; i >= 0; i--) {
      const d = new Date(Date.now() - i * 24 * 60 * 60 * 1000);
      points.push({
        date: d.toISOString().slice(0, 10),
        milestones: 12 + (6 - i) * 2,
        productivityEvents: 2 + Math.floor((6 - i) / 2),
        processedLines: 8 + (6 - i) * 3
      });
    }
    return {
      dateFrom: points[0].date,
      dateTo: points[points.length - 1].date,
      trendPoints: points
    };
  }
}
