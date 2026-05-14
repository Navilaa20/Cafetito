export interface AdminTransportistaResponse {
  idTransportista: number;
  cui: string;
  nombreCompleto: string;
  fechaNacimiento: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: string;
  estado: boolean;
  disponible: boolean;
  // ✅ CAMBIAMOS nitAgricultor POR nombreAgricultor
  nombreAgricultor: string;
  pesajeAsociado: number | null;
  observaciones: string | null;
}