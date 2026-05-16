import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransporteRequest, TransporteResponse } from '../models/transporte.model';
import { TRANSPORTE_API_URL } from '../../../core/constants/api';

@Injectable({ providedIn: 'root' })
export class TransporteService {
  private apiUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient) {}

  crearTransporte(dto: TransporteRequest): Observable<TransporteResponse> {
    return this.http.post<TransporteResponse>(TRANSPORTE_API_URL, dto);
  }

  listarTransportes(): Observable<TransporteResponse[]> {
    return this.http.get<TransporteResponse[]>(TRANSPORTE_API_URL);
  }

  getMarcas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogos/marcas`);
  }

  getColores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogos/colores`);
  }

  getLinea(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogos/lineas`);
  }

// Si implementaste el filtro por marca:
  getLineasPorMarca(marcaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogos/marcas/${marcaId}/lineas`);
  }

  getTiposPlaca(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/catalogos/tipos-placa`);
  }

}
