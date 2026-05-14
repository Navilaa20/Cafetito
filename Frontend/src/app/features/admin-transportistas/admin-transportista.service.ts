import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ADMIN_TRANSPORTISTA_API_URL } from '../../core/constants/api';
import { AdminTransportistaResponse } from './models/admin-transportista.model';

export interface ActualizarEstadoTransportistaRequest {
  cui: string;
  nuevoEstado: boolean;
  observaciones: string;
}

@Injectable({ providedIn: 'root' })
export class AdminTransportistaService {
  constructor(private http: HttpClient) {}

  listarTodos(): Observable<AdminTransportistaResponse[]> {
    return this.http.get<AdminTransportistaResponse[]>(ADMIN_TRANSPORTISTA_API_URL);
  }

  buscarPorCui(cui: string): Observable<AdminTransportistaResponse[]> {
    const params = new HttpParams().set('cui', cui);
    return this.http.get<AdminTransportistaResponse[]>(`${ADMIN_TRANSPORTISTA_API_URL}/buscar`, { params });
  }

  filtrarPorEstado(activo: boolean): Observable<AdminTransportistaResponse[]> {
    const params = new HttpParams().set('activo', String(activo));
    return this.http.get<AdminTransportistaResponse[]>(`${ADMIN_TRANSPORTISTA_API_URL}/filtrar`, { params });
  }

  actualizarEstado(id: number, dto: ActualizarEstadoTransportistaRequest): Observable<AdminTransportistaResponse> {
    return this.http.put<AdminTransportistaResponse>(`${ADMIN_TRANSPORTISTA_API_URL}/${id}/estado`, dto);
  }
}
