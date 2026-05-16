import { HttpInterceptorFn } from '@angular/common/http';
import { STORAGE_KEYS } from '../constants/storage-keys';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // Usamos sessionStorage para mayor seguridad al cerrar la pestaña
  const token = sessionStorage.getItem(STORAGE_KEYS.TOKEN);
  let headers = req.headers;

  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }

  return next(req.clone({ headers }));
};