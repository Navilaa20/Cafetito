import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PesajeService } from './pesaje.service';
import { PesajeResponse } from './models/pesaje.model';
import { NuevoPesajeFormComponent } from './nuevo-pesaje-form/nuevo-pesaje-form.component';

@Component({
  selector: 'app-pesajes',
  standalone: true,
  imports: [CommonModule, NuevoPesajeFormComponent],
  templateUrl: './pesajes.component.html',
  styleUrl: './pesajes.component.css',
})
export class PesajesComponent implements OnInit {
  pesajes: PesajeResponse[] = [];
  loading = true;
  showForm = false;

  constructor(private pesajeService: PesajeService, private router: Router) {}

  ngOnInit(): void {
    this.cargarPesajes();
  }

  cargarPesajes(): void {
    this.loading = true;
    this.pesajeService.listarPesajes().subscribe({
      next: (data) => {
        this.pesajes = data;
        this.loading = false;
      },
      error: () => {
        this.pesajes = [];
        this.loading = false;
      },
    });
  }

  abrirFormulario() { this.showForm = true; }
  cerrarFormulario() { this.showForm = false; }

  onPesajeCreado(): void {
    this.cerrarFormulario();
    // Agregamos el mensaje para cumplir con los pasos 12 y 13
    alert('El pesaje se ha creado con éxito.');
    this.cargarPesajes();
  }

  verDetalle(idPesaje: number): void {
    this.router.navigate(['/dashboard/agricultor', 'pesajes', idPesaje]);
  }

  get mensajeVacio(): string { return 'No existen registros'; }

  formatFecha(fecha: string | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    return isNaN(d.getTime()) ? '-' : d.toLocaleDateString('es-GT', {
      day: '2-digit', month: '2-digit', year: 'numeric',
    });
  }


}