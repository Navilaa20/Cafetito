import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminTransportistaService, ActualizarEstadoTransportistaRequest } from '../admin-transportista.service';
import { AdminTransportistaResponse } from '../models/admin-transportista.model';

const MSG_EXITO_7 = 'Se actualizo con exito.';
const MSG_EXITO_9 = 'El estado del transportista se actualizo con exito';
const MSG_ESTADO_IGUAL = 'El transportista ya se encuentra ';

@Component({
  selector: 'app-cambiar-estado-transportista-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cambiar-estado-transportista-form.component.html',
  styleUrl: './cambiar-estado-transportista-form.component.css',
})
export class CambiarEstadoTransportistaFormComponent implements OnChanges {
  @Input() transportista!: AdminTransportistaResponse;
  @Output() cerrar = new EventEmitter<void>();
  @Output() actualizado = new EventEmitter<void>();

  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private adminTransportistaService: AdminTransportistaService,
  ) {
    this.form = this.fb.nonNullable.group({
      cui: [{ value: '', disabled: true }],
      nuevoEstado: [true, Validators.required],
      observaciones: ['', Validators.required],
    });
  }

  ngOnChanges(): void {
    if (this.transportista) {
      this.form.patchValue({
        cui: this.transportista.cui,
        nuevoEstado: this.transportista.estado,
        observaciones: this.transportista.observaciones ?? '',
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
    const dto: ActualizarEstadoTransportistaRequest = {
      cui: this.transportista.cui,
      nuevoEstado: raw.nuevoEstado,
      observaciones: raw.observaciones?.trim() ?? '',
    };
    this.loading = true;
    this.adminTransportistaService.actualizarEstado(this.transportista.idTransportista, dto).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = MSG_EXITO_7 + ' ' + MSG_EXITO_9;
        setTimeout(() => {
          this.actualizado.emit();
        }, 1200);
      },
      error: (err: { error?: { message?: string; error?: string } }) => {
        this.loading = false;
        const msg = err.error?.message ?? '';
        if (err.error?.error === 'ESTADO_IGUAL') {
          this.errorMessage = msg || MSG_ESTADO_IGUAL + (this.transportista.estado ? 'Activo' : 'Inactivo');
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
