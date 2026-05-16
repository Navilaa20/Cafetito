import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-dashboard-agricultor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-agricultor.component.html',
  styleUrl: './dashboard-agricultor.component.css',
})
export class DashboardAgricultorComponent {
  tabs = [
    { id: 'pesajes' as const, label: 'Pesajes', icon: '&#128204;', path: '/dashboard/agricultor/pesajes' },
    { id: 'transportes' as const, label: 'Transportes', icon: '&#128666;', path: '/dashboard/agricultor/transportes' },
    { id: 'transportistas' as const, label: 'Transportistas', icon: '&#128666;', path: '/dashboard/agricultor/transportistas' },
  ];

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  get username(): string {
    return this.auth.displayUsername('Agricultor');
  }

  get activeTab(): 'pesajes' | 'transportes' | 'transportistas' {
    const url = this.router.url;
    if (url.includes('transportistas')) return 'transportistas';
    if (url.includes('transportes')) return 'transportes';
    return 'pesajes';
  }

  logout(): void {
    this.auth.logout();
  }
}
