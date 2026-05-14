import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PESAJE_CABAL_API_URL } from '../constants/api'; // Usamos tu archivo centralizado

@Injectable({ providedIn: 'root' })
export class PesajeCabalService {

    constructor(private http: HttpClient) {}

    listarCuentas(): Observable<any[]> {
        return this.http.get<any[]>(`${PESAJE_CABAL_API_URL}/cuentas`);
    }

    listarParcialidades(idCuenta: number): Observable<any[]> {
        return this.http.get<any[]>(`${PESAJE_CABAL_API_URL}/cuentas/${idCuenta}/parcialidades`);
    }

    actualizarPeso(idParcialidad: number, payload: any): Observable<void> {
        return this.http.put<void>(`${PESAJE_CABAL_API_URL}/parcialidades/${idParcialidad}/peso`, payload);
    }
}