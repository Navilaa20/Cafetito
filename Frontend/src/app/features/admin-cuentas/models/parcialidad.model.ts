export interface ParcialidadDetalleResponse {
  idCuenta: number;
  idParcialidad: number;
  qrCode: string;
  aceptado: boolean | null;
  transporte: {
    placa: string;
    estado: boolean;
    observaciones: string | null;
  } | null;
  transportista: {
    cui: string;
    nombre: string;
    estado: boolean;
    observaciones: string | null;
  } | null;
}
