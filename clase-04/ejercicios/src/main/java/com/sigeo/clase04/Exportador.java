package com.sigeo.clase04;

import java.nio.file.Path;
import java.util.List;

public class Exportador {

    /**
     * Escribe una lista de líneas en un archivo de destino de forma atómica.
     * Para evitar archivos parciales si ocurre un error durante la escritura,
     * debe escribir primero en un archivo temporal y luego moverlo al destino final.
     * 
     * @param lineas Contenido a escribir
     * @param destino Ruta del archivo final
     */
    public void exportarAtomicamente(List<String> lineas, Path destino) {
        // TODO(C04-E04): Implementar escritura en archivo temporal y movimiento atómico
        // Pista: Files.createTempFile, Files.write (con StandardCharsets.UTF_8), Files.move (con StandardCopyOption.ATOMIC_MOVE)
        throw new UnsupportedOperationException("TODO C04-E04");
    }
}
