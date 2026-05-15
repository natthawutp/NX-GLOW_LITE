import {
  LayoutStats,
  LayoutGeneratorConfig,
  LiveLocationState,
  SlottingAssignment,
  WarehouseAisle,
  WarehouseBoundaryPoint,
  WarehouseLayout,
  WarehouseLayoutLocation
} from './warehouse-optimize.models';

const DEFAULT_LAYOUT: WarehouseLayout = {
  warehouseWidth: 130,
  warehouseHeight: 70,
  aisles: []
};

export function normalizeLayout(raw: unknown): WarehouseLayout {
  const source = (raw ?? {}) as Record<string, unknown>;
  const warehouseWidth = toNumber(source['warehouseWidth'], DEFAULT_LAYOUT.warehouseWidth);
  const warehouseHeight = toNumber(source['warehouseHeight'], toNumber(source['warehouseLength'], DEFAULT_LAYOUT.warehouseHeight));
  const boundaryPolygon = normalizeBoundaryPolygon(source['boundaryPolygon']);
  const aislesRaw = Array.isArray(source['aisles']) ? source['aisles'] : [];

  const aisles = aislesRaw.map((aisleRaw, index) => normalizeAisle(aisleRaw as unknown as Record<string, unknown>, index + 1));
  return {
    warehouseWidth,
    warehouseHeight,
    boundaryPolygon,
    aisles
  };
}

export function prettyLayoutJson(layout: WarehouseLayout): string {
  return JSON.stringify(layout, null, 2);
}

export function createEmptyLayout(warehouseWidth = DEFAULT_LAYOUT.warehouseWidth, warehouseHeight = DEFAULT_LAYOUT.warehouseHeight): WarehouseLayout {
  return {
    warehouseWidth: round(Math.max(10, warehouseWidth)),
    warehouseHeight: round(Math.max(10, warehouseHeight)),
    boundaryPolygon: null,
    aisles: []
  };
}

export function extractLocations(layout: WarehouseLayout | null | undefined): WarehouseLayoutLocation[] {
  if (!layout) {
    return [];
  }
  return layout.aisles.flatMap(aisle => aisle.locations);
}

export function layoutStats(layout: WarehouseLayout | null | undefined): LayoutStats {
  const locations = extractLocations(layout);
  const highRack = locations.filter(location => location.type === 'HIGH_RACK').length;
  const shelf = locations.filter(location => location.type === 'SHELF').length;
  const driveIn = locations.filter(location => location.type === 'DRIVE_IN').length;
  const floor = locations.filter(location => location.type === 'FLOOR').length;
  return {
    aisles: layout?.aisles.length ?? 0,
    locations: locations.length,
    highRack,
    shelf,
    driveIn,
    floor
  };
}

export function createGeneratedLayout(config: LayoutGeneratorConfig): WarehouseLayout {
  const aisleCount = clampInt(config.aisleCount, 1, 24, 6);
  const baysPerAisle = clampInt(config.baysPerAisle, 4, 120, 20);
  const levels = clampInt(config.levels, 1, 12, 4);
  const zonePrefix = (config.zonePrefix || 'Zone').trim();
  const rackType = (config.rackType || 'HIGH_RACK').trim().toUpperCase();
  const bayWidth = toNumber(config.bayWidth, 1.2);
  const bayDepth = toNumber(config.bayDepth, 1);
  const aisleWidth = toNumber(config.aisleWidth, 2.6);
  const startX = toNumber(config.startX, 8);
  const startY = toNumber(config.startY, 8);
  const warehouseWidth = Math.max(toNumber(config.warehouseWidth, DEFAULT_LAYOUT.warehouseWidth), startX + aisleCount * (bayDepth * 2 + aisleWidth) + 8);
  const warehouseHeight = Math.max(toNumber(config.warehouseHeight, DEFAULT_LAYOUT.warehouseHeight), startY + baysPerAisle * bayWidth + 8);

  const aisles: WarehouseAisle[] = [];
  for (let aisleIndex = 0; aisleIndex < aisleCount; aisleIndex++) {
    const aisleNo = aisleIndex + 1;
    const zone = `${zonePrefix}-${aisleNo}`;
    const x = startX + aisleIndex * (bayDepth * 2 + aisleWidth);
    const y = startY;
    const locations: WarehouseLayoutLocation[] = [];

    for (let bayIndex = 0; bayIndex < baysPerAisle; bayIndex++) {
      const position = bayIndex + 1;
      const positionCode = `${position * 2 - 1}`.padStart(3, '0');
      const locationY = y + bayIndex * bayWidth;

      for (const side of ['L', 'R']) {
        const locationX = side === 'L' ? x : x + bayDepth + aisleWidth;
        for (let level = 1; level <= levels; level++) {
          const location: WarehouseLayoutLocation = {
            location: `${zone}-${String(aisleNo).padStart(2, '0')}-${positionCode}${side}-L${level}`,
            x: round(locationX),
            y: round(locationY),
            zone,
            type: rackType,
            level,
            aisle: aisleNo,
            position,
            side
          };
          locations.push(location);
        }
      }
    }

    aisles.push({
      id: aisleNo,
      x: round(x),
      y: round(y),
      width: round(bayDepth * 2 + aisleWidth),
      height: round(baysPerAisle * bayWidth),
      type: rackType,
      levels,
      zone,
      bayWidth: round(bayWidth),
      bayDepth: round(bayDepth),
      aisleWidth: round(aisleWidth),
      locations
    });
  }

  return {
    warehouseWidth: round(warehouseWidth),
    warehouseHeight: round(warehouseHeight),
    boundaryPolygon: null,
    aisles
  };
}

export function parseLayoutText(layoutText: string): WarehouseLayout {
  if (!layoutText.trim()) {
    return createGeneratedLayout({
      warehouseWidth: DEFAULT_LAYOUT.warehouseWidth,
      warehouseHeight: DEFAULT_LAYOUT.warehouseHeight,
      aisleCount: 4,
      baysPerAisle: 16,
      levels: 4,
      zonePrefix: 'Zone',
      rackType: 'HIGH_RACK',
      bayWidth: 1.2,
      bayDepth: 1,
      aisleWidth: 2.5,
      startX: 10,
      startY: 10
    });
  }

  return normalizeLayout(JSON.parse(layoutText));
}

export function assignmentByLocation(assignments: SlottingAssignment[]): Map<string, SlottingAssignment> {
  return new Map(assignments.map(assignment => [assignment.location, assignment]));
}

export function liveStateByLocation(states: LiveLocationState[]): Map<string, LiveLocationState> {
  return new Map(states.map(state => [state.location, state]));
}

export function colorForLocation(
  location: WarehouseLayoutLocation,
  assignments: Map<string, SlottingAssignment>,
  liveStates?: Map<string, LiveLocationState>
): string {
  const live = liveStates?.get(location.location);
  if (live?.colorClass) {
    return colorClassToHex(live.colorClass);
  }

  const assignment = assignments.get(location.location);
  const velocity = assignment?.velocityClass ?? location.slottedClass ?? '';
  if (velocity === 'A') {
    return '#ef4444';
  }
  if (velocity === 'B') {
    return '#f59e0b';
  }
  if (velocity === 'C') {
    return '#22c55e';
  }

  return '#94a3b8';
}

export function colorClassToHex(colorClass: string | null | undefined): string {
  switch ((colorClass || '').toLowerCase()) {
    case 'velocity-a':
      return '#ef4444';
    case 'velocity-b':
      return '#f59e0b';
    case 'velocity-c':
      return '#22c55e';
    case 'category-0':
      return '#2563eb';
    case 'category-1':
      return '#8b5cf6';
    case 'category-2':
      return '#14b8a6';
    case 'category-3':
      return '#ec4899';
    case 'category-4':
      return '#f97316';
    case 'category-5':
      return '#0f766e';
    default:
      return '#cbd5e1';
  }
}

export function formatNumber(value: number | null | undefined, digits = 0): string {
  if (value === null || value === undefined || Number.isNaN(value)) {
    return '-';
  }
  return new Intl.NumberFormat('en-US', {
    minimumFractionDigits: digits,
    maximumFractionDigits: digits
  }).format(value);
}

function normalizeAisle(aisleRaw: Record<string, unknown>, fallbackId: number): WarehouseAisle {
  const locationsRaw = Array.isArray(aisleRaw['locations']) ? aisleRaw['locations'] : [];
  const bayWidth = toNumber(aisleRaw['bayWidth'], 1.2);
  const bayDepth = toNumber(aisleRaw['bayDepth'], 1);
  const aisleWidth = toNumber(aisleRaw['aisleWidth'], 2.5);
  const width = toNumber(aisleRaw['width'], bayDepth * 2 + aisleWidth);
  const height = toNumber(aisleRaw['height'], Math.max(locationsRaw.length / 4, 10) * bayWidth);
  const levels = clampInt(aisleRaw['levels'], 1, 12, 4);
  const zone = String(aisleRaw['zone'] ?? `Zone-${fallbackId}`);
  const type = String(aisleRaw['type'] ?? 'HIGH_RACK').toUpperCase();

  return {
    id: clampInt(aisleRaw['id'], 1, 9999, fallbackId),
    x: toNumber(aisleRaw['x'], fallbackId * 4),
    y: toNumber(aisleRaw['y'], 8),
    width,
    height,
    type,
    direction: aisleRaw['direction'] ? String(aisleRaw['direction']) : 'AUTO',
    levels,
    zone,
    bayWidth,
    bayDepth,
    aisleWidth,
    tunnelLevelFrom: clampInt(aisleRaw['tunnelLevelFrom'], 1, 12, 1),
    tunnelLevelTo: clampInt(aisleRaw['tunnelLevelTo'], 1, 12, levels),
    pickFaceLevels: normalizeNumberArray(aisleRaw['pickFaceLevels']),
    startPointX: toNullableNumber(aisleRaw['startPointX']),
    startPointY: toNullableNumber(aisleRaw['startPointY']),
    baseLocations: normalizeLocationArray(aisleRaw['baseLocations'], zone, type),
    locations: locationsRaw.map((locationRaw, index) => normalizeLocation(locationRaw as Record<string, unknown>, index + 1, zone, type))
  };
}

function normalizeLocation(
  locationRaw: Record<string, unknown>,
  fallbackPosition: number,
  zone: string,
  type: string
): WarehouseLayoutLocation {
  return {
    location: String(locationRaw['location'] ?? `${zone}-${String(fallbackPosition).padStart(3, '0')}`),
    zone: String(locationRaw['zone'] ?? zone),
    type: String(locationRaw['type'] ?? type).toUpperCase(),
    level: clampInt(locationRaw['level'], 1, 12, 1),
    aisle: clampInt(locationRaw['aisle'], 1, 9999, 1),
    position: clampInt(locationRaw['position'], 1, 99999, fallbackPosition),
    side: locationRaw['side'] ? String(locationRaw['side']) : 'L',
    x: toNumber(locationRaw['x'], 0),
    y: toNumber(locationRaw['y'], 0),
    slottedClass: locationRaw['slottedClass'] ? String(locationRaw['slottedClass']) : null,
    slottedSku: locationRaw['slottedSku'] ? String(locationRaw['slottedSku']) : null
  };
}

function toNumber(value: unknown, fallback: number): number {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}

function toNullableNumber(value: unknown): number | null {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
}

function clampInt(value: unknown, min: number, max: number, fallback: number): number {
  const parsed = Math.round(toNumber(value, fallback));
  return Math.min(max, Math.max(min, parsed));
}

function round(value: number): number {
  return Math.round(value * 100) / 100;
}

function normalizeBoundaryPolygon(raw: unknown): WarehouseBoundaryPoint[] | null {
  if (!Array.isArray(raw)) {
    return null;
  }

  const points = raw
    .map(point => point as Record<string, unknown>)
    .filter(point => Number.isFinite(Number(point['x'])) && Number.isFinite(Number(point['y'])))
    .map(point => ({
      x: round(Number(point['x'])),
      y: round(Number(point['y']))
    }));

  return points.length >= 3 ? points : null;
}

function normalizeNumberArray(raw: unknown): number[] | null {
  if (!Array.isArray(raw)) {
    return null;
  }

  const values = raw
    .map(value => Number(value))
    .filter((value): value is number => Number.isFinite(value))
    .map(value => Math.round(value));

  return values.length > 0 ? values : null;
}

function normalizeLocationArray(raw: unknown, zone: string, type: string): WarehouseLayoutLocation[] | null {
  if (!Array.isArray(raw)) {
    return null;
  }

  return raw.map((locationRaw, index) => normalizeLocation(locationRaw as Record<string, unknown>, index + 1, zone, type));
}
