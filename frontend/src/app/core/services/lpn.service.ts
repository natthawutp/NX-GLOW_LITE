import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { ApiResponse } from '@core/models/auth.model';

export interface LpnDto {
  lpnNumber: string;
  lpnRfNumber?: string;
  lpnType: string;
  lpnTypeLabel: string;
  lpnStatus: string;
  lpnStatusLabel: string;
  parentLpnNumber?: string;
  locationCode?: string;
  arrivalNumber?: string;
  shipmentNumber?: string;
  totalQuantity: number;
  totalWeight?: number;
  totalVolume?: number;
  barcodeFormat: string;
  printDate?: string;
  receiveDate?: string;
  remarks?: string;
  contents?: LpnContentDto[];
  childCount: number;
}

export interface LpnContentDto {
  lineNumber: number;
  productCode: string;
  productName?: string;
  lotNumber?: string;
  quantity: number;
  allocatedQuantity: number;
  availableQuantity: number;
  subInventoryCode?: string;
  stockStatus?: string;
  arrivalNumber?: string;
  arrivalLineNumber?: number;
}

export interface LpnReceiveRequest {
  arrivalNumber: string;
  lpnNumber?: string;
  lpnRfNumber?: string;
  lpnType?: string;
  items: LpnReceiveItem[];
}

export interface LpnReceiveItem {
  productCode: string;
  lotNumber?: string;
  quantity: number;
  subInventoryCode?: string;
  arrivalLineNumber?: number;
}

export interface LpnPutawayRequest {
  lpnNumber: string;
  targetLocationCode: string;
}

export interface LpnConfigDto {
  companyCode: string;
  warehouseCode: string;
  customerCode: string;
  lpnEnabled: boolean;
  autoGenerate: boolean;
  barcodeFormat: string;
  barcodePrefix?: string;
  printTemplate?: string;
  hierarchyEnabled: boolean;
  mixedSkuAllowed: boolean;
  outboundLpnEnabled: boolean;
}

@Injectable({ providedIn: 'root' })
export class LpnService {

  constructor(private api: ApiService) {}

  search(params?: Record<string, any>): Observable<ApiResponse<LpnDto[]>> {
    return this.api.get<LpnDto[]>('/lpn', params);
  }

  getDetail(lpnNumber: string): Observable<ApiResponse<LpnDto>> {
    return this.api.get<LpnDto>(`/lpn/${lpnNumber}`);
  }

  validateBarcode(barcode: string): Observable<ApiResponse<boolean>> {
    return this.api.get<boolean>('/lpn/validate-barcode', { barcode });
  }

  receive(request: LpnReceiveRequest): Observable<ApiResponse<LpnDto>> {
    return this.api.post<LpnDto>('/lpn/receive', request);
  }

  putaway(request: LpnPutawayRequest): Observable<ApiResponse<LpnDto>> {
    return this.api.post<LpnDto>('/lpn/putaway', request);
  }

  remove(lpnNumber: string): Observable<ApiResponse<void>> {
    return this.api.delete<void>(`/lpn/${encodeURIComponent(lpnNumber)}`);
  }

  updateStatus(lpnNumber: string, status: string): Observable<ApiResponse<void>> {
    return this.api.put<void>(`/lpn/${lpnNumber}/status`, { status });
  }

  getConfig(): Observable<ApiResponse<LpnConfigDto>> {
    return this.api.get<LpnConfigDto>('/lpn/config');
  }

  saveConfig(config: LpnConfigDto): Observable<ApiResponse<LpnConfigDto>> {
    return this.api.put<LpnConfigDto>('/lpn/config', config);
  }
}
