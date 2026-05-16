import { HttpInterceptorFn } from '@angular/common/http';
import { authSessionStore } from '../storage/auth-session.store';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const token = authSessionStore.get('TOKEN');
  let headers = req.headers;
  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }
  return next(req.clone({ headers }));
};
