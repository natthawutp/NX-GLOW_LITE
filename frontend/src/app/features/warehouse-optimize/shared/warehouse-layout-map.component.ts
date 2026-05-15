import { CommonModule } from '@angular/common';
import { Component, Input, computed, signal } from '@angular/core';
import {
  LiveLocationState,
  SlottingAssignment,
  WarehouseAisle,
  WarehouseLayout,
  WarehouseLayoutLocation
} from './warehouse-optimize.models';
import {
  assignmentByLocation,
  colorForLocation,
  extractLocations,
  liveStateByLocation
} from './warehouse-optimize-layout.utils';

@Component({
  selector: 'wms-warehouse-layout-map',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="map-shell" *ngIf="layout(); else emptyState">
      <div class="map-legend">
        <span class="legend-chip"><i class="legend-swatch velocity-a"></i>Fast moving</span>
        <span class="legend-chip"><i class="legend-swatch velocity-b"></i>Medium moving</span>
        <span class="legend-chip"><i class="legend-swatch velocity-c"></i>Slow moving</span>
        <span class="legend-chip"><i class="legend-swatch category"></i>Category fallback</span>
        <span class="legend-chip"><i class="legend-swatch empty"></i>Empty / unassigned</span>
      </div>

      <div class="map-surface">
        <svg [attr.viewBox]="viewBox()" class="layout-svg" preserveAspectRatio="xMidYMid meet">
          <rect
            class="warehouse-boundary"
            [attr.x]="padding"
            [attr.y]="padding"
            [attr.width]="layout()!.warehouseWidth"
            [attr.height]="layout()!.warehouseHeight">
          </rect>

          <g *ngFor="let aisle of layout()!.aisles; trackBy: trackAisle">
            <rect
              class="aisle-outline"
              [attr.x]="padding + aisle.x"
              [attr.y]="padding + aisle.y"
              [attr.width]="aisle.width"
              [attr.height]="aisle.height">
            </rect>
            <text
              class="aisle-label"
              [attr.x]="padding + aisle.x + 0.75"
              [attr.y]="padding + aisle.y - 0.5">
              {{ aisle.zone }}
            </text>
          </g>

          <g *ngFor="let location of locations(); trackBy: trackLocation">
            <rect
              class="location-cell"
              [attr.x]="cellX(location)"
              [attr.y]="cellY(location)"
              [attr.width]="cellWidth(location)"
              [attr.height]="cellHeight(location)"
              [attr.fill]="locationFill(location)"
              [attr.stroke]="isHighlighted(location.location) ? '#0f172a' : '#ffffff'"
              [attr.stroke-width]="isHighlighted(location.location) ? 0.22 : 0.08"
              [attr.opacity]="locationOpacity(location)"
              [attr.rx]="0.14"
              [attr.ry]="0.14">
              <title>{{ tooltip(location) }}</title>
            </rect>

            <text
              *ngIf="showLabels && shouldShowLabel(location)"
              class="location-label"
              [attr.x]="cellX(location) + (cellWidth(location) / 2)"
              [attr.y]="cellY(location) + (cellHeight(location) / 2) + 0.16">
              {{ shortLabel(location.location) }}
            </text>
          </g>
        </svg>
      </div>
    </div>

    <ng-template #emptyState>
      <div class="empty-state">No warehouse layout loaded.</div>
    </ng-template>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
      height: 100%;
      min-height: 320px;
    }

    .map-shell {
      display: flex;
      flex-direction: column;
      gap: 10px;
      height: 100%;
    }

    .map-legend {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      font-size: 12px;
      color: #475569;
    }

    .legend-chip {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 4px 10px;
      border: 1px solid #dbe2ea;
      border-radius: 999px;
      background: #fff;
    }

    .legend-swatch {
      width: 10px;
      height: 10px;
      border-radius: 999px;
      display: inline-block;
    }

    .velocity-a { background: #ef4444; }
    .velocity-b { background: #f59e0b; }
    .velocity-c { background: #22c55e; }
    .category { background: #2563eb; }
    .empty { background: #cbd5e1; }

    .map-surface {
      flex: 1;
      min-height: 280px;
      border: 1px solid #dbe2ea;
      border-radius: 8px;
      background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
      overflow: hidden;
    }

    .layout-svg {
      width: 100%;
      height: 100%;
      display: block;
    }

    .warehouse-boundary {
      fill: rgba(255, 255, 255, 0.55);
      stroke: #334155;
      stroke-width: 0.3;
    }

    .aisle-outline {
      fill: rgba(148, 163, 184, 0.1);
      stroke: rgba(51, 65, 85, 0.28);
      stroke-width: 0.12;
      stroke-dasharray: 0.35 0.25;
    }

    .aisle-label {
      font-size: 1px;
      fill: #334155;
      font-weight: 700;
    }

    .location-cell {
      transition: opacity 0.2s ease;
    }

    .location-label {
      font-size: 0.62px;
      fill: rgba(255, 255, 255, 0.95);
      text-anchor: middle;
      dominant-baseline: middle;
      pointer-events: none;
      font-weight: 700;
    }

    .empty-state {
      min-height: 320px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #64748b;
      border: 1px dashed #cbd5e1;
      border-radius: 8px;
      background: #f8fafc;
    }
  `]
})
export class WarehouseLayoutMapComponent {
  readonly padding = 3;
  readonly layout = signal<WarehouseLayout | null>(null);
  readonly assignmentMap = signal(new Map<string, SlottingAssignment>());
  readonly liveMap = signal(new Map<string, LiveLocationState>());
  readonly highlightMap = signal(new Set<string>());
  readonly locations = computed(() => extractLocations(this.layout()));
  readonly viewBox = computed(() => {
    const layout = this.layout();
    if (!layout) {
      return '0 0 100 100';
    }
    return `0 0 ${layout.warehouseWidth + this.padding * 2} ${layout.warehouseHeight + this.padding * 2}`;
  });

  @Input() set dataLayout(value: WarehouseLayout | null) {
    this.layout.set(value);
  }

  @Input() set assignments(value: SlottingAssignment[] | null) {
    this.assignmentMap.set(assignmentByLocation(value ?? []));
  }

  @Input() set liveStates(value: LiveLocationState[] | null) {
    this.liveMap.set(liveStateByLocation(value ?? []));
  }

  @Input() set highlightLocations(value: string[] | null) {
    this.highlightMap.set(new Set(value ?? []));
  }

  @Input() showLabels = false;

  trackAisle(_: number, aisle: WarehouseAisle): number {
    return aisle.id;
  }

  trackLocation(_: number, location: WarehouseLayoutLocation): string {
    return location.location;
  }

  cellX(location: WarehouseLayoutLocation): number {
    return this.padding + (location.x ?? 0);
  }

  cellY(location: WarehouseLayoutLocation): number {
    return this.padding + (location.y ?? 0);
  }

  cellWidth(location: WarehouseLayoutLocation): number {
    return location.type === 'HIGH_RACK' ? 0.9 : 1.2;
  }

  cellHeight(location: WarehouseLayoutLocation): number {
    return location.type === 'HIGH_RACK' ? 0.9 : 1.1;
  }

  locationFill(location: WarehouseLayoutLocation): string {
    return colorForLocation(location, this.assignmentMap(), this.liveMap());
  }

  locationOpacity(location: WarehouseLayoutLocation): number {
    const state = this.liveMap().get(location.location);
    if (state?.physicalQty && state.physicalQty > 0) {
      return 1;
    }
    if (this.assignmentMap().has(location.location)) {
      return 0.9;
    }
    return 0.72;
  }

  isHighlighted(locationCode: string): boolean {
    return this.highlightMap().has(locationCode);
  }

  shouldShowLabel(location: WarehouseLayoutLocation): boolean {
    return this.isHighlighted(location.location) || (location.level ?? 1) === 1;
  }

  shortLabel(locationCode: string): string {
    const parts = locationCode.split('-');
    return parts.slice(-2).join('-');
  }

  tooltip(location: WarehouseLayoutLocation): string {
    const assignment = this.assignmentMap().get(location.location);
    const live = this.liveMap().get(location.location);
    const lines = [
      `Location: ${location.location}`,
      `Zone: ${location.zone ?? '-'}`,
      `Type: ${location.type ?? '-'}`,
      `Level: ${location.level ?? '-'}`,
      `Velocity: ${live?.velocityClass ?? assignment?.velocityClass ?? location.slottedClass ?? '-'}`,
      `SKU: ${live?.productCode ?? assignment?.productSku ?? location.slottedSku ?? '-'}`,
      `Physical Qty: ${live?.physicalQty ?? 0}`,
      `Available Qty: ${live?.availableQty ?? 0}`
    ];
    return lines.join('\n');
  }
}
