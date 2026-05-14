import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminCuentaService } from './admin-cuenta.service';
import {
  CuentaResponse,
  EstadoCuenta,
} from './models/cuenta.model';

const ESTADO_LABELS: Record<EstadoCuenta, string> = {
  CUENTA_CREADA: 'Cuenta Creada',
  CUENTA_ABIERTA: 'Cuenta Abierta',
  PESAJE_INICIADO: 'Pesaje Iniciado',
  PESAJE_FINALIZADO: 'Pesaje Finalizado',
  CUENTA_CERRADA: 'Cuenta Cerrada',
  CUENTA_CONFIRMADA: 'Cuenta Confirmada',
};

@Component({
  selector: 'app-admin-cuentas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-cuentas.component.html',
  styleUrl: './admin-cuentas.component.css',
})
export class AdminCuentasComponent implements OnInit {
  // Datos principales
  cuentas: CuentaResponse[] = [];
  loading = true;

  // Filtros
  filterFecha = '';
  filterNumeroCuenta = '';
  filterEstado: EstadoCuenta | '' = '';

  // Solicitudes Pendientes (Flujo Beneficio)
  solicitudes: any[] = [];
  showSolicitudesModal = false;

  constructor(
      private adminCuentaService: AdminCuentaService,
      private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarTodas();
    this.cargarSolicitudes(); // Cargamos el contador de solicitudes al iniciar
  }

  // --- LÓGICA DE SOLICITUDES (PESAJES PENDIENTES) ---

  abrirSolicitudes(): void {
    this.showSolicitudesModal = true;
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    // Usamos el servicio de admin para traer los pesajes en estado 'PENDIENTE'
    this.adminCuentaService.listarSolicitudes().subscribe({
      next: (data) => {
        this.solicitudes = data ?? [];
      },
      error: () => {
        this.solicitudes = [];
      }
    });
  }

  confirmarGenerarCuenta(idPesaje: number): void {
    if (confirm('¿Desea generar una cuenta oficial para esta solicitud de pesaje?')) {
      this.loading = true;
      this.adminCuentaService.generarCuentaDesdeSolicitud(idPesaje).subscribe({
        next: () => {
          alert('Cuenta generada con éxito. Ahora aparece en la tabla principal.');
          this.cargarSolicitudes(); // Refrescar modal
          this.cargarTodas();       // Refrescar tabla fondo
          if (this.solicitudes.length === 0) this.showSolicitudesModal = false;
        },
        error: (err) => {
          this.loading = false;
          alert(err.error?.message || 'Error al generar la cuenta');
        }
      });
    }
  }

  // --- LÓGICA DE CUENTAS EXISTENTES ---

  cargarTodas(): void {
    this.loading = true;
    this.adminCuentaService.listarTodas().subscribe({
      next: (data) => {
        this.cuentas = Array.isArray(data) ? data : [data];
        this.loading = false;
      },
      error: () => {
        this.cuentas = [];
        this.loading = false;
      },
    });
  }

  estadoLabel(estado: EstadoCuenta): string {
    return ESTADO_LABELS[estado] ?? estado;
  }

  get puedeBuscar(): boolean {
    return (
        this.filterFecha.trim().length > 0 ||
        (this.filterNumeroCuenta.trim().length > 0 &&
            !isNaN(Number(this.filterNumeroCuenta.trim())))
    );
  }

  buscar(): void {
    if (this.filterNumeroCuenta.trim() && !isNaN(Number(this.filterNumeroCuenta.trim()))) {
      this.filterFecha = '';
      this.filterEstado = '';
      this.loading = true;
      this.adminCuentaService
          .buscarPorNumero(Number(this.filterNumeroCuenta.trim()))
          .subscribe({
            next: (data) => {
              this.cuentas = data ? [data] : [];
              this.loading = false;
            },
            error: () => {
              this.cuentas = [];
              this.loading = false;
            },
          });
      return;
    }

    if (this.filterFecha.trim().length > 0) {
      this.filterNumeroCuenta = '';
      this.filterEstado = '';
      this.loading = true;
      this.adminCuentaService.buscarPorFecha(this.filterFecha.trim()).subscribe({
        next: (data) => {
          this.cuentas = data ?? [];
          this.loading = false;
        },
        error: () => {
          this.cuentas = [];
          this.loading = false;
        },
      });
    }
  }

  filtrarPorEstado(): void {
    if (this.filterEstado === '') {
      this.cargarTodas();
      return;
    }
    this.filterFecha = '';
    this.filterNumeroCuenta = '';
    this.loading = true;
    this.adminCuentaService.filtrarPorEstado(this.filterEstado as EstadoCuenta).subscribe({
      next: (data) => {
        this.cuentas = data ?? [];
        this.loading = false;
      },
      error: () => {
        this.cuentas = [];
        this.loading = false;
      },
    });
  }

  limpiarFiltros(): void {
    this.filterFecha = '';
    this.filterNumeroCuenta = '';
    this.filterEstado = '';
    this.cargarTodas();
  }

  verDetalle(id: number): void {
    // Asegúrate de que la ruta coincida con tu app-routing.module.ts
    this.router.navigate(['/dashboard/administrador/cuentas', id]);
  }

  autorizar(id: number): void {
    if (confirm('¿Desea autorizar y abrir esta cuenta oficialmente?')) {
      this.loading = true;
      this.adminCuentaService.autorizarCuenta(id).subscribe({
        next: () => {
          this.cargarTodas();
          alert('Cuenta autorizada con éxito.');
        },
        error: (err) => {
          this.loading = false;
          alert(err.error?.message || 'Error al autorizar la cuenta');
        }
      });
    }
  }

}