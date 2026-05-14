export interface TransportistaRequest {
  cui: string;
  nombreCompleto: string;
  fechaNacimiento: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: string;
}

export interface TransportistaResponse {
  id: number;
  idTransportista: number;
  cui: string;
  nombreCompleto: string;
  fechaNacimiento: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: string;
  estado: boolean;
  disponible: boolean;
  nitAgricultor: string;
  pesajeAsociado: number | null;
}
