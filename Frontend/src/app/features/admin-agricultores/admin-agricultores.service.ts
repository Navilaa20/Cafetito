import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AgricultorResponseDTO } from "./models/agricultor.model";
import { ADMIN_AGRICULTORES_API_URL } from '../../core/constants/api';

@Injectable({
    providedIn: 'root'
})
export class AdminAgricultoresService {

    constructor(private http: HttpClient) {}

    // CUMPLE FA01: Buscar por NIT o listar todos (Fuerte tipado con tu DTO)
    listar(nit?: string): Observable<AgricultorResponseDTO[]> {
        let params = new HttpParams();
        if (nit && nit.trim() !== '') {
            params = params.set('nit', nit.trim());
        }
        return this.http.get<AgricultorResponseDTO[]>(ADMIN_AGRICULTORES_API_URL, { params });
    }

    // CUMPLE Paso 9: Ver detalle con contadores
    obtenerDetalle(nit: string): Observable<any> {
        return this.http.get<any>(`${ADMIN_AGRICULTORES_API_URL}/${nit}`);
    }
}