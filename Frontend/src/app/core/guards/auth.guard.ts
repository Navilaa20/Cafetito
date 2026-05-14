import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { STORAGE_KEYS } from '../constants/storage-keys';

export const authGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
  const rol = localStorage.getItem(STORAGE_KEYS.ROL);

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRol = route.data['role'] as string | undefined;
  if (requiredRol && rol !== requiredRol) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};
