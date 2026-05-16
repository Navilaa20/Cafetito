import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'dashboard/agricultor',
    loadComponent: () =>
      import('./features/dashboard/dashboard-agricultor/dashboard-agricultor.component').then(
        (m) => m.DashboardAgricultorComponent
      ),
    canActivate: [authGuard],
    canActivateChild: [authGuard],
    data: { role: 'ROLE_AGRICULTOR' },
    children: [
      { path: '', redirectTo: 'pesajes', pathMatch: 'full' },
      {
        path: 'pesajes',
        loadComponent: () =>
          import('./features/pesajes/pesajes.component').then((m) => m.PesajesComponent),
      },
      {
        path: 'pesajes/:idPesaje',
        loadComponent: () =>
          import('./features/pesajes/pesaje-detalle/pesaje-detalle.component').then(
            (m) => m.PesajeDetalleComponent
          ),
      },
      {
        path: 'transportes',
        loadComponent: () =>
          import('./features/transportes/transportes.component').then(
            (m) => m.TransportesComponent
          ),
      },
      {
        path: 'transportistas',
        loadComponent: () =>
          import('./features/transportistas/transportistas.component').then(
            (m) => m.TransportistasComponent
          ),
      },
    ],
  },
  {
    path: 'dashboard/administrador',
    loadComponent: () =>
      import('./features/dashboard/dashboard-administrador/dashboard-administrador.component').then(
        (m) => m.DashboardAdministradorComponent
      ),
    canActivate: [authGuard],
    canActivateChild: [authGuard],
    data: { role: 'ROLE_BENEFICIO' },
    children: [
      { path: '', redirectTo: 'cuentas', pathMatch: 'full' },
      {
        path: 'cuentas',
        loadComponent: () =>
          import('./features/admin-cuentas/admin-cuentas.component').then(
            (m) => m.AdminCuentasComponent
          ),
      },
      {
        path: 'cuentas/:id',
        loadComponent: () =>
          import('./features/admin-cuentas/cuenta-detalle/cuenta-detalle.component').then(
            (m) => m.CuentaDetalleComponent
          ),
      },
      {
        path: 'cuentas/:id/parcialidades/:idParcialidad',
        loadComponent: () =>
          import('./features/admin-cuentas/parcialidad-detalle/parcialidad-detalle.component').then(
            (m) => m.ParcialidadDetalleComponent
          ),
      },
      {
        path: 'agricultores',
        loadComponent: () =>
            import('./features/admin-agricultores/admin-agricultores-component').then(
                (m) => m.AdminAgricultoresComponent
            ),
      },
    ],
  },
  {
    path: 'dashboard/pesaje',
    loadComponent: () =>
      import('./features/dashboard/dashboard-pesaje/dashboard-pesaje.component').then(
        (m) => m.DashboardPesajeComponent
      ),
    canActivate: [authGuard],
    data: { role: 'ROLE_PESOCABAL' },
  },
  { path: '**', redirectTo: 'login' },
];
