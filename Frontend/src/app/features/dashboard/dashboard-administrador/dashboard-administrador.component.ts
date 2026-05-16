import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../core/services/auth.service';
import { AdminTransportistasComponent } from '../../admin-transportistas/admin-transportistas.component';
import { AdminTransportesComponent } from '../../admin-transportes/admin-transportes.component';

@Component({
  selector: 'app-dashboard-administrador',
  standalone: true,
  imports: [CommonModule, RouterModule, AdminTransportistasComponent, AdminTransportesComponent],
  templateUrl: './dashboard-administrador.component.html',
  styleUrl: './dashboard-administrador.component.css',
})
export class DashboardAdministradorComponent implements OnInit {
  tabActiva: 'cuentas' | 'transportes' | 'transportistas' = 'cuentas';

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  get username(): string {
    return this.auth.displayUsername('Administrador');
  }

  ngOnInit(): void {
    this.actualizarTabDesdeUrl(this.router.url);
    this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe((e) => this.actualizarTabDesdeUrl(e.url));
  }

  private actualizarTabDesdeUrl(url: string): void {
    if (url.includes('/cuentas')) {
      this.tabActiva = 'cuentas';
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
