import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PESAJE_API_URL, PARCIALIDAD_AGRICULTOR_API_URL, TRANSPORTE_DISPONIBLES_URL, TRANSPORTISTA_DISPONIBLES_URL } from '../../core/constants/api';
import { PesajeResponse } from './models/pesaje.model';
import { ParcialidadResponseAgricultor, ParcialidadRequestAgricultor } from './models/parcialidad-agricultor.model';
import { TransporteResponse } from '../transportes/models/transporte.model';
import { TransportistaResponse } from '../transportistas/models/transportista.model';

@Injectable({ providedIn: 'root' })
export class PesajeService {
  constructor(private http: HttpClient) {}

  // ✅ Obtener catálogo de medidas (Paso 7.b del CU)
  listarMedidas(): Observable<any[]> {
    return this.http.get<any[]>(`${PESAJE_API_URL}/medidas`);
  }

  // ✅ Crear el pesaje principal (Paso 11 del CU)
  crearPesaje(dto: any): Observable<PesajeResponse> {
    return this.http.post<PesajeResponse>(PESAJE_API_URL, dto);
  }

  listarPesajes(): Observable<PesajeResponse[]> {
    return this.http.get<PesajeResponse[]>(PESAJE_API_URL);
  }

  obtenerPesaje(idPesaje: number): Observable<PesajeResponse> {
    return this.http.get<PesajeResponse>(`${PESAJE_API_URL}/${idPesaje}`);
  }

  listarParcialidades(idCuenta: number): Observable<ParcialidadResponseAgricultor[]> {
    return this.http.get<ParcialidadResponseAgricultor[]>(
      `${PARCIALIDAD_AGRICULTOR_API_URL}/cuenta/${idCuenta}`
    );
  }

  crearParcialidad(dto: ParcialidadRequestAgricultor): Observable<ParcialidadResponseAgricultor> {
    return this.http.post<ParcialidadResponseAgricultor>(PARCIALIDAD_AGRICULTOR_API_URL, dto);
  }

  listarTransportesDisponibles(): Observable<TransporteResponse[]> {
    return this.http.get<TransporteResponse[]>(TRANSPORTE_DISPONIBLES_URL);
  }

  listarTransportistasDisponibles(): Observable<TransportistaResponse[]> {
    return this.http.get<TransportistaResponse[]>(TRANSPORTISTA_DISPONIBLES_URL);
  }

  // para buscar por Pesaje (FA01 de tu Caso de Uso)
  listarParcialidadesPorPesaje(idPesaje: number): Observable<ParcialidadResponseAgricultor[]> {
    return this.http.get<ParcialidadResponseAgricultor[]>(
        `${PARCIALIDAD_AGRICULTOR_API_URL}/pesaje/${idPesaje}`
    );
  }

}
