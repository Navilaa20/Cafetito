export interface ParcialidadResponseAgricultor {
  idParcialidad: number;
  placaTransporte: string;
  nombreTransportista: string;
  pesoEnviado: number;
  tipoDeMedida: string;
  fechaRecepcionParcialidad: string | null;
  detalle: string | null;
  aceptado: boolean | null;
}

export interface ParcialidadRequestAgricultor {
  idCuenta: number;
  peso: number;
  idTransporte: string;
  idTransportista: number;
  tipoDeMedida: string;
}
