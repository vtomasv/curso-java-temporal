-- TODO(C06-E01): Crear tabla solicitud
-- Debe incluir id (UUID), descripcion, estado, prioridad, email, telefono, fecha_creacion, fecha_actualizacion, version
CREATE TABLE solicitud (
    id UUID PRIMARY KEY,
    descripcion VARCHAR(255),
    estado VARCHAR(50),
    prioridad INTEGER,
    email VARCHAR(255),
    telefono VARCHAR(50),
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL,
    version BIGINT NOT NULL
);
