-- TODO(C06-E08): Hacer el campo 'departamento' obligatorio
UPDATE solicitud SET departamento = 'GENERAL' WHERE departamento IS NULL;
ALTER TABLE solicitud ALTER COLUMN departamento SET NOT NULL;
