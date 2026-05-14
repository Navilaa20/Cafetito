import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminTransporteService, ActualizarEstadoTransporteRequest } from '../admin-transporte.service';
import { AdminTransporteResponse } from '../models/admin-transporte.model';

const MSG_EXITO_7 = 'Se actualizo con exito.';
const MSG_EXITO_8 = 'El estado del transporte se actualizo con exito';
const MSG_ESTADO_IGUAL = 'El transporte ya se encuentra ';

@Component({
  selector: 'app-cambiar-estado-transporte-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cambiar-estado-transporte-form.component.html',
  styleUrl: './cambiar-estado-transporte-form.component.css',
})
export class CambiarEstadoTransporteFormComponent implements OnChanges {
  @Input() transporte!: AdminTransporteResponse;
  @Output() cerrar = new EventEmitter<void>();
  @Output() actualizado = new EventEmitter<void>();

  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
      private fb: FormBuilder,
      private adminTransporteService: AdminTransporteService,
  ) {
    this.form = this.fb.nonNullable.group({
      placa: [{ value: '', disabled: true }],
      nuevoEstado: [true, Validators.required],
      observaciones: ['', Validators.required],
    });
  }

  // ✅ CORREGIDO: Ahora usa la propiedad 'placa' que viene del JSON
  placaDisplay(t: AdminTransporteResponse): string {
    return t.placa;
  }

  ngOnChanges(): void {
    if (this.transporte) {
      this.form.patchValue({
        placa: this.placaDisplay(this.transporte),
        nuevoEstado: this.transporte.activo,
        observaciones: this.transporte.observaciones ?? '',
      });
      this.successMessage = '';
      this.errorMessage = '';
    }
  }

  get canSubmit(): boolean {
    return this.form.valid && !this.loading;
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (!this.form.valid) return;

    const raw = this.form.getRawValue();
    const dto: ActualizarEstadoTransporteRequest = {
      placa: this.placaDisplay(this.transporte),
      nuevoEstado: raw.nuevoEstado,
      observaciones: raw.observaciones?.trim() ?? '',
    };

    this.loading = true;

    // ✅ CORREGIDO: Cambiado 'idTransporte' por 'id' para coincidir con el modelo
    this.adminTransporteService.actualizarEstado(this.transporte.id, dto).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = MSG_EXITO_7 + ' ' + MSG_EXITO_8;
        setTimeout(() => {
          this.actualizado.emit();
        }, 1200);
      },
      error: (err: { error?: { message?: string; error?: string } }) => {
        this.loading = false;
        const msg = err.error?.message ?? '';
        if (err.error?.error === 'ESTADO_IGUAL') {
          this.errorMessage = msg || MSG_ESTADO_IGUAL + (this.transporte.activo ? 'Activo' : 'Inactivo');
        } else {
          this.errorMessage = msg || 'Error al actualizar el estado.';
        }
      },
    });
  }

  onCancel(): void {
    this.cerrar.emit();
  }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.cerrar.emit();
    }
  }
}