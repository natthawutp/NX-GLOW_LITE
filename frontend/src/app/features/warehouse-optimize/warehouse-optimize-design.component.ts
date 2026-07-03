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
import { finalize } from 'rxjs';
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
  WarehouseDesignerLayout,
  WarehouseLocationHierarchyMapping,
  WarehouseLocationHierarchyResponse,
  WarehouseLocationHierarchyScope,
  WarehouseLocationHierarchySource
} from './shared/warehouse-optimize.models';
import {
  aisleDisplayLabel,
  createEmptyLayout,
  createGeneratedLayout,
  layoutStats,
  normalizeLayout,
  prettyLayoutJson
} from './shared/warehouse-optimize-layout.utils';
import {
  WORKING_STATUS_FLOW_OPTIONS,
  workingStatusDefinition,
  workingStatusOptionsForFlow
} from './shared/warehouse-working-status.utils';

type DesignerMode = 'draw' | 'boundary' | 'select' | 'move' | 'delete' | 'measure';
type SaveState = 'idle' | 'dirty' | 'saving' | 'success' | 'error';
type GenerateState = 'idle' | 'generating' | 'success' | 'error';

interface PropertyFormModel {
  zone: string;
  type: string;
  direction: string;
  workingStatusFlow: string;
  workingStatusCode: string;
  workingStatusLabel: string;
  workingStatusColor: string;
  levels: number;
  tunnelLevelFrom: number;
  tunnelLevelTo: number;
  lowerShelfLevels: number;
  bayWidth: number;
  bayDepth: number;
  aisleWidth: number;
  pickFaceLevels: number[];
  startPointX: number;
  startPointY: number;
}

interface HierarchyFormModel {
  zone: WarehouseLocationHierarchySource | '';
  aisle: WarehouseLocationHierarchySource | '';
  bay: WarehouseLocationHierarchySource;
  slot: WarehouseLocationHierarchySource | '';
  level: WarehouseLocationHierarchySource;
}

type CollapsiblePanelKey = 'profile' | 'hierarchy' | 'zones' | 'statistics' | 'quickGenerate';

const DESIGNER_SCRIPT_PATH = 'assets/warehouse-optimize/aisle-drawer.js';

const STORAGE_TYPE_OPTIONS = [
  { value: 'HIGH_RACK', label: 'High Rack' },
  { value: 'SHELF', label: 'Shelving' },
  { value: 'DRIVE_IN', label: 'Drive-In Rack' },
  { value: 'FLOOR', label: 'Floor Storage' },
  { value: 'STAGING', label: 'Staging Area' },
  { value: 'WORKING_STATUS', label: 'Working Status' },
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

const HIERARCHY_SCOPE_OPTIONS: Array<{ value: WarehouseLocationHierarchyScope; label: string }> = [
  { value: 'WAREHOUSE', label: 'Warehouse default' },
  { value: 'WAREHOUSE_CUSTOMER', label: 'Warehouse + customer override' }
];

const HIERARCHY_SOURCE_OPTIONS: Array<{ value: WarehouseLocationHierarchySource; label: string }> = [
  { value: 'AREA_CODE', label: 'Area code' },
  { value: 'RACK_CODE', label: 'Rack code' },
  { value: 'POSITION_CODE', label: 'Position code' },
  { value: 'LEVEL_CODE', label: 'Level code' }
];

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
      <span *ngIf="showProfileLoading" class="load-state" aria-live="polite">
        <span class="save-spinner" aria-hidden="true"></span>
        Loading profile...
      </span>
      <span *ngIf="saveStatusText" class="save-state" [class.save-state-dirty]="saveState === 'dirty'" [class.save-state-saving]="saveState === 'saving'" [class.save-state-success]="saveState === 'success'" [class.save-state-error]="saveState === 'error'">
        <span *ngIf="saveState === 'saving'" class="save-spinner" aria-hidden="true"></span>
        {{ saveStatusText }}
      </span>
      <button
        type="button"
        class="header-action header-action-secondary"
        (click)="refreshProfiles()"
        [disabled]="showWorkspaceBusy">
        <i class="pi pi-refresh"></i>
        <span>Refresh Profiles</span>
      </button>
      <button type="button" class="header-action" (click)="saveProfile()" [disabled]="isSaving || isGeneratingFromDb || isHierarchyBusy">
        <i class="pi" [class.pi-save]="!isSaving" [class.pi-spin]="isSaving" [class.pi-spinner]="isSaving"></i>
        <span>{{ isSaving ? 'Saving Layout...' : 'Save Layout' }}</span>
      </button>
    </wms-page-header>

    <wms-warehouse-optimize-nav></wms-warehouse-optimize-nav>

    <div class="design-shell" [class.design-shell-busy]="showWorkspaceBusy">
      <section class="panel profile-panel" [class.collapsed-panel]="isPanelCollapsed('profile')">
        <div class="section-head">
          <div>
            <h3>Profile</h3>
            <p>Shared warehouse geometry lives here, while 2D and 3D pages stay read-only.</p>
          </div>
          <div class="section-head-actions">
            <span class="chip">Warehouse shared</span>
            <button
              type="button"
              class="icon-btn panel-collapse-btn"
              [attr.aria-label]="isPanelCollapsed('profile') ? 'Expand profile panel' : 'Collapse profile panel'"
              [attr.title]="isPanelCollapsed('profile') ? 'Expand profile panel' : 'Collapse profile panel'"
              (click)="togglePanel('profile')">
              <i class="pi" [class.pi-angle-down]="isPanelCollapsed('profile')" [class.pi-angle-up]="!isPanelCollapsed('profile')"></i>
            </button>
          </div>
        </div>

        <div class="profile-grid" *ngIf="!isPanelCollapsed('profile')">
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
            <input type="text" [(ngModel)]="profileName" (ngModelChange)="updateDraftMetadata()" maxlength="100" />
          </label>

          <label class="field field-span-2">
            <span>Description</span>
            <textarea rows="2" [(ngModel)]="description" (ngModelChange)="updateDraftMetadata()" maxlength="255"></textarea>
          </label>

          <label class="field">
            <span>Warehouse width (m)</span>
            <input type="number" [(ngModel)]="warehouseWidth" min="10" max="500" step="1" />
          </label>

          <label class="field">
            <span>Warehouse height (m)</span>
            <input type="number" [(ngModel)]="warehouseHeight" min="10" max="500" step="1" />
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

      <section class="panel profile-panel" [class.collapsed-panel]="isPanelCollapsed('hierarchy')">
        <div class="section-head">
          <div>
            <h3>Location Hierarchy</h3>
            <p>Map Oracle location fields into a logical warehouse structure for DB generation and live stock matching.</p>
          </div>
          <div class="section-head-actions">
            <span class="chip" [class.chip-override]="hierarchyResponse?.effective?.scope === 'WAREHOUSE_CUSTOMER'">
              {{ hierarchyEffectiveSummary }}
            </span>
            <button
              type="button"
              class="icon-btn panel-collapse-btn"
              [attr.aria-label]="isPanelCollapsed('hierarchy') ? 'Expand location hierarchy panel' : 'Collapse location hierarchy panel'"
              [attr.title]="isPanelCollapsed('hierarchy') ? 'Expand location hierarchy panel' : 'Collapse location hierarchy panel'"
              (click)="togglePanel('hierarchy')">
              <i class="pi" [class.pi-angle-down]="isPanelCollapsed('hierarchy')" [class.pi-angle-up]="!isPanelCollapsed('hierarchy')"></i>
            </button>
          </div>
        </div>

        <div class="profile-grid" *ngIf="!isPanelCollapsed('hierarchy')">
          <label class="field">
            <span>Scope</span>
            <select [ngModel]="hierarchyScope" (ngModelChange)="changeHierarchyScope($event)" [disabled]="isHierarchyBusy">
              <option *ngFor="let option of hierarchyScopeOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <div class="field">
            <span>Customer target</span>
            <div class="inline-summary">
              {{ hierarchyScope === 'WAREHOUSE_CUSTOMER' ? (state.activeCustomerCode() || 'No customer selected') : 'All customers in this warehouse' }}
            </div>
          </div>

          <div class="field field-span-2 hierarchy-summary-card">
            <span>Effective setting</span>
            <strong>{{ hierarchyEffectiveDetail }}</strong>
            <small>{{ hierarchySelectedRecordSummary }}</small>
          </div>

          <label class="field">
            <span>Zone</span>
            <select [(ngModel)]="hierarchyForm.zone" [disabled]="isHierarchyBusy">
              <option [ngValue]="''">Not used</option>
              <option *ngFor="let option of hierarchySourceOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <label class="field">
            <span>Aisle</span>
            <select [(ngModel)]="hierarchyForm.aisle" [disabled]="isHierarchyBusy">
              <option [ngValue]="''">Not used</option>
              <option *ngFor="let option of hierarchySourceOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <label class="field">
            <span>Rack / Bay</span>
            <select [(ngModel)]="hierarchyForm.bay" [disabled]="isHierarchyBusy">
              <option *ngFor="let option of hierarchySourceOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <label class="field">
            <span>Position / Slot</span>
            <select [(ngModel)]="hierarchyForm.slot" [disabled]="isHierarchyBusy">
              <option [ngValue]="''">Not used</option>
              <option *ngFor="let option of hierarchySourceOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <label class="field">
            <span>Level</span>
            <select [(ngModel)]="hierarchyForm.level" [disabled]="isHierarchyBusy">
              <option *ngFor="let option of hierarchySourceOptions" [ngValue]="option.value">{{ option.label }}</option>
            </select>
          </label>

          <div class="field field-span-2 hierarchy-status-row">
            <span>Status</span>
            <strong>{{ hierarchyStatusText || 'Ready' }}</strong>
          </div>

          <div class="field field-actions">
            <button type="button" class="soft-btn" (click)="reloadLocationHierarchy()" [disabled]="isHierarchyBusy || isGeneratingFromDb || isSaving">
              <i class="pi" [class.pi-refresh]="!isLoadingHierarchy" [class.pi-spin]="isLoadingHierarchy" [class.pi-spinner]="isLoadingHierarchy"></i>
              <span>{{ isLoadingHierarchy ? 'Loading hierarchy...' : 'Reload hierarchy' }}</span>
            </button>
            <button type="button" class="soft-btn danger" (click)="deleteLocationHierarchy()" [disabled]="isHierarchyBusy || !selectedHierarchySetting">
              <i class="pi" [class.pi-trash]="!isDeletingHierarchy" [class.pi-spin]="isDeletingHierarchy" [class.pi-spinner]="isDeletingHierarchy"></i>
              <span>{{ isDeletingHierarchy ? 'Deleting...' : hierarchyDeleteLabel }}</span>
            </button>
            <button type="button" class="soft-btn primary" (click)="saveLocationHierarchy()" [disabled]="isHierarchyBusy || isGeneratingFromDb || isSaving">
              <i class="pi" [class.pi-save]="!isSavingHierarchy" [class.pi-spin]="isSavingHierarchy" [class.pi-spinner]="isSavingHierarchy"></i>
              <span>{{ isSavingHierarchy ? 'Saving hierarchy...' : 'Save hierarchy' }}</span>
            </button>
          </div>
        </div>
      </section>

      <section class="workspace-grid" [class.side-collapsed]="sidebarPanelsCollapsed">
      <section class="panel collapsed-rail collapsed-sidebar-rail" *ngIf="sidebarPanelsCollapsed">
        <div class="panel-head collapsed-rail-head">
          <h3>Design Panels</h3>
          <button
            type="button"
            class="icon-btn"
            aria-label="Expand design panels"
            title="Expand design panels"
            (click)="toggleSidebarPanels()">
            <i class="pi pi-angle-right"></i>
          </button>
        </div>
      </section>

      <aside class="sidebar" *ngIf="!sidebarPanelsCollapsed">
        <section class="panel" [class.collapsed-panel]="isPanelCollapsed('zones')">
          <div class="section-head compact">
            <div>
              <h3>Zones</h3>
              <p>{{ layout.aisles.length }} zone{{ layout.aisles.length === 1 ? '' : 's' }}</p>
            </div>
            <div class="section-head-actions">
              <button
                *ngIf="selectedAisleIds.length > 1 && !isPanelCollapsed('zones')"
                type="button"
                class="soft-btn danger"
                (click)="deleteSelectedAisles()">
                Delete Selection
              </button>
              <button
                type="button"
                class="icon-btn panel-collapse-btn"
                [attr.aria-label]="isPanelCollapsed('zones') ? 'Expand zones panel' : 'Collapse zones panel'"
                [attr.title]="isPanelCollapsed('zones') ? 'Expand zones panel' : 'Collapse zones panel'"
                (click)="togglePanel('zones')">
                <i class="pi" [class.pi-angle-down]="isPanelCollapsed('zones')" [class.pi-angle-up]="!isPanelCollapsed('zones')"></i>
              </button>
            </div>
          </div>

          <ng-container *ngIf="!isPanelCollapsed('zones')">
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
                <strong>{{ aisleLabel(aisle) }}</strong>
                <span class="type-badge">{{ typeLabel(aisle.type) }}</span>
              </div>
              <div class="aisle-card-meta">
                {{ formatDimension(aisle.width) }} x {{ formatDimension(aisle.height) }} m
              </div>
              <div class="aisle-card-meta">
                {{ aisle.levels }} level{{ aisle.levels === 1 ? '' : 's' }} and {{ aisle.locations.length }} locations
              </div>
              <div class="aisle-card-meta" *ngIf="mixedLevelSummaryForAisle(aisle) as splitSummary">
                {{ splitSummary }}
              </div>
            </button>
          </div>

          <ng-template #emptyAisles>
            <div class="empty-state">
              Draw on the canvas or use Quick Generate to create your first zone.
            </div>
          </ng-template>
          </ng-container>
        </section>

        <section class="panel" *ngIf="selectedAisleIds.length === 1 && selectedAisle">
          <div class="section-head compact">
            <div>
              <h3>Properties</h3>
              <p>{{ aisleLabel(selectedAisle) }}</p>
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

            <label class="field" *ngIf="propertyForm.type === 'WORKING_STATUS'">
              <span>Operation flow</span>
              <select
                [ngModel]="propertyForm.workingStatusFlow"
                (ngModelChange)="updateWorkingStatusFlow($event)">
                <option *ngFor="let option of workingStatusFlowOptions" [ngValue]="option.value">{{ option.label }}</option>
              </select>
            </label>

            <label class="field" *ngIf="propertyForm.type === 'WORKING_STATUS'">
              <span>System status</span>
              <select
                [ngModel]="propertyForm.workingStatusCode"
                (ngModelChange)="updateWorkingStatusCode($event)">
                <option
                  *ngFor="let option of workingStatusOptions(propertyForm.workingStatusFlow)"
                  [ngValue]="option.code">
                  {{ option.label }}
                </option>
              </select>
            </label>

            <div class="field field-span-2 status-preview" *ngIf="propertyForm.type === 'WORKING_STATUS'">
              <span>Status preview</span>
              <div class="status-preview-card">
                <i class="status-preview-swatch" [style.background]="propertyForm.workingStatusColor"></i>
                <div class="status-preview-copy">
                  <strong>{{ propertyForm.workingStatusLabel || 'No status selected' }}</strong>
                  <small>{{ propertyForm.workingStatusFlow }} {{ propertyForm.workingStatusCode }}</small>
                </div>
              </div>
            </div>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Levels</span>
              <input type="number" [(ngModel)]="propertyForm.levels" min="1" max="12" step="1" />
            </label>

            <label class="field" *ngIf="propertyForm.type === 'HIGH_RACK'">
              <span>Lower shelf levels</span>
              <input
                type="number"
                [(ngModel)]="propertyForm.lowerShelfLevels"
                min="0"
                [max]="maxLowerShelfLevelsForForm"
                step="1" />
            </label>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Tunnel from</span>
              <input type="number" [(ngModel)]="propertyForm.tunnelLevelFrom" min="1" max="12" step="1" />
            </label>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Tunnel to</span>
              <input type="number" [(ngModel)]="propertyForm.tunnelLevelTo" min="1" max="12" step="1" />
            </label>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Bay width (m)</span>
              <input type="number" [(ngModel)]="propertyForm.bayWidth" min="0.5" max="5" step="0.1" />
            </label>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Bay depth (m)</span>
              <input type="number" [(ngModel)]="propertyForm.bayDepth" min="0.5" max="3" step="0.1" />
            </label>

            <label class="field" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
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

            <label class="field field-span-2" *ngIf="propertyForm.type !== 'WORKING_STATUS'">
              <span>Pick face levels</span>
              <select multiple [ngModel]="propertyForm.pickFaceLevels" (ngModelChange)="updatePickFaceLevels($event)" class="multi-select">
                <option *ngFor="let level of levelOptions(propertyForm.levels)" [ngValue]="level">
                  Level {{ level }}
                </option>
              </select>
            </label>

            <div class="field field-span-2 split-summary" *ngIf="propertyForm.type === 'HIGH_RACK'">
              <span>Storage split</span>
              <strong>{{ mixedLevelSummary(propertyForm.levels, propertyForm.lowerShelfLevels) }}</strong>
            </div>

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

        <section class="panel" [class.collapsed-panel]="isPanelCollapsed('statistics')">
          <div class="section-head compact">
            <div>
              <h3>Statistics</h3>
              <p>Live values from the current design.</p>
            </div>
            <div class="section-head-actions">
              <button
                type="button"
                class="icon-btn panel-collapse-btn"
                [attr.aria-label]="isPanelCollapsed('statistics') ? 'Expand statistics panel' : 'Collapse statistics panel'"
                [attr.title]="isPanelCollapsed('statistics') ? 'Expand statistics panel' : 'Collapse statistics panel'"
                (click)="togglePanel('statistics')">
                <i class="pi" [class.pi-angle-down]="isPanelCollapsed('statistics')" [class.pi-angle-up]="!isPanelCollapsed('statistics')"></i>
              </button>
            </div>
          </div>

          <div class="stats-grid" *ngIf="!isPanelCollapsed('statistics')">
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

        <section class="panel" [class.collapsed-panel]="isPanelCollapsed('quickGenerate')">
          <div class="section-head compact">
            <div>
              <h3>Quick Generate</h3>
              <p>Use this for a starting grid, then fine-tune with drawing tools.</p>
            </div>
            <div class="section-head-actions">
              <button
                type="button"
                class="icon-btn panel-collapse-btn"
                [attr.aria-label]="isPanelCollapsed('quickGenerate') ? 'Expand quick generate panel' : 'Collapse quick generate panel'"
                [attr.title]="isPanelCollapsed('quickGenerate') ? 'Expand quick generate panel' : 'Collapse quick generate panel'"
                (click)="togglePanel('quickGenerate')">
                <i class="pi" [class.pi-angle-down]="isPanelCollapsed('quickGenerate')" [class.pi-angle-up]="!isPanelCollapsed('quickGenerate')"></i>
              </button>
            </div>
          </div>

          <div class="property-grid" *ngIf="!isPanelCollapsed('quickGenerate')">
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
              <span>Zone start</span>
              <input type="number" [(ngModel)]="generator.zoneStartNumber" min="1" max="999" step="1" />
            </label>

            <label class="field">
              <span>Zone naming</span>
              <select [(ngModel)]="generator.zoneNamingPattern">
                <option value="prefix-row">{{ '{Prefix}-{Row}' }}</option>
                <option value="prefixrow">{{ '{Prefix}{Row}' }}</option>
                <option value="prefix-row-suffix">{{ '{Prefix}-{Row}-{Suffix}' }}</option>
                <option value="custom">Custom</option>
              </select>
            </label>

            <label class="field">
              <span>Zone suffix</span>
              <input type="text" [(ngModel)]="generator.zoneSuffix" />
            </label>

            <label class="field field-span-2" *ngIf="generator.zoneNamingPattern === 'custom'">
              <span>Custom zone pattern</span>
              <input type="text" [(ngModel)]="generator.customZonePattern" placeholder="{{ '{prefix}-{row}' }}" />
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
              <span>Row format</span>
              <select [(ngModel)]="generator.rowFormat">
                <option value="numeric">Numeric</option>
                <option value="alpha">Alpha</option>
                <option value="alpha2">Alpha2</option>
              </select>
            </label>

            <label class="field">
              <span>Location naming</span>
              <select [(ngModel)]="generator.locationNamingPattern">
                <option value="zone-row-bay-level">{{ '{Zone}-{Row}-{Bay}-L{Level}' }}</option>
                <option value="zone-bay-level">{{ '{Zone}-{Bay}-L{Level}' }}</option>
                <option value="zone-row-bay-side-level">{{ '{Zone}-{Row}-{Bay}{Side}-L{Level}' }}</option>
                <option value="custom">Custom</option>
              </select>
            </label>

            <label class="field field-span-2" *ngIf="generator.locationNamingPattern === 'custom'">
              <span>Custom location pattern</span>
              <input type="text" [(ngModel)]="generator.customLocationPattern" placeholder="{{ '{zone}-{row}-{bay}{side}-{level}' }}" />
            </label>

            <label class="field">
              <span>Row padding</span>
              <input type="number" [(ngModel)]="generator.rowPadding" min="1" max="4" step="1" />
            </label>

            <label class="field">
              <span>Bay padding</span>
              <input type="number" [(ngModel)]="generator.bayPadding" min="1" max="4" step="1" />
            </label>

            <label class="field">
              <span>Level prefix</span>
              <input type="text" [(ngModel)]="generator.levelPrefix" maxlength="4" />
            </label>

            <label class="field">
              <span>Left bays</span>
              <select [(ngModel)]="generator.leftBayNumbering">
                <option value="running">Running</option>
                <option value="odd">Odd</option>
                <option value="even">Even</option>
              </select>
            </label>

            <label class="field">
              <span>Right bays</span>
              <select [(ngModel)]="generator.rightBayNumbering">
                <option value="running">Running</option>
                <option value="odd">Odd</option>
                <option value="even">Even</option>
              </select>
            </label>

            <label class="field">
              <span>Row start</span>
              <input type="number" [(ngModel)]="generator.rowNumberStart" min="1" max="999" step="1" />
            </label>

            <label class="field">
              <span>Bay start</span>
              <input type="number" [(ngModel)]="generator.bayNumberStart" min="1" max="999" step="1" />
            </label>

            <label class="field">
              <span>Level start</span>
              <input type="number" [(ngModel)]="generator.levelNumberStart" min="1" max="99" step="1" />
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

          <div class="generate-preview" *ngIf="!isPanelCollapsed('quickGenerate')">
            <div class="generate-preview-row"><span>Zone preview</span><strong>{{ generatorZonePreview }}</strong></div>
            <div class="generate-preview-row"><span>Location preview</span><strong>{{ generatorLocationPreview }}</strong></div>
            <div class="generate-preview-row" *ngIf="generateStatusText"><span>Status</span><strong [class.generate-status-saving]="generateState === 'generating'" [class.generate-status-error]="generateState === 'error'">{{ generateStatusText }}</strong></div>
          </div>

          <div class="property-actions" *ngIf="!isPanelCollapsed('quickGenerate')">
            <button type="button" class="soft-btn primary" (click)="generateLayout()" [disabled]="isGeneratingLayout || isGeneratingFromDb || isSaving || isHierarchyBusy">{{ isGeneratingLayout ? 'Generating Layout...' : 'Generate Layout' }}</button>
            <button type="button" class="soft-btn" (click)="generateLayoutFromDb()" [disabled]="isGeneratingFromDb || isSaving || isHierarchyBusy || isGeneratingLayout">
              <i class="pi" [class.pi-database]="!isGeneratingFromDb" [class.pi-spin]="isGeneratingFromDb" [class.pi-spinner]="isGeneratingFromDb"></i>
              <span>{{ isGeneratingFromDb ? 'Generating from DB...' : 'Generate from DB' }}</span>
            </button>
            <button type="button" class="soft-btn danger" (click)="clearDesign()" [disabled]="isGeneratingFromDb || isSaving || isHierarchyBusy || isGeneratingLayout">Clear Design</button>
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
        <div class="panel-head designer-panel-head">
          <div>
            <h3>Layout Designer</h3>
            <span class="panel-meta">{{ layout.aisles.length }} zones and {{ stats.locations }} locations</span>
          </div>
          <div class="panel-head-actions">
            <span *ngIf="showProfileLoading" class="inline-loading" aria-live="polite">
              <span class="save-spinner" aria-hidden="true"></span>
              Applying saved layout...
            </span>
            <button
              type="button"
              class="icon-btn"
              [attr.aria-label]="sidebarPanelsCollapsed ? 'Expand design panels' : 'Collapse design panels'"
              [attr.title]="sidebarPanelsCollapsed ? 'Expand design panels' : 'Collapse design panels'"
              (click)="toggleSidebarPanels()">
              <i class="pi" [class.pi-angle-right]="sidebarPanelsCollapsed" [class.pi-angle-left]="!sidebarPanelsCollapsed"></i>
            </button>
          </div>
        </div>

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
              <button
                type="button"
                class="icon-btn toggle-icon-btn"
                [class.active]="showGrid"
                title="Toggle grid"
                aria-label="Toggle grid"
                (click)="toggleGrid()">
                <i class="pi pi-th-large"></i>
              </button>
              <button
                type="button"
                class="icon-btn toggle-icon-btn"
                [class.active]="showLocations"
                title="Toggle locations"
                aria-label="Toggle locations"
                (click)="toggleLocations()">
                <i class="pi pi-map-marker"></i>
              </button>
              <button
                type="button"
                class="icon-btn toggle-icon-btn"
                [class.active]="showLabels"
                title="Toggle labels"
                aria-label="Toggle labels"
                (click)="toggleLabels()">
                <i class="pi pi-tag"></i>
              </button>
              <button
                type="button"
                class="icon-btn toggle-icon-btn"
                [class.active]="showHeatmap"
                title="Toggle heatmap"
                aria-label="Toggle heatmap"
                (click)="toggleHeatmap()">
                <i class="pi pi-chart-bar"></i>
              </button>
            </div>
          </div>

          <div class="toolbar-row modes">
            <label class="toolbar-inline-field">
              <span>Default type</span>
              <select [(ngModel)]="defaultType" (ngModelChange)="updateDefaultType($event)">
                <option *ngFor="let option of storageTypeOptions" [ngValue]="option.value">{{ option.label }}</option>
              </select>
            </label>

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

      <div class="workspace-loading-overlay" *ngIf="showWorkspaceBusy" aria-live="polite">
        <span class="save-spinner" aria-hidden="true"></span>
        <strong>{{ workspaceBusyTitle }}</strong>
        <span>{{ workspaceBusyDetail }}</span>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
    }

    .design-shell {
      position: relative;
    }

    .design-shell-busy .panel,
    .design-shell-busy .workspace-grid {
      user-select: none;
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

    .header-action:disabled,
    .header-action-secondary:disabled,
    .soft-btn:disabled,
    .icon-btn:disabled,
    .toggle-btn:disabled,
    .mode-btn:disabled {
      cursor: wait;
      opacity: 0.7;
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

    .save-state {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      min-height: 36px;
      padding: 0 12px;
      border-radius: 999px;
      border: 1px solid #dbe2ea;
      background: #ffffff;
      color: #475569;
      font-size: 12px;
      font-weight: 600;
    }

    .load-state {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      min-height: 36px;
      padding: 0 12px;
      border-radius: 999px;
      border: 1px solid #93c5fd;
      background: #eff6ff;
      color: #1d4ed8;
      font-size: 12px;
      font-weight: 600;
    }

    .save-state-dirty {
      border-color: #fdba74;
      background: #fff7ed;
      color: #9a3412;
    }

    .save-state-saving {
      border-color: #93c5fd;
      background: #eff6ff;
      color: #1d4ed8;
    }

    .save-state-success {
      border-color: #86efac;
      background: #f0fdf4;
      color: #166534;
    }

    .save-state-error {
      border-color: #fca5a5;
      background: #fef2f2;
      color: #b91c1c;
    }

    .save-spinner {
      width: 12px;
      height: 12px;
      border-radius: 999px;
      border: 2px solid currentColor;
      border-right-color: transparent;
      animation: save-spin 0.7s linear infinite;
    }

    @keyframes save-spin {
      to {
        transform: rotate(360deg);
      }
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

    .collapsed-panel .section-head,
    .collapsed-panel .section-head.compact {
      margin-bottom: 0;
    }

    .section-head-actions {
      display: inline-flex;
      align-items: center;
      justify-content: flex-end;
      gap: 8px;
      flex-wrap: wrap;
      min-width: 0;
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

    .chip-override {
      background: #eef2ff;
      color: #4338ca;
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

    .inline-summary,
    .hierarchy-summary-card,
    .hierarchy-status-row {
      width: 100%;
      min-height: 44px;
      border: 1px solid #dbe2ea;
      border-radius: 8px;
      background: #f8fafc;
      color: #0f172a;
      padding: 10px 12px;
      font-size: 13px;
      line-height: 1.5;
    }

    .inline-summary {
      display: flex;
      align-items: center;
    }

    .hierarchy-summary-card {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .hierarchy-summary-card strong,
    .hierarchy-status-row strong {
      font-size: 13px;
      color: #0f172a;
    }

    .hierarchy-summary-card small {
      font-size: 11px;
      color: #64748b;
      line-height: 1.5;
    }

    .hierarchy-status-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
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
      grid-column: span 2;
    }

    .workspace-grid {
      display: grid;
      grid-template-columns: minmax(310px, 360px) minmax(0, 1fr);
      gap: 18px;
      align-items: start;
      position: relative;
    }

    .workspace-grid.side-collapsed {
      grid-template-columns: 88px minmax(0, 1fr);
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

    .status-preview-card {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px 12px;
      border: 1px solid #dbe2ea;
      border-radius: 8px;
      background: #f8fafc;
      min-height: 52px;
    }

    .split-summary {
      justify-content: space-between;
      padding: 10px 12px;
      border: 1px solid #dbe2ea;
      border-radius: 8px;
      background: #f8fafc;
      gap: 8px;
    }

    .split-summary strong {
      font-size: 13px;
      color: #0f172a;
      text-align: right;
    }

    .status-preview-swatch {
      width: 16px;
      height: 16px;
      border-radius: 999px;
      flex: 0 0 auto;
      box-shadow: 0 0 0 1px rgba(15, 23, 42, 0.08) inset;
    }

    .status-preview-copy {
      display: flex;
      flex-direction: column;
      gap: 2px;
      min-width: 0;
    }

    .status-preview-copy strong {
      font-size: 13px;
      color: #0f172a;
      word-break: break-word;
    }

    .status-preview-copy small {
      font-size: 11px;
      color: #64748b;
      text-transform: uppercase;
      letter-spacing: 0;
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

    .panel-head {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      margin-bottom: 0;
    }

    .panel-head h3 {
      margin: 0;
      font-size: 15px;
      font-weight: 700;
      color: #0f172a;
    }

    .panel-head-actions {
      display: flex;
      align-items: center;
      gap: 10px;
      color: #64748b;
      font-size: 12px;
    }

    .panel-meta {
      display: inline-block;
      margin-top: 4px;
      font-size: 12px;
      color: #64748b;
    }

    .designer-panel-head {
      padding: 16px;
      border-bottom: 1px solid #e2e8f0;
      background: #ffffff;
    }

    .inline-loading {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      color: #1d4ed8;
      font-weight: 600;
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

    .panel-collapse-btn {
      flex: 0 0 auto;
    }

    .toggle-btn.active,
    .toggle-icon-btn.active,
    .mode-btn.active {
      background: #dbeafe;
      border-color: #60a5fa;
      color: #1d4ed8;
    }

    .toggle-icon-btn {
      color: #475569;
    }

    .toolbar-inline-field {
      display: inline-flex;
      align-items: center;
      gap: 10px;
      padding: 0 12px;
      min-height: 38px;
      border: 1px solid #d6deea;
      border-radius: 8px;
      background: #ffffff;
    }

    .toolbar-inline-field span {
      font-size: 12px;
      font-weight: 600;
      color: #475569;
      white-space: nowrap;
    }

    .toolbar-inline-field select {
      min-width: 170px;
      border: 0;
      background: transparent;
      color: #0f172a;
      font-size: 12px;
      font-weight: 600;
      padding: 0;
      outline: none;
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

    .collapsed-rail {
      min-height: 760px;
      padding: 12px 8px;
      position: sticky;
      top: 0;
    }

    .collapsed-sidebar-rail .collapsed-rail-head {
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      gap: 16px;
      min-height: 100%;
    }

    .collapsed-sidebar-rail .collapsed-rail-head h3 {
      writing-mode: vertical-rl;
      transform: rotate(180deg);
      font-size: 13px;
      color: #475569;
      min-height: 180px;
      text-align: center;
    }

    .workspace-loading-overlay {
      position: absolute;
      inset: 0;
      z-index: 20;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 10px;
      border-radius: 8px;
      background: rgba(248, 250, 252, 0.88);
      backdrop-filter: blur(4px);
      color: #1e293b;
      text-align: center;
      pointer-events: all;
    }

    .workspace-loading-overlay strong {
      font-size: 16px;
      color: #0f172a;
    }

    .workspace-loading-overlay span:last-child {
      font-size: 12px;
      color: #64748b;
    }

    @media (max-width: 1320px) {
      .workspace-grid {
        grid-template-columns: 1fr;
      }

      .workspace-grid.side-collapsed {
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

      .field-actions {
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

      .collapsed-rail {
        min-height: 120px;
        position: static;
      }

      .collapsed-sidebar-rail .collapsed-rail-head h3 {
        writing-mode: horizontal-tb;
        transform: none;
        min-height: 0;
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
  readonly workingStatusFlowOptions = WORKING_STATUS_FLOW_OPTIONS;
  readonly hierarchyScopeOptions = HIERARCHY_SCOPE_OPTIONS;
  readonly hierarchySourceOptions = HIERARCHY_SOURCE_OPTIONS;
  readonly panelCollapsed: Record<CollapsiblePanelKey, boolean> = {
    profile: true,
    hierarchy: true,
    zones: true,
    statistics: true,
    quickGenerate: true
  };

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
  sidebarPanelsCollapsed = false;
  zoomPercent = 100;
  coordinatesText = 'X: -, Y: -';
  isSaving = false;
  isGeneratingFromDb = false;
  generateState: GenerateState = 'idle';
  generateStatusText = '';
  saveState: SaveState = 'idle';
  saveStatusText = '';
  hierarchyScope: WarehouseLocationHierarchyScope = 'WAREHOUSE';
  hierarchyForm: HierarchyFormModel = this.createDefaultHierarchyForm();
  hierarchyResponse: WarehouseLocationHierarchyResponse | null = null;
  isLoadingHierarchy = false;
  isSavingHierarchy = false;
  isDeletingHierarchy = false;
  hierarchyStatusText = '';
  hierarchyStatusState: SaveState = 'idle';

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
  private pendingLayoutPersistsDraft = false;
  private isMinimapDragging = false;
  private suppressDraftPersistence = false;

  get modeHint(): string {
    return MODE_HINTS[this.activeMode];
  }

  get showProfileLoading(): boolean {
    if (this.state.designDraft()) {
      return false;
    }
    return this.state.loadingProfiles() || this.state.loadingProfile();
  }

  get showWorkspaceBusy(): boolean {
    return this.showProfileLoading || this.isGeneratingFromDb;
  }


  get isGeneratingLayout(): boolean {
    return this.generateState === 'generating';
  }
  get workspaceBusyTitle(): string {
    return this.isGeneratingFromDb ? 'Generating layout from DB' : 'Loading saved layout';
  }

  get workspaceBusyDetail(): string {
    if (this.isGeneratingFromDb) {
      return 'Reading warehouse master data and applying the generated draft. The designer is locked until this finishes.';
    }
    return 'Preparing the latest profile geometry for the designer.';
  }

  get utilizationPercent(): number {
    const totalArea = this.warehouseWidth * this.warehouseHeight;
    if (totalArea <= 0) {
      return 0;
    }
    const usedArea = this.layout.aisles.reduce((sum, aisle) => sum + (aisle.width * aisle.height), 0);
    return Math.round((usedArea / totalArea) * 100);
  }

  get maxLowerShelfLevelsForForm(): number {
    return Math.max(0, Math.round(this.propertyForm.levels) - 1);
  }

  get isHierarchyBusy(): boolean {
    return this.isLoadingHierarchy || this.isSavingHierarchy || this.isDeletingHierarchy;
  }

  get selectedHierarchySetting() {
    return this.hierarchyScope === 'WAREHOUSE'
      ? this.hierarchyResponse?.warehouseDefault ?? null
      : this.hierarchyResponse?.customerOverride ?? null;
  }

  get hierarchyEffectiveSummary(): string {
    if (this.hierarchyResponse?.effective?.scope === 'WAREHOUSE_CUSTOMER') {
      return 'Customer override active';
    }
    if (this.hierarchyResponse?.effective?.scope === 'WAREHOUSE') {
      return 'Warehouse default active';
    }
    return 'Standard mapping';
  }

  get hierarchyEffectiveDetail(): string {
    if (!this.hierarchyResponse?.effective) {
      return 'No hierarchy setting is saved. Auto-generate and live sync use the standard area + rack + position + level mapping.';
    }
    return this.hierarchyResponse.effective.scope === 'WAREHOUSE_CUSTOMER'
      ? `Using the customer override for ${this.hierarchyResponse.effective.customerCode}.`
      : 'Using the warehouse default for every customer unless an override exists.';
  }

  get hierarchySelectedRecordSummary(): string {
    if (this.selectedHierarchySetting) {
      const mapping = this.selectedHierarchySetting.mapping;
      return `Zone: ${this.hierarchySourceLabel(mapping.zone)}, Aisle: ${this.hierarchySourceLabel(mapping.aisle ?? '')}, Bay: ${this.hierarchySourceLabel(mapping.bay)}, Slot: ${this.hierarchySourceLabel(mapping.slot)}, Level: ${this.hierarchySourceLabel(mapping.level)}`;
    }
    return this.hierarchyScope === 'WAREHOUSE'
      ? 'No warehouse default is saved yet.'
      : `No override is saved yet for ${this.state.activeCustomerCode() || 'the current customer'}.`;
  }

  get hierarchyDeleteLabel(): string {
    return this.hierarchyScope === 'WAREHOUSE' ? 'Delete default' : 'Delete override';
  }

  constructor() {
    this.state.initialize();
    const draft = this.state.designDraft();

    if (draft) {
      this.selectedProfileId = draft.profileId;
      this.profileName = draft.profileName;
      this.description = draft.description;
      this.loadLayoutData(draft.layout, false, true);
      this.setSaveStatus('dirty', 'Unsaved changes');
    }

    effect(() => {
      if (this.state.designDraft()) {
        return;
      }

      const profile = this.state.selectedProfile();
      if (!profile) {
        return;
      }

      this.selectedProfileId = profile.id;
      this.profileName = profile.profileName;
      this.description = profile.description ?? '';

      try {
        const parsed = JSON.parse(profile.layoutData) as WarehouseDesignerLayout;
        this.loadLayoutData(parsed, false, false);
        this.setSaveStatus('success', this.savedMessage(profile.updatedAt));
      } catch (error) {
        const fallback = createEmptyLayout(
          profile.warehouseWidth ?? this.warehouseWidth,
          profile.warehouseLength ?? this.warehouseHeight
        );
        this.loadLayoutData(fallback, false, false);
        this.setSaveStatus('error', 'Loaded fallback layout');
        this.messageService.add({
          severity: 'warn',
          summary: 'Profile loaded with fallback',
          detail: error instanceof Error ? error.message : 'Could not parse layout JSON.'
        });
      }
    });

    effect(() => {
      const activeCustomerCode = this.state.activeCustomerCode();
      if (!activeCustomerCode) {
        return;
      }
      this.reloadLocationHierarchy();
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

  aisleLabel(aisle: Pick<WarehouseAisle, 'zone' | 'aisleCode'> | null | undefined): string {
    return aisleDisplayLabel(aisle);
  }

  changeHierarchyScope(scope: WarehouseLocationHierarchyScope): void {
    this.hierarchyScope = scope;
    this.applyHierarchyFormFromResponse();
    this.setHierarchyStatus('idle', this.selectedHierarchySetting ? 'Loaded saved hierarchy mapping' : 'No saved hierarchy mapping for this scope');
  }

  reloadLocationHierarchy(): void {
    if (this.isLoadingHierarchy || this.isSavingHierarchy || this.isDeletingHierarchy) {
      return;
    }
    this.isLoadingHierarchy = true;
    this.setHierarchyStatus('saving', 'Loading hierarchy mapping...');
    this.service.getLocationHierarchy(this.state.activeCustomerCode()).pipe(
      finalize(() => {
        this.isLoadingHierarchy = false;
      })
    ).subscribe({
      next: response => {
        this.hierarchyResponse = response;
        this.applyHierarchyFormFromResponse();
        this.setHierarchyStatus('success', this.selectedHierarchySetting ? 'Hierarchy mapping loaded' : 'No hierarchy mapping saved for this scope');
      },
      error: error => {
        this.setHierarchyStatus('error', this.saveErrorMessage(error));
        this.messageService.add({
          severity: 'error',
          summary: 'Hierarchy load failed',
          detail: this.saveErrorMessage(error)
        });
      }
    });
  }

  saveLocationHierarchy(): void {
    if (this.isHierarchyBusy || this.isGeneratingFromDb || this.isSaving) {
      return;
    }

    this.isSavingHierarchy = true;
    this.setHierarchyStatus('saving', 'Saving hierarchy mapping...');
    this.service.saveLocationHierarchy({
      scope: this.hierarchyScope,
      customerCode: this.hierarchyScope === 'WAREHOUSE_CUSTOMER' ? this.state.activeCustomerCode() : null,
      mapping: this.locationHierarchyPayload()
    }).pipe(
      finalize(() => {
        this.isSavingHierarchy = false;
      })
    ).subscribe({
      next: response => {
        this.hierarchyResponse = response;
        this.applyHierarchyFormFromResponse();
        this.setHierarchyStatus('success', this.hierarchyScope === 'WAREHOUSE' ? 'Warehouse default saved' : `Override saved for ${this.state.activeCustomerCode()}`);
        this.messageService.add({
          severity: 'success',
          summary: 'Hierarchy saved',
          detail: this.hierarchyScope === 'WAREHOUSE'
            ? 'The warehouse default mapping is ready for DB generation and live sync.'
            : `The customer override for ${this.state.activeCustomerCode()} is ready for DB generation and live sync.`
        });
      },
      error: error => {
        this.setHierarchyStatus('error', this.saveErrorMessage(error));
        this.messageService.add({
          severity: 'error',
          summary: 'Hierarchy save failed',
          detail: this.saveErrorMessage(error)
        });
      }
    });
  }

  deleteLocationHierarchy(): void {
    if (this.isHierarchyBusy || !this.selectedHierarchySetting) {
      return;
    }
    const scopeLabel = this.hierarchyScope === 'WAREHOUSE' ? 'warehouse default' : `override for ${this.state.activeCustomerCode()}`;
    if (!window.confirm(`Delete the ${scopeLabel} hierarchy mapping?`)) {
      return;
    }

    this.isDeletingHierarchy = true;
    this.setHierarchyStatus('saving', 'Deleting hierarchy mapping...');
    this.service.deleteLocationHierarchy(
      this.hierarchyScope,
      this.hierarchyScope === 'WAREHOUSE_CUSTOMER' ? this.state.activeCustomerCode() : null
    ).pipe(
      finalize(() => {
        this.isDeletingHierarchy = false;
      })
    ).subscribe({
      next: response => {
        this.hierarchyResponse = response;
        this.applyHierarchyFormFromResponse();
        this.setHierarchyStatus('success', this.hierarchyScope === 'WAREHOUSE' ? 'Warehouse default deleted' : `Override deleted for ${this.state.activeCustomerCode()}`);
        this.messageService.add({
          severity: 'success',
          summary: 'Hierarchy deleted',
          detail: this.hierarchyScope === 'WAREHOUSE'
            ? 'The warehouse default mapping was removed. The standard mapping is active again.'
            : `The override for ${this.state.activeCustomerCode()} was removed. Fallback rules are active again.`
        });
      },
      error: error => {
        this.setHierarchyStatus('error', this.saveErrorMessage(error));
        this.messageService.add({
          severity: 'error',
          summary: 'Hierarchy delete failed',
          detail: this.saveErrorMessage(error)
        });
      }
    });
  }

  isSelected(aisleId: number): boolean {
    return this.selectedAisleIds.includes(aisleId);
  }

  typeLabel(type: string): string {
    return STORAGE_TYPE_OPTIONS.find(option => option.value === type)?.label ?? type;
  }

  mixedLevelSummary(levels: number, lowerShelfLevels: number | null | undefined): string {
    const resolvedLevels = clampWhole(levels, 1, 12, 1);
    const resolvedLowerShelfLevels = clampWhole(
      Number(lowerShelfLevels ?? 0),
      0,
      Math.max(resolvedLevels - 1, 0),
      0
    );

    if (resolvedLowerShelfLevels <= 0) {
      return resolvedLevels === 1 ? 'Rack L1' : `Rack L1-L${resolvedLevels}`;
    }

    const rackStartLevel = resolvedLowerShelfLevels + 1;
    const shelfSummary = resolvedLowerShelfLevels === 1
      ? 'Shelf L1'
      : `Shelf L1-L${resolvedLowerShelfLevels}`;
    const rackSummary = rackStartLevel === resolvedLevels
      ? `Rack L${rackStartLevel}`
      : `Rack L${rackStartLevel}-L${resolvedLevels}`;

    return `${shelfSummary}, ${rackSummary}`;
  }

  mixedLevelSummaryForAisle(aisle: WarehouseAisle): string | null {
    if (aisle.type !== 'HIGH_RACK') {
      return null;
    }
    return this.mixedLevelSummary(aisle.levels, aisle.lowerShelfLevels);
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

  toggleSidebarPanels(): void {
    this.sidebarPanelsCollapsed = !this.sidebarPanelsCollapsed;
  }

  isPanelCollapsed(panel: CollapsiblePanelKey): boolean {
    return this.panelCollapsed[panel];
  }

  togglePanel(panel: CollapsiblePanelKey): void {
    this.panelCollapsed[panel] = !this.panelCollapsed[panel];
  }

  updateDraftMetadata(): void {
    this.persistDraftState();
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

    this.warehouseWidth = nextWidth;
    this.warehouseHeight = nextHeight;

    if (!hasContent) {
      this.loadLayoutData(createEmptyLayout(nextWidth, nextHeight), true);
      this.messageService.add({
        severity: 'success',
        summary: 'Warehouse size applied',
        detail: `Updated empty warehouse to ${nextWidth} x ${nextHeight} m.`
      });
      return;
    }

    const shouldClearLayout = window.confirm(
      'Apply new warehouse size?\n\nSelect OK to clear the current layout and apply the new size.\nSelect Cancel to keep the current layout and resize the warehouse from the right and top edges.'
    );

    if (shouldClearLayout) {
      this.loadLayoutData(createEmptyLayout(nextWidth, nextHeight), true);
      this.messageService.add({
        severity: 'success',
        summary: 'Warehouse resized',
        detail: `Cleared the layout and resized the warehouse to ${nextWidth} x ${nextHeight} m.`
      });
      return;
    }

    if (this.drawer) {
      const result = this.drawer.resizeWarehouseBounds(nextWidth, nextHeight);
      this.captureDrawerState();
      this.updateMinimap();
      this.messageService.add({
        severity: 'success',
        summary: 'Warehouse resized',
        detail: this.preservedResizeSummaryText(nextWidth, nextHeight, result)
      });
      return;
    }

    const resizedLayout = this.resizeLayoutPreservingContent(this.pendingLayoutData, nextWidth, nextHeight);
    this.loadLayoutData(resizedLayout.layout, false);
    this.messageService.add({
      severity: 'success',
      summary: 'Warehouse resized',
      detail: this.preservedResizeSummaryText(nextWidth, nextHeight, resizedLayout.summary)
    });
  }

  get generatorZonePreview(): string {
    return this.buildGeneratorZonePreview();
  }

  get generatorLocationPreview(): string {
    return this.buildGeneratorLocationPreview();
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

    this.generateState = 'generating';
    this.generateStatusText = 'Generating layout preview...';
    this.generator = config;
    try {
      this.loadLayoutData(createGeneratedLayout(config), true);
      this.generateState = 'success';
      this.generateStatusText = 'Generate completed.';
      this.messageService.add({
        severity: 'success',
        summary: 'Starter layout generated',
        detail: 'You can keep drawing and adjusting zones directly on the canvas.'
      });
    } catch (error) {
      this.generateState = 'error';
      this.generateStatusText = 'Generate failed.';
      this.messageService.add({
        severity: 'error',
        summary: 'Generate failed',
        detail: error instanceof Error ? error.message : 'Could not generate the layout.'
      });
    }
  }

  generateLayoutFromDb(): void {
    if (this.isGeneratingFromDb || this.isSaving || this.isHierarchyBusy) {
      return;
    }

    if (this.hasDesignContent() &&
      !window.confirm('Generate a draft from the warehouse DB and replace the current design?')) {
      return;
    }

    this.isGeneratingFromDb = true;
    this.setSaveStatus('saving', 'Generating layout from DB...');

    this.service.generateLayoutFromDb(this.state.activeCustomerCode()).pipe(
      finalize(() => {
        this.isGeneratingFromDb = false;
      })
    ).subscribe({
      next: response => {
        this.loadLayoutData(response.layout, true);
        this.setSaveStatus('dirty', 'Unsaved changes');
        this.messageService.add({
          severity: 'success',
          summary: 'Layout generated from DB',
          detail: this.generatedLayoutSummaryText(response)
        });

        if (response.warnings?.length) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Generation warnings',
            detail: response.warnings[0]
          });
        }

        if ((response.summary.normalizedDimensionRows ?? 0) > 0 || (response.summary.normalizedHeightRows ?? 0) > 0) {
          this.messageService.add({
            severity: 'info',
            summary: 'Dimensions normalized',
            detail: this.generatedLayoutNormalizationText(response.summary)
          });
        }

        if ((response.summary.mixedAreasUncertain ?? 0) > 0) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Mixed levels need review',
            detail: `${response.summary.mixedAreasUncertain} generated area${response.summary.mixedAreasUncertain === 1 ? '' : 's'} may contain mixed shelf-to-rack levels. Review Lower shelf levels after generation.`
          });
        }
      },
      error: error => {
        this.setSaveStatus('error', 'Could not generate layout from DB');
        this.messageService.add({
          severity: 'error',
          summary: 'Generation failed',
          detail: this.saveErrorMessage(error)
        });
      }
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

  workingStatusOptions(flow: string): ReadonlyArray<{ flow: 'INBOUND' | 'OUTBOUND'; code: string; label: string; color: string }> {
    return workingStatusOptionsForFlow(flow);
  }

  updateWorkingStatusFlow(flow: string): void {
    this.propertyForm.workingStatusFlow = (flow || 'OUTBOUND').toUpperCase();
    const selected =
      workingStatusDefinition(this.propertyForm.workingStatusFlow, this.propertyForm.workingStatusCode)
      ?? workingStatusOptionsForFlow(this.propertyForm.workingStatusFlow)[0];
    if (!selected) {
      this.propertyForm.workingStatusCode = '';
      this.propertyForm.workingStatusLabel = '';
      this.propertyForm.workingStatusColor = '#0EA5E9';
      return;
    }
    this.propertyForm.workingStatusCode = selected.code;
    this.propertyForm.workingStatusLabel = selected.label;
    this.propertyForm.workingStatusColor = selected.color;
  }

  updateWorkingStatusCode(code: string): void {
    const selected =
      workingStatusDefinition(this.propertyForm.workingStatusFlow, code)
      ?? workingStatusOptionsForFlow(this.propertyForm.workingStatusFlow)[0];
    if (!selected) {
      this.propertyForm.workingStatusCode = '';
      this.propertyForm.workingStatusLabel = '';
      this.propertyForm.workingStatusColor = '#0EA5E9';
      return;
    }
    this.propertyForm.workingStatusCode = selected.code;
    this.propertyForm.workingStatusLabel = selected.label;
    this.propertyForm.workingStatusColor = selected.color;
  }

  applyProperties(): void {
    if (!this.drawer?.selectedAisle) {
      return;
    }

    const levels = clampWhole(this.propertyForm.levels, 1, 12, this.drawer.selectedAisle.levels);
    const tunnelFrom = clampWhole(this.propertyForm.tunnelLevelFrom, 1, 12, 1);
    const tunnelTo = clampWhole(this.propertyForm.tunnelLevelTo, tunnelFrom, 12, levels);
    const lowerShelfLevels = this.propertyForm.type === 'HIGH_RACK'
      ? clampWhole(this.propertyForm.lowerShelfLevels, 0, Math.max(levels - 1, 0), 0)
      : 0;
    const pickFaceLevels = (this.propertyForm.pickFaceLevels.length > 0 ? this.propertyForm.pickFaceLevels : [1])
      .map(value => clampWhole(value, 1, levels, 1))
      .sort((left, right) => left - right);
    const workingStatusType = this.propertyForm.type === 'WORKING_STATUS';
    const selectedWorkingStatus = workingStatusType
      ? workingStatusDefinition(this.propertyForm.workingStatusFlow, this.propertyForm.workingStatusCode)
      : undefined;

    if (workingStatusType && !selectedWorkingStatus) {
      this.messageService.add({
        severity: 'warn',
        summary: 'System status required',
        detail: 'Choose the inbound or outbound system status for this working-status zone.'
      });
      return;
    }

    this.drawer.selectedAisle.updateProperties({
      zone: this.propertyForm.zone.trim() || this.drawer.selectedAisle.zone,
      type: this.propertyForm.type,
      direction: this.propertyForm.direction,
      workingStatusFlow: workingStatusType ? selectedWorkingStatus?.flow ?? null : null,
      workingStatusCode: workingStatusType ? selectedWorkingStatus?.code ?? null : null,
      workingStatusLabel: workingStatusType ? selectedWorkingStatus?.label ?? null : null,
      workingStatusColor: workingStatusType ? selectedWorkingStatus?.color ?? null : null,
      levels,
      tunnelLevelFrom: tunnelFrom,
      tunnelLevelTo: tunnelTo,
      lowerShelfLevels,
      bayWidth: clampFloat(this.propertyForm.bayWidth, 0.5, 5, this.drawer.selectedAisle.bayWidth),
      bayDepth: clampFloat(this.propertyForm.bayDepth, 0.5, 3, this.drawer.selectedAisle.bayDepth),
      aisleWidth: clampFloat(this.propertyForm.aisleWidth, 0, 6, this.drawer.selectedAisle.aisleWidth),
      pickFaceLevels,
      startPointX: clampFloat(this.propertyForm.startPointX, 0, 9999, this.drawer.selectedAisle.x),
      startPointY: clampFloat(this.propertyForm.startPointY, 0, 9999, this.drawer.selectedAisle.y)
    });
    this.drawer.selectedAisle.x = Math.max(0, Math.min(this.drawer.warehouseWidth - this.drawer.selectedAisle.width, this.drawer.selectedAisle.x));
    this.drawer.selectedAisle.y = Math.max(0, Math.min(this.drawer.warehouseHeight - this.drawer.selectedAisle.height, this.drawer.selectedAisle.y));
    this.drawer.selectedAisle.generateLocations();
    this.drawer.applyConstraintFiltering();
    this.drawer.saveState();
    this.drawer.render();
    this.captureDrawerState();
    this.messageService.add({
      severity: 'success',
      summary: 'Properties updated',
      detail: `${this.aisleLabel(this.drawer.selectedAisle ? {
        zone: this.drawer.selectedAisle.zone,
        aisleCode: this.drawer.selectedAisle.aisleCode ?? null
      } : null)} was updated.`
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
      detail: `${this.aisleLabel({
        zone: aisle.zone,
        aisleCode: aisle.aisleCode ?? null
      })} is ready for editing.`
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
    if (this.isSaving) {
      return;
    }

    this.captureDrawerState();
    if (!this.profileName.trim()) {
      this.setSaveStatus('error', 'Profile name required');
      this.messageService.add({
        severity: 'warn',
        summary: 'Profile name required',
        detail: 'Enter a profile name before saving.'
      });
      return;
    }

    const payload = this.snapshotLayoutData();
    const normalized = normalizeLayout(payload);
    this.isSaving = true;
    this.setSaveStatus('saving', 'Saving layout...');

    this.service.saveProfile({
      id: this.selectedProfileId,
      profileName: this.profileName.trim(),
      description: this.description.trim(),
      warehouseLength: normalized.warehouseHeight,
      warehouseWidth: normalized.warehouseWidth,
      layoutData: payload
    }).pipe(
      finalize(() => {
        this.isSaving = false;
      })
    ).subscribe({
      next: profile => {
        this.selectedProfileId = profile.id;
        this.state.patchSelectedProfile(profile);
        this.state.clearDesignDraft();
        this.state.refreshProfiles();
        this.setSaveStatus('success', this.savedMessage(profile.updatedAt));
        this.messageService.add({
          severity: 'success',
          summary: 'Layout saved',
          detail: `${profile.profileName} is now available in Warehouse Optimize.`
        });
      },
      error: error => {
        this.setSaveStatus('error', this.saveErrorMessage(error));
        this.messageService.add({
          severity: 'error',
          summary: 'Save failed',
          detail: this.saveErrorMessage(error)
        });
      }
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
      const previousSuppression = this.suppressDraftPersistence;
      this.suppressDraftPersistence = !this.pendingLayoutPersistsDraft;
      try {
        this.drawer.fromJSON(this.pendingLayoutData);
        this.captureDrawerState();
      } finally {
        this.suppressDraftPersistence = previousSuppression;
      }
      this.updateMinimap();
    } catch (error) {
      this.messageService.add({
        severity: 'error',
        summary: 'Designer failed to load',
        detail: error instanceof Error ? error.message : 'Could not initialize the warehouse designer.'
      });
    }
  }

  private loadLayoutData(layoutData: WarehouseDesignerLayout, resetSelection: boolean, persistDraft = true): void {
    const normalized = normalizeLayout(layoutData);
    const nextLayout: WarehouseDesignerLayout = {
      ...layoutData,
      warehouseWidth: normalized.warehouseWidth,
      warehouseHeight: normalized.warehouseHeight,
      boundaryPolygon: normalized.boundaryPolygon ?? null,
      aisles: normalized.aisles
    };

    this.pendingLayoutData = nextLayout;
    this.pendingLayoutPersistsDraft = persistDraft;
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

    const previousSuppression = this.suppressDraftPersistence;
    this.suppressDraftPersistence = !persistDraft;
    try {
      this.persistDraftState();

      if (this.drawer) {
        this.drawer.fromJSON(nextLayout);
        this.applyDrawerVisualState();
        this.drawer.zoomToFit();
        this.captureDrawerState();
        this.updateMinimap();
      }
    } finally {
      this.suppressDraftPersistence = previousSuppression;
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
    this.persistDraftState();
  }

  private persistDraftState(): void {
    if (this.suppressDraftPersistence) {
      return;
    }
    this.state.setDesignDraft({
      profileId: this.selectedProfileId,
      profileName: this.profileName.trim() || 'Main Shared Layout',
      description: this.description.trim(),
      warehouseWidth: this.layout.warehouseWidth,
      warehouseHeight: this.layout.warehouseHeight,
      layout: this.pendingLayoutData
    });
    if (!this.isSaving) {
      this.setSaveStatus('dirty', 'Unsaved changes');
    }
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
    const selectedWorkingStatus =
      workingStatusDefinition(aisle.workingStatusFlow, aisle.workingStatusCode)
      ?? workingStatusOptionsForFlow(aisle.workingStatusFlow ?? 'OUTBOUND')[0]
      ?? workingStatusOptionsForFlow('OUTBOUND')[0];
    this.propertyForm = {
      zone: aisle.zone,
      type: aisle.type,
      direction: aisle.direction ?? 'AUTO',
      workingStatusFlow: (aisle.workingStatusFlow ?? selectedWorkingStatus?.flow ?? 'OUTBOUND').toUpperCase(),
      workingStatusCode: aisle.workingStatusCode ?? selectedWorkingStatus?.code ?? '',
      workingStatusLabel: aisle.workingStatusLabel ?? selectedWorkingStatus?.label ?? '',
      workingStatusColor: aisle.workingStatusColor ?? selectedWorkingStatus?.color ?? '#0EA5E9',
      levels: aisle.levels,
      tunnelLevelFrom: aisle.tunnelLevelFrom ?? 1,
      tunnelLevelTo: aisle.tunnelLevelTo ?? aisle.levels,
      lowerShelfLevels: clampWhole(aisle.lowerShelfLevels ?? 0, 0, Math.max(aisle.levels - 1, 0), 0),
      bayWidth: aisle.bayWidth,
      bayDepth: aisle.bayDepth,
      aisleWidth: aisle.aisleWidth,
      pickFaceLevels: [...(aisle.pickFaceLevels ?? [1])],
      startPointX: aisle.startPointX ?? aisle.x,
      startPointY: aisle.startPointY ?? aisle.y
    };
    if (this.propertyForm.type === 'WORKING_STATUS') {
      this.updateWorkingStatusCode(this.propertyForm.workingStatusCode);
    }
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
      workingStatusFlow: 'OUTBOUND',
      workingStatusCode: '300',
      workingStatusLabel: 'Allocated',
      workingStatusColor: '#1A005D',
      levels: 4,
      tunnelLevelFrom: 1,
      tunnelLevelTo: 4,
      lowerShelfLevels: 0,
      bayWidth: 1.2,
      bayDepth: 1,
      aisleWidth: 2.5,
      pickFaceLevels: [1],
      startPointX: 0,
      startPointY: 0
    };
  }

  private createDefaultHierarchyForm(): HierarchyFormModel {
    return {
      zone: 'AREA_CODE',
      aisle: 'RACK_CODE',
      bay: 'POSITION_CODE',
      slot: '',
      level: 'LEVEL_CODE'
    };
  }

  private applyHierarchyFormFromResponse(): void {
    const mapping = this.selectedHierarchySetting?.mapping;
    if (!mapping) {
      this.hierarchyForm = this.createDefaultHierarchyForm();
      return;
    }
    this.hierarchyForm = {
      zone: mapping.zone ?? '',
      aisle: mapping.aisle ?? '',
      bay: mapping.bay,
      slot: mapping.slot ?? '',
      level: mapping.level
    };
  }

  private locationHierarchyPayload(): WarehouseLocationHierarchyMapping {
    return {
      zone: this.hierarchyForm.zone || null,
      aisle: this.hierarchyForm.aisle || null,
      bay: this.hierarchyForm.bay,
      slot: this.hierarchyForm.slot || null,
      level: this.hierarchyForm.level
    };
  }

  private setHierarchyStatus(state: SaveState, text: string): void {
    this.hierarchyStatusState = state;
    this.hierarchyStatusText = text;
  }

  private hierarchySourceLabel(value: WarehouseLocationHierarchySource | '' | null | undefined): string {
    const option = this.hierarchySourceOptions.find(item => item.value === value);
    return option?.label ?? 'Not used';
  }

  private buildGeneratorZonePreview(): string {
    const config: LayoutGeneratorConfig = {
      ...this.generator,
      warehouseWidth: clampWhole(this.warehouseWidth, 10, 500, 130),
      warehouseHeight: clampWhole(this.warehouseHeight, 10, 500, 70),
      aisleCount: Math.max(1, this.generator.aisleCount)
    };
    const layout = createGeneratedLayout(config);
    const first = layout.aisles[0]?.zone ?? '-';
    const last = layout.aisles[layout.aisles.length - 1]?.zone ?? first;
    return `${first} → ${last}`;
  }

  private buildGeneratorLocationPreview(): string {
    const config: LayoutGeneratorConfig = {
      ...this.generator,
      warehouseWidth: clampWhole(this.warehouseWidth, 10, 500, 130),
      warehouseHeight: clampWhole(this.warehouseHeight, 10, 500, 70),
      aisleCount: Math.max(1, this.generator.aisleCount),
      baysPerAisle: Math.max(1, this.generator.baysPerAisle)
    };
    const layout = createGeneratedLayout(config);
    const first = layout.aisles[0]?.locations[0]?.location ?? '-';
    const lastAisle = layout.aisles[layout.aisles.length - 1];
    const last = lastAisle?.locations[lastAisle.locations.length - 1]?.location ?? first;
    return `${first} → ${last}`;
  }

  private defaultGenerator(): LayoutGeneratorConfig {
    return {
      warehouseWidth: 130,
      warehouseHeight: 70,
      aisleCount: 4,
      baysPerAisle: 18,
      levels: 4,
      zonePrefix: 'Zone',
      zoneStartNumber: 1,
      zoneNamingPattern: 'prefix-row',
      zoneSuffix: '',
      customZonePattern: '{prefix}-{row}',
      locationNamingPattern: 'zone-row-bay-side-level',
      customLocationPattern: '{zone}-{row}-{bay}{side}-{level}',
      rowFormat: 'numeric',
      rowPadding: 2,
      bayPadding: 3,
      levelPrefix: 'L',
      leftBayNumbering: 'odd',
      rightBayNumbering: 'even',
      rowNumberStart: 1,
      bayNumberStart: 1,
      levelNumberStart: 1,
      rackType: 'HIGH_RACK',
      bayWidth: 1.2,
      bayDepth: 1,
      aisleWidth: 2.5,
      startX: 10,
      startY: 10
    };
  }

  private setSaveStatus(state: SaveState, text: string): void {
    this.saveState = state;
    this.saveStatusText = text;
  }

  private hasDesignContent(): boolean {
    return this.drawer?.hasDesignContent() ?? this.layout.aisles.length > 0;
  }

  private resizeLayoutPreservingContent(
    layout: WarehouseDesignerLayout,
    width: number,
    height: number
  ): {
    layout: WarehouseDesignerLayout;
    summary: { movedAisles: number; resizedAisles: number; adjustedBoundaryPoints: number };
  } {
    const normalized = normalizeLayout(layout);
    let movedAisles = 0;
    let resizedAisles = 0;
    let adjustedBoundaryPoints = 0;

    const aisles = normalized.aisles.map(aisle => {
      let changed = false;
      let resized = false;
      const nextAisle: WarehouseAisle = {
        ...aisle,
        locations: aisle.locations.map(location => ({ ...location })),
        baseLocations: aisle.baseLocations?.map(location => ({ ...location })) ?? null,
        lowerShelfLevels: aisle.lowerShelfLevels ?? 0,
        pickFaceLevels: aisle.pickFaceLevels ? [...aisle.pickFaceLevels] : null
      };

      if (nextAisle.width > width) {
        nextAisle.width = width;
        resized = true;
        changed = true;
      }

      if (nextAisle.height > height) {
        nextAisle.height = height;
        resized = true;
        changed = true;
      }

      const maxX = Math.max(0, width - nextAisle.width);
      const maxY = Math.max(0, height - nextAisle.height);
      const clampedX = Math.max(0, Math.min(maxX, nextAisle.x));
      const clampedY = Math.max(0, Math.min(maxY, nextAisle.y));

      if (clampedX !== nextAisle.x || clampedY !== nextAisle.y) {
        nextAisle.x = clampedX;
        nextAisle.y = clampedY;
        movedAisles += 1;
        changed = true;
      }

      if (resized) {
        resizedAisles += 1;
      }

      if (changed) {
        nextAisle.locations = nextAisle.locations.map(location => ({
          ...location,
          x: location.x === null ? null : Math.max(0, Math.min(width, nextAisle.x + ((location.x ?? nextAisle.x) - aisle.x))),
          y: location.y === null ? null : Math.max(0, Math.min(height, nextAisle.y + ((location.y ?? nextAisle.y) - aisle.y)))
        }));
      }

      return nextAisle;
    });

    const boundaryPolygon = normalized.boundaryPolygon?.map(point => {
      const clampedPoint = {
        x: Math.max(0, Math.min(width, point.x)),
        y: Math.max(0, Math.min(height, point.y))
      };
      if (clampedPoint.x !== point.x || clampedPoint.y !== point.y) {
        adjustedBoundaryPoints += 1;
      }
      return clampedPoint;
    }) ?? null;

    return {
      layout: {
        ...normalized,
        warehouseWidth: width,
        warehouseHeight: height,
        boundaryPolygon,
        aisles
      },
      summary: {
        movedAisles,
        resizedAisles,
        adjustedBoundaryPoints
      }
    };
  }

  private preservedResizeSummaryText(
    width: number,
    height: number,
    result?: { movedAisles?: number; resizedAisles?: number; adjustedBoundaryPoints?: number }
  ): string {
    const messages = [`Kept the current layout and resized the warehouse to ${width} x ${height} m.`];
    if ((result?.movedAisles ?? 0) > 0) {
      messages.push(`${result?.movedAisles} zone${result?.movedAisles === 1 ? '' : 's'} were moved inside the new bounds.`);
    }
    if ((result?.resizedAisles ?? 0) > 0) {
      messages.push(`${result?.resizedAisles} zone${result?.resizedAisles === 1 ? '' : 's'} were reduced to fit.`);
    }
    if ((result?.adjustedBoundaryPoints ?? 0) > 0) {
      messages.push(`${result?.adjustedBoundaryPoints} boundary point${result?.adjustedBoundaryPoints === 1 ? '' : 's'} were clamped to the new size.`);
    }
    return messages.join(' ');
  }

  private generatedLayoutSummaryText(response: {
    summary: {
      areas: number;
      racks: number;
      locations: number;
      inferredPositions: number;
      inferredLevels: number;
      skippedRows: number;
      warehouseWidth?: number | null;
      warehouseHeight?: number | null;
      mixedAreasDetected?: number;
    };
  }): string {
    const { summary } = response;
    let detail = `${summary.areas} areas, ${summary.racks} racks, and ${summary.locations} locations loaded into the draft.`;
    if (summary.warehouseWidth && summary.warehouseHeight) {
      detail += ` Final footprint: ${this.formatDimension(summary.warehouseWidth)}m x ${this.formatDimension(summary.warehouseHeight)}m.`;
    }
    if ((summary.mixedAreasDetected ?? 0) > 0) {
      detail += ` ${summary.mixedAreasDetected} mixed area${summary.mixedAreasDetected === 1 ? '' : 's'} detected.`;
    }
    if (summary.inferredPositions > 0 || summary.inferredLevels > 0 || summary.skippedRows > 0) {
      detail += ` ${summary.inferredPositions} positions inferred, ${summary.inferredLevels} levels inferred, ${summary.skippedRows} rows skipped.`;
    }
    return detail;
  }

  private generatedLayoutNormalizationText(summary: {
    normalizedDimensionRows?: number;
    normalizedHeightRows?: number;
  }): string {
    const messages: string[] = [];
    if ((summary.normalizedDimensionRows ?? 0) > 0) {
      messages.push(`${summary.normalizedDimensionRows} row${summary.normalizedDimensionRows === 1 ? '' : 's'} used normalized LOC_LEN or LOC_WID values.`);
    }
    if ((summary.normalizedHeightRows ?? 0) > 0) {
      messages.push(`${summary.normalizedHeightRows} row${summary.normalizedHeightRows === 1 ? '' : 's'} used normalized LOC_HIG values for mixed-level detection.`);
    }
    return messages.join(' ');
  }

  private savedMessage(updatedAt: string | null | undefined): string {
    if (!updatedAt) {
      return 'Saved';
    }

    const parsed = new Date(updatedAt);
    if (Number.isNaN(parsed.getTime())) {
      return 'Saved';
    }

    return `Saved ${parsed.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false
    })}`;
  }

  private saveErrorMessage(error: unknown): string {
    const apiMessages = (error as { error?: { messages?: Array<{ message?: string }> } })?.error?.messages;
    const apiMessage = apiMessages?.find(message => !!message?.message)?.message;
    if (apiMessage) {
      return apiMessage;
    }

    const directMessage = (error as { error?: { message?: string } })?.error?.message;
    if (directMessage) {
      return directMessage;
    }

    return 'Could not save this layout. Please try again.';
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
    case 'WORKING_STATUS':
      return 'rgba(14, 165, 233, 0.7)';
    case 'OBSTACLE':
    case 'PILLAR':
      return 'rgba(100, 116, 139, 0.75)';
    case 'DOCK_DOOR':
      return 'rgba(14, 165, 233, 0.65)';
    default:
      return 'rgba(148, 163, 184, 0.55)';
  }
}


