-- TODO(C06-E04): Crear tabla aprobacion
-- Debe incluir id (UUID), responsable, comentario, fecha, solicitud_id (FK)
CREATE TABLE aprobacion (
    id UUID PRIMARY KEY,
    responsable VARCHAR(255) NOT NULL,
    comentario TEXT,
    fecha TIMESTAMP NOT NULL,
    solicitud_id UUID NOT NULL,
    CONSTRAINT fk_aprobacion_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud(id)
);
