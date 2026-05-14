-- Crear tabla transportes si no existe (esquema esperado por la entidad Transporte)
CREATE TABLE IF NOT EXISTS transportes (
  id_transporte VARCHAR(20) PRIMARY KEY,
  tipo_placa VARCHAR(1) NOT NULL,
  numero_placa VARCHAR(10) NOT NULL,
  marca VARCHAR(80) NOT NULL,
  color VARCHAR(50) NOT NULL,
  linea VARCHAR(80) NOT NULL,
  modelo INTEGER NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT true,
  observaciones VARCHAR(500),
  nit_agricultor VARCHAR(50) NOT NULL,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)^^
-- Crear tabla transportistas si no existe (esquema esperado por la entidad Transportista)
CREATE TABLE IF NOT EXISTS transportistas (
  id_transportista BIGSERIAL PRIMARY KEY,
  cui VARCHAR(20) UNIQUE NOT NULL,
  nombre_completo VARCHAR(150) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  tipo_licencia VARCHAR(50) NOT NULL,
  fecha_vencimiento_licencia DATE NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT true,
  disponible BOOLEAN NOT NULL DEFAULT true,
  nit_agricultor VARCHAR(50) NOT NULL,
  pesaje_asociado BIGINT
)^^
-- Asegurar valor por defecto para columna activo (si la tabla ya existe)
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'transportes') THEN
    ALTER TABLE transportes ALTER COLUMN activo SET DEFAULT true;
  END IF;
END $$^^
