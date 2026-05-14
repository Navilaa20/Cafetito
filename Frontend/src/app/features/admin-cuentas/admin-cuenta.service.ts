import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ADMIN_CUENTA_API_URL,
  ADMIN_PARCIALIDAD_API_URL,
} from '../../core/constants/api';
import {
  CuentaResponse,
  CuentaDetalleResponse,
  CambiarEstadoCuentaRequest,
  EstadoCuenta,
} from './models/cuenta.model';
import { ParcialidadDetalleResponse } from './models/parcialidad.model';

@Injectable({ providedIn: 'root' })
export class AdminCuentaService {
  constructor(private http: HttpClient) {}

  listarTodas(): Observable<CuentaResponse[]> {
    return this.http.get<CuentaResponse[]>(ADMIN_CUENTA_API_URL);
  }

  buscarPorFecha(fecha: string): Observable<CuentaResponse[]> {
    const params = new HttpParams().set('fecha', fecha);
    return this.http.get<CuentaResponse[]>(`${ADMIN_CUENTA_API_URL}/buscar`, {
      params,
    });
  }

  buscarPorNumero(idCuenta: number): Observable<CuentaResponse> {
    const params = new HttpParams().set('idCuenta', String(idCuenta));
    return this.http.get<CuentaResponse>(`${ADMIN_CUENTA_API_URL}/buscar`, {
      params,
    });
  }

  filtrarPorEstado(estado: EstadoCuenta): Observable<CuentaResponse[]> {
    const params = new HttpParams().set('estado', estado);
    return this.http.get<CuentaResponse[]>(`${ADMIN_CUENTA_API_URL}/filtrar`, {
      params,
    });
  }

  obtenerDetalle(id: number): Observable<CuentaDetalleResponse> {
    return this.http.get<CuentaDetalleResponse>(`${ADMIN_CUENTA_API_URL}/${id}`);
  }

  cambiarEstado(
      id: number,
      dto: CambiarEstadoCuentaRequest
  ): Observable<CuentaResponse> {
    return this.http.put<CuentaResponse>(
        `${ADMIN_CUENTA_API_URL}/${id}/estado`,
        dto
    );
  }

  obtenerDetalleParcialidad(id: number): Observable<ParcialidadDetalleResponse> {
    return this.http.get<ParcialidadDetalleResponse>(
        `${ADMIN_PARCIALIDAD_API_URL}/${id}`
    );
  }

  recibirParcialidad(id: number): Observable<void> {
    return this.http.put<void>(
        `${ADMIN_PARCIALIDAD_API_URL}/${id}/recibir`,
        {}
    );
  }

  rechazarParcialidad(id: number): Observable<void> {
    return this.http.put<void>(
        `${ADMIN_PARCIALIDAD_API_URL}/${id}/rechazar`,
        {}
    );
  } // ✅ <-- La llave de cierre ahora está aquí


  // ✅ Retornamos Observable<any> y pedimos texto plano limpiamente
  liquidarCuenta(idCuenta: number): Observable<any> {
    return this.http.put(`${ADMIN_CUENTA_API_URL}/${idCuenta}/liquidar`, {}, { responseType: 'text' });
  }

  autorizarCuenta(id: number): Observable<CuentaResponse> {
    return this.http.put<CuentaResponse>(`${ADMIN_CUENTA_API_URL}/${id}/autorizar`, {});
  }

  listarSolicitudes(): Observable<any[]> {
    return this.http.get<any[]>(`${ADMIN_CUENTA_API_URL}/solicitudes`);
  }

  generarCuentaDesdeSolicitud(idPesaje: number): Observable<CuentaResponse> {
    return this.http.post<CuentaResponse>(`${ADMIN_CUENTA_API_URL}/solicitudes/${idPesaje}/generar-cuenta`, {});
  }
}