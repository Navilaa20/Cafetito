import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { PesajeCabalService } from '../../../core/services/pesaje-cabal.service';

@Component({
  selector: 'app-dashboard-pesaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-pesaje.component.html',
  styleUrl: './dashboard-pesaje.component.css',
})
export class DashboardPesajeComponent implements OnInit {
  vistaActual: 'BANDEJA' | 'DETALLE' = 'BANDEJA';

  cuentas: any[] = [];
  cuentaSeleccionada: any = null;
  parcialidades: any[] = [];

  mostrarModalPeso = false;
  parcialidadSeleccionada: any = null;
  pesoObtenido: number | null = null;
  medidaPeso: string = 'kg';
  observaciones: string = '';

  mostrarModalBoleta = false;
  datosBoleta: any = null;

  constructor(
      private auth: AuthService,
      private pesajeService: PesajeCabalService
  ) {}

  get username(): string {
    return this.auth.displayUsername('Pesaje');
  }

  ngOnInit(): void {
    this.cargarCuentasActivas();
  }

  logout(): void {
    this.auth.logout();
  }

  // --- MÉTODOS DE BANDEJA ---
  cargarCuentasActivas(): void {
    this.pesajeService.listarCuentas().subscribe({
      // ✅ 2. Se agrega ': any' para cumplir con las reglas estrictas
      next: (data: any) => {
        this.cuentas = data;
      },
      error: (err: any) => console.error('Error al cargar cuentas', err)
    });
  }

  verDetalle(cuenta: any): void {
    this.cuentaSeleccionada = cuenta;
    this.vistaActual = 'DETALLE';
    this.cargarParcialidades(cuenta.idCuenta);
  }

  regresar(): void {
    this.vistaActual = 'BANDEJA';
    this.cuentaSeleccionada = null;
    this.parcialidades = [];
    this.cargarCuentasActivas();
  }

  // --- MÉTODOS DE DETALLE ---
  cargarParcialidades(idCuenta: number): void {
    this.pesajeService.listarParcialidades(idCuenta).subscribe({
      // ✅ Se agrega ': any'
      next: (data: any) => {
        this.parcialidades = data;
      },
      error: (err: any) => console.error('Error al cargar parcialidades', err)
    });
  }

  abrirModalPeso(parcialidad: any): void {
    this.parcialidadSeleccionada = parcialidad;
    this.pesoObtenido = null;
    this.observaciones = '';
    this.mostrarModalPeso = true;
  }

  cerrarModalPeso(): void {
    this.mostrarModalPeso = false;
    this.parcialidadSeleccionada = null;
  }

  // --- ACTUALIZAR PESO ---
  actualizarPeso(): void {
    if (!this.pesoObtenido || this.pesoObtenido <= 0) {
      alert('Debe ingresar un peso válido.');
      return;
    }

    const payload = {
      pesoObtenido: this.pesoObtenido,
      medidaPeso: this.medidaPeso,
      observaciones: this.observaciones
    };

    this.pesajeService.actualizarPeso(this.parcialidadSeleccionada.idParcialidad, payload)
        .subscribe({
          // En un request vacío (Void), no recibimos 'data', por lo que los paréntesis van vacíos
          next: () => {
            alert('Pesaje registrado con éxito');
            this.mostrarModalPeso = false;
            this.cargarParcialidades(this.cuentaSeleccionada.idCuenta);
          },
          // ✅ Se agrega ': any'
          error: (err: any) => {
            console.error('Error al guardar peso', err);
            alert('Hubo un error al registrar el pesaje');
          }
        });
  }

  generarBoleta(parcialidad: any): void {
    // FA03: Recopilamos la información para la boleta
    this.datosBoleta = {
      fechaBoleta: new Date().toLocaleString(),
      usuario: this.username,
      cuenta: this.cuentaSeleccionada.idCuenta,
      // Usamos el ID de cuenta como fallback si el backend aún no envía el idPesaje
      idPesaje: this.cuentaSeleccionada.idCuenta,
      idParcialidad: parcialidad.idParcialidad,
      placa: parcialidad.placa,
      cui: parcialidad.cuiTransportista,
      medida: parcialidad.tipoMedida,
      peso: parcialidad.pesoBascula,
      fechaPesaje: parcialidad.fechaPeso ? new Date(parcialidad.fechaPeso).toLocaleString() : '---'
    };
    this.mostrarModalBoleta = true;
  }

  cerrarBoleta(): void {
    this.mostrarModalBoleta = false;
    this.datosBoleta = null;
  }

  imprimirBoleta(): void {
    // Llama a la ventana de impresión nativa del navegador (Ctrl+P)
    window.print();
  }
}