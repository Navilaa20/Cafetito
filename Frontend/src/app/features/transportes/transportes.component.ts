import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransporteService } from './services/transporte.service';
import { TransporteFormComponent } from './transporte-form/transporte-form.component';

@Component({
  selector: 'app-transportes',
  standalone: true,
  imports: [CommonModule, TransporteFormComponent],
  templateUrl: './transportes.component.html',
  styleUrl: './transportes.component.css'
})
export class TransportesComponent implements OnInit {

  transportes: any[] = [];

  // Catálogos
  marcas: any[] = [];
  colores: any[] = [];
  lineas: any[] = [];

  // ✅ Variables que pedía el HTML
  showForm = false;
  loading = false;
  successMessage = '';

  constructor(private transporteService: TransporteService) {}

  ngOnInit(): void {
    this.cargarCatalogos();
    this.cargarTransportes();
  }

  cargarCatalogos(): void {
    // ✅ Se añade (data: any[]) para solucionar el error de TypeScript estricto
    this.transporteService.getMarcas().subscribe((data: any[]) => this.marcas = data);
    this.transporteService.getColores().subscribe((data: any[]) => this.colores = data);
    this.transporteService.getLinea().subscribe((data: any[]) => this.lineas = data);
  }

  cargarTransportes(): void {
    this.loading = true; // Mostramos el spinner mientras carga
    this.transporteService.listarTransportes().subscribe({
      next: (data: any[]) => { // ✅ Especificamos el tipo explícito aquí también
        this.transportes = data;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error al cargar transportes', err);
        this.loading = false;
      }
    });
  }

  getNombreMarca(id: number): string {
    const marca = this.marcas.find(m => m.id == id);
    return marca ? (marca.nombre || marca.descripcion) : (id ? id.toString() : 'N/A');
  }

  getNombreColor(id: number): string {
    const color = this.colores.find(c => c.id == id);
    return color ? (color.nombre || color.descripcion) : (id ? id.toString() : 'N/A');
  }

  getNombreLinea(id: number): string {
    const linea = this.lineas.find(l => l.id == id);
    return linea ? (linea.nombre || linea.descripcion) : (id ? id.toString() : 'N/A');
  }

  abrirFormulario(): void {
    this.showForm = true; // ✅ Cambiado para coincidir con el HTML
  }

  cerrarFormulario(): void {
    this.showForm = false;
  }

  onTransporteCreado(): void {
    this.cerrarFormulario();
    this.successMessage = '¡Transporte registrado con éxito!';
    this.cargarTransportes();

    // Opcional: Ocultar el mensaje de éxito después de 3 segundos
    setTimeout(() => {
      this.successMessage = '';
    }, 3000);
  }
}