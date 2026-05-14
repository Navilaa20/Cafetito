import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminTransporteService } from './admin-transporte.service';
import { AdminTransporteResponse } from './models/admin-transporte.model';
import { CambiarEstadoTransporteFormComponent } from './cambiar-estado-transporte-form/cambiar-estado-transporte-form.component';

type FiltroEstado = '' | 'activo' | 'inactivo';

@Component({
  selector: 'app-admin-transportes',
  standalone: true,
  imports: [CommonModule, FormsModule, CambiarEstadoTransporteFormComponent],
  templateUrl: './admin-transportes.component.html',
  styleUrl: './admin-transportes.component.css',
})
export class AdminTransportesComponent implements OnInit {
  transportes: AdminTransporteResponse[] = [];
  transportistas: AdminTransporteResponse[] = [];
  loading = true;
  filterPlaca = '';
  filterEstado: FiltroEstado = '';
  showCambiarEstadoModal = false;
  transporteSeleccionado: AdminTransporteResponse | null = null;

  constructor(private adminTransporteService: AdminTransporteService) {}

  ngOnInit(): void {
    this.cargarTodos();
  }

  placaDisplay(t: AdminTransporteResponse): string {
    return t.placa;
  }

  cargarTodos(): void {
    this.loading = true;
    this.adminTransporteService.listarTodos().subscribe({
      next: (data) => {
        this.transportes = data;
        this.loading = false;
      },
      error: () => {
        this.transportes = [];
        this.loading = false;
      },
    });
  }

  get puedeBuscarPlaca(): boolean {
    return this.filterPlaca.trim().length > 0;
  }

  buscarPorPlaca(): void {
    if (!this.puedeBuscarPlaca) return;
    this.filterEstado = '';
    this.loading = true;
    this.adminTransporteService.buscarPorPlaca(this.filterPlaca.trim()).subscribe({
      next: (data) => {
        this.transportes = data;
        this.loading = false;
      },
      error: () => {
        this.transportes = [];
        this.loading = false;
      },
    });
  }

  filtrarPorEstado(): void {
    if (this.filterEstado === '') {
      this.cargarTodos();
      return;
    }

    this.filterPlaca = ''; // Limpiamos el otro filtro
    this.loading = true;
    const activo = this.filterEstado === 'activo';

    this.adminTransporteService.filtrarPorEstado(activo).subscribe({
      next: (data) => {
        // Usamos el operador spread [...] para asegurar que Angular detecte el cambio de datos
        this.transportes = [...data];
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al filtrar:', err);
        this.transportes = [];
        this.loading = false;
      },
    });
  }

  limpiarFiltros(): void {
    this.filterPlaca = '';
    this.filterEstado = '';
    this.cargarTodos();
  }

  abrirCambiarEstado(t: AdminTransporteResponse): void {
    this.transporteSeleccionado = t;
    this.showCambiarEstadoModal = true;
  }

  cerrarCambiarEstado(): void {
    this.showCambiarEstadoModal = false;
    this.transporteSeleccionado = null;
  }

  onEstadoActualizado(): void {
    this.cerrarCambiarEstado();
    this.cargarTodos();
  }
}
