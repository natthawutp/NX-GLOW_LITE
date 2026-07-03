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
  const zoneStartNumber = clampInt(config.zoneStartNumber, 1, 999, 1);
  const zoneNamingPattern = config.zoneNamingPattern || 'prefix-row';
  const zoneSuffix = String(config.zoneSuffix || '').trim();
  const customZonePattern = config.customZonePattern || '{prefix}-{row}';
  const locationNamingPattern = config.locationNamingPattern || 'zone-row-bay-side-level';
  const customLocationPattern = config.customLocationPattern || '{zone}-{row}-{bay}{side}-{level}';
  const rowFormat = config.rowFormat || 'numeric';
  const rowPadding = clampInt(config.rowPadding, 1, 4, 2);
  const bayPadding = clampInt(config.bayPadding, 1, 4, 3);
  const levelPrefix = String(config.levelPrefix || 'L').trim() || 'L';
  const leftBayNumbering = config.leftBayNumbering || 'odd';
  const rightBayNumbering = config.rightBayNumbering || 'even';
  const rowNumberStart = clampInt(config.rowNumberStart, 1, 999, 1);
  const bayNumberStart = clampInt(config.bayNumberStart, 1, 999, 1);
  const levelNumberStart = clampInt(config.levelNumberStart, 1, 99, 1);
  const rackType = (config.rackType || 'HIGH_RACK').trim().toUpperCase();
  const bayWidth = toNumber(config.bayWidth, 1.2);
  const bayDepth = toNumber(config.bayDepth, 1);
  const aisleWidth = toNumber(config.aisleWidth, 2.6);
  const startX = toNumber(config.startX, 8);
  const startY = toNumber(config.startY, 8);
  const warehouseWidth = Math.max(toNumber(config.warehouseWidth, DEFAULT_LAYOUT.warehouseWidth), startX + aisleCount * (bayDepth * 2 + aisleWidth) + 8);
  const warehouseHeight = Math.max(toNumber(config.warehouseHeight, DEFAULT_LAYOUT.warehouseHeight), startY + baysPerAisle * bayWidth + 8);

  const formatRowValue = (value: number): string => {
    switch (rowFormat) {
      case 'alpha':
        return numberToAlpha(value);
      case 'alpha2':
        return numberToAlpha2(value);
      default:
        return String(value).padStart(rowPadding, '0');
    }
  };

  const formatZoneName = (rowValue: string): string => {
    switch (zoneNamingPattern) {
      case 'prefixrow':
        return `${zonePrefix}${rowValue}${zoneSuffix}`;
      case 'prefix-row-suffix':
        return zoneSuffix ? `${zonePrefix}-${rowValue}-${zoneSuffix}` : `${zonePrefix}-${rowValue}`;
      case 'custom':
        return (customZonePattern || '{prefix}-{row}')
          .replaceAll('{prefix}', zonePrefix)
          .replaceAll('{row}', rowValue)
          .replaceAll('{suffix}', zoneSuffix)
          .replaceAll('--', '-');
      default:
        return zoneSuffix ? `${zonePrefix}-${rowValue}-${zoneSuffix}` : `${zonePrefix}-${rowValue}`;
    }
  };

  const calculateBayNumber = (bayIndex: number, side: 'L' | 'R'): number => {
    const numbering = side === 'L' ? leftBayNumbering : rightBayNumbering;
    switch (numbering) {
      case 'odd':
        return (bayIndex - 1) * 2 + 1 + bayNumberStart - 1;
      case 'even':
        return bayIndex * 2 + bayNumberStart - 1;
      default:
        return bayNumberStart + bayIndex - 1;
    }
  };

  const formatLocationName = (zone: string, rowNumber: number, bayIndex: number, side: 'L' | 'R', levelNumber: number): string => {
    const row = formatRowValue(rowNumber);
    const bay = String(calculateBayNumber(bayIndex, side)).padStart(bayPadding, '0');
    const level = `${levelPrefix}${levelNumber}`;
    switch (locationNamingPattern) {
      case 'zone-row-bay-level':
        return `${zone}-${row}-${bay}-${level}`;
      case 'zone-bay-level':
        return `${zone}-${bay}-${level}`;
      case 'custom':
        return (customLocationPattern || '{zone}-{row}-{bay}{side}-{level}')
          .replaceAll('{zone}', zone)
          .replaceAll('{row}', row)
          .replaceAll('{bay}', bay)
          .replaceAll('{side}', side)
          .replaceAll('{level}', level);
      default:
        return `${zone}-${row}-${bay}${side}-${level}`;
    }
  };

  const aisles: WarehouseAisle[] = [];
  for (let aisleIndex = 0; aisleIndex < aisleCount; aisleIndex++) {
    const aisleNo = aisleIndex + 1;
    const zoneSequence = zoneStartNumber + aisleIndex;
    const rowValue = formatRowValue(zoneSequence);
    const zone = formatZoneName(rowValue);
    const rowNumber = rowNumberStart + aisleIndex;
    const x = startX + aisleIndex * (bayDepth * 2 + aisleWidth);
    const y = startY;
    const locations: WarehouseLayoutLocation[] = [];

    for (let bayIndex = 0; bayIndex < baysPerAisle; bayIndex++) {
      const position = bayIndex + 1;
      const locationY = y + bayIndex * bayWidth;

      for (const side of ['L', 'R'] as const) {
        const locationX = side === 'L' ? x : x + bayDepth + aisleWidth;
        for (let levelOffset = 0; levelOffset < levels; levelOffset++) {
          const levelNumber = levelNumberStart + levelOffset;
          locations.push({
            location: formatLocationName(zone, rowNumber, position, side, levelNumber),
            x: round(locationX),
            y: round(locationY),
            zone,
            type: rackType,
            level: levelNumber,
            aisle: rowNumber,
            position,
            side
          });
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
      aisleCode: null,
      bayWidth: round(bayWidth),
      bayDepth: round(bayDepth),
      aisleWidth: round(aisleWidth),
      lowerShelfLevels: 0,
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

export function aisleDisplayLabel(aisle: Pick<WarehouseAisle, 'zone' | 'aisleCode'> | null | undefined): string {
  if (!aisle) {
    return '-';
  }
  const zone = String(aisle.zone ?? '').trim();
  const aisleCode = String(aisle.aisleCode ?? '').trim();
  if (!aisleCode) {
    return zone || '-';
  }
  if (!zone) {
    return aisleCode;
  }
  return `${zone} / ${aisleCode}`;
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
    workingStatusFlow: aisleRaw['workingStatusFlow'] ? String(aisleRaw['workingStatusFlow']).toUpperCase() : null,
    workingStatusCode: aisleRaw['workingStatusCode'] ? String(aisleRaw['workingStatusCode']) : null,
    workingStatusLabel: aisleRaw['workingStatusLabel'] ? String(aisleRaw['workingStatusLabel']) : null,
    workingStatusColor: aisleRaw['workingStatusColor'] ? String(aisleRaw['workingStatusColor']) : null,
    levels,
    zone,
    aisleCode: aisleRaw['aisleCode'] ? String(aisleRaw['aisleCode']) : null,
    bayWidth,
    bayDepth,
    aisleWidth,
    tunnelLevelFrom: clampInt(aisleRaw['tunnelLevelFrom'], 1, 12, 1),
    tunnelLevelTo: clampInt(aisleRaw['tunnelLevelTo'], 1, 12, levels),
    lowerShelfLevels: clampInt(aisleRaw['lowerShelfLevels'], 0, Math.max(levels - 1, 0), 0),
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
  const hasExplicitSide = Object.prototype.hasOwnProperty.call(locationRaw, 'side');
  const rawSide = hasExplicitSide ? locationRaw['side'] : undefined;
  return {
    location: String(locationRaw['location'] ?? `${zone}-${String(fallbackPosition).padStart(3, '0')}`),
    zone: String(locationRaw['zone'] ?? zone),
    type: String(locationRaw['type'] ?? type).toUpperCase(),
    level: clampInt(locationRaw['level'], 1, 12, 1),
    aisle: clampInt(locationRaw['aisle'], 1, 9999, 1),
    position: clampInt(locationRaw['position'], 1, 99999, fallbackPosition),
    side: rawSide === null ? null : (rawSide ? String(rawSide) : 'L'),
    x: toNumber(locationRaw['x'], 0),
    y: toNumber(locationRaw['y'], 0),
    footprintWidth: toNullableNumber(locationRaw['footprintWidth']),
    footprintDepth: toNullableNumber(locationRaw['footprintDepth']),
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

function numberToAlpha(num: number): string {
  let result = '';
  let current = num;
  while (current > 0) {
    current--;
    result = String.fromCharCode(65 + (current % 26)) + result;
    current = Math.floor(current / 26);
  }
  return result || 'A';
}

function numberToAlpha2(num: number): string {
  const safeNum = Math.max(1, num);
  const first = Math.floor((safeNum - 1) / 26);
  const second = (safeNum - 1) % 26;
  return String.fromCharCode(65 + first) + String.fromCharCode(65 + second);
}


