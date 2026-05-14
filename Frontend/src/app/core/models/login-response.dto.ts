export type Rol = 'ROLE_AGRICULTOR' | 'ROLE_BENEFICIO' | 'ROLE_PESOCABAL';

export interface LoginResponseDTO {
  token: string;
  rol: Rol;
  username: string;
  nitAgricultor?: string | null;
}
