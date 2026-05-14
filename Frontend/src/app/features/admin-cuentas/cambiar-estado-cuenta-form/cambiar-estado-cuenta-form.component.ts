import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminCuentaService } from '../admin-cuenta.service';
import {
  CuentaDetalleResponse,
  EstadoCuenta,
  CambiarEstadoCuentaRequest,
} from '../models/cuenta.model';

const MSG_EXITO_7 = 'Se actualizo con exito.';
const MSG_EXITO_10 = 'El cambio de estado se realizo con exito';

@Component({
  selector: 'app-cambiar-estado-cuenta-form',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cambiar-estado-cuenta-form.component.html',
  styleUrl: './cambiar-estado-cuenta-form.component.css',
})
export class CambiarEstadoCuentaFormComponent {
  @Input() cuenta!: CuentaDetalleResponse;
  @Output() cerrar = new EventEmitter<void>();
  @Output() actualizado = new EventEmitter<void>();

  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(private adminCuentaService: AdminCuentaService) {}

  get estadoActualLabel(): string {
    if (!this.cuenta) return '';
    return this.estadoLabel(this.cuenta.estadoCuenta);
  }

  get estadoNuevoLabel(): string {
    if (!this.cuenta) return '';
    if (this.cuenta.estadoCuenta === 'PESAJE_FINALIZADO') return 'Cuenta Cerrada';
    if (this.cuenta.estadoCuenta === 'CUENTA_CERRADA') return 'Cuenta Confirmada';
    return '';
  }

  estadoLabel(estado: EstadoCuenta): string {
    const labels: Record<EstadoCuenta, string> = {
      CUENTA_CREADA: 'Cuenta Creada',
      CUENTA_ABIERTA: 'Cuenta Abierta',
      PESAJE_INICIADO: 'Pesaje Iniciado',
      PESAJE_FINALIZADO: 'Pesaje Finalizado',
      CUENTA_CERRADA: 'Cuenta Cerrada',
      CUENTA_CONFIRMADA: 'Cuenta Confirmada',
    };
    return labels[estado] ?? estado;
  }

  get estadoNuevoValor(): EstadoCuenta {
    if (!this.cuenta) return 'CUENTA_CERRADA';
    if (this.cuenta.estadoCuenta === 'PESAJE_FINALIZADO') return 'CUENTA_CERRADA';
    if (this.cuenta.estadoCuenta === 'CUENTA_CERRADA') return 'CUENTA_CONFIRMADA';
    return 'CUENTA_CERRADA';
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (!this.cuenta) return;
    const dto: CambiarEstadoCuentaRequest = {
      idCuenta: this.cuenta.idCuenta,
      estadoActual: this.cuenta.estadoCuenta,
      estadoNuevo: this.estadoNuevoValor,
    };
    this.loading = true;
    this.adminCuentaService.cambiarEstado(this.cuenta.idCuenta, dto).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = MSG_EXITO_7 + ' ' + MSG_EXITO_10;
      },
      error: (err: { error?: { message?: string; error?: string } }) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message ?? 'Error al cambiar el estado de la cuenta.';
      },
    });
  }

  onOk(): void {
    this.actualizado.emit();
  }

  onCancel(): void {
    this.cerrar.emit();
  }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.cerrar.emit();
    }
  }
}
