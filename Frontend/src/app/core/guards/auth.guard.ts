import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { STORAGE_KEYS } from '../constants/storage-keys';

// 1. Decodifica el token para obtener la fecha de expiración
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

// 2. Verifica si el token ya caducó
function isJwtExpired(token: string): boolean {
  const exp = parseJwtExpSeconds(token);
  if (exp == null) return false;
  return exp * 1000 <= Date.now();
}

// 3. Busca el rol requerido subiendo por el árbol de rutas
function requiredRoleFrom(route: ActivatedRouteSnapshot): string | undefined {
  let cur: ActivatedRouteSnapshot | null = route;
  while (cur) {
    const role = cur.data['role'] as string | undefined;
    if (role) return role;
    cur = cur.parent;
  }
  return undefined;
}

// LÓGICA PRINCIPAL
function checkAuth(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
  const router = inject(Router);


  const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN)?.trim() ?? '';
  const rol = sessionStorage.getItem(STORAGE_KEYS.ROL);

  if (!token) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  if (isJwtExpired(token)) {
    // ✅ Limpieza en sessionStorage
    sessionStorage.removeItem(STORAGE_KEYS.TOKEN);
    sessionStorage.removeItem(STORAGE_KEYS.ROL);
    router.navigate(['/login'], { queryParams: { returnUrl: state.url, reason: 'expired' } });
    return false;
  }

  const requiredRol = requiredRoleFrom(route);
  if (requiredRol && rol !== requiredRol) {
    router.navigate(['/login']);
    return false;
  }

  return true;
}

export const authGuard: CanActivateFn = (route, state) => checkAuth(route, state);