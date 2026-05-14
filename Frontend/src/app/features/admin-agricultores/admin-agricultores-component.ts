import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminAgricultoresService } from './admin-agricultores.service'; // ✅ Corregido el nombre del archivo
import { AgricultorResponseDTO } from './models/agricultor.model';

@Component({
    selector: 'app-admin-agricultores',
    standalone: true,
    imports: [CommonModule, FormsModule],
    // ✅ Cambia guiones por puntos para que coincida con el estándar de Angular
    templateUrl: './admin-agricultores.component.html',
    styleUrl: './admin-agricultores.component.css'
})

export class AdminAgricultoresComponent implements OnInit {
    vistaActual: 'BANDEJA' | 'DETALLE' = 'BANDEJA';
    loading = false;
    agricultores: AgricultorResponseDTO[] = [];
    detalleAgricultor: any = null;
    filtroNit = '';

    constructor(private agricultorService: AdminAgricultoresService) {}

    ngOnInit(): void {
        this.cargarAgricultores();
    }

    cargarAgricultores(): void {
        this.loading = true;
        this.agricultorService.listar(this.filtroNit).subscribe({
            next: (data: AgricultorResponseDTO[]) => { // ✅ Tipado explícito para evitar error TS7006
                this.agricultores = data || [];
                this.loading = false;
            },
            error: (err: any) => { // ✅ Tipado explícito para evitar error TS7006
                console.error('Error al cargar agricultores', err);
                this.loading = false;
            }
        });
    }

    buscarPorNit(): void {
        this.cargarAgricultores();
    }

    limpiarFiltros(): void {
        this.filtroNit = '';
        this.cargarAgricultores();
    }

    verDetalle(nit: string): void {
        this.loading = true;
        this.agricultorService.obtenerDetalle(nit).subscribe({
            next: (data: any) => { // ✅ Tipado explícito
                this.detalleAgricultor = data;
                this.vistaActual = 'DETALLE';
                this.loading = false;
            },
            error: (err: any) => { // ✅ Tipado explícito
                console.error('Error al cargar el detalle', err);
                this.loading = false;
            }
        });
    }

    regresar(): void {
        this.vistaActual = 'BANDEJA';
        this.detalleAgricultor = null;
    }
}