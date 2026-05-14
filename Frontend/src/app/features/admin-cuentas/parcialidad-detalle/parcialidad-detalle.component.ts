import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminCuentaService } from '../admin-cuenta.service';
import { ParcialidadDetalleResponse } from '../models/parcialidad.model';

const MSG_EXITO_7 = 'Se actualizo con exito.';
const MSG_EXITO_11 = 'Se confirma la recepcion de la parcialidad';
const MSG_EXITO_12 = 'Se rechaza la parcialidad';

@Component({
  selector: 'app-parcialidad-detalle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './parcialidad-detalle.component.html',
  styleUrl: './parcialidad-detalle.component.css',
})
export class ParcialidadDetalleComponent implements OnInit {
  detalle: ParcialidadDetalleResponse | null = null;
  loading = true;
  showSegmentos = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private adminCuentaService: AdminCuentaService
  ) {}

  ngOnInit(): void {
    const idParcialidad = this.route.snapshot.paramMap.get('idParcialidad');
    if (idParcialidad) {
      this.cargarDetalle(Number(idParcialidad));
    }
  }

  get idCuenta(): number {
    return Number(this.route.snapshot.paramMap.get('id')) || 0;
  }

  get puedeRecibirRechazar(): boolean {
    return this.detalle?.aceptado === null;
  }

  cargarDetalle(idParcialidad: number): void {
    this.loading = true;
    this.successMessage = '';
    this.errorMessage = '';
    this.adminCuentaService.obtenerDetalleParcialidad(idParcialidad).subscribe({
      next: (data) => {
        this.detalle = data;
        this.loading = false;
      },
      error: () => {
        this.detalle = null;
        this.loading = false;
      },
    });
  }

  ir(): void {
    this.showSegmentos = true;
  }

  regresar(): void {
    this.router.navigate(['/dashboard/administrador/cuentas', this.idCuenta]);
  }

  recibir(): void {
    if (!this.detalle || this.detalle.aceptado !== null) return;
    this.errorMessage = '';
    this.adminCuentaService.recibirParcialidad(this.detalle.idParcialidad).subscribe({
      next: () => {
        this.successMessage = MSG_EXITO_7 + ' ' + MSG_EXITO_11;
        this.cargarDetalle(this.detalle!.idParcialidad);
      },
      error: (err: { error?: { message?: string; error?: string } }) => {
        this.errorMessage = err.error?.message ?? 'Error al recibir la parcialidad.';
      },
    });
  }

  rechazar(): void {
    if (!this.detalle || this.detalle.aceptado !== null) return;
    this.errorMessage = '';
    this.adminCuentaService.rechazarParcialidad(this.detalle.idParcialidad).subscribe({
      next: () => {
        this.successMessage = MSG_EXITO_7 + ' ' + MSG_EXITO_12;
        this.cargarDetalle(this.detalle!.idParcialidad);
      },
      error: (err: { error?: { message?: string } }) => {
        this.errorMessage = err.error?.message ?? 'Error al rechazar la parcialidad.';
      },
    });
  }

  get qrImageSrc(): string {
    if (!this.detalle?.qrCode) return '';
    return 'data:image/png;base64,' + this.detalle.qrCode;
  }
}
