import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { TransporteService } from '../services/transporte.service';

const PLACA_REGEX = /^\d{3}[A-Za-z]{3}$/;
const MSG_FORMATO_PLACA = 'Formato de placa incorrecto, ej: 000AAA';

@Component({
  selector: 'app-transporte-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transporte-form.component.html',
  styleUrl: './transporte-form.component.css',
})
export class TransporteFormComponent implements OnInit {

  marcas: any[] = [];
  lineas: any[] = [];
  colores: any[] = [];
  tiposPlaca: any[] = [];

  @Output() cerrar = new EventEmitter<void>();
  @Output() creado = new EventEmitter<void>();

  form: FormGroup;
  errorMessage = '';
  loading = false;
  anioActual = new Date().getFullYear();

  constructor(
      private fb: FormBuilder,
      private transporteService: TransporteService,
  ) {
    this.form = this.fb.nonNullable.group({
      // ❌ BORRA ESTA LÍNEA: tipoPlaca: ['', Validators.required],
      // ✅ PON ESTA:
      idTipoPlaca: [null, Validators.required],

      numeroPlaca: ['', [Validators.required, this.placaPatternValidator]],
      marca: [null, Validators.required],
      color: [null, Validators.required],
      linea: [null, Validators.required],
      modelo: [null as number | null, [Validators.required, this.modeloRangeValidator]],
      observaciones: [''],
    });
  }

  ngOnInit() {
    this.cargarCatalogos();

    // ✅ Lógica dinámica: Cargar líneas solo cuando la marca cambie
    this.form.get('marca')?.valueChanges.subscribe((marcaId: number) => {
      if (marcaId) {
        this.transporteService.getLineasPorMarca(marcaId).subscribe({
          next: (data: any[]) => {
            this.lineas = data;
            this.form.get('linea')?.setValue(null); // Resetear línea al cambiar marca
          },
          error: () => (this.errorMessage = 'Error al cargar las líneas de esta marca')
        });
      } else {
        this.lineas = [];
      }
    });
  }

  cargarCatalogos() {
    this.transporteService.getTiposPlaca().subscribe((data: any[]) => this.tiposPlaca = data); // ✅ Cargamos el catálogo
    this.transporteService.getMarcas().subscribe((data: any[]) => this.marcas = data);
    this.transporteService.getColores().subscribe((data: any[]) => this.colores = data);
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();
    this.loading = true;

    console.log("Valor crudo del formulario:", raw);

    // ✅ IMPORTANTE: Convertimos a Number() para evitar el error 400 Bad Request
    this.transporteService.crearTransporte({
      idTipoPlaca: Number(raw.idTipoPlaca),
      numeroPlaca: raw.numeroPlaca.toUpperCase().trim(),
      marca: Number(raw.marca),
      color: Number(raw.color),
      linea: Number(raw.linea),
      modelo: Number(raw.modelo),
      observaciones: raw.observaciones || undefined,
    }).subscribe({
      next: () => {
        this.loading = false;
        this.creado.emit();
      },
      error: (err: any) => { // Se pone any para poder leer bien el mensaje de la API
        this.loading = false;
        this.errorMessage = err.error?.message || err.error?.error || 'Error al registrar el transporte.';
      },
    });
  }

  onCancel(): void { this.cerrar.emit(); }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.cerrar.emit();
    }
  }

  private placaPatternValidator(control: AbstractControl): ValidationErrors | null {
    const v = (control.value ?? '').toString().trim().toUpperCase();
    if (v.length === 0) return null;
    return PLACA_REGEX.test(v) ? null : { formatoPlaca: true };
  }

  private modeloRangeValidator = (control: AbstractControl): ValidationErrors | null => {
    const v = control.value;
    if (v == null || v === '') return null;
    const anio = Number(v);
    if (isNaN(anio) || anio < 1980 || anio > this.anioActual) return { modeloRango: true };
    return null;
  };

  get canSubmit(): boolean {
    return this.form.valid && !this.loading;
  }

  get numeroPlacaError(): string | null {
    const c = this.form.get('numeroPlaca');
    if (!c?.errors || !c.touched) return null;
    if (c.errors['formatoPlaca']) return MSG_FORMATO_PLACA;
    if (c.errors['required']) return 'El número de placa es obligatorio';
    return null;
  }

  get modeloError(): string | null {
    const c = this.form.get('modelo');
    if (!c?.errors || !c.touched) return null;
    if (c.errors['modeloRango']) return `El año debe estar entre 1980 y ${this.anioActual}`;
    if (c.errors['required']) return 'El modelo (año) es obligatorio';
    return null;
  }
}