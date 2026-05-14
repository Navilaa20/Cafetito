export type EstadoCuenta =
  | 'CUENTA_CREADA'
  | 'CUENTA_ABIERTA'
  | 'PESAJE_INICIADO'
  | 'PESAJE_FINALIZADO'
  | 'CUENTA_CERRADA'
  | 'CUENTA_CONFIRMADA';

export interface CuentaResponse {
  idCuenta: number;
  nitAgricultor: string;
  pesoEnviado: number;
  cantidadParcialidades: number;
  fechaCreacion: string;
  estadoCuenta: EstadoCuenta;
}

export interface CuentaDetalleResponse {
  idCuenta: number;
  nitAgricultor: string;
  estadoCuenta: EstadoCuenta;
  cantidadParcialidades: number;
  diferenciaTotal: number;
  tolerancia: number;
  resultadoTolerancia: string | null;
  parcialidades: ParcialidadResponse[];
}

export interface ParcialidadResponse {
  idParcialidad: number;
  placaTransporte: string;
  nombreTransportista: string;
  pesoEnviado: number | null;
  tipoDeMedida: string | null;
  fechaRecepcionParcialidad: string | null;
  detalle: string | null;
  aceptado: boolean | null;
}

export interface CambiarEstadoCuentaRequest {
  idCuenta: number;
  estadoActual: string;
  estadoNuevo: EstadoCuenta;
}
