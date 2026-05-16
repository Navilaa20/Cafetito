/**
 * Claves de sesion (valores reales en sessionStorage).
 * Acceso: solo desde authSessionStore (core/storage) o AuthService.
 */
export const STORAGE_KEYS = {
  TOKEN: 'cafetito_token',
  ROL: 'cafetito_rol',
  USERNAME: 'cafetito_username',
  NIT_AGRICULTOR: 'cafetito_nit_agricultor',
} as const;
