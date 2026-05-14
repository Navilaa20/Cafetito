import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ADMIN_TRANSPORTE_API_URL } from '../../core/constants/api'; // Asegúrate que esto sea http://localhost:8080/api/transportes
import { AdminTransporteResponse } from './models/admin-transporte.model';

export interface ActualizarEstadoTransporteRequest {
  placa: string;
  nuevoEstado: boolean;
  observaciones: string;
}

@Injectable({ providedIn: 'root' })
export class AdminTransporteService {
  constructor(private http: HttpClient) {}

  listarTodos(): Observable<AdminTransporteResponse[]> {
    // Usamos el endpoint /todos que creamos para el administrador
    return this.http.get<AdminTransporteResponse[]>(`${ADMIN_TRANSPORTE_API_URL}/todos`);
  }

  buscarPorPlaca(placa: string): Observable<AdminTransporteResponse[]> {
    const params = new HttpParams().set('placa', placa);
    return this.http.get<AdminTransporteResponse[]>(`${ADMIN_TRANSPORTE_API_URL}/buscar`, { params });
  }

  filtrarPorEstado(activo: boolean): Observable<AdminTransporteResponse[]> {
    // Usamos HttpParams correctamente para que Java reciba el booleano
    const params = new HttpParams().set('activo', activo);
    return this.http.get<AdminTransporteResponse[]>(`${ADMIN_TRANSPORTE_API_URL}/filtrar`, { params });
  }

  actualizarEstado(id: number, dto: ActualizarEstadoTransporteRequest) {
    return this.http.put<any>(`${ADMIN_TRANSPORTE_API_URL}/${id}/estado`, dto);
  }
}