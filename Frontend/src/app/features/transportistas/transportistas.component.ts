import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransportistaService } from './services/transportista.service';
import { TransportistaResponse } from './models/transportista.model';
import { TransportistaFormComponent } from './transportista-form/transportista-form.component';

const MSG_EXITO_CREAR = 'El transportista se ha creado con exito';

@Component({
  selector: 'app-transportistas',
  standalone: true,
  imports: [CommonModule, TransportistaFormComponent],
  templateUrl: './transportistas.component.html',
  styleUrl: './transportistas.component.css',
})
export class TransportistasComponent implements OnInit {
  transportistas: TransportistaResponse[] = [];
  loading = true;
  showForm = false;
  successMessage = '';

  constructor(private transportistaService: TransportistaService) {}

  ngOnInit(): void {
    this.cargarTransportistas();
  }

  cargarTransportistas(): void {
    this.loading = true;
    this.transportistaService.listarTransportistas().subscribe({
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

  abrirFormulario(): void {
    this.successMessage = '';
    this.showForm = true;
  }

  cerrarFormulario(): void {
    this.showForm = false;
  }

  onTransportistaCreado(): void {
    this.showForm = false;
    this.successMessage = MSG_EXITO_CREAR;
    this.cargarTransportistas();
    setTimeout(() => (this.successMessage = ''), 5000);
  }
}
