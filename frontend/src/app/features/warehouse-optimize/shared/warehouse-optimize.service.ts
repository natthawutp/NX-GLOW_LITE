import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiService } from '@core/services/api.service';
import { ApiResponse } from '@core/models/auth.model';
import { environment } from '@env/environment';
import {
  CustomerOption,
  OrdersMutationResponse,
  PickOrder,
  ProductRecord,
  ProductsSummaryResponse,
  RouteOptimizationResult,
  SlottingAssignment,
  SlottingAssignmentsResponse,
  StockDelta,
  StockSyncSnapshot,
  WarehouseOptimizeAnalytics,
  WarehouseProfileDetail,
  WarehouseProfileSummary
} from './warehouse-optimize.models';

@Injectable({ providedIn: 'root' })
export class WarehouseOptimizeService {
  private readonly api = inject(ApiService);
  private readonly http = inject(HttpClient);
  private readonly basePath = '/warehouse-optimize';

  getCustomers(): Observable<CustomerOption[]> {
    return this.api.get<CustomerOption[]>(`${this.basePath}/customers`).pipe(map(response => response.data));
  }

  getProfiles(search = ''): Observable<WarehouseProfileSummary[]> {
    return this.api.get<WarehouseProfileSummary[]>(`${this.basePath}/profiles`, { q: search }).pipe(map(response => response.data));
  }

  getProfile(profileId: number): Observable<WarehouseProfileDetail> {
    return this.api.get<WarehouseProfileDetail>(`${this.basePath}/profiles/${profileId}`).pipe(map(response => response.data));
  }

  saveProfile(payload: Record<string, unknown>): Observable<WarehouseProfileDetail> {
    return this.api.post<WarehouseProfileDetail>(`${this.basePath}/profiles`, payload).pipe(map(response => response.data));
  }

  generateProducts(count: number): Observable<ProductsSummaryResponse> {
    return this.api.post<ProductsSummaryResponse>(`${this.basePath}/products/generate`, { count }).pipe(map(response => response.data));
  }

  getProducts(): Observable<ProductRecord[]> {
    return this.api.get<ProductRecord[]>(`${this.basePath}/products`).pipe(map(response => response.data));
  }

  uploadProducts(products: Record<string, unknown>[]): Observable<ProductsSummaryResponse> {
    return this.api.post<ProductsSummaryResponse>(`${this.basePath}/products/upload`, { products }).pipe(map(response => response.data));
  }

  initializeSlotting(): Observable<Record<string, unknown>> {
    return this.api.post<Record<string, unknown>>(`${this.basePath}/slotting/init-db`, {}).pipe(map(response => response.data));
  }

  optimizeSlotting(profileId: number, algorithm: string): Observable<SlottingAssignmentsResponse> {
    return this.api.post<SlottingAssignmentsResponse>(`${this.basePath}/slotting/optimize`, { profileId, algorithm }).pipe(map(response => response.data));
  }

  getAssignments(profileId: number): Observable<SlottingAssignmentsResponse> {
    return this.api.get<SlottingAssignmentsResponse>(`${this.basePath}/slotting/assignments/${profileId}`).pipe(map(response => response.data));
  }

  clearAssignments(profileId: number): Observable<Record<string, unknown>> {
    return this.api.delete<Record<string, unknown>>(`${this.basePath}/assignments?profileId=${profileId}`).pipe(map(response => response.data));
  }

  generateOrders(profileId: number, count: number): Observable<OrdersMutationResponse> {
    return this.api.post<OrdersMutationResponse>(`${this.basePath}/orders/generate`, { profileId, count }).pipe(map(response => response.data));
  }

  uploadOrders(profileId: number, orders: Record<string, unknown>[]): Observable<OrdersMutationResponse> {
    return this.api.post<OrdersMutationResponse>(`${this.basePath}/orders/upload`, { profileId, orders }).pipe(map(response => response.data));
  }

  getOrders(profileId: number): Observable<PickOrder[]> {
    return this.api.get<PickOrder[]>(`${this.basePath}/orders/${profileId}`).pipe(map(response => response.data));
  }

  getOrderDetail(orderId: number): Observable<PickOrder> {
    return this.api.get<PickOrder>(`${this.basePath}/orders/detail/${orderId}`).pipe(map(response => response.data));
  }

  optimizeRoute(orderId: number): Observable<RouteOptimizationResult> {
    return this.api.post<RouteOptimizationResult>(`${this.basePath}/routes/optimize/${orderId}`, {}).pipe(map(response => response.data));
  }

  getRouteResults(orderId: number): Observable<Record<string, unknown>[]> {
    return this.api.get<Record<string, unknown>[]>(`${this.basePath}/routes/results/${orderId}`).pipe(map(response => response.data));
  }

  getAnalytics(profileId: number): Observable<WarehouseOptimizeAnalytics> {
    return this.api.get<WarehouseOptimizeAnalytics>(`${this.basePath}/analytics/${profileId}`).pipe(map(response => response.data));
  }

  exportAnalytics(profileId: number): Observable<string> {
    return this.http.get(`${environment.apiUrl}${this.basePath}/analytics/export?profileId=${profileId}`, {
      responseType: 'text'
    });
  }

  syncStock(profileId: number, customerCode: string): Observable<StockSyncSnapshot> {
    return this.api.post<StockSyncSnapshot>(`${this.basePath}/viewer/${profileId}/sync-stock`, { customerCode }).pipe(map(response => response.data));
  }

  loadStockDelta(profileId: number, customerCode: string, cursor: string): Observable<StockDelta> {
    return this.api.post<StockDelta>(`${this.basePath}/viewer/${profileId}/stock-delta`, {
      customerCode,
      cursor
    }).pipe(map(response => response.data));
  }
}
