import { STORAGE_KEYS } from '../constants/storage-keys';

export type AuthSessionKey = keyof typeof STORAGE_KEYS;

/**
 * Unico punto de acceso a la sesion en sessionStorage para toda la app.
 * No usar sessionStorage/localStorage directamente con STORAGE_KEYS fuera de aqui.
 */
export const authSessionStore = {
  get(key: AuthSessionKey): string | null {
    return sessionStorage.getItem(STORAGE_KEYS[key]);
  },

  set(key: AuthSessionKey, value: string): void {
    sessionStorage.setItem(STORAGE_KEYS[key], value);
  },

  remove(key: AuthSessionKey): void {
    sessionStorage.removeItem(STORAGE_KEYS[key]);
  },

  clearAll(): void {
    (Object.keys(STORAGE_KEYS) as AuthSessionKey[]).forEach((k) => this.remove(k));
  },

  /** Elimina restos si antes la sesion se guardaba en localStorage. */
  clearLegacyLocalStorage(): void {
    for (const fullKey of Object.values(STORAGE_KEYS)) {
      localStorage.removeItem(fullKey);
    }
  },
};
