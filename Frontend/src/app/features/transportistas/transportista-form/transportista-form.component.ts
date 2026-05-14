import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { TransportistaService } from '../services/transportista.service';

const CUI_13_DIGITOS = 'CUI debe contener 13 digitos';
const NOMBRE_APELLIDO_MINIMO = 'Debe colocar un nombre y un apellido como minimo';
const MSG_MENOR_EDAD = 'El transportista es menor de edad';
const MSG_LICENCIA_VENCIDA = 'La licencia se encuentra vencida';

function cuiValidator(control: AbstractControl): ValidationErrors | null {
  const v = (control.value ?? '').toString().replace(/\s|-/g, '');
  if (v.length === 0) return null;
  return /^\d{13}$/.test(v) ? null : { cuiDigitos: true };
}

function nombreApellidoValidator(control: AbstractControl): ValidationErrors | null {
  const s = (control.value ?? '').trim();
  if (!s) return null;
  const palabras = s.split(/\s+/).filter((p: string) => p.length > 0);
  if (palabras.length < 2) return { nombreMinimo: true };
  if (palabras.some((p: string) => p.length < 3)) return { nombreMinimo: true };
  return null;
}

@Component({
  selector: 'app-transportista-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transportista-form.component.html',
  styleUrl: './transportista-form.component.css',
})
export class TransportistaFormComponent {
  @Output() cerrar = new EventEmitter<void>();
  @Output() creado = new EventEmitter<void>();

  form: FormGroup;
  errorMessage = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private transportistaService: TransportistaService,
  ) {
    this.form = this.fb.nonNullable.group({
      cui: ['', [Validators.required, cuiValidator]],
      nombreCompleto: ['', [Validators.required, nombreApellidoValidator]],
      fechaNacimiento: ['', Validators.required],
      tipoLicencia: ['', Validators.required],
      fechaVencimientoLicencia: ['', Validators.required],
    });
  }

  get canSubmit(): boolean {
    return this.form.valid && !this.loading;
  }

  get cuiError(): string | null {
    const c = this.form.get('cui');
    if (!c?.errors || !c.touched) return null;
    if (c.errors['cuiDigitos']) return CUI_13_DIGITOS;
    if (c.errors['required']) return 'El CUI es obligatorio';
    return null;
  }

  get nombreError(): string | null {
    const c = this.form.get('nombreCompleto');
    if (!c?.errors || !c.touched) return null;
    if (c.errors['nombreMinimo']) return NOMBRE_APELLIDO_MINIMO;
    if (c.errors['required']) return 'El nombre es obligatorio';
    return null;
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (!this.form.valid) return;
    const raw = this.form.getRawValue();
    const cuiNormalizado = (raw.cui ?? '').toString().replace(/\s|-/g, '');
    this.loading = true;
    this.transportistaService.crearTransportista({
      ...raw,
      cui: cuiNormalizado,
    }).subscribe({
      next: () => {
        this.loading = false;
        this.creado.emit();
      },
      error: (err: { error?: { message?: string; error?: string } }) => {
        this.loading = false;
        const msg = err.error?.message ?? '';
        if (err.error?.error === 'CUI_DUPLICADO') {
          this.errorMessage = (msg && msg.trim()) ? msg : `Ya existe un transportista registrado con el CUI ${cuiNormalizado || 'indicado'}`;
        } else if (err.error?.error === 'MENOR_DE_EDAD') {
          this.errorMessage = MSG_MENOR_EDAD;
        } else if (err.error?.error === 'LICENCIA_VENCIDA') {
          this.errorMessage = MSG_LICENCIA_VENCIDA;
        } else {
          this.errorMessage = msg || 'Error al registrar el transportista.';
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
