import { WarehouseDesignerLayout } from './warehouse-optimize.models';

export interface WarehouseDesignerAisleLike {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  type: string;
  direction?: string;
  levels: number;
  zone: string;
  bayWidth: number;
  bayDepth: number;
  aisleWidth: number;
  tunnelLevelFrom?: number;
  tunnelLevelTo?: number;
  pickFaceLevels?: number[];
  startPointX?: number;
  startPointY?: number;
  locations: Array<Record<string, unknown>>;
  updateProperties(props: Record<string, unknown>): void;
  generateLocations(): void;
}

export interface WarehouseDesignerDrawer {
  canvas: HTMLCanvasElement;
  warehouseWidth: number;
  warehouseHeight: number;
  defaultType: string;
  showGrid: boolean;
  showLocations: boolean;
  showLabels: boolean;
  showHeatmap: boolean;
  zoom: number;
  panX: number;
  panY: number;
  scale: number;
  baseScale: number;
  mode: string;
  aisles: WarehouseDesignerAisleLike[];
  selectedAisle: WarehouseDesignerAisleLike | null;
  selectedAisles: WarehouseDesignerAisleLike[];
  boundaryPolygon: Array<{ x: number; y: number }> | null;
  onAisleCreated?: ((aisle: WarehouseDesignerAisleLike) => void) | null;
  onAisleSelected?: ((aisle: WarehouseDesignerAisleLike) => void) | null;
  onMultipleAislesSelected?: ((aisles: WarehouseDesignerAisleLike[]) => void) | null;
  onAislesChanged?: (() => void) | null;
  onZoomChanged?: ((zoomPercent: number) => void) | null;
  onCoordinatesChanged?: ((x: string, y: string) => void) | null;
  onPanChanged?: (() => void) | null;
  onLocationCopied?: ((locationName: string) => void) | null;
  setMode(mode: string): void;
  zoomIn(): void;
  zoomOut(): void;
  zoomToFit(): void;
  render(): void;
  setupCanvas(): void;
  clearAll(): boolean;
  clearBoundary(): boolean;
  hasBoundary(): boolean;
  hasBoundaryDraft(): boolean;
  hasDesignContent(): boolean;
  saveState(): void;
  applyConstraintFiltering(): void;
  deleteAisle(aisle: WarehouseDesignerAisleLike): void;
  deleteSelectedAisles(): void;
  duplicateAisle(aisle: WarehouseDesignerAisleLike): WarehouseDesignerAisleLike;
  getAllLocations(): Array<Record<string, unknown>>;
  toJSON(): WarehouseDesignerLayout;
  fromJSON(data: WarehouseDesignerLayout): void;
}

export interface WarehouseDesignerDrawerConstructor {
  new (canvas: HTMLCanvasElement, warehouseWidth: number, warehouseHeight: number): WarehouseDesignerDrawer;
}

declare global {
  interface Window {
    AisleDrawer?: WarehouseDesignerDrawerConstructor;
    WarehouseDesignerAisle?: unknown;
  }
}

export {};
