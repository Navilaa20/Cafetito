import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminTransportistaService } from './admin-transportista.service';
import { AdminTransportistaResponse } from './models/admin-transportista.model';
import { CambiarEstadoTransportistaFormComponent } from './cambiar-estado-transportista-form/cambiar-estado-transportista-form.component';

type FiltroEstado = '' | 'activo' | 'inactivo';

@Component({
  selector: 'app-admin-transportistas',
  standalone: true,
  imports: [CommonModule, FormsModule, CambiarEstadoTransportistaFormComponent],
  templateUrl: './admin-transportistas.component.html',
  styleUrl: './admin-transportistas.component.css',
})
export class AdminTransportistasComponent implements OnInit {
  transportistas: AdminTransportistaResponse[] = [];
  loading = true;
  filterCui = '';
  filterEstado: FiltroEstado = '';
  showCambiarEstadoModal = false;
  transportistaSeleccionado: AdminTransportistaResponse | null = null;

  constructor(private adminTransportistaService: AdminTransportistaService) {}

  ngOnInit(): void {
    this.cargarTodos();
  }

  cargarTodos(): void {
    this.loading = true;
    this.adminTransportistaService.listarTodos().subscribe({
      next: (data) => {
        this.transportistas = data;
        this.loading = false;
      },
      error: () => {
        this.transportistas = [];
        this.loading = false;
      },
    });
  }

  get puedeBuscarCui(): boolean {
    return this.filterCui.trim().length > 0;
  }

  buscarPorCui(): void {
    if (!this.puedeBuscarCui) return;
    this.filterEstado = '';
    this.loading = true;
    this.adminTransportistaService.buscarPorCui(this.filterCui.trim()).subscribe({
      next: (data) => {
        this.transportistas = data;
        this.loading = false;
      },
      error: () => {
        this.transportistas = [];
        this.loading = false;
      },
    });
  }

  filtrarPorEstado(): void {
    if (this.filterEstado === '') {
      this.cargarTodos();
      return;
    }
    this.filterCui = '';
    this.loading = true;
    const activo = this.filterEstado === 'activo';
    this.adminTransportistaService.filtrarPorEstado(activo).subscribe({
      next: (data) => {
        this.transportistas = data;
        this.loading = false;
      },
      error: () => {
        this.transportistas = [];
        this.loading = false;
      },
    });
  }

  limpiarFiltros(): void {
    this.filterCui = '';
    this.filterEstado = '';
    this.cargarTodos();
  }

  abrirCambiarEstado(t: AdminTransportistaResponse): void {
    this.transportistaSeleccionado = t;
    this.showCambiarEstadoModal = true;
  }

  cerrarCambiarEstado(): void {
    this.showCambiarEstadoModal = false;
    this.transportistaSeleccionado = null;
  }

  onEstadoActualizado(): void {
    this.cerrarCambiarEstado();
    this.cargarTodos();
  }
}
