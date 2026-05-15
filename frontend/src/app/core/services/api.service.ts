import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { ApiResponse, PageRequest } from '@core/models/auth.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get<T>(path: string, params?: Record<string, any>): Observable<ApiResponse<T>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          httpParams = httpParams.set(key, value.toString());
        }
      });
    }
    return this.http.get<ApiResponse<T>>(`${this.baseUrl}${path}`, { params: httpParams });
  }

  post<T>(path: string, body: any): Observable<ApiResponse<T>> {
    return this.http.post<ApiResponse<T>>(`${this.baseUrl}${path}`, body);
  }

  put<T>(path: string, body: any): Observable<ApiResponse<T>> {
    return this.http.put<ApiResponse<T>>(`${this.baseUrl}${path}`, body);
  }

  delete<T>(path: string): Observable<ApiResponse<T>> {
    return this.http.delete<ApiResponse<T>>(`${this.baseUrl}${path}`);
  }

  getPagedList<T>(path: string, pageRequest: PageRequest, filters?: Record<string, any>): Observable<ApiResponse<T[]>> {
    let params: Record<string, any> = {
      page: pageRequest.page,
      size: pageRequest.size
    };
    if (pageRequest.sortField) {
      params['sortField'] = pageRequest.sortField;
      params['sortOrder'] = pageRequest.sortOrder || 'ASC';
    }
    if (filters) {
      params = { ...params, ...filters };
    }
    return this.get<T[]>(path, params);
  }
}
