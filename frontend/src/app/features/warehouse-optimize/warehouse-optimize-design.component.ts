import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  NgZone,
  ViewChild,
  effect,
  inject
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { PageHeaderComponent } from '@shared/components/page-header/page-header.component';
import { WarehouseOptimizeNavComponent } from './shared/warehouse-optimize-nav.component';
import { WarehouseOptimizeStateService } from './shared/warehouse-optimize-state.service';
import { WarehouseOptimizeService } from './shared/warehouse-optimize.service';
import type { WarehouseDesignerDrawer } from './shared/warehouse-designer-globals';
import {
  LayoutGeneratorConfig,
  LayoutStats,
  WarehouseAisle,
  WarehouseDesignerLayout
} from './shared/warehouse-optimize.models';
import {
  createEmptyLayout,
  createGeneratedLayout,
  layoutStats,
  normalizeLayout,
  prettyLayoutJson
} from './shared/warehouse-optimize-layout.utils';

type DesignerMode = 'draw' | 'boundary' | 'select' | 'move' | 'delete' | 'measure';

interface PropertyFormModel {
  zone: string;
  type: string;
  direction: string;
  levels: number;
  tunnelLevelFrom: number;
  tunnelLevelTo: number;
  bayWidth: number;
  bayDepth: number;
  aisleWidth: number;
  pickFaceLevels: number[];
  startPointX: number;
  startPointY: number;
}

const DESIGNER_SCRIPT_PATH = 'assets/warehouse-optimize/aisle-drawer.js';

const STORAGE_TYPE_OPTIONS = [
  { value: 'HIGH_RACK', label: 'High Rack' },
  { value: 'SHELF', label: 'Shelving' },
  { value: 'DRIVE_IN', label: 'Drive-In Rack' },
  { value: 'FLOOR', label: 'Floor Storage' },
  { value: 'STAGING', label: 'Staging Area' },
  { value: 'EMPTY_FLOOR', label: 'Empty Floor' },
  { value: 'WALKWAY', label: 'Walkway' },
  { value: 'OBSTACLE', label: 'Obstacle' },
  { value: 'PILLAR', label: 'Pillar' },
  { value: 'TUNNEL_ZONE', label: 'Tunnel Zone' },
  { value: 'WORKSTATION', label: 'Workstation' },
  { value: 'MHE_PARKING', label: 'MHE Parking' },
  { value: 'BATTERY_ROOM', label: 'Battery Room' },
  { value: 'SAFETY_BUFFER', label: 'Safety Buffer' },
  { value: 'OFFICE_AREA', label: 'Office Area' },
  { value: 'DOCK_DOOR', label: 'Dock Door' }
] as const;

const DIRECTION_OPTIONS = [
  { value: 'AUTO', label: 'Auto' },
  { value: 'HORIZONTAL', label: 'Horizontal' },
  { value: 'VERTICAL', label: 'Vertical' }
] as const;

const MODE_OPTIONS: Array<{ value: DesignerMode; label: string }> = [
  { value: 'draw', label: 'Draw' },
  { value: 'boundary', label: 'Boundary' },
  { value: 'select', label: 'Select' },
  { value: 'move', label: 'Move' },
  { value: 'delete', label: 'Delete' },
  { value: 'measure', label: 'Measure' }
];

const MODE_HINTS: Record<DesignerMode, string> = {
  draw: 'Drag on the canvas to create a zone.',
  boundary: 'Click to add boundary points and click the first point to close the boundary.',
  select: 'Click a zone to edit it. Hold Ctrl or Shift to multi-select.',
  move: 'Drag selected zones to reposition them.',
  delete: 'Click a zone or boundary edge to remove it.',
  measure: 'Drag between two points to measure distance.'
};

let designerEnginePromise: Promise<void> | null = null;

function ensureDesignerEngineLoaded(): Promise<void> {
  if (window.AisleDrawer) {
    return Promise.resolve();
  }

  if (designerEnginePromise) {
    return designerEnginePromise;
  }

  designerEnginePromise = new Promise<void>((resolve, reject) => {
    const existing = document.querySelector<HTMLScriptElement>(`script[data-warehouse-designer="true"]`);
    if (existing) {
      existing.addEventListener('load', () => resolve(), { once: true });
      existing.addEventListener('error', () => reject(new Error('Could not load warehouse designer engine.')), { once: true });
      return;
    }

    const script = document.createElement('script');
    script.src = DESIGNER_SCRIPT_PATH;
    script.async = true;
    script.dataset['warehouseDesigner'] = 'true';
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Could not load warehouse designer engine.'));
    document.body.appendChild(script);
  });

  return designerEnginePromise;
}

@Component({
  selector: 'wms-warehouse-optimize-design',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PageHeaderComponent,
    WarehouseOptimizeNavComponent
  ],
  template: `
    <wms-page-header
      title="Warehouse Optimize"
      subtitle="Interactive warehouse design with the original drawing workflow"
      icon="pi pi-compass">
      <button type="button" class="header-action header-action-secondary" (click)="refreshProfiles()">
        <i class="pi pi-refresh"></i>
        <span>Refresh Profiles</span>
      </button>
      <button type="button" class="header-action" (click)="saveProfile()">
        <i class="pi pi-save"></i>
        <span>Save Layout</span>
      </button>
    </wms-page-header>

    <wms-warehouse-optimize-nav></wms-warehouse-optimize-nav>

    <section class="panel profile-panel">
      <div class="section-head">
        <div>
          <h3>Profile</h3>
          <p>Shared warehouse geometry lives here, while 2D and 3D pages stay read-only.</p>
        </div>
        <span class="chip">Warehouse shared</span>
      </div>

      <div class="profile-grid">
        <label class="field">
          <span>Existing profile</span>
          <select [ngModel]="selectedProfileId" (ngModelChange)="selectProfile($event)">
            <option [ngValue]="null">Choose a profile</option>
            <option *ngFor="let profile of state.profiles(); trackBy: trackProfile" [ngValue]="profile.id">
              {{ profile.profileName }} ({{ profile.warehouseCode }})
            </option>
          </select>
        </label>

        <label class="field">
          <span>Profile name</span>
          <input type="text" [(ngModel)]="profileName" maxlength="100" />
        </label>

        <label class="field field-span-2">
          <span>Description</span>
          <textarea rows="2" [(ngModel)]="description" maxlength="255"></textarea>
        </label>

        <label class="field">
          <span>Warehouse width (m)</span>
          <input type="number" [(ngModel)]="warehouseWidth" min="10" max="500" step="1" />
        </label>

        <label class="field">
          <span>Warehouse height (m)</span>
          <input type="number" [(ngModel)]="warehouseHeight" min="10" max="500" step="1" />
        </label>

        <label class="field">
          <span>Default type</span>
          <select [(ngModel)]="defaultType" (ngModelChange)="updateDefaultType($event)">
            <option *ngFor="let option of storageTypeOptions" [ngValue]="option.value">{{ option.label }}</option>
          </select>
        </label>

        <div class="field field-actions">
          <button type="button" class="soft-btn" (click)="newBlankLayout()">New Blank</button>
          <button type="button" class="soft-btn" (click)="resizeWarehouse()">Apply Size</button>
          <button type="button" class="soft-btn" (click)="toggleAdvancedJson()">
            {{ showAdvancedJson ? 'Hide JSON' : 'Show JSON' }}
          </button>
        </div>
      </div>
    </section>

    <section class="workspace-grid">
      <aside class="sidebar">
        <section class="panel">
          <div class="section-head compact">
            <div>
              <h3>Zones</h3>
              <p>{{ layout.aisles.length }} zone{{ layout.aisles.length === 1 ? '' : 's' }}</p>
            </div>
            <button
              *ngIf="selectedAisleIds.length > 1"
              type="button"
              class="soft-btn danger"
              (click)="deleteSelectedAisles()">
              Delete Selection
            </button>
          </div>

          <div *ngIf="selectedAisleIds.length > 1" class="selection-banner">
            {{ multiSelectionSummary }}
          </div>

          <div class="aisle-list" *ngIf="layout.aisles.length > 0; else emptyAisles">
            <button
              type="button"
              class="aisle-card"
              *ngFor="let aisle of layout.aisles; trackBy: trackAisle"
              [class.selected]="isSelected(aisle.id)"
              (click)="selectAisleFromList(aisle.id)">
              <div class="aisle-card-head">
                <strong>{{ aisle.zone }}</strong>
                <span class="type-badge">{{ typeLabel(aisle.type) }}</span>
              </div>
              <div class="aisle-card-meta">
                {{ formatDimension(aisle.width) }} x {{ formatDimension(aisle.height) }} m
              </div>
              <div class="aisle-card-meta">
                {{ aisle.levels }} level{{ aisle.levels === 1 ? '' : 's' }} and {{ aisle.locations.length }} locations
              </div>
            </button>
          </div>

          <ng-template #emptyAisles>
            <div class="empty-state">
              Draw on the canvas or use Quick Generate to create your first zone.
            </div>
          </ng-template>
        </section>

        <section class="panel" *ngIf="selectedAisleIds.length === 1 && selectedAisle">
          <div class="section-head compact">
            <div>
              <h3>Properties</h3>
              <p>{{ selectedAisle.zone }}</p>
            </div>
          </div>

          <div class="property-grid">
            <label class="field">
              <span>Zone name</span>
              <input type="text" [(ngModel)]="propertyForm.zone" />
            </label>

            <label class="field">
              <span>Type</span>
              <select [(ngModel)]="propertyForm.type">
                <option *ngFor="let option of storageTypeOptions" [ngValue]="option.value">{{ option.label }}</option>
              </select>
            </label>

            <label class="field">
              <span>Direction</span>
              <select [(ngModel)]="propertyForm.direction">
                <option *ngFor="let option of directionOptions" [ngValue]="option.value">{{ option.label }}</option>
              </select>
            </label>

            <label class="field">
              <span>Levels</span>
              <input type="number" [(ngModel)]="propertyForm.levels" min="1" max="12" step="1" />
            </label>

            <label class="field">
              <span>Tunnel from</span>
              <input type="number" [(ngModel)]="propertyForm.tunnelLevelFrom" min="1" max="12" step="1" />
            </label>

            <label class="field">
              <span>Tunnel to</span>
              <input type="number" [(ngModel)]="propertyForm.tunnelLevelTo" min="1" max="12" step="1" />
            </label>

            <label class="field">
              <span>Bay width (m)</span>
              <input type="number" [(ngModel)]="propertyForm.bayWidth" min="0.5" max="5" step="0.1" />
            </label>

            <label class="field">
              <span>Bay depth (m)</span>
              <input type="number" [(ngModel)]="propertyForm.bayDepth" min="0.5" max="3" step="0.1" />
            </label>

            <label class="field">
              <span>Aisle width (m)</span>
              <input type="number" [(ngModel)]="propertyForm.aisleWidth" min="0" max="6" step="0.1" />
            </label>

            <label class="field">
              <span>Start point X (m)</span>
              <input type="number" [(ngModel)]="propertyForm.startPointX" min="0" step="0.1" />
            </label>

            <label class="field">
              <span>Start point Y (m)</span>
              <input type="number" [(ngModel)]="propertyForm.startPointY" min="0" step="0.1" />
            </label>

            <label class="field field-span-2">
              <span>Pick face levels</span>
              <select multiple [ngModel]="propertyForm.pickFaceLevels" (ngModelChange)="updatePickFaceLevels($event)" class="multi-select">
                <option *ngFor="let level of levelOptions(propertyForm.levels)" [ngValue]="level">
                  Level {{ level }}
                </option>
              </select>
            </label>

            <div class="field field-span-2 locations-note">
              <span>Locations</span>
              <strong>{{ selectedAisle.locations.length }}</strong>
            </div>
          </div>

          <div class="property-actions">
            <button type="button" class="soft-btn primary" (click)="applyProperties()">Apply</button>
            <button type="button" class="soft-btn" (click)="duplicateSelectedAisle()">Duplicate</button>
            <button type="button" class="soft-btn danger" (click)="deleteSelectedAisle()">Delete</button>
          </div>
        </section>

        <section class="panel">
          <div class="section-head compact">
            <div>
              <h3>Statistics</h3>
              <p>Live values from the current design.</p>
            </div>
          </div>

          <div class="stats-grid">
            <div class="stat-card">
              <span>Warehouse</span>
              <strong>{{ warehouseWidth }} x {{ warehouseHeight }} m</strong>
            </div>
            <div class="stat-card">
              <span>Total area</span>
              <strong>{{ formatDimension(warehouseWidth * warehouseHeight) }} m2</strong>
            </div>
            <div class="stat-card">
              <span>Locations</span>
              <strong>{{ stats.locations }}</strong>
            </div>
            <div class="stat-card">
              <span>High rack</span>
              <strong>{{ stats.highRack }}</strong>
            </div>
            <div class="stat-card">
              <span>Shelving</span>
              <strong>{{ stats.shelf }}</strong>
            </div>
            <div class="stat-card">
              <span>Drive-in</span>
              <strong>{{ stats.driveIn }}</strong>
            </div>
            <div class="stat-card">
              <span>Floor / staging</span>
              <strong>{{ stats.floor }}</strong>
            </div>
            <div class="stat-card">
              <span>Utilization</span>
              <strong>{{ utilizationPercent }}%</strong>
            </div>
          </div>
        </section>

        <section class="panel">
          <div class="section-head compact">
            <div>
              <h3>Quick Generate</h3>
              <p>Use this for a starting grid, then fine-tune with drawing tools.</p>
            </div>
          </div>

          <div class="property-grid">
            <label class="field">
              <span>Zones</span>
              <input type="number" [(ngModel)]="generator.aisleCount" min="1" max="24" step="1" />
            </label>

            <label class="field">
              <span>Bays / zone</span>
              <input type="number" [(ngModel)]="generator.baysPerAisle" min="4" max="120" step="1" />
            </label>

            <label class="field">
              <span>Levels</span>
              <input type="number" [(ngModel)]="generator.levels" min="1" max="12" step="1" />
            </label>

            <label class="field">
              <span>Zone prefix</span>
              <input type="text" [(ngModel)]="generator.zonePrefix" />
            </label>

            <label class="field">
              <span>Rack type</span>
              <select [(ngModel)]="generator.rackType">
                <option *ngFor="let option of storageTypeOptions" [ngValue]="option.value">{{ option.label }}</option>
              </select>
            </label>

            <label class="field">
              <span>Bay width (m)</span>
              <input type="number" [(ngModel)]="generator.bayWidth" min="0.6" max="6" step="0.1" />
            </label>

            <label class="field">
              <span>Bay depth (m)</span>
              <input type="number" [(ngModel)]="generator.bayDepth" min="0.5" max="6" step="0.1" />
            </label>

            <label class="field">
              <span>Aisle width (m)</span>
              <input type="number" [(ngModel)]="generator.aisleWidth" min="0" max="10" step="0.1" />
            </label>

            <label class="field">
              <span>Start X</span>
              <input type="number" [(ngModel)]="generator.startX" min="0" max="120" step="0.5" />
            </label>

            <label class="field">
              <span>Start Y</span>
              <input type="number" [(ngModel)]="generator.startY" min="0" max="120" step="0.5" />
            </label>
          </div>

          <div class="property-actions">
            <button type="button" class="soft-btn primary" (click)="generateLayout()">Generate Grid</button>
            <button type="button" class="soft-btn danger" (click)="clearDesign()">Clear Design</button>
          </div>
        </section>

        <section class="panel" *ngIf="showAdvancedJson">
          <div class="section-head compact">
            <div>
              <h3>Layout JSON</h3>
              <p>Advanced import and export stays available for direct edits.</p>
            </div>
            <div class="json-actions">
              <input #importInput type="file" accept=".json" hidden (change)="importLayout($event)" />
              <button type="button" class="soft-btn" (click)="importInput.click()">Import</button>
              <button type="button" class="soft-btn" (click)="applyJson()">Apply</button>
              <button type="button" class="soft-btn" (click)="exportJson()">Export</button>
            </div>
          </div>
          <textarea class="json-editor" [(ngModel)]="layoutText"></textarea>
        </section>
      </aside>

      <section class="panel designer-panel">
        <div class="designer-toolbar">
          <div class="toolbar-row">
            <div class="toolbar-group">
              <button type="button" class="icon-btn" (click)="zoomIn()" title="Zoom in">
                <i class="pi pi-search-plus"></i>
              </button>
              <span class="zoom-pill">{{ zoomPercent }}%</span>
              <button type="button" class="icon-btn" (click)="zoomOut()" title="Zoom out">
                <i class="pi pi-search-minus"></i>
              </button>
              <button type="button" class="icon-btn" (click)="zoomToFit()" title="Fit to screen">
                <i class="pi pi-expand"></i>
              </button>
            </div>

            <div class="toolbar-group">
              <button type="button" class="toggle-btn" [class.active]="showGrid" (click)="toggleGrid()">Grid</button>
              <button type="button" class="toggle-btn" [class.active]="showLocations" (click)="toggleLocations()">Locations</button>
              <button type="button" class="toggle-btn" [class.active]="showLabels" (click)="toggleLabels()">Labels</button>
              <button type="button" class="toggle-btn" [class.active]="showHeatmap" (click)="toggleHeatmap()">Heatmap</button>
            </div>
          </div>

          <div class="toolbar-row modes">
            <button
              *ngFor="let mode of modeOptions"
              type="button"
              class="mode-btn btn-mode"
              [class.active]="activeMode === mode.value"
              [attr.data-mode]="mode.value"
              (click)="setMode(mode.value)">
              {{ mode.label }}
            </button>
          </div>
        </div>

        <div class="canvas-shell">
          <canvas #designerCanvas id="designerCanvas" class="designer-canvas"></canvas>

          <div class="minimap-card">
            <div class="minimap-head">
              <strong>Minimap</strong>
              <span>Drag to pan</span>
            </div>
            <div
              class="minimap"
              (mousedown)="onMinimapMouseDown($event)"
              (mousemove)="onMinimapMouseMove($event)">
              <canvas #minimapCanvas id="designerMinimapCanvas" class="minimap-canvas"></canvas>
              <div #minimapViewport class="minimap-viewport"></div>
            </div>
          </div>

          <div class="canvas-status">
            <span>{{ modeHint }}</span>
            <span>{{ coordinatesText }}</span>
            <span *ngIf="layout.boundaryPolygon?.length">Boundary ready</span>
          </div>
        </div>
      </section>
    </section>
  `,
  styles: [`
    :host {
      display: block;
    }

    .header-action,
    .header-action-secondary,
    .soft-btn,
    .icon-btn,
    .toggle-btn,
    .mode-btn,
    .aisle-card {
      border: 1px solid #d6deea;
      border-radius: 8px;
      background: #ffffff;
      color: #0f172a;
      transition: background-color 0.2s ease, border-color 0.2s ease, color 0.2s ease, box-shadow 0.2s ease;
    }

    .header-action,
    .header-action-secondary {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 9px 14px;
      font-size: 13px;
      font-weight: 600;
      cursor: pointer;
    }

    .header-action {
      background: #2563eb;
      border-color: #2563eb;
      color: #ffffff;
    }

    .header-action-secondary {
      background: #ffffff;
      color: #1e293b;
    }

    .panel {
      border: 1px solid #dbe2ea;
      border-radius: 8px;
      background: #ffffff;
      padding: 16px;
    }

    .profile-panel {
      margin-bottom: 18px;
    }

    .section-head {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      gap: 12px;
      margin-bottom: 14px;
    }

    .section-head.compact {
      margin-bottom: 12px;
    }

    .section-head h3 {
      margin: 0;
      font-size: 15px;
      font-weight: 700;
      color: #0f172a;
    }

    .section-head p {
      margin: 4px 0 0;
      font-size: 12px;
      color: #64748b;
    }

    .chip {
      display: inline-flex;
      align-items: center;
      padding: 5px 10px;
      border-radius: 999px;
      background: #eff6ff;
      color: #1d4ed8;
      font-size: 12px;
      font-weight: 600;
    }

    .profile-grid,
    .property-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 12px;
    }

    .field {
      display: flex;
      flex-direction: column;
      gap: 6px;
      min-width: 0;
    }

    .field span {
      font-size: 12px;
      font-weight: 600;
      color: #334155;
    }

    .field input,
    .field select,
    .field textarea,
    .json-editor {
      width: 100%;
      border: 1px solid #d6deea;
      border-radius: 8px;
      background: #ffffff;
      color: #0f172a;
      padding: 10px 12px;
      font-size: 13px;
      line-height: 1.4;
      font-family: inherit;
    }

    .field textarea,
    .json-editor {
      resize: vertical;
    }

    .field-span-2 {
      grid-column: span 2;
    }

    .field-actions {
      justify-content: flex-end;
      align-items: end;
      gap: 8px;
      flex-direction: row;
      flex-wrap: wrap;
    }

    .workspace-grid {
      display: grid;
      grid-template-columns: minmax(310px, 360px) minmax(0, 1fr);
      gap: 18px;
      align-items: start;
    }

    .sidebar {
      display: flex;
      flex-direction: column;
      gap: 18px;
      min-width: 0;
    }

    .selection-banner {
      margin-bottom: 12px;
      padding: 10px 12px;
      border-radius: 8px;
      background: #eff6ff;
      color: #1d4ed8;
      font-size: 12px;
      line-height: 1.5;
    }

    .aisle-list {
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-height: 360px;
      overflow: auto;
    }

    .aisle-card {
      width: 100%;
      text-align: left;
      padding: 12px;
      cursor: pointer;
    }

    .aisle-card.selected {
      border-color: #2563eb;
      background: #eff6ff;
      box-shadow: 0 0 0 1px #bfdbfe inset;
    }

    .aisle-card-head {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      margin-bottom: 8px;
    }

    .aisle-card-head strong {
      font-size: 13px;
    }

    .type-badge {
      display: inline-flex;
      align-items: center;
      padding: 4px 8px;
      border-radius: 999px;
      background: #f1f5f9;
      color: #475569;
      font-size: 11px;
      font-weight: 600;
    }

    .aisle-card-meta {
      font-size: 12px;
      color: #64748b;
      line-height: 1.5;
    }

    .empty-state {
      padding: 14px;
      border-radius: 8px;
      background: #f8fafc;
      color: #64748b;
      font-size: 12px;
      line-height: 1.6;
    }

    .property-actions,
    .json-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-top: 14px;
    }

    .soft-btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      padding: 9px 12px;
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
    }

    .soft-btn.primary {
      background: #2563eb;
      border-color: #2563eb;
      color: #ffffff;
    }

    .soft-btn.danger {
      background: #ffffff;
      border-color: #ef4444;
      color: #b91c1c;
    }

    .locations-note {
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
      padding: 10px 12px;
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      background: #f8fafc;
    }

    .multi-select {
      min-height: 112px;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 10px;
    }

    .stat-card {
      padding: 12px;
      border-radius: 8px;
      border: 1px solid #e2e8f0;
      background: #f8fafc;
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .stat-card span {
      font-size: 12px;
      color: #64748b;
    }

    .stat-card strong {
      font-size: 15px;
      color: #0f172a;
    }

    .designer-panel {
      padding: 0;
      overflow: hidden;
    }

    .designer-toolbar {
      padding: 16px;
      border-bottom: 1px solid #e2e8f0;
      background: #f8fafc;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .toolbar-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      flex-wrap: wrap;
    }

    .toolbar-row.modes {
      justify-content: flex-start;
    }

    .toolbar-group {
      display: flex;
      align-items: center;
      gap: 8px;
      flex-wrap: wrap;
    }

    .icon-btn,
    .toggle-btn,
    .mode-btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      min-height: 38px;
      padding: 0 12px;
      cursor: pointer;
      font-size: 12px;
      font-weight: 600;
    }

    .icon-btn {
      min-width: 38px;
      padding: 0;
    }

    .toggle-btn.active,
    .mode-btn.active {
      background: #dbeafe;
      border-color: #60a5fa;
      color: #1d4ed8;
    }

    .zoom-pill {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      min-width: 62px;
      min-height: 38px;
      padding: 0 12px;
      border-radius: 8px;
      background: #ffffff;
      border: 1px solid #d6deea;
      font-size: 12px;
      font-weight: 700;
      color: #334155;
    }

    .canvas-shell {
      position: relative;
      min-height: 760px;
      background: #f8fafc;
    }

    canvas {
      display: block;
      width: 100%;
      cursor: crosshair;
      background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
    }

    .canvas-shell canvas.select-mode,
    .canvas-shell canvas.move-mode {
      cursor: move;
    }

    .canvas-shell canvas.delete-mode {
      cursor: not-allowed;
    }

    .canvas-shell canvas.measure-mode {
      cursor: crosshair;
    }

    .canvas-shell canvas.panning {
      cursor: grabbing;
    }

    .canvas-status {
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      min-height: 50px;
      padding: 0 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      border-top: 1px solid #e2e8f0;
      background: rgba(248, 250, 252, 0.95);
      font-size: 12px;
      color: #475569;
      backdrop-filter: blur(10px);
    }

    .minimap-card {
      position: absolute;
      right: 16px;
      bottom: 66px;
      width: 220px;
      border: 1px solid #d6deea;
      border-radius: 8px;
      background: rgba(255, 255, 255, 0.96);
      box-shadow: 0 16px 30px rgba(15, 23, 42, 0.12);
      overflow: hidden;
    }

    .minimap-head {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      padding: 10px 12px;
      border-bottom: 1px solid #e2e8f0;
      font-size: 11px;
      color: #64748b;
    }

    .minimap {
      position: relative;
      height: 150px;
      cursor: grab;
      background: #f8fafc;
    }

    .minimap canvas {
      width: 100%;
      height: 100%;
      background: #f8fafc;
    }

    .minimap-viewport {
      position: absolute;
      border: 2px solid #2563eb;
      background: rgba(37, 99, 235, 0.12);
      pointer-events: none;
      box-sizing: border-box;
    }

    .json-editor {
      min-height: 280px;
      font-family: Consolas, monospace;
      font-size: 12px;
      line-height: 1.5;
    }

    @media (max-width: 1320px) {
      .workspace-grid {
        grid-template-columns: 1fr;
      }

      .canvas-shell {
        min-height: 700px;
      }
    }

    @media (max-width: 900px) {
      .profile-grid,
      .property-grid,
      .stats-grid {
        grid-template-columns: 1fr;
      }

      .field-span-2 {
        grid-column: span 1;
      }

      .minimap-card {
        position: static;
        width: auto;
        margin: 12px;
      }

      .canvas-status {
        position: static;
        min-height: 56px;
        flex-wrap: wrap;
        justify-content: flex-start;
        padding: 10px 16px;
      }

      .canvas-shell {
        min-height: 620px;
      }
    }
  `]
})
export class WarehouseOptimizeDesignComponent implements AfterViewInit {
  readonly state = inject(WarehouseOptimizeStateService);
  private readonly service = inject(WarehouseOptimizeService);
  private readonly messageService = inject(MessageService);
  private readonly zone = inject(NgZone);

  @ViewChild('designerCanvas') private canvasRef?: ElementRef<HTMLCanvasElement>;
  @ViewChild('minimapCanvas') private minimapCanvasRef?: ElementRef<HTMLCanvasElement>;
  @ViewChild('minimapViewport') private minimapViewportRef?: ElementRef<HTMLDivElement>;

  readonly storageTypeOptions = STORAGE_TYPE_OPTIONS;
  readonly directionOptions = DIRECTION_OPTIONS;
  readonly modeOptions = MODE_OPTIONS;

  selectedProfileId: number | null = null;
  profileName = 'Main Shared Layout';
  description = 'Warehouse-shared geometry for digital warehouse visibility';
  warehouseWidth = 130;
  warehouseHeight = 70;
  defaultType = 'HIGH_RACK';
  activeMode: DesignerMode = 'draw';
  showGrid = true;
  showLocations = false;
  showLabels = false;
  showHeatmap = false;
  showAdvancedJson = false;
  zoomPercent = 100;
  coordinatesText = 'X: -, Y: -';

  layout: WarehouseDesignerLayout = createEmptyLayout(this.warehouseWidth, this.warehouseHeight);
  layoutText = prettyLayoutJson(this.layout);
  generator: LayoutGeneratorConfig = this.defaultGenerator();
  stats: LayoutStats = layoutStats(this.layout);
  selectedAisle: WarehouseAisle | null = null;
  selectedAisleIds: number[] = [];
  multiSelectionSummary = '';
  propertyForm: PropertyFormModel = this.createDefaultPropertyForm();

  private drawer: WarehouseDesignerDrawer | null = null;
  private pendingLayoutData: WarehouseDesignerLayout = this.layout;
  private isMinimapDragging = false;

  get modeHint(): string {
    return MODE_HINTS[this.activeMode];
  }

  get utilizationPercent(): number {
    const totalArea = this.warehouseWidth * this.warehouseHeight;
    if (totalArea <= 0) {
      return 0;
    }
    const usedArea = this.layout.aisles.reduce((sum, aisle) => sum + (aisle.width * aisle.height), 0);
    return Math.round((usedArea / totalArea) * 100);
  }

  constructor() {
    this.state.initialize();

    effect(() => {
      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }

      this.selectedProfileId = profile.id;
      this.profileName = profile.profileName;
      this.description = profile.description ?? '';

      try {
        const parsed = JSON.parse(profile.layoutData) as WarehouseDesignerLayout;
        this.loadLayoutData(parsed, false);
      } catch (error) {
        const fallback = createEmptyLayout(
          profile.warehouseWidth ?? this.warehouseWidth,
          profile.warehouseLength ?? this.warehouseHeight
        );
        this.loadLayoutData(fallback, false);
        this.messageService.add({
          severity: 'warn',
          summary: 'Profile loaded with fallback',
          detail: error instanceof Error ? error.message : 'Could not parse layout JSON.'
        });
      }
    });
  }

  async ngAfterViewInit(): Promise<void> {
    await this.initializeDesigner();
  }

  @HostListener('document:mouseup')
  onDocumentMouseUp(): void {
    this.isMinimapDragging = false;
  }

  refreshProfiles(): void {
    this.state.refreshProfiles();
  }

  selectProfile(profileId: number | null): void {
    this.selectedProfileId = typeof profileId === 'number' ? profileId : null;
    if (this.selectedProfileId) {
      this.state.selectProfile(this.selectedProfileId);
    }
  }

  trackProfile(_: number, profile: { id: number }): number {
    return profile.id;
  }

  trackAisle(_: number, aisle: WarehouseAisle): number {
    return aisle.id;
  }

  isSelected(aisleId: number): boolean {
    return this.selectedAisleIds.includes(aisleId);
  }

  typeLabel(type: string): string {
    return STORAGE_TYPE_OPTIONS.find(option => option.value === type)?.label ?? type;
  }

  formatDimension(value: number): string {
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: Number.isInteger(value) ? 0 : 1,
      maximumFractionDigits: 1
    }).format(value);
  }

  updateDefaultType(type: string): void {
    this.defaultType = type;
    if (this.drawer) {
      this.drawer.defaultType = type;
    }
  }

  toggleAdvancedJson(): void {
    this.showAdvancedJson = !this.showAdvancedJson;
    if (this.showAdvancedJson) {
      this.captureDrawerState();
    }
  }

  newBlankLayout(): void {
    if (this.drawer?.hasDesignContent() && !window.confirm('Create a new blank layout and clear the current design?')) {
      return;
    }

    this.selectedProfileId = null;
    this.profileName = 'Main Shared Layout';
    this.description = 'Warehouse-shared geometry for digital warehouse visibility';
    this.loadLayoutData(createEmptyLayout(this.warehouseWidth, this.warehouseHeight), true);
  }

  resizeWarehouse(): void {
    const nextWidth = clampWhole(this.warehouseWidth, 10, 500, 130);
    const nextHeight = clampWhole(this.warehouseHeight, 10, 500, 70);
    const hasContent = this.drawer?.hasDesignContent() ?? this.layout.aisles.length > 0;

    if (hasContent && !window.confirm('Applying a new warehouse size will clear the current design. Continue?')) {
      return;
    }

    this.warehouseWidth = nextWidth;
    this.warehouseHeight = nextHeight;
    this.loadLayoutData(createEmptyLayout(nextWidth, nextHeight), true);
  }

  generateLayout(): void {
    const config: LayoutGeneratorConfig = {
      ...this.generator,
      warehouseWidth: clampWhole(this.warehouseWidth, 10, 500, 130),
      warehouseHeight: clampWhole(this.warehouseHeight, 10, 500, 70)
    };

    if ((this.drawer?.hasDesignContent() ?? this.layout.aisles.length > 0) &&
      !window.confirm('Generate a new starter layout and replace the current design?')) {
      return;
    }

    this.generator = config;
    this.loadLayoutData(createGeneratedLayout(config), true);
    this.messageService.add({
      severity: 'success',
      summary: 'Starter layout generated',
      detail: 'You can keep drawing and adjusting zones directly on the canvas.'
    });
  }

  clearDesign(): void {
    if (!(this.drawer?.hasDesignContent() ?? this.layout.aisles.length > 0)) {
      return;
    }

    if (!window.confirm('Clear the current warehouse design?')) {
      return;
    }

    if (this.drawer && this.drawer.clearAll()) {
      this.captureDrawerState();
      this.updateMinimap();
      return;
    }

    this.loadLayoutData(createEmptyLayout(this.warehouseWidth, this.warehouseHeight), true);
  }

  setMode(mode: DesignerMode): void {
    this.activeMode = mode;
    this.drawer?.setMode(mode);
  }

  zoomIn(): void {
    this.drawer?.zoomIn();
    this.updateMinimapViewport();
  }

  zoomOut(): void {
    this.drawer?.zoomOut();
    this.updateMinimapViewport();
  }

  zoomToFit(): void {
    this.drawer?.zoomToFit();
    this.updateMinimapViewport();
  }

  toggleGrid(): void {
    if (!this.drawer) {
      return;
    }
    this.drawer.showGrid = !this.drawer.showGrid;
    this.showGrid = this.drawer.showGrid;
    this.drawer.render();
  }

  toggleLocations(): void {
    if (!this.drawer) {
      return;
    }
    this.drawer.showLocations = !this.drawer.showLocations;
    this.showLocations = this.drawer.showLocations;
    this.drawer.render();
  }

  toggleLabels(): void {
    if (!this.drawer) {
      return;
    }
    this.drawer.showLabels = !this.drawer.showLabels;
    this.showLabels = this.drawer.showLabels;
    this.drawer.render();
  }

  toggleHeatmap(): void {
    if (!this.drawer) {
      return;
    }
    this.drawer.showHeatmap = !this.drawer.showHeatmap;
    this.showHeatmap = this.drawer.showHeatmap;
    this.drawer.render();
  }

  selectAisleFromList(aisleId: number): void {
    if (!this.drawer) {
      return;
    }

    const aisle = this.drawer.aisles.find(item => item.id === aisleId) ?? null;
    this.drawer.selectedAisle = aisle;
    this.drawer.selectedAisles = aisle ? [aisle] : [];
    this.drawer.render();
    this.syncSelectionFromDrawer();
  }

  levelOptions(levels: number): number[] {
    return Array.from({ length: Math.max(1, Math.round(levels)) }, (_, index) => index + 1);
  }

  updatePickFaceLevels(levels: number[] | number | null): void {
    if (Array.isArray(levels)) {
      this.propertyForm.pickFaceLevels = levels.map(value => Math.round(value)).filter(value => Number.isFinite(value));
      return;
    }

    if (typeof levels === 'number' && Number.isFinite(levels)) {
      this.propertyForm.pickFaceLevels = [Math.round(levels)];
      return;
    }

    this.propertyForm.pickFaceLevels = [];
  }

  applyProperties(): void {
    if (!this.drawer?.selectedAisle) {
      return;
    }

    const levels = clampWhole(this.propertyForm.levels, 1, 12, this.drawer.selectedAisle.levels);
    const tunnelFrom = clampWhole(this.propertyForm.tunnelLevelFrom, 1, 12, 1);
    const tunnelTo = clampWhole(this.propertyForm.tunnelLevelTo, tunnelFrom, 12, levels);
    const pickFaceLevels = (this.propertyForm.pickFaceLevels.length > 0 ? this.propertyForm.pickFaceLevels : [1])
      .map(value => clampWhole(value, 1, levels, 1))
      .sort((left, right) => left - right);

    this.drawer.selectedAisle.updateProperties({
      zone: this.propertyForm.zone.trim() || this.drawer.selectedAisle.zone,
      type: this.propertyForm.type,
      direction: this.propertyForm.direction,
      levels,
      tunnelLevelFrom: tunnelFrom,
      tunnelLevelTo: tunnelTo,
      bayWidth: clampFloat(this.propertyForm.bayWidth, 0.5, 5, this.drawer.selectedAisle.bayWidth),
      bayDepth: clampFloat(this.propertyForm.bayDepth, 0.5, 3, this.drawer.selectedAisle.bayDepth),
      aisleWidth: clampFloat(this.propertyForm.aisleWidth, 0, 6, this.drawer.selectedAisle.aisleWidth),
      pickFaceLevels,
      startPointX: clampFloat(this.propertyForm.startPointX, 0, 9999, this.drawer.selectedAisle.x),
      startPointY: clampFloat(this.propertyForm.startPointY, 0, 9999, this.drawer.selectedAisle.y)
    });
    this.drawer.applyConstraintFiltering();
    this.drawer.saveState();
    this.drawer.render();
    this.captureDrawerState();
    this.messageService.add({
      severity: 'success',
      summary: 'Properties updated',
      detail: `${this.drawer.selectedAisle.zone} was updated.`
    });
  }

  duplicateSelectedAisle(): void {
    if (!this.drawer?.selectedAisle) {
      return;
    }

    const aisle = this.drawer.duplicateAisle(this.drawer.selectedAisle);
    this.captureDrawerState();
    this.messageService.add({
      severity: 'success',
      summary: 'Zone duplicated',
      detail: `${aisle.zone} is ready for editing.`
    });
  }

  deleteSelectedAisle(): void {
    if (!this.drawer?.selectedAisle) {
      return;
    }

    const zone = this.drawer.selectedAisle.zone;
    if (!window.confirm(`Delete zone ${zone}?`)) {
      return;
    }

    this.drawer.deleteAisle(this.drawer.selectedAisle);
    this.captureDrawerState();
    this.messageService.add({
      severity: 'success',
      summary: 'Zone deleted',
      detail: `${zone} was removed from the design.`
    });
  }

  deleteSelectedAisles(): void {
    if (!this.drawer || this.drawer.selectedAisles.length < 2) {
      return;
    }

    const count = this.drawer.selectedAisles.length;
    if (!window.confirm(`Delete ${count} selected zones?`)) {
      return;
    }

    this.drawer.deleteSelectedAisles();
    this.captureDrawerState();
    this.messageService.add({
      severity: 'success',
      summary: 'Selection deleted',
      detail: `${count} zones were removed from the design.`
    });
  }

  applyJson(): void {
    try {
      const parsed = JSON.parse(this.layoutText) as Partial<WarehouseDesignerLayout>;
      const normalized = normalizeLayout(parsed);
      const layoutData: WarehouseDesignerLayout = {
        ...parsed,
        warehouseWidth: normalized.warehouseWidth,
        warehouseHeight: normalized.warehouseHeight,
        boundaryPolygon: normalized.boundaryPolygon ?? null,
        aisles: normalized.aisles
      };
      this.loadLayoutData(layoutData, true);
      this.messageService.add({
        severity: 'success',
        summary: 'Layout updated',
        detail: 'The JSON was applied to the interactive designer.'
      });
    } catch (error) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid JSON',
        detail: error instanceof Error ? error.message : 'Could not parse the layout JSON.'
      });
    }
  }

  importLayout(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }

    void file.text().then(text => {
      this.layoutText = text;
      this.applyJson();
      input.value = '';
    });
  }

  exportJson(): void {
    this.captureDrawerState();
    const blob = new Blob([this.layoutText], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${this.profileName.replace(/\s+/g, '-').toLowerCase() || 'warehouse-layout'}.json`;
    link.click();
    URL.revokeObjectURL(url);
  }

  saveProfile(): void {
    this.captureDrawerState();
    if (!this.profileName.trim()) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Profile name required',
        detail: 'Enter a profile name before saving.'
      });
      return;
    }

    const payload = this.snapshotLayoutData();
    const normalized = normalizeLayout(payload);

    this.service.saveProfile({
      id: this.selectedProfileId,
      profileName: this.profileName.trim(),
      description: this.description.trim(),
      warehouseLength: normalized.warehouseHeight,
      warehouseWidth: normalized.warehouseWidth,
      layoutData: payload
    }).subscribe(profile => {
      this.selectedProfileId = profile.id;
      this.state.patchSelectedProfile(profile);
      this.state.refreshProfiles();
      this.messageService.add({
        severity: 'success',
        summary: 'Layout saved',
        detail: `${profile.profileName} is now available in Warehouse Optimize.`
      });
    });
  }

  onMinimapMouseDown(event: MouseEvent): void {
    this.isMinimapDragging = true;
    this.panFromMinimap(event);
  }

  onMinimapMouseMove(event: MouseEvent): void {
    if (!this.isMinimapDragging) {
      return;
    }
    this.panFromMinimap(event);
  }

  private async initializeDesigner(): Promise<void> {
    try {
      await ensureDesignerEngineLoaded();
      const canvas = this.canvasRef?.nativeElement;
      if (!canvas || !window.AisleDrawer) {
        throw new Error('Warehouse designer canvas is not available.');
      }

      this.drawer = new window.AisleDrawer(
        canvas,
        this.pendingLayoutData.warehouseWidth,
        this.pendingLayoutData.warehouseHeight
      );
      this.drawer.defaultType = this.defaultType;
      this.drawer.onAisleSelected = () => this.zone.run(() => this.syncSelectionFromDrawer());
      this.drawer.onMultipleAislesSelected = () => this.zone.run(() => this.syncSelectionFromDrawer());
      this.drawer.onAislesChanged = () => this.zone.run(() => {
        this.captureDrawerState();
        this.updateMinimap();
      });
      this.drawer.onZoomChanged = zoom => this.zone.run(() => {
        this.zoomPercent = zoom;
        this.updateMinimapViewport();
      });
      this.drawer.onCoordinatesChanged = (x, y) => this.zone.run(() => {
        this.coordinatesText = `X: ${x} m, Y: ${y} m`;
      });
      this.drawer.onPanChanged = () => this.zone.run(() => this.updateMinimapViewport());
      this.drawer.onLocationCopied = locationName => this.zone.run(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Location copied',
          detail: locationName
        });
      });

      this.applyDrawerVisualState();
      this.drawer.setMode(this.activeMode);
      this.drawer.fromJSON(this.pendingLayoutData);
      this.captureDrawerState();
      this.updateMinimap();
    } catch (error) {
      this.messageService.add({
        severity: 'error',
        summary: 'Designer failed to load',
        detail: error instanceof Error ? error.message : 'Could not initialize the warehouse designer.'
      });
    }
  }

  private loadLayoutData(layoutData: WarehouseDesignerLayout, resetSelection: boolean): void {
    const normalized = normalizeLayout(layoutData);
    const nextLayout: WarehouseDesignerLayout = {
      ...layoutData,
      warehouseWidth: normalized.warehouseWidth,
      warehouseHeight: normalized.warehouseHeight,
      boundaryPolygon: normalized.boundaryPolygon ?? null,
      aisles: normalized.aisles
    };

    this.pendingLayoutData = nextLayout;
    this.layout = nextLayout;
    this.layoutText = prettyLayoutJson(nextLayout);
    this.stats = layoutStats(nextLayout);
    this.warehouseWidth = nextLayout.warehouseWidth;
    this.warehouseHeight = nextLayout.warehouseHeight;
    this.generator = {
      ...this.generator,
      warehouseWidth: nextLayout.warehouseWidth,
      warehouseHeight: nextLayout.warehouseHeight
    };

    if (resetSelection) {
      this.selectedAisle = null;
      this.selectedAisleIds = [];
      this.propertyForm = this.createDefaultPropertyForm();
      this.multiSelectionSummary = '';
    }

    if (this.drawer) {
      this.drawer.fromJSON(nextLayout);
      this.applyDrawerVisualState();
      this.drawer.zoomToFit();
      this.captureDrawerState();
      this.updateMinimap();
    }
  }

  private applyDrawerVisualState(): void {
    if (!this.drawer) {
      return;
    }
    this.drawer.defaultType = this.defaultType;
    this.drawer.showGrid = this.showGrid;
    this.drawer.showLocations = this.showLocations;
    this.drawer.showLabels = this.showLabels;
    this.drawer.showHeatmap = this.showHeatmap;
  }

  private captureDrawerState(): void {
    const snapshot = this.snapshotLayoutData();
    this.pendingLayoutData = snapshot;
    this.layout = normalizeLayout(snapshot);
    this.layoutText = prettyLayoutJson(snapshot);
    this.stats = layoutStats(this.layout);
    this.warehouseWidth = this.layout.warehouseWidth;
    this.warehouseHeight = this.layout.warehouseHeight;
    this.generator = {
      ...this.generator,
      warehouseWidth: this.layout.warehouseWidth,
      warehouseHeight: this.layout.warehouseHeight
    };

    if (this.drawer) {
      this.showGrid = this.drawer.showGrid;
      this.showLocations = this.drawer.showLocations;
      this.showLabels = this.drawer.showLabels;
      this.showHeatmap = this.drawer.showHeatmap;
      this.activeMode = this.drawer.mode as DesignerMode;
      this.zoomPercent = Math.round(this.drawer.zoom * 100);
    }

    this.syncSelectionFromDrawer();
  }

  private syncSelectionFromDrawer(): void {
    if (!this.drawer) {
      this.selectedAisle = null;
      this.selectedAisleIds = [];
      this.multiSelectionSummary = '';
      return;
    }

    const selectedAisles = this.drawer.selectedAisles.length > 0
      ? this.drawer.selectedAisles
      : (this.drawer.selectedAisle ? [this.drawer.selectedAisle] : []);

    this.selectedAisleIds = selectedAisles.map(aisle => aisle.id);

    if (selectedAisles.length === 1) {
      const normalizedAisle = this.layout.aisles.find(aisle => aisle.id === selectedAisles[0].id) ?? null;
      this.selectedAisle = normalizedAisle;
      if (normalizedAisle) {
        this.patchPropertyForm(normalizedAisle);
      }
      this.multiSelectionSummary = '';
      return;
    }

    this.selectedAisle = null;
    if (selectedAisles.length > 1) {
      const totalLocations = selectedAisles.reduce((sum, aisle) => sum + aisle.locations.length, 0);
      this.multiSelectionSummary = `${selectedAisles.length} zones selected with ${totalLocations} locations in total.`;
    } else {
      this.multiSelectionSummary = '';
    }
  }

  private patchPropertyForm(aisle: WarehouseAisle): void {
    this.propertyForm = {
      zone: aisle.zone,
      type: aisle.type,
      direction: aisle.direction ?? 'AUTO',
      levels: aisle.levels,
      tunnelLevelFrom: aisle.tunnelLevelFrom ?? 1,
      tunnelLevelTo: aisle.tunnelLevelTo ?? aisle.levels,
      bayWidth: aisle.bayWidth,
      bayDepth: aisle.bayDepth,
      aisleWidth: aisle.aisleWidth,
      pickFaceLevels: [...(aisle.pickFaceLevels ?? [1])],
      startPointX: aisle.startPointX ?? aisle.x,
      startPointY: aisle.startPointY ?? aisle.y
    };
  }

  private snapshotLayoutData(): WarehouseDesignerLayout {
    return this.drawer?.toJSON() ?? this.pendingLayoutData;
  }

  private updateMinimap(): void {
    const canvas = this.minimapCanvasRef?.nativeElement;
    const drawer = this.drawer;
    if (!canvas || !drawer) {
      return;
    }

    const container = canvas.parentElement;
    if (!container) {
      return;
    }

    canvas.width = container.clientWidth;
    canvas.height = container.clientHeight;

    const context = canvas.getContext('2d');
    if (!context) {
      return;
    }

    context.clearRect(0, 0, canvas.width, canvas.height);
    context.fillStyle = '#f8fafc';
    context.fillRect(0, 0, canvas.width, canvas.height);

    const padding = 8;
    const scaleX = (canvas.width - padding * 2) / drawer.warehouseWidth;
    const scaleY = (canvas.height - padding * 2) / drawer.warehouseHeight;
    const scale = Math.min(scaleX, scaleY);
    const offsetX = padding + ((canvas.width - padding * 2) - drawer.warehouseWidth * scale) / 2;
    const offsetY = padding + ((canvas.height - padding * 2) - drawer.warehouseHeight * scale) / 2;

    container.dataset['scale'] = String(scale);
    container.dataset['offsetX'] = String(offsetX);
    container.dataset['offsetY'] = String(offsetY);

    context.save();
    context.translate(offsetX, offsetY);
    context.scale(scale, scale);
    context.translate(0, drawer.warehouseHeight);
    context.scale(1, -1);

    context.fillStyle = '#ffffff';
    context.fillRect(0, 0, drawer.warehouseWidth, drawer.warehouseHeight);
    context.strokeStyle = '#1e293b';
    context.lineWidth = 1 / scale;
    context.strokeRect(0, 0, drawer.warehouseWidth, drawer.warehouseHeight);

    if (Array.isArray(drawer.boundaryPolygon) && drawer.boundaryPolygon.length >= 3) {
      context.strokeStyle = '#16a34a';
      context.lineWidth = 1.2 / scale;
      context.beginPath();
      context.moveTo(drawer.boundaryPolygon[0].x, drawer.boundaryPolygon[0].y);
      for (let index = 1; index < drawer.boundaryPolygon.length; index++) {
        context.lineTo(drawer.boundaryPolygon[index].x, drawer.boundaryPolygon[index].y);
      }
      context.closePath();
      context.stroke();
    }

    for (const aisle of drawer.aisles) {
      context.fillStyle = colorForAisleType(aisle.type);
      context.fillRect(aisle.x, aisle.y, aisle.width, aisle.height);

      if (this.selectedAisleIds.includes(aisle.id)) {
        context.strokeStyle = '#dc2626';
        context.lineWidth = 1.4 / scale;
        context.strokeRect(aisle.x, aisle.y, aisle.width, aisle.height);
      }
    }

    context.restore();
    this.updateMinimapViewport();
  }

  private updateMinimapViewport(): void {
    const drawer = this.drawer;
    const viewport = this.minimapViewportRef?.nativeElement;
    const minimapCanvas = this.minimapCanvasRef?.nativeElement;
    if (!drawer || !viewport || !minimapCanvas?.parentElement) {
      return;
    }

    const scale = Number(minimapCanvas.parentElement.dataset['scale'] ?? '0');
    const offsetX = Number(minimapCanvas.parentElement.dataset['offsetX'] ?? '0');
    const offsetY = Number(minimapCanvas.parentElement.dataset['offsetY'] ?? '0');
    if (!scale) {
      return;
    }

    const visibleWidth = drawer.canvas.width / drawer.scale;
    const visibleHeight = drawer.canvas.height / drawer.scale;
    const viewX = -drawer.panX / drawer.scale;
    const viewY = drawer.warehouseHeight - (-drawer.panY / drawer.scale) - visibleHeight;

    const left = offsetX + viewX * scale;
    const top = offsetY + (drawer.warehouseHeight - viewY - visibleHeight) * scale;
    const width = visibleWidth * scale;
    const height = visibleHeight * scale;

    viewport.style.left = `${Math.max(0, left)}px`;
    viewport.style.top = `${Math.max(0, top)}px`;
    viewport.style.width = `${Math.min(width, minimapCanvas.clientWidth)}px`;
    viewport.style.height = `${Math.min(height, minimapCanvas.clientHeight)}px`;
  }

  private panFromMinimap(event: MouseEvent): void {
    const drawer = this.drawer;
    const minimapCanvas = this.minimapCanvasRef?.nativeElement;
    if (!drawer || !minimapCanvas?.parentElement) {
      return;
    }

    const scale = Number(minimapCanvas.parentElement.dataset['scale'] ?? '0');
    const offsetX = Number(minimapCanvas.parentElement.dataset['offsetX'] ?? '0');
    const offsetY = Number(minimapCanvas.parentElement.dataset['offsetY'] ?? '0');
    if (!scale) {
      return;
    }

    const rect = minimapCanvas.parentElement.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;
    const warehouseX = (clickX - offsetX) / scale;
    const warehouseY = drawer.warehouseHeight - ((clickY - offsetY) / scale);
    const visibleWidth = drawer.canvas.width / drawer.scale;
    const visibleHeight = drawer.canvas.height / drawer.scale;
    const targetX = warehouseX - visibleWidth / 2;
    const targetY = warehouseY - visibleHeight / 2;

    drawer.panX = -targetX * drawer.scale;
    drawer.panY = -(drawer.warehouseHeight - targetY - visibleHeight) * drawer.scale;
    drawer.render();
    this.updateMinimapViewport();
  }

  private createDefaultPropertyForm(): PropertyFormModel {
    return {
      zone: '',
      type: 'HIGH_RACK',
      direction: 'AUTO',
      levels: 4,
      tunnelLevelFrom: 1,
      tunnelLevelTo: 4,
      bayWidth: 1.2,
      bayDepth: 1,
      aisleWidth: 2.5,
      pickFaceLevels: [1],
      startPointX: 0,
      startPointY: 0
    };
  }

  private defaultGenerator(): LayoutGeneratorConfig {
    return {
      warehouseWidth: 130,
      warehouseHeight: 70,
      aisleCount: 4,
      baysPerAisle: 18,
      levels: 4,
      zonePrefix: 'Zone',
      rackType: 'HIGH_RACK',
      bayWidth: 1.2,
      bayDepth: 1,
      aisleWidth: 2.5,
      startX: 10,
      startY: 10
    };
  }
}

function clampWhole(value: number, min: number, max: number, fallback: number): number {
  const parsed = Math.round(Number(value));
  if (!Number.isFinite(parsed)) {
    return fallback;
  }
  return Math.min(max, Math.max(min, parsed));
}

function clampFloat(value: number, min: number, max: number, fallback: number): number {
  const parsed = Number(value);
  if (!Number.isFinite(parsed)) {
    return fallback;
  }
  return Math.min(max, Math.max(min, parsed));
}

function colorForAisleType(type: string): string {
  switch (type) {
    case 'HIGH_RACK':
      return 'rgba(37, 99, 235, 0.7)';
    case 'SHELF':
      return 'rgba(34, 197, 94, 0.7)';
    case 'DRIVE_IN':
      return 'rgba(168, 85, 247, 0.7)';
    case 'FLOOR':
      return 'rgba(249, 115, 22, 0.65)';
    case 'STAGING':
      return 'rgba(20, 184, 166, 0.7)';
    case 'OBSTACLE':
    case 'PILLAR':
      return 'rgba(100, 116, 139, 0.75)';
    case 'DOCK_DOOR':
      return 'rgba(14, 165, 233, 0.65)';
    default:
      return 'rgba(148, 163, 184, 0.55)';
  }
}
