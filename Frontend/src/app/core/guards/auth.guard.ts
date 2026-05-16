import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { authSessionStore } from '../storage/auth-session.store';

function parseJwtExpSeconds(token: string): number | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const pad = base64.length % 4;
    if (pad) base64 += '='.repeat(4 - pad);
    const payload = JSON.parse(atob(base64)) as { exp?: number };
    return typeof payload.exp === 'number' ? payload.exp : null;
  } catch {
    return null;
  }
}

function isJwtExpired(token: string): boolean {
  const exp = parseJwtExpSeconds(token);
  if (exp == null) return false;
  return exp * 1000 <= Date.now();
}

function requiredRoleFrom(route: ActivatedRouteSnapshot): string | undefined {
  let cur: ActivatedRouteSnapshot | null = route;
  while (cur) {
    const role = cur.data['role'] as string | undefined;
    if (role) return role;
    cur = cur.parent;
  }
  return undefined;
}

function checkAuth(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
  const router = inject(Router);
  const auth = inject(AuthService);
  const token = authSessionStore.get('TOKEN')?.trim() ?? '';
  const rol = authSessionStore.get('ROL');

  if (!token) {
    void router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  if (isJwtExpired(token)) {
    auth.clearSession();
    void router.navigate(['/login'], {
      queryParams: { returnUrl: state.url, reason: 'expired' },
    });
    return false;
  }

  const requiredRol = requiredRoleFrom(route);
  if (requiredRol && rol !== requiredRol) {
    void router.navigate(['/login']);
    return false;
  }

  return true;
}

export const authGuard: CanActivateFn = (route, state) => checkAuth(route, state);
