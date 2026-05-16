import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequestDTO } from '../models/login-request.dto';
import { LoginResponseDTO, Rol } from '../models/login-response.dto';
import { STORAGE_KEYS } from '../constants/storage-keys';
import { AUTH_API_URL } from '../constants/api';

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
    // Limpiamos rastros de versiones anteriores
    this.clearLegacyLocalStorage();
  }

  login(credentials: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${AUTH_API_URL}/login`, credentials).pipe(
        tap((res) => {
          // ✅ TUS CONSTANTES + NUEVO sessionStorage
          sessionStorage.setItem(STORAGE_KEYS.TOKEN, res.token);
          sessionStorage.setItem(STORAGE_KEYS.ROL, res.rol);
          sessionStorage.setItem(STORAGE_KEYS.USERNAME, res.username ?? '');

          if (res.nitAgricultor) {
            sessionStorage.setItem(STORAGE_KEYS.NIT_AGRICULTOR, res.nitAgricultor);
          } else {
            sessionStorage.removeItem(STORAGE_KEYS.NIT_AGRICULTOR);
          }

          this.tokenSignal.set(res.token);
          this.rolSignal.set(res.rol);
          this.usernameSignal.set(res.username ?? null);
        }),
    );
  }

  // independiente para limpiar datos
  clearSession(): void {
    sessionStorage.removeItem(STORAGE_KEYS.TOKEN);
    sessionStorage.removeItem(STORAGE_KEYS.ROL);
    sessionStorage.removeItem(STORAGE_KEYS.USERNAME);
    sessionStorage.removeItem(STORAGE_KEYS.NIT_AGRICULTOR);
    this.tokenSignal.set(null);
    this.rolSignal.set(null);
    this.usernameSignal.set(null);
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  // Helper para la interfaz gráfica (Navbar)

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
    return sessionStorage.getItem(STORAGE_KEYS.NIT_AGRICULTOR);
  }

  private getStoredToken(): string | null {
    return sessionStorage.getItem(STORAGE_KEYS.TOKEN);
  }

  private getStoredRol(): Rol | null {
    const r = sessionStorage.getItem(STORAGE_KEYS.ROL);
    if (r === 'ROLE_AGRICULTOR' || r === 'ROLE_BENEFICIO' || r === 'ROLE_PESOCABAL') return r;
    return null;
  }

  private getStoredUsername(): string | null {
    return sessionStorage.getItem(STORAGE_KEYS.USERNAME);
  }

  // 🔥 Función de limpieza que previene bugs fantasma de usuarios antiguos
  private clearLegacyLocalStorage(): void {
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
    localStorage.removeItem(STORAGE_KEYS.ROL);
    localStorage.removeItem(STORAGE_KEYS.USERNAME);
    localStorage.removeItem(STORAGE_KEYS.NIT_AGRICULTOR);
  }
}