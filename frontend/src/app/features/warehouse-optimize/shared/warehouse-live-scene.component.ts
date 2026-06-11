import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import type { PerspectiveCamera, Scene, WebGLRenderer, Group, Mesh, MeshStandardMaterial } from 'three';
import {
  LiveLocationState,
  SlottingAssignment,
  WarehouseLayout,
  WarehouseLayoutLocation
} from './warehouse-optimize.models';
import {
  assignmentByLocation,
  colorClassToHex,
  colorForLocation,
  extractLocations,
  liveStateByLocation
} from './warehouse-optimize-layout.utils';

type OrbitControlsType = typeof import('three/examples/jsm/controls/OrbitControls.js').OrbitControls;
type ThreeModule = typeof import('three');

@Component({
  selector: 'wms-warehouse-live-scene',
  standalone: true,
  imports: [CommonModule],
  template: `<div #host class="scene-host"></div>`,
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
  `]
})
export class WarehouseLiveSceneComponent implements AfterViewInit, OnChanges, OnDestroy {
  @ViewChild('host', { static: true }) private readonly hostRef?: ElementRef<HTMLDivElement>;

  @Input() layout: WarehouseLayout | null = null;
  @Input() assignments: SlottingAssignment[] = [];
  @Input() liveStates: LiveLocationState[] = [];

  private three?: ThreeModule;
  private camera?: PerspectiveCamera;
  private scene?: Scene;
  private renderer?: WebGLRenderer;
  private controls?: InstanceType<OrbitControlsType>;
  private layoutGroup?: Group;
  private floorGroup?: Group;
  private animationId: number | null = null;
  private resizeObserver?: ResizeObserver;
  private readonly meshMap = new Map<string, Mesh>();
  private readonly blinkUntil = new Map<string, number>();
  private readonly resizeHandler = () => this.resize();

  async ngAfterViewInit(): Promise<void> {
    await this.ensureScene();
    this.rebuildScene();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.hostRef || !this.scene) {
      return;
    }
    if (changes['layout']) {
      this.rebuildScene();
      return;
    }
    if (changes['assignments'] || changes['liveStates']) {
      this.applyState();
    }
  }

  ngOnDestroy(): void {
    if (this.animationId !== null) {
      cancelAnimationFrame(this.animationId);
    }
    window.removeEventListener('resize', this.resizeHandler);
    this.resizeObserver?.disconnect();
    this.controls?.dispose();
    this.renderer?.dispose();
    this.meshMap.clear();
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

    const OrbitControls = controlsModule.OrbitControls;
    this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    this.controls.enableDamping = true;
    this.controls.target.set(24, 0, 24);

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

    this.layoutGroup = new THREE.Group();
    this.scene.add(this.layoutGroup);

    window.addEventListener('resize', this.resizeHandler);
    this.observeHostSize();
    this.animate();
  }

  private rebuildScene(): void {
    if (!this.three || !this.layoutGroup || !this.floorGroup || !this.layout) {
      return;
    }

    const THREE = this.three;
    this.meshMap.clear();
    this.blinkUntil.clear();

    while (this.layoutGroup.children.length) {
      const child = this.layoutGroup.children[0];
      this.layoutGroup.remove(child);
    }
    while (this.floorGroup.children.length) {
      const child = this.floorGroup.children[0];
      this.floorGroup.remove(child);
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

    const locations = extractLocations(this.layout);
    for (const location of locations) {
      const material = new THREE.MeshStandardMaterial({
        color: colorForLocation(location, assignmentByLocation(this.assignments), liveStateByLocation(this.liveStates)),
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
      this.controls.target.set(this.layout.warehouseWidth / 2, 1, this.layout.warehouseHeight / 2);
      this.controls.update();
    }

    this.applyState();
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
      if (!location) {
        continue;
      }
      const fallbackColor = colorForLocation(location, assignments, states);
      material.color.set(state?.colorClass ? colorClassToHex(state.colorClass) : fallbackColor);
      material.opacity = state?.physicalQty && state.physicalQty > 0 ? 1 : assignment ? 0.9 : 0.72;
      material.transparent = material.opacity < 1;
      material.emissive.set(state?.blink ? '#ffffff' : '#0f172a');
      material.emissiveIntensity = state?.blink ? 0.35 : 0.04;
      if (state?.blink) {
        this.blinkUntil.set(locationCode, now + 15000);
      }
    }
  }

  private findLocation(locationCode: string): WarehouseLayoutLocation | undefined {
    return extractLocations(this.layout).find(location => location.location === locationCode);
  }

  private animate(): void {
    this.animationId = requestAnimationFrame(() => this.animate());
    if (!this.renderer || !this.scene || !this.camera) {
      return;
    }

    this.controls?.update();
    this.updateBlinkAnimation();
    this.renderer.render(this.scene, this.camera);
  }

  private updateBlinkAnimation(): void {
    const now = performance.now();
    for (const [locationCode, mesh] of this.meshMap.entries()) {
      const material = mesh.material as MeshStandardMaterial;
      const expiresAt = this.blinkUntil.get(locationCode);
      if (!expiresAt || now >= expiresAt) {
        this.blinkUntil.delete(locationCode);
        mesh.scale.set(1, 1, 1);
        material.emissiveIntensity = Math.max(0.04, material.emissiveIntensity * 0.85);
        continue;
      }

      const intensity = 0.4 + Math.abs(Math.sin(now / 180)) * 0.55;
      const pulse = 1 + (Math.abs(Math.sin(now / 180)) * 0.18);
      mesh.scale.set(pulse, pulse, pulse);
      material.emissiveIntensity = intensity;
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
  }

  private observeHostSize(): void {
    if (!this.hostRef || typeof ResizeObserver === 'undefined') {
      return;
    }
    this.resizeObserver?.disconnect();
    this.resizeObserver = new ResizeObserver(() => this.resize());
    this.resizeObserver.observe(this.hostRef.nativeElement);
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
}
