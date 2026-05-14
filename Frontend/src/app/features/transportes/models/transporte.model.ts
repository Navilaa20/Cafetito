// modelo para enviar datos (Crear)
export interface TransporteRequest {
  idTipoPlaca: number;    // ✅ Cambiado de tipoPlaca (string) a idTipoPlaca (number)
  numeroPlaca: string;
  marca: number;          // ✅ Cambiado a number
  color: number;          // ✅ Cambiado a number
  linea: number;          // ✅ Cambiado a number
  modelo: number;
  observaciones?: string; // El '?' significa que es opcional
}

// modelo para recibir datos (Listar)
export interface TransporteResponse {
  id: number;             // ✅ Cambiado de idTransporte (string) a id (number)
  idTipoPlaca: number;
  placa: string;
  idMarca: number;
  idColor: number;
  linea: string;          // O number, dependiendo de cómo lo dejaste en el DTO de Java
  modelo: number;
  activo: boolean;
  observaciones?: string;
  idUsuario: number;      // ✅ Reemplaza a nitAgricultor
  fechaCreacion: string;
}