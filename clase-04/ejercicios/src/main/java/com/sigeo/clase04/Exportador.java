package com.sigeo.clase04;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
        try {
            Path archivoTemporal = Files.createTempFile(destino.getParent(), "temp", ".tmp");
            Files.write(archivoTemporal, lineas, StandardCharsets.UTF_8);
            Files.move(archivoTemporal, destino, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) { 
            e.printStackTrace();
        }   
    }
}
