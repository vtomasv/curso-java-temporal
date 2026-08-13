package com.sigeo.clase03;

public class SafeImporter {

    public static class FailingResource implements AutoCloseable {
        private boolean closed = false;

        public void doWork() {
            throw new RuntimeException("Error durante el trabajo");
        }

        @Override
        public void close() throws Exception {
            if (closed) {
                throw new IllegalStateException("Recurso ya cerrado");
            }
            closed = true;
            throw new Exception("Error al cerrar el recurso");
        }

        public boolean isClosed() {
            return closed;
        }
    }

    public void importData(FailingResource resource) throws Exception {
        // TODO(C03-E02): Usar try-with-resources para asegurar que resource se cierre.
        // Llamar a resource.doWork() dentro del bloque try.
        // Observar cómo la excepción de close() se agrega como suppressed a la excepción original.
        try (FailingResource r = resource) {
            r.doWork();
        }
       
    }
}
