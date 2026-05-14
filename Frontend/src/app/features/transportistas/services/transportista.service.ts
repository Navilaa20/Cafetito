import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransportistaRequest, TransportistaResponse } from '../models/transportista.model';
import { TRANSPORTISTA_API_URL } from '../../../core/constants/api';

@Injectable({ providedIn: 'root' })
export class TransportistaService {
  constructor(private http: HttpClient) {}

  crearTransportista(dto: TransportistaRequest): Observable<TransportistaResponse> {
    return this.http.post<TransportistaResponse>(TRANSPORTISTA_API_URL, dto);
  }

  listarTransportistas(): Observable<TransportistaResponse[]> {
    return this.http.get<TransportistaResponse[]>(TRANSPORTISTA_API_URL);
  }
}
