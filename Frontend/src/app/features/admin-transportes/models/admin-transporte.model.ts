export interface AdminTransporteResponse {
  id: number;
  placa: string;
  linea: string;
  modelo: number;
  activo: boolean;
  observaciones: string | null;
  // ✅ AGREGA ESTA LÍNEA AQUÍ TAMBIÉN:
  nombreAgricultor: string;
  idUsuario: number;
  fechaCreacion: string | null;
  idTipoPlaca?: number;
  idMarca?: number;
  idColor?: number;
}
