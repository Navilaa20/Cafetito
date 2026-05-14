// ✅ Importamos SIEMPRE el de desarrollo, Angular lo cambiará en el build de Render
import { environment } from '../../../environments/environment';

// ✅ Dejamos que 'base' tome el valor dinámico
const base = environment.apiUrl;

export const AUTH_API_URL = `${base}/api/auth`;
export const TRANSPORTISTA_API_URL = `${base}/api/transportistas`;
export const TRANSPORTE_API_URL = `${base}/api/transportes`;
export const ADMIN_TRANSPORTISTA_API_URL = `${base}/api/admin/transportistas`;
export const ADMIN_TRANSPORTE_API_URL = `${base}/api/admin/transportes`;
export const ADMIN_CUENTA_API_URL = `${base}/api/admin/cuentas`;
export const ADMIN_PARCIALIDAD_API_URL = `${base}/api/admin/parcialidades`;
export const PESAJE_API_URL = `${base}/api/pesajes`;
export const PARCIALIDAD_AGRICULTOR_API_URL = `${base}/api/parcialidades`;
export const TRANSPORTE_DISPONIBLES_URL = `${base}/api/transportes/disponibles`;
export const TRANSPORTISTA_DISPONIBLES_URL = `${base}/api/transportistas/disponibles`;
export const PESAJE_CABAL_API_URL = `${base}/api/pesaje-cabal`;
export const ADMIN_AGRICULTORES_API_URL = `${base}/api/admin/agricultores`;