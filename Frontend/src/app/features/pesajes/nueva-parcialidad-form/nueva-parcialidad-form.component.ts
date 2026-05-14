import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PesajeService } from '../pesaje.service';
import { TransporteResponse } from '../../transportes/models/transporte.model';
import { TransportistaResponse } from '../../transportistas/models/transportista.model';

const MSG_EXITO_1 = 'Se creo con exito';
const MSG_EXITO_2 = 'La parcialidad se ha creado con exito';
const MSG_ESTADO_CUENTA = "La cuenta se encuentra en estado: '{{estado}}' no es posible agregar mas parcialidades";
const MSG_SIN_TRANSPORTES = 'No existen transportes registrados';
const MSG_SIN_TRANSPORTISTAS = 'No existen transportistas registrados';
const PESO_MIN = 1;

@Component({
  selector: 'app-nueva-parcialidad-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './nueva-parcialidad-form.component.html',
  styleUrl: './nueva-parcialidad-form.component.css',
})
export class NuevaParcialidadFormComponent implements OnInit {
  // ✅ Cambiamos idCuenta por idPesaje para que coincida con el Backend
  @Input() idPesaje!: number;
  @Input() medidaDePeso = 'kg';

  @Output() cerrar = new EventEmitter<void>();
  @Output() creada = new EventEmitter<void>();

  form: FormGroup;
  transportes: TransporteResponse[] = [];
  transportistas: TransportistaResponse[] = [];
  loadingTransportes = true;
  loadingTransportistas = true;
  loadingSubmit = false;
  errorMessage = '';
  successMessage = '';

  constructor(
      private fb: FormBuilder,
      private pesajeService: PesajeService,
  ) {
    this.form = this.fb.nonNullable.group({
      peso: [null as number | null, [Validators.required, Validators.min(PESO_MIN)]],
      idTransporte: ['', Validators.required],
      idTransportista: [null as number | null, Validators.required],
      tipoDeMedida: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.pesajeService.listarTransportesDisponibles().subscribe({
      next: (data) => {
        this.transportes = data;
        this.loadingTransportes = false;
      },
      error: () => {
        this.transportes = [];
        this.loadingTransportes = false;
      },
    });
    this.pesajeService.listarTransportistasDisponibles().subscribe({
      next: (data) => {
        this.transportistas = data;
        this.loadingTransportistas = false;
      },
      error: () => {
        this.transportistas = [];
        this.loadingTransportistas = false;
      },
    });
  }

  get canSubmit(): boolean {
    return this.form.valid && !this.loadingSubmit;
  }

  get pesoLabel(): string {
    return `Peso (${this.medidaDePeso})`;
  }

  get pesoError(): string | null {
    const c = this.form.get('peso');
    if (!c?.errors || !c.touched) return null;
    if (c.errors['min']) return 'valor minimo 1';
    if (c.errors['required']) return 'El peso es obligatorio';
    return null;
  }

  get mensajeTransportesVacio(): string {
    return MSG_SIN_TRANSPORTES;
  }

  get mensajeTransportistasVacio(): string {
    return MSG_SIN_TRANSPORTISTAS;
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.form.valid) return;

    const raw = this.form.getRawValue();

    // ✅ Construimos el DTO con idPesaje. Se tipa como 'any' temporalmente
    // por si tu interfaz ParcialidadRequestAgricultor aún tiene 'idCuenta' adentro.
    const dto: any = {
      idPesaje: this.idPesaje,
      peso: Number(raw.peso),
      idTransporte: String(raw.idTransporte),
      idTransportista: Number(raw.idTransportista),
      tipoDeMedida: String(raw.tipoDeMedida).trim(),
    };

    this.loadingSubmit = true;

    this.pesajeService.crearParcialidad(dto).subscribe({
      next: () => {
        this.loadingSubmit = false;
        this.successMessage = MSG_EXITO_1 + '. ' + MSG_EXITO_2;
      },
      error: (err: any) => {
        this.loadingSubmit = false;

        // ✅ Capturamos el error 400 específico (err.error?.idPesaje) que te mandaba el Backend
        const msg = err.error?.message || err.error?.idPesaje || '';

        if (err.error?.error === 'ESTADO_CUENTA') {
          this.errorMessage = msg || MSG_ESTADO_CUENTA.replace('{{estado}}', '');
        } else {
          this.errorMessage = msg || 'Error al crear la parcialidad.';
        }
      },
    });
  }

  onCerrar(): void {
    if (this.successMessage) {
      this.creada.emit();
    }
    this.cerrar.emit();
  }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.onCerrar();
    }
  }
}