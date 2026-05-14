export interface PesajeResponse {
  idPesaje: number;
  idCuenta: number | null;
  pesoTotalActual: number;
  cantidadParcialidades: number;
  fechaCreacion: string;
  estadoPesaje: string;
  mediaDePeso: string;
}
