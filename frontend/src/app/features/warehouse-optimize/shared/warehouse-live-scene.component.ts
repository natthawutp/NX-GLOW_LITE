import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
  ViewEncapsulation,
  ViewChild
} from '@angular/core';
import type { Group, Mesh, MeshStandardMaterial, PerspectiveCamera, Raycaster, Scene, Vector2, WebGLRenderer } from 'three';
import {
  LiveLocationState,
  SlottingAssignment,
  WarehouseLayout,
  WarehouseLayoutLocation,
  WorkingStatusZoneSnapshot
} from './warehouse-optimize.models';
import {
  assignmentByLocation,
  extractLocations,
  liveStateByLocation
} from './warehouse-optimize-layout.utils';

type OrbitControlsType = typeof import('three/examples/jsm/controls/OrbitControls.js').OrbitControls;
type ThreeModule = typeof import('three');

@Component({
  selector: 'wms-warehouse-live-scene',
  standalone: true,
  imports: [CommonModule],
  encapsulation: ViewEncapsulation.None,
  template: `
    <div class="scene-shell">
      <div #host class="scene-host"></div>
      <div #statusLayer class="working-status-layer"></div>

      <div class="scene-legend">
        <span class="legend-chip"><i class="legend-swatch occupied"></i>Occupied</span>
        <span class="legend-chip"><i class="legend-swatch empty"></i>Empty</span>
        <span class="legend-chip"><i class="legend-swatch highlight"></i>Search result</span>
        <span class="legend-chip"><i class="legend-swatch blinking"></i>Recent change</span>
      </div>

      <div
        *ngIf="hoverLocation"
        class="scene-tooltip"
        [style.left.px]="tooltipLeft"
        [style.top.px]="tooltipTop">
        <div class="scene-tooltip-title">{{ hoverLocation.location }}</div>
        <div class="scene-tooltip-grid">
          <span>Status</span>
          <strong>{{ hoverStatusLabel }}</strong>
          <span>Product</span>
          <strong>{{ hoverLiveState?.productCode || hoverAssignment?.productSku || '-' }}</strong>
          <span>Category</span>
          <strong>{{ hoverLiveState?.productCategory || hoverAssignment?.productCategory || '-' }}</strong>
          <span>Velocity</span>
          <strong>{{ hoverLiveState?.velocityClass || hoverAssignment?.velocityClass || '-' }}</strong>
          <span>Physical Qty</span>
          <strong>{{ formatQuantity(hoverLiveState?.physicalQty) }}</strong>
          <span>Available Qty</span>
          <strong>{{ formatQuantity(hoverLiveState?.availableQty) }}</strong>
          <span>Zone</span>
          <strong>{{ hoverLocation.zone || '-' }}</strong>
          <span>Level</span>
          <strong>{{ hoverLocation.level ?? '-' }}</strong>
          <span>Updated</span>
          <strong>{{ hoverLiveState?.updatedAt || '-' }}</strong>
        </div>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
      height: 100%;
      min-height: 420px;
      border-radius: 8px;
      overflow: hidden;
      border: 1px solid #dbe2ea;
      background: #e2e8f0;
    }

    .scene-host {
      width: 100%;
      height: 100%;
      min-height: 420px;
      position: relative;
      overflow: hidden;
    }

    .scene-shell {
      width: 100%;
      height: 100%;
      min-height: 420px;
      position: relative;
      overflow: hidden;
    }

    .scene-legend {
      position: absolute;
      top: 12px;
      left: 12px;
      z-index: 5;
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      pointer-events: none;
    }

    .legend-chip {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 4px 10px;
      border: 1px solid rgba(148, 163, 184, 0.32);
      border-radius: 999px;
      background: rgba(255, 255, 255, 0.92);
      color: #334155;
      font-size: 12px;
      font-weight: 600;
      backdrop-filter: blur(8px);
    }

    .legend-swatch {
      width: 10px;
      height: 10px;
      border-radius: 999px;
      display: inline-block;
    }

    .legend-swatch.occupied { background: #22c55e; }
    .legend-swatch.empty { background: #cbd5e1; }
    .legend-swatch.highlight { background: #facc15; }
    .legend-swatch.blinking { background: #f59e0b; }

    .working-status-layer {
      position: absolute;
      inset: 0;
      z-index: 4;
      pointer-events: none;
    }

    .working-status-badge {
      position: absolute;
      min-width: 148px;
      max-width: 208px;
      padding: 10px 12px 12px;
      border-radius: 12px;
      border: 1px solid color-mix(in srgb, var(--status-color, #0ea5e9) 58%, #ffffff);
      background: linear-gradient(
        180deg,
        color-mix(in srgb, var(--status-color, #0ea5e9) 20%, #ffffff) 0%,
        rgba(255, 255, 255, 0.97) 100%
      );
      box-shadow: 0 16px 30px rgba(15, 23, 42, 0.2);
      color: #0f172a;
      transform: translate(-50%, calc(-100% - 34px));
      backdrop-filter: blur(10px);
    }

    .working-status-badge::before {
      content: '';
      position: absolute;
      left: 50%;
      bottom: -24px;
      width: 4px;
      height: 24px;
      border-radius: 999px;
      background: linear-gradient(
        180deg,
        color-mix(in srgb, var(--status-color, #0ea5e9) 88%, #ffffff) 0%,
        color-mix(in srgb, var(--status-color, #0ea5e9) 58%, #0f172a) 100%
      );
      transform: translateX(-50%);
    }

    .working-status-badge::after {
      content: '';
      position: absolute;
      left: 50%;
      bottom: -34px;
      width: 14px;
      height: 14px;
      border-radius: 999px;
      border: 3px solid rgba(255, 255, 255, 0.95);
      background: color-mix(in srgb, var(--status-color, #0ea5e9) 90%, #0f172a);
      box-shadow: 0 6px 16px rgba(15, 23, 42, 0.25);
      transform: translateX(-50%);
    }

    .working-status-badge-zone {
      display: block;
      margin-bottom: 4px;
      font-size: 11px;
      font-weight: 700;
      color: #334155;
      text-transform: uppercase;
      word-break: break-word;
    }

    .working-status-badge-progress {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      min-width: 72px;
      margin-bottom: 6px;
      padding: 4px 10px;
      border-radius: 999px;
      background: color-mix(in srgb, var(--status-color, #0ea5e9) 18%, #ffffff);
      color: color-mix(in srgb, var(--status-color, #0ea5e9) 72%, #0f172a);
      font-size: 16px;
      line-height: 1.1;
      font-weight: 800;
      box-shadow: inset 0 0 0 1px color-mix(in srgb, var(--status-color, #0ea5e9) 26%, #ffffff);
    }

    .working-status-badge-status {
      display: block;
      margin-bottom: 8px;
      font-size: 12px;
      font-weight: 700;
      color: #0f172a;
      word-break: break-word;
    }

    .working-status-badge-qty {
      display: block;
      font-size: 11px;
      color: #334155;
      word-break: break-word;
    }

    .scene-tooltip {
      position: absolute;
      z-index: 6;
      width: min(260px, calc(100% - 24px));
      padding: 12px;
      border-radius: 8px;
      border: 1px solid rgba(148, 163, 184, 0.36);
      background: rgba(255, 255, 255, 0.97);
      box-shadow: 0 18px 36px rgba(15, 23, 42, 0.18);
      color: #0f172a;
      pointer-events: none;
      backdrop-filter: blur(10px);
    }

    .scene-tooltip-title {
      margin-bottom: 10px;
      font-size: 13px;
      font-weight: 700;
      color: #0f172a;
      word-break: break-word;
    }

    .scene-tooltip-grid {
      display: grid;
      grid-template-columns: minmax(72px, auto) minmax(0, 1fr);
      gap: 6px 10px;
      font-size: 12px;
      line-height: 1.4;
    }

    .scene-tooltip-grid span {
      color: #64748b;
    }

    .scene-tooltip-grid strong {
      margin: 0;
      color: #0f172a;
      font-weight: 600;
      word-break: break-word;
    }
  `]
})
export class WarehouseLiveSceneComponent implements AfterViewInit, OnChanges, OnDestroy {
  @ViewChild('host', { static: true }) private readonly hostRef?: ElementRef<HTMLDivElement>;
  @ViewChild('statusLayer', { static: true }) private readonly statusLayerRef?: ElementRef<HTMLDivElement>;

  @Input() layout: WarehouseLayout | null = null;
  @Input() assignments: SlottingAssignment[] = [];
  @Input() liveStates: LiveLocationState[] = [];
  @Input() workingStatusZones: WorkingStatusZoneSnapshot[] = [];
  @Input() highlightLocations: string[] = [];

  private three?: ThreeModule;
  private camera?: PerspectiveCamera;
  private scene?: Scene;
  private renderer?: WebGLRenderer;
  private controls?: InstanceType<OrbitControlsType>;
  private layoutGroup?: Group;
  private floorGroup?: Group;
  private statusZoneGroup?: Group;
  private raycaster?: Raycaster;
  private pointer?: Vector2;
  private animationId: number | null = null;
  private resizeObserver?: ResizeObserver;
  private readonly meshMap = new Map<string, Mesh>();
  private readonly locationMap = new Map<string, WarehouseLayoutLocation>();
  private readonly blinkUntil = new Map<string, number>();
  private readonly workingStatusBadgeElements = new Map<string, HTMLDivElement>();
  private highlightLocationSet = new Set<string>();
  private readonly resizeHandler = () => this.resize();
  private readonly pointerMoveHandler = (event: PointerEvent) => this.handlePointerMove(event);
  private readonly pointerLeaveHandler = () => this.clearHover();
  private readonly doubleClickHandler = (event: MouseEvent) => this.handleDoubleClick(event);
  hoverLocation: WarehouseLayoutLocation | null = null;
  hoverLiveState: LiveLocationState | null = null;
  hoverAssignment: SlottingAssignment | null = null;
  tooltipLeft = 16;
  tooltipTop = 16;

  get hoverStatusLabel(): string {
    return this.stockStatusLabel(this.hoverLiveState);
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ensureScene();
    this.rebuildScene();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['highlightLocations']) {
      this.highlightLocationSet = new Set(this.highlightLocations ?? []);
    }
    if (changes['workingStatusZones']) {
      this.rebuildWorkingStatusBadges();
    }
    if (!this.hostRef || !this.scene) {
      return;
    }
    if (changes['layout']) {
      this.rebuildScene();
      return;
    }
    if (changes['assignments'] || changes['liveStates'] || changes['highlightLocations']) {
      this.applyState();
    }
    if (changes['workingStatusZones']) {
      this.updateWorkingStatusBadges();
    }
  }

  ngOnDestroy(): void {
    if (this.animationId !== null) {
      cancelAnimationFrame(this.animationId);
    }
    window.removeEventListener('resize', this.resizeHandler);
    this.resizeObserver?.disconnect();
    this.controls?.dispose();
    this.renderer?.domElement.removeEventListener('pointermove', this.pointerMoveHandler);
    this.renderer?.domElement.removeEventListener('pointerleave', this.pointerLeaveHandler);
    this.renderer?.domElement.removeEventListener('dblclick', this.doubleClickHandler);
    this.renderer?.dispose();
    this.clearWorkingStatusBadges();
    this.meshMap.clear();
    this.locationMap.clear();
    this.blinkUntil.clear();
  }

  private async ensureScene(): Promise<void> {
    if (this.scene || !this.hostRef) {
      return;
    }

    const THREE = await import('three');
    const controlsModule = await import('three/examples/jsm/controls/OrbitControls.js');
    this.three = THREE;

    const host = this.hostRef.nativeElement;
    const width = Math.max(host.clientWidth, 320);
    const height = Math.max(host.clientHeight, 420);

    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color('#e2e8f0');

    this.camera = new THREE.PerspectiveCamera(52, width / height, 0.1, 2000);
    this.camera.position.set(36, 42, 54);

    this.renderer = new THREE.WebGLRenderer({ antialias: true });
    this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    this.renderer.setSize(width, height);
    this.renderer.shadowMap.enabled = true;
    host.innerHTML = '';
    host.appendChild(this.renderer.domElement);
    this.renderer.domElement.addEventListener('pointermove', this.pointerMoveHandler);
    this.renderer.domElement.addEventListener('pointerleave', this.pointerLeaveHandler);
    this.renderer.domElement.addEventListener('dblclick', this.doubleClickHandler);

    const OrbitControls = controlsModule.OrbitControls;
    this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    this.controls.enableDamping = true;
    this.controls.minDistance = 0.8;
    this.controls.zoomSpeed = 1.35;
    this.controls.panSpeed = 1.15;
    this.controls.maxPolarAngle = Math.PI / 2.05;
    this.controls.target.set(24, 0, 24);
    this.renderer.domElement.style.cursor = 'grab';

    const ambientLight = new THREE.AmbientLight(0xffffff, 1.3);
    this.scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.2);
    directionalLight.position.set(30, 50, 20);
    directionalLight.castShadow = true;
    directionalLight.shadow.mapSize.width = 2048;
    directionalLight.shadow.mapSize.height = 2048;
    this.scene.add(directionalLight);

    const hemiLight = new THREE.HemisphereLight(0xffffff, 0x94a3b8, 0.75);
    this.scene.add(hemiLight);

    this.floorGroup = new THREE.Group();
    this.scene.add(this.floorGroup);

    this.statusZoneGroup = new THREE.Group();
    this.scene.add(this.statusZoneGroup);

    this.layoutGroup = new THREE.Group();
    this.scene.add(this.layoutGroup);
    this.raycaster = new THREE.Raycaster();
    this.pointer = new THREE.Vector2();

    window.addEventListener('resize', this.resizeHandler);
    this.observeHostSize();
    this.animate();
  }

  private rebuildScene(): void {
    if (!this.three || !this.layoutGroup || !this.floorGroup || !this.statusZoneGroup || !this.layout) {
      return;
    }

    const THREE = this.three;
    this.meshMap.clear();
    this.locationMap.clear();
    this.blinkUntil.clear();
    this.clearHover();
    this.clearWorkingStatusBadges();

    while (this.layoutGroup.children.length) {
      const child = this.layoutGroup.children[0];
      this.layoutGroup.remove(child);
    }
    while (this.floorGroup.children.length) {
      const child = this.floorGroup.children[0];
      this.floorGroup.remove(child);
    }
    while (this.statusZoneGroup.children.length) {
      const child = this.statusZoneGroup.children[0];
      this.statusZoneGroup.remove(child);
    }

    const planeGeometry = new THREE.PlaneGeometry(this.layout.warehouseWidth + 10, this.layout.warehouseHeight + 10);
    const planeMaterial = new THREE.MeshStandardMaterial({ color: '#f8fafc', roughness: 0.95, metalness: 0.05 });
    const floor = new THREE.Mesh(planeGeometry, planeMaterial);
    floor.rotation.x = -Math.PI / 2;
    floor.receiveShadow = true;
    floor.position.set(this.layout.warehouseWidth / 2, 0, this.layout.warehouseHeight / 2);
    this.floorGroup.add(floor);

    const grid = new THREE.GridHelper(
      Math.max(this.layout.warehouseWidth, this.layout.warehouseHeight) + 10,
      40,
      0x94a3b8,
      0xcbd5e1
    );
    grid.position.set(this.layout.warehouseWidth / 2, 0.01, this.layout.warehouseHeight / 2);
    this.floorGroup.add(grid);

    const boundaryGeometry = new THREE.BufferGeometry().setFromPoints([
      new THREE.Vector3(0, 0.04, 0),
      new THREE.Vector3(this.layout.warehouseWidth, 0.04, 0),
      new THREE.Vector3(this.layout.warehouseWidth, 0.04, this.layout.warehouseHeight),
      new THREE.Vector3(0, 0.04, this.layout.warehouseHeight),
      new THREE.Vector3(0, 0.04, 0)
    ]);
    const boundary = new THREE.Line(boundaryGeometry, new THREE.LineBasicMaterial({ color: '#334155' }));
    this.floorGroup.add(boundary);

    for (const aisle of this.layout.aisles.filter(item => item.type === 'WORKING_STATUS')) {
      const zoneColor = String(aisle.workingStatusColor || '#0ea5e9');
      const geometry = new THREE.BoxGeometry(aisle.width, 0.12, aisle.height);
      const material = new THREE.MeshStandardMaterial({
        color: zoneColor,
        roughness: 0.68,
        metalness: 0.04,
        transparent: true,
        opacity: 0.28
      });
      const mesh = new THREE.Mesh(geometry, material);
      mesh.position.set(
        aisle.x + (aisle.width / 2),
        0.06,
        this.layout.warehouseHeight - aisle.y - (aisle.height / 2)
      );
      mesh.receiveShadow = true;
      this.statusZoneGroup.add(mesh);
    }

    const locations = extractLocations(this.layout);
    for (const location of locations) {
      this.locationMap.set(location.location, location);
      const material = new THREE.MeshStandardMaterial({
        color: this.meshColor(location, liveStateByLocation(this.liveStates).get(location.location)),
        roughness: 0.45,
        metalness: 0.18
      });
      const geometry = new THREE.BoxGeometry(this.meshWidth(location), this.meshHeight(location), this.meshDepth(location));
      const mesh = new THREE.Mesh(geometry, material);
      mesh.castShadow = true;
      mesh.receiveShadow = true;
      mesh.position.set(
        (location.x ?? 0) + (this.meshWidth(location) / 2),
        this.yLevel(location),
        this.sceneZ(location)
      );
      mesh.userData = { location: location.location };
      this.layoutGroup.add(mesh);
      this.meshMap.set(location.location, mesh);
    }

    if (this.controls) {
      this.updateControlLimits();
      this.controls.target.set(this.layout.warehouseWidth / 2, 1, this.layout.warehouseHeight / 2);
      this.controls.update();
    }

    this.applyState();
    this.rebuildWorkingStatusBadges();
    this.updateWorkingStatusBadges();
    this.resize();
  }

  private applyState(): void {
    if (!this.three) {
      return;
    }
    const assignments = assignmentByLocation(this.assignments);
    const states = liveStateByLocation(this.liveStates);
    const now = performance.now();

    for (const [locationCode, mesh] of this.meshMap.entries()) {
      const state = states.get(locationCode);
      const assignment = assignments.get(locationCode);
      const location = this.findLocation(locationCode);
      const material = mesh.material as MeshStandardMaterial;
      const highlighted = this.highlightLocationSet.has(locationCode);
      if (!location) {
        continue;
      }
      material.color.set(this.meshColor(location, state));
      material.opacity = highlighted ? 1 : state ? (this.hasStock(state) ? 1 : 0.82) : assignment ? 0.86 : 0.72;
      material.transparent = material.opacity < 1;
      material.emissive.set(highlighted ? '#f59e0b' : state?.blink ? '#ffffff' : '#0f172a');
      material.emissiveIntensity = highlighted ? 0.58 : state?.blink ? 0.35 : 0.04;
      const baseScale = highlighted ? 1.08 : 1;
      mesh.scale.set(baseScale, baseScale, baseScale);
      if (state?.blink) {
        this.blinkUntil.set(locationCode, now + 15000);
      }
    }

    if (this.hoverLocation) {
      const locationCode = this.hoverLocation.location;
      this.hoverLiveState = states.get(locationCode) ?? null;
      this.hoverAssignment = assignments.get(locationCode) ?? null;
    }
  }

  private findLocation(locationCode: string): WarehouseLayoutLocation | undefined {
    return this.locationMap.get(locationCode);
  }

  private animate(): void {
    this.animationId = requestAnimationFrame(() => this.animate());
    if (!this.renderer || !this.scene || !this.camera) {
      return;
    }

    this.controls?.update();
    this.updateBlinkAnimation();
    this.updateWorkingStatusBadges();
    this.renderer.render(this.scene, this.camera);
  }

  private updateBlinkAnimation(): void {
    const now = performance.now();
    for (const [locationCode, mesh] of this.meshMap.entries()) {
      const material = mesh.material as MeshStandardMaterial;
      const expiresAt = this.blinkUntil.get(locationCode);
      const highlighted = this.highlightLocationSet.has(locationCode);
      if (!expiresAt || now >= expiresAt) {
        this.blinkUntil.delete(locationCode);
        const baseScale = highlighted ? 1.08 : 1;
        mesh.scale.set(baseScale, baseScale, baseScale);
        material.emissive.set(highlighted ? '#f59e0b' : '#0f172a');
        material.emissiveIntensity = highlighted ? 0.58 : Math.max(0.04, material.emissiveIntensity * 0.85);
        continue;
      }

      const intensity = 0.4 + Math.abs(Math.sin(now / 180)) * 0.55;
      const pulse = (highlighted ? 1.08 : 1) + (Math.abs(Math.sin(now / 180)) * 0.18);
      mesh.scale.set(pulse, pulse, pulse);
      material.emissive.set(highlighted ? '#f59e0b' : '#ffffff');
      material.emissiveIntensity = highlighted ? Math.max(0.58, intensity) : intensity;
    }
  }

  private resize(): void {
    if (!this.hostRef || !this.camera || !this.renderer) {
      return;
    }
    const host = this.hostRef.nativeElement;
    const width = Math.max(host.clientWidth, 320);
    const height = Math.max(host.clientHeight, 420);
    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();
    this.renderer.setSize(width, height);
    this.updateWorkingStatusBadges();
  }

  private observeHostSize(): void {
    if (!this.hostRef || typeof ResizeObserver === 'undefined') {
      return;
    }
    this.resizeObserver?.disconnect();
    this.resizeObserver = new ResizeObserver(() => this.resize());
    this.resizeObserver.observe(this.hostRef.nativeElement);
  }

  private handlePointerMove(event: PointerEvent): void {
    if (!this.renderer || !this.camera || !this.layoutGroup || !this.raycaster || !this.pointer || !this.hostRef) {
      return;
    }

    const rect = this.renderer.domElement.getBoundingClientRect();
    if (!rect.width || !rect.height) {
      return;
    }

    const relativeX = event.clientX - rect.left;
    const relativeY = event.clientY - rect.top;
    this.pointer.x = (relativeX / rect.width) * 2 - 1;
    this.pointer.y = -((relativeY / rect.height) * 2 - 1);
    this.raycaster.setFromCamera(this.pointer, this.camera);

    const intersections = this.raycaster.intersectObjects(this.layoutGroup.children, false);
    const hit = intersections.find(item => !!(item.object as Mesh).userData?.['location']);
    if (!hit) {
      this.clearHover();
      return;
    }

    const mesh = hit.object as Mesh;
    const locationCode = String(mesh.userData['location'] ?? '');
    const location = this.findLocation(locationCode);
    if (!location) {
      this.clearHover();
      return;
    }

    const assignments = assignmentByLocation(this.assignments);
    const states = liveStateByLocation(this.liveStates);
    this.hoverLocation = location;
    this.hoverLiveState = states.get(locationCode) ?? null;
    this.hoverAssignment = assignments.get(locationCode) ?? null;

    const host = this.hostRef.nativeElement;
    const tooltipWidth = 272;
    const tooltipHeight = 220;
    this.tooltipLeft = Math.max(12, Math.min(relativeX + 16, host.clientWidth - tooltipWidth));
    this.tooltipTop = Math.max(12, Math.min(relativeY + 16, host.clientHeight - tooltipHeight));
    this.renderer.domElement.style.cursor = 'pointer';
  }

  private handleDoubleClick(event: MouseEvent): void {
    const locationCode = this.pickLocation(event.clientX, event.clientY);
    if (locationCode) {
      this.focusLocation(locationCode);
    }
  }

  private pickLocation(clientX: number, clientY: number): string | null {
    if (!this.renderer || !this.camera || !this.layoutGroup || !this.raycaster || !this.pointer) {
      return null;
    }

    const rect = this.renderer.domElement.getBoundingClientRect();
    if (!rect.width || !rect.height) {
      return null;
    }

    this.pointer.x = ((clientX - rect.left) / rect.width) * 2 - 1;
    this.pointer.y = -(((clientY - rect.top) / rect.height) * 2 - 1);
    this.raycaster.setFromCamera(this.pointer, this.camera);

    const intersections = this.raycaster.intersectObjects(this.layoutGroup.children, false);
    const hit = intersections.find(item => !!(item.object as Mesh).userData?.['location']);
    return hit ? String((hit.object as Mesh).userData['location'] ?? '') : null;
  }

  private clearHover(): void {
    this.hoverLocation = null;
    this.hoverLiveState = null;
    this.hoverAssignment = null;
    if (this.renderer) {
      this.renderer.domElement.style.cursor = 'grab';
    }
  }

  private hasStock(state: LiveLocationState | null | undefined): boolean {
    return !!state && Number(state.physicalQty ?? 0) > 0;
  }

  private stockStatusLabel(state: LiveLocationState | null | undefined): string {
    if (!state) {
      return 'Not synced';
    }
    return this.hasStock(state) ? 'Occupied' : 'Empty';
  }

  private meshColor(location: WarehouseLayoutLocation, state: LiveLocationState | null | undefined): string {
    if (!state) {
      return location.type === 'HIGH_RACK' ? '#94a3b8' : '#cbd5e1';
    }
    return this.hasStock(state) ? '#22c55e' : '#cbd5e1';
  }

  formatQuantity(value: number | null | undefined): string {
    const normalized = Number(value ?? 0);
    if (!Number.isFinite(normalized)) {
      return '0';
    }
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: Number.isInteger(normalized) ? 0 : 2,
      maximumFractionDigits: Number.isInteger(normalized) ? 0 : 2
    }).format(normalized);
  }

  private meshWidth(location: WarehouseLayoutLocation): number {
    return location.type === 'HIGH_RACK' ? 0.9 : 1.2;
  }

  private meshDepth(location: WarehouseLayoutLocation): number {
    return location.type === 'HIGH_RACK' ? 0.9 : 1.2;
  }

  private meshHeight(location: WarehouseLayoutLocation): number {
    const base = location.type === 'HIGH_RACK' ? 0.72 : 0.55;
    return base;
  }

  private yLevel(location: WarehouseLayoutLocation): number {
    return ((location.level ?? 1) - 1) * 0.9 + (this.meshHeight(location) / 2) + 0.08;
  }

  private sceneZ(location: WarehouseLayoutLocation): number {
    if (!this.layout) {
      return this.meshDepth(location) / 2;
    }
    return this.layout.warehouseHeight - (location.y ?? 0) - (this.meshDepth(location) / 2);
  }

  private updateControlLimits(): void {
    if (!this.controls || !this.layout) {
      return;
    }

    const largestSpan = Math.max(this.layout.warehouseWidth, this.layout.warehouseHeight, 20);
    this.controls.minDistance = 0.8;
    this.controls.maxDistance = largestSpan * 2.4;
  }

  focusLocation(locationCode: string): void {
    const mesh = this.meshMap.get(locationCode);
    const location = this.findLocation(locationCode);
    if (location) {
      const assignments = assignmentByLocation(this.assignments);
      const states = liveStateByLocation(this.liveStates);
      this.hoverLocation = location;
      this.hoverLiveState = states.get(locationCode) ?? null;
      this.hoverAssignment = assignments.get(locationCode) ?? null;
      this.tooltipLeft = 16;
      this.tooltipTop = 16;
    }

    if (!mesh || !this.camera || !this.controls) {
      return;
    }

    const target = mesh.position.clone();
    const focusDistance = 2.8;
    this.controls.target.set(target.x, target.y, target.z);
    this.camera.position.set(
      target.x + focusDistance * 0.75,
      Math.max(target.y + focusDistance * 0.95, 2.2),
      target.z + focusDistance
    );
    this.controls.update();
  }

  private rebuildWorkingStatusBadges(): void {
    this.clearWorkingStatusBadges();
    const layer = this.statusLayerRef?.nativeElement;
    if (!layer) {
      return;
    }

    for (const zone of this.workingStatusZones) {
      const badge = document.createElement('div');
      badge.className = 'working-status-badge';
      badge.style.setProperty('--status-color', zone.color || '#0ea5e9');

      const zoneLabel = document.createElement('span');
      zoneLabel.className = 'working-status-badge-zone';
      zoneLabel.textContent = zone.zone;

      const progressLabel = document.createElement('strong');
      progressLabel.className = 'working-status-badge-progress';
      progressLabel.textContent = `${this.formatWhole(zone.orderCount)}/${this.formatWhole(zone.totalOrders)}`;

      const statusLabel = document.createElement('span');
      statusLabel.className = 'working-status-badge-status';
      statusLabel.textContent = `${zone.flow} ${zone.statusLabel}`;

      const quantityLabel = document.createElement('span');
      quantityLabel.className = 'working-status-badge-qty';
      quantityLabel.textContent = `CS ${this.formatQuantity(zone.csQty)} | PCS ${this.formatQuantity(zone.pcsQty)}`;

      badge.title = `${zone.zone}\n${zone.statusLabel}\nOrders ${zone.orderCount}/${zone.totalOrders}\nCS ${zone.csQty} | PCS ${zone.pcsQty}`;
      badge.appendChild(zoneLabel);
      badge.appendChild(progressLabel);
      badge.appendChild(statusLabel);
      badge.appendChild(quantityLabel);
      layer.appendChild(badge);
      this.workingStatusBadgeElements.set(this.workingStatusZoneKey(zone), badge);
    }
  }

  private clearWorkingStatusBadges(): void {
    const layer = this.statusLayerRef?.nativeElement;
    if (layer) {
      layer.innerHTML = '';
    }
    this.workingStatusBadgeElements.clear();
  }

  private updateWorkingStatusBadges(): void {
    if (!this.camera || !this.hostRef || !this.layout) {
      return;
    }

    const host = this.hostRef.nativeElement;
    const hostWidth = host.clientWidth;
    const hostHeight = host.clientHeight;
    if (!hostWidth || !hostHeight) {
      return;
    }

    const THREE = this.three;
    if (!THREE) {
      return;
    }

    for (const zone of this.workingStatusZones) {
      const badge = this.workingStatusBadgeElements.get(this.workingStatusZoneKey(zone));
      if (!badge) {
        continue;
      }

      const worldPosition = new THREE.Vector3(
        zone.x + (zone.width / 2),
        0.72,
        this.layout.warehouseHeight - zone.y - (zone.height / 2)
      );
      const projected = worldPosition.project(this.camera);
      const visible = projected.z >= -1 && projected.z <= 1;

      if (!visible) {
        badge.style.display = 'none';
        continue;
      }

      const screenX = ((projected.x + 1) / 2) * hostWidth;
      const screenY = ((-projected.y + 1) / 2) * hostHeight;

      if (screenX < -120 || screenX > hostWidth + 120 || screenY < -80 || screenY > hostHeight + 80) {
        badge.style.display = 'none';
        continue;
      }

      badge.style.display = 'block';
      badge.style.left = `${screenX}px`;
      badge.style.top = `${screenY}px`;
    }
  }

  private workingStatusZoneKey(zone: WorkingStatusZoneSnapshot): string {
    return `${zone.aisleId}:${zone.flow}:${zone.statusCode}`;
  }

  private formatWhole(value: number): string {
    return new Intl.NumberFormat('en-US', {
      maximumFractionDigits: 0
    }).format(value);
  }
}
