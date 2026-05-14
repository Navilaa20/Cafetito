import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminCuentaService } from '../admin-cuenta.service';
import { CuentaDetalleResponse, EstadoCuenta } from '../models/cuenta.model';
import { CambiarEstadoCuentaFormComponent } from '../cambiar-estado-cuenta-form/cambiar-estado-cuenta-form.component';

const ESTADO_LABELS: Record<EstadoCuenta, string> = {
  CUENTA_CREADA: 'Cuenta Creada',
  CUENTA_ABIERTA: 'Cuenta Abierta',
  PESAJE_INICIADO: 'Pesaje Iniciado',
  PESAJE_FINALIZADO: 'Pesaje Finalizado',
  CUENTA_CERRADA: 'Cuenta Cerrada',
  CUENTA_CONFIRMADA: 'Cuenta Confirmada',
};

@Component({
  selector: 'app-cuenta-detalle',
  standalone: true,
  imports: [CommonModule, CambiarEstadoCuentaFormComponent],
  templateUrl: './cuenta-detalle.component.html',
  styleUrl: './cuenta-detalle.component.css',
})
export class CuentaDetalleComponent implements OnInit {
  detalle: CuentaDetalleResponse | null = null;
  loading = true;
  showCambiarEstadoModal = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private adminCuentaService: AdminCuentaService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarDetalle(Number(id));
    }
  }

  get idCuenta(): number {
    return this.detalle?.idCuenta ?? 0;
  }

  estadoLabel(estado: EstadoCuenta): string {
    return ESTADO_LABELS[estado] ?? estado;
  }

  get puedeCambiarEstado(): boolean {
    if (!this.detalle) return false;
    const e = this.detalle.estadoCuenta;
    return e === 'PESAJE_FINALIZADO' || e === 'CUENTA_CERRADA';
  }

  cargarDetalle(id: number): void {
    this.loading = true;
    this.adminCuentaService.obtenerDetalle(id).subscribe({
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

  regresar(): void {
    this.router.navigate(['/dashboard/administrador/cuentas']);
  }

  verDetalleParcialidad(idParcialidad: number): void {
    this.router.navigate([
      '/dashboard/administrador/cuentas',
      this.idCuenta,
      'parcialidades',
      idParcialidad,
    ]);
  }

  abrirCambiarEstado(): void {
    this.showCambiarEstadoModal = true;
  }

  cerrarCambiarEstado(): void {
    this.showCambiarEstadoModal = false;
  }

  onEstadoActualizado(): void {
    this.cerrarCambiarEstado();
    this.cargarDetalle(this.idCuenta);
  }

  mostrarBotonVerDetalleParcialidad(aceptado: boolean | null): boolean {
    return aceptado === null;
  }

  liquidarCuenta(idCuenta: number): void {
    const confirmar = confirm('¿Estás seguro de liquidar esta cuenta? Se calculará la tolerancia (5%) y se cerrará definitivamente.');

    if (confirmar) {
      this.adminCuentaService.liquidarCuenta(idCuenta).subscribe({
        next: () => {
          alert('¡Cuenta liquidada con éxito! Revisa los resultados de tolerancia.');

          // ✅ DESCOMENTADO: Esto refresca la pantalla instantáneamente
          this.cargarDetalle(idCuenta);
        },
        error: (err: any) => {
          console.error('Error al liquidar la cuenta', err);
          alert('Hubo un error al calcular la liquidación. Revisa la consola.');
        }
      });
    }
  }

}
