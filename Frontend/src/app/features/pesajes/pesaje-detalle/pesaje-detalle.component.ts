import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PesajeService } from '../pesaje.service';
import { PesajeResponse } from '../models/pesaje.model';
import { ParcialidadResponseAgricultor } from '../models/parcialidad-agricultor.model';
import { NuevaParcialidadFormComponent } from '../nueva-parcialidad-form/nueva-parcialidad-form.component';

const MSG_SIN_REGISTROS = 'No existen registros';

@Component({
  selector: 'app-pesaje-detalle',
  standalone: true,
  imports: [CommonModule, NuevaParcialidadFormComponent],
  templateUrl: './pesaje-detalle.component.html',
  styleUrl: './pesaje-detalle.component.css',
})
export class PesajeDetalleComponent implements OnInit {
  idPesaje: number | null = null;
  pesaje: PesajeResponse | null = null;
  parcialidades: ParcialidadResponseAgricultor[] = [];
  loading = true;
  showNuevaParcialidad = false;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private pesajeService: PesajeService,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('idPesaje');
    if (id) {
      this.idPesaje = +id;
      this.cargarPesaje();
    } else {
      this.loading = false;
    }
  }

  cargarPesaje(): void {
    if (this.idPesaje == null) return;
    this.loading = true;
    this.pesajeService.obtenerPesaje(this.idPesaje).subscribe({
      next: (p) => {
        this.pesaje = p;
        // ✅ Ahora cargamos las parcialidades SIEMPRE, usando el idPesaje
        this.cargarParcialidadesPorPesaje();
      },
      error: () => {
        this.pesaje = null;
        this.parcialidades = [];
        this.loading = false;
      },
    });
  }

  // ✅ Modificado para buscar por idPesaje (como lo configuramos en el backend)
  cargarParcialidadesPorPesaje(): void {
    if (this.idPesaje == null) return;
    this.pesajeService.listarParcialidadesPorPesaje(this.idPesaje).subscribe({
      next: (list) => {
        this.parcialidades = list;
        this.loading = false;
      },
      error: () => {
        this.parcialidades = [];
        this.loading = false;
      },
    });
  }

  regresar(): void {
    this.router.navigate(['/dashboard/agricultor', 'pesajes']);
  }

  abrirNuevaParcialidad(): void {
    this.showNuevaParcialidad = true;
  }

  cerrarNuevaParcialidad(): void {
    this.showNuevaParcialidad = false;
  }

  onParcialidadCreada(): void {
    this.showNuevaParcialidad = false;
    this.cargarParcialidadesPorPesaje(); // ✅ Refrescamos la tabla
  }

  get mensajeVacio(): string {
    return MSG_SIN_REGISTROS;
  }

  formatFecha(fecha: string | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    return isNaN(d.getTime()) ? '-' : d.toLocaleDateString('es-GT', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}