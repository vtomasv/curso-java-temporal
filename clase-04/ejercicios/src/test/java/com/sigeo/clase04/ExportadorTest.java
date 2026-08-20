package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ExportadorTest {

    @Test
    void debeExportarArchivoCorrectamente(@TempDir Path tempDir) throws Exception {
        Exportador exportador = new Exportador();
        Path destino = tempDir.resolve("reporte.txt");
        List<String> lineas = List.of("Línea 1", "Línea 2");
        
        exportador.exportarAtomicamente(lineas, destino);
    
        List<String> lineas2 = List.of("ls -ltr", "echo 'Hola mundo'", "rm -Rf archivo_01.txt");
        exportador.exportarAtomicamente(lineas2, Path.of("./archivo_01.sh"));

        assertThat(Files.exists(destino)).isTrue();
        assertThat(Files.readAllLines(destino)).containsExactlyElementsOf(lineas);
    }
}
