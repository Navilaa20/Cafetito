import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransporteRequest, TransporteResponse } from '../models/transporte.model';
import { TRANSPORTE_API_URL } from '../../../core/constants/api';

@Injectable({ providedIn: 'root' })
export class TransporteService {
  // ✅ ELIMINAMOS la url de localhost y usamos la constante 'base'
  constructor(private http: HttpClient) {}

  crearTransporte(dto: TransporteRequest): Observable<TransporteResponse> {
    return this.http.post<TransporteResponse>(TRANSPORTE_API_URL, dto);
  }

  listarTransportes(): Observable<TransporteResponse[]> {
    return this.http.get<TransporteResponse[]>(TRANSPORTE_API_URL);
  }

  // ✅ USAMOS LA RUTA CORRECTA PARA CATÁLOGOS DESDE EL BACKEND
  getMarcas(): Observable<any[]> {
    return this.http.get<any[]>(`${TRANSPORTE_API_URL.replace('/transportes', '/catalogos/marcas')}`);
  }

  getColores(): Observable<any[]> {
    return this.http.get<any[]>(`${TRANSPORTE_API_URL.replace('/transportes', '/catalogos/colores')}`);
  }

  getLinea(): Observable<any[]> {
    return this.http.get<any[]>(`${TRANSPORTE_API_URL.replace('/transportes', '/catalogos/lineas')}`);
  }

  getTiposPlaca(): Observable<any[]> {
    return this.http.get<any[]>(`${TRANSPORTE_API_URL.replace('/transportes', '/catalogos/tipos-placa')}`);
  }

  getLineasPorMarca(marcaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${TRANSPORTE_API_URL.replace('/transportes', `/catalogos/marcas/${marcaId}/lineas`)}`);
  }
}
