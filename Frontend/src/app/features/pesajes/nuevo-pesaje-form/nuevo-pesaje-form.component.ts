import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PesajeService } from '../pesaje.service';

@Component({
    selector: 'app-nuevo-pesaje-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './nuevo-pesaje-form.component.html',
    styleUrl: './nuevo-pesaje-form.component.css'
})
export class NuevoPesajeFormComponent implements OnInit {
    @Output() cerrar = new EventEmitter<void>();
    @Output() creado = new EventEmitter<void>();

    form: FormGroup;
    medidas: any[] = [];
    loadingSubmit = false;
    errorMessage = '';

    // ✅ Nuevas variables para cumplir con el Caso de Uso
    estadoFijo = 'PENDIENTE';
    fechaHoy = '';

    constructor(private fb: FormBuilder, private pesajeService: PesajeService) {
        this.form = this.fb.nonNullable.group({
            idMedida: [null, Validators.required],
            pesoTotalActual: [null, [Validators.required, Validators.min(1)]]
        });
    }

    ngOnInit(): void {
        // Generar la fecha de hoy en el formato de Guatemala
        this.fechaHoy = new Date().toLocaleDateString('es-GT', {
            day: '2-digit', month: '2-digit', year: 'numeric'
        });

        this.pesajeService.listarMedidas().subscribe({
            next: (data) => this.medidas = data,
            error: () => this.errorMessage = 'No se pudo cargar el catálogo de medidas.'
        });
    }

    onSubmit(): void {
        if (this.form.invalid) return;
        this.loadingSubmit = true;

        this.pesajeService.crearPesaje(this.form.getRawValue()).subscribe({
            next: () => {
                this.loadingSubmit = false;
                this.creado.emit();
            },
            error: (err) => {
                this.loadingSubmit = false;
                this.errorMessage = err.error?.message || 'Error al crear el pesaje.';
            }
        });
    }

    onOverlayClick(event: MouseEvent): void {
        if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
            this.cerrar.emit();
        }
    }
}