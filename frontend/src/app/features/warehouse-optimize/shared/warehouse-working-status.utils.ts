import {
  WarehouseAisle,
  WarehouseLayout,
  WorkingStatusSnapshotItem,
  WorkingStatusSnapshotResponse,
  WorkingStatusZoneSnapshot
} from './warehouse-optimize.models';

export interface WorkingStatusDefinition {
  flow: 'INBOUND' | 'OUTBOUND';
  code: string;
  label: string;
  color: string;
}

export const WORKING_STATUS_DEFINITIONS: readonly WorkingStatusDefinition[] = [
  { flow: 'INBOUND', code: '100', label: 'Created', color: '#5BC2E7' },
  { flow: 'INBOUND', code: '200', label: 'Located', color: '#FF9E1B' },
  { flow: 'INBOUND', code: '205', label: 'Inspected', color: '#1A005D' },
  { flow: 'INBOUND', code: '209', label: 'Confirmed', color: '#8EC400' },
  { flow: 'INBOUND', code: '300', label: 'Receiving', color: '#0F766E' },
  { flow: 'INBOUND', code: '500', label: 'Putaway', color: '#7C3AED' },
  { flow: 'INBOUND', code: '605', label: 'Putaway Done', color: '#2563EB' },
  { flow: 'INBOUND', code: '609', label: 'Stored', color: '#0EA5E9' },
  { flow: 'INBOUND', code: '700', label: 'Closed', color: '#475569' },
  { flow: 'INBOUND', code: '809', label: 'Completed', color: '#16A34A' },
  { flow: 'OUTBOUND', code: '100', label: 'New', color: '#5BC2E7' },
  { flow: 'OUTBOUND', code: '200', label: 'Processing', color: '#8B5CF6' },
  { flow: 'OUTBOUND', code: '209', label: 'Ready', color: '#14B8A6' },
  { flow: 'OUTBOUND', code: '300', label: 'Allocated', color: '#1A005D' },
  { flow: 'OUTBOUND', code: '500', label: 'Picking', color: '#FF9E1B' },
  { flow: 'OUTBOUND', code: '605', label: 'Pick Complete', color: '#C2410C' },
  { flow: 'OUTBOUND', code: '609', label: 'Packed', color: '#8EC400' },
  { flow: 'OUTBOUND', code: '700', label: 'Shipped', color: '#16A34A' },
  { flow: 'OUTBOUND', code: '809', label: 'Completed', color: '#0F766E' }
] as const;

export const WORKING_STATUS_FLOW_OPTIONS = [
  { value: 'INBOUND', label: 'Inbound' },
  { value: 'OUTBOUND', label: 'Outbound' }
] as const;

export function workingStatusMetricKey(flow: string | null | undefined, statusCode: string | null | undefined): string {
  return `${normalizeWorkingStatusText(flow)}::${normalizeWorkingStatusText(statusCode)}`;
}

export function workingStatusDefinition(
  flow: string | null | undefined,
  statusCode: string | null | undefined
): WorkingStatusDefinition | undefined {
  const key = workingStatusMetricKey(flow, statusCode);
  return WORKING_STATUS_DEFINITIONS.find(definition => workingStatusMetricKey(definition.flow, definition.code) === key);
}

export function workingStatusOptionsForFlow(flow: string | null | undefined): WorkingStatusDefinition[] {
  const normalizedFlow = normalizeWorkingStatusText(flow);
  return WORKING_STATUS_DEFINITIONS.filter(definition => normalizeWorkingStatusText(definition.flow) === normalizedFlow);
}

export function isWorkingStatusAisle(aisle: WarehouseAisle | null | undefined): boolean {
  return (aisle?.type || '').toUpperCase() === 'WORKING_STATUS';
}

export function buildWorkingStatusZones(
  layout: WarehouseLayout | null | undefined,
  snapshot: WorkingStatusSnapshotResponse | null | undefined
): WorkingStatusZoneSnapshot[] {
  if (!layout) {
    return [];
  }

  const metrics = new Map<string, WorkingStatusSnapshotItem>();
  for (const status of snapshot?.statuses ?? []) {
    metrics.set(workingStatusMetricKey(status.flow, status.statusCode), status);
  }

  return layout.aisles
    .filter(aisle => isWorkingStatusAisle(aisle))
    .map(aisle => toWorkingStatusZone(aisle, metrics))
    .filter((zone): zone is WorkingStatusZoneSnapshot => zone !== null);
}

function toWorkingStatusZone(
  aisle: WarehouseAisle,
  metrics: Map<string, WorkingStatusSnapshotItem>
): WorkingStatusZoneSnapshot | null {
  const flow = normalizeWorkingStatusText(aisle.workingStatusFlow);
  const statusCode = normalizeWorkingStatusText(aisle.workingStatusCode);
  if (!flow || !statusCode) {
    return null;
  }

  const definition = workingStatusDefinition(flow, statusCode);
  const metric = metrics.get(workingStatusMetricKey(flow, statusCode));
  const color = aisle.workingStatusColor || metric?.color || definition?.color || '#0EA5E9';

  return {
    aisleId: aisle.id,
    zone: aisle.zone,
    flow,
    statusCode,
    statusLabel: aisle.workingStatusLabel || metric?.statusLabel || definition?.label || `Status ${statusCode}`,
    color,
    orderCount: metric?.orderCount ?? 0,
    totalOrders: metric?.totalOrders ?? 0,
    csQty: Number(metric?.csQty ?? 0),
    pcsQty: Number(metric?.pcsQty ?? 0),
    x: aisle.x,
    y: aisle.y,
    width: aisle.width,
    height: aisle.height
  };
}

function normalizeWorkingStatusText(value: string | null | undefined): string {
  return (value || '').trim().toUpperCase();
}
