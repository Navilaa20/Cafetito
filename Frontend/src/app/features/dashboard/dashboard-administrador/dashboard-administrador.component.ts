import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { STORAGE_KEYS } from '../../../core/constants/storage-keys';
import { AuthService } from '../../../core/services/auth.service';
import { AdminTransportistasComponent } from '../../admin-transportistas/admin-transportistas.component';
import { AdminTransportesComponent } from '../../admin-transportes/admin-transportes.component';
// ✅ 1. Importa el componente de agricultores
import { AdminAgricultoresComponent } from '../../admin-agricultores/admin-agricultores-component';

@Component({
  selector: 'app-dashboard-administrador',
  standalone: true,
  // ✅ 2. Agrégalo al arreglo de imports
  imports: [
    CommonModule,
    RouterModule,
    AdminTransportistasComponent,
    AdminTransportesComponent,
    AdminAgricultoresComponent //
  ],
  templateUrl: './dashboard-administrador.component.html',
  styleUrl: './dashboard-administrador.component.css',
})
export class DashboardAdministradorComponent implements OnInit {
  username = localStorage.getItem(STORAGE_KEYS.USERNAME) ?? 'Administrador';
  tabActiva: 'cuentas' | 'transportes' | 'transportistas' | 'agricultores' = 'cuentas';

  constructor(
      private auth: AuthService,
      private router: Router
  ) {}

  ngOnInit(): void {
    this.actualizarTabDesdeUrl(this.router.url);
    this.router.events
        .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
        .subscribe((e) => this.actualizarTabDesdeUrl(e.url));
  }

  private actualizarTabDesdeUrl(url: string): void {
    if (url.includes('/cuentas')) {
      this.tabActiva = 'cuentas';
    } else if (url.includes('/agricultores')) { // ✅ 3. Lógica para la URL de agricultores
      this.tabActiva = 'agricultores';
    }
  }

  irACuentas(): void {
    this.tabActiva = 'cuentas';
    this.router.navigate(['/dashboard/administrador/cuentas']);
  }

  logout(): void {
    this.auth.logout();
  }
}