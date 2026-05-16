import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequestDTO } from '../models/login-request.dto';
import { LoginResponseDTO, Rol } from '../models/login-response.dto';
import { AUTH_API_URL } from '../constants/api';
import { authSessionStore } from '../storage/auth-session.store';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenSignal = signal<string | null>(this.getStoredToken());
  private rolSignal = signal<Rol | null>(this.getStoredRol());
  private usernameSignal = signal<string | null>(this.getStoredUsername());

  isLoggedIn = computed(() => !!this.tokenSignal());
  currentRol = computed(() => this.rolSignal());
  currentUsername = computed(() => this.usernameSignal());

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {
    authSessionStore.clearLegacyLocalStorage();
  }

  login(credentials: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${AUTH_API_URL}/login`, credentials).pipe(
      tap((res) => {
        authSessionStore.set('TOKEN', res.token);
        authSessionStore.set('ROL', res.rol);
        authSessionStore.set('USERNAME', res.username ?? '');
        if (res.nitAgricultor) {
          authSessionStore.set('NIT_AGRICULTOR', res.nitAgricultor);
        } else {
          authSessionStore.remove('NIT_AGRICULTOR');
        }
        this.tokenSignal.set(res.token);
        this.rolSignal.set(res.rol);
        this.usernameSignal.set(res.username ?? null);
      }),
    );
  }

  /** Limpia sesion en cliente (sessionStorage + signals) sin navegar. */
  clearSession(): void {
    authSessionStore.clearAll();
    this.tokenSignal.set(null);
    this.rolSignal.set(null);
    this.usernameSignal.set(null);
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  /** Nombre para UI; si no hay sesion devuelve el texto por defecto. */
  displayUsername(fallback: string): string {
    return this.currentUsername() ?? fallback;
  }

  getToken(): string | null {
    return this.tokenSignal() ?? this.getStoredToken();
  }

  getRol(): Rol | null {
    return this.rolSignal() ?? this.getStoredRol();
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getNitAgricultor(): string | null {
    return authSessionStore.get('NIT_AGRICULTOR');
  }

  private getStoredToken(): string | null {
    return authSessionStore.get('TOKEN');
  }

  private getStoredRol(): Rol | null {
    const r = authSessionStore.get('ROL');
    if (r === 'ROLE_AGRICULTOR' || r === 'ROLE_BENEFICIO' || r === 'ROLE_PESOCABAL') return r;
    return null;
  }

  private getStoredUsername(): string | null {
    return authSessionStore.get('USERNAME');
  }
}
