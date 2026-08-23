package com.sigeo.evaluacion01;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas públicas de la evaluación.
 *
 * <p>No modifique este archivo. Los números de orden sugieren una ruta de trabajo,
 * pero cada prueba debe poder ejecutarse de forma independiente.</p>
 */
@TestMethodOrder(OrderAnnotation.class)
class GestorSolicitudesTest {

    @Test
    @Order(1)
    void prioridadesExponenSusHorasDeAtencion() {
        assertThat(Prioridad.BAJA.horasAtencion()).isEqualTo(72);
        assertThat(Prioridad.MEDIA.horasAtencion()).isEqualTo(48);
        assertThat(Prioridad.ALTA.horasAtencion()).isEqualTo(24);
        assertThat(Prioridad.CRITICA.horasAtencion()).isEqualTo(4);
    }

    @Test
    @Order(2)
    void creaUnaSolicitudValida() {
        Solicitud solicitud = solicitud("SOL-001", Prioridad.ALTA);

        assertThat(solicitud.id()).isEqualTo("SOL-001");
        assertThat(solicitud.solicitante()).isEqualTo("Cabo Rojas");
        assertThat(solicitud.descripcion()).isEqualTo("Reponer radio");
        assertThat(solicitud.prioridad()).isEqualTo(Prioridad.ALTA);
    }

    @Test
    @Order(3)
    void rechazaCamposDeTextoEnBlanco() {
        assertThatThrownBy(() -> new Solicitud(" ", "Cabo Rojas", "Reponer radio", Prioridad.ALTA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id");

        assertThatThrownBy(() -> new Solicitud("SOL-001", "Cabo Rojas", " ", Prioridad.ALTA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("descripcion");
    }

    @Test
    @Order(4)
    void rechazaUnaPrioridadNula() {
        assertThatThrownBy(() -> new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("prioridad");
    }

    @Test
    @Order(5)
    void registraYBuscaUnaSolicitudPorId() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        Solicitud solicitud = solicitud("SOL-001", Prioridad.ALTA);

        gestor.registrar(solicitud);

        assertThat(gestor.buscarPorId("SOL-001")).isEqualTo(solicitud);
    }

    @Test
    @Order(6)
    void rechazaUnIdDuplicadoConMensajeContextual() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        gestor.registrar(solicitud("SOL-001", Prioridad.ALTA));

        assertThatThrownBy(() -> gestor.registrar(
                new Solicitud("SOL-001", "Sargento Diaz", "Solicitud repetida", Prioridad.MEDIA)))
                .isInstanceOf(SolicitudDuplicadaException.class)
                .hasMessageContaining("SOL-001");
    }

    @Test
    @Order(7)
    void informaCuandoElIdBuscadoNoExiste() {
        GestorSolicitudes gestor = new GestorSolicitudes();

        assertThatThrownBy(() -> gestor.buscarPorId("SOL-404"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("SOL-404");
    }

    @Test
    @Order(8)
    void filtraPorPrioridadSinExponerLaListaInterna() {
        GestorSolicitudes gestor = gestorConTresSolicitudes();

        List<Solicitud> altas = gestor.filtrarPorPrioridad(Prioridad.ALTA);

        assertThat(altas)
                .extracting(Solicitud::id)
                .containsExactly("SOL-001", "SOL-003");

        try {
            altas.clear();
        } catch (UnsupportedOperationException ignored) {
            // Una lista inmutable también protege la colección interna.
        }

        assertThat(gestor.filtrarPorPrioridad(Prioridad.ALTA)).hasSize(2);
    }

    @Test
    @Order(9)
    void cuentaLasSolicitudesObservadasPorPrioridad() {
        GestorSolicitudes gestor = gestorConTresSolicitudes();

        assertThat(gestor.contarPorPrioridad())
                .containsEntry(Prioridad.ALTA, 2L)
                .containsEntry(Prioridad.MEDIA, 1L);
    }

    @Test
    @Order(10)
    void exportaUnReporteUtf8ConEncabezadoYUnaLineaPorSolicitud(@TempDir Path tempDir)
            throws IOException {
        GestorSolicitudes gestor = gestorConTresSolicitudes();
        Path destino = tempDir.resolve("reporte-solicitudes.txt");

        gestor.exportarReporte(destino);

        assertThat(destino).exists().isRegularFile();
        List<String> lineas = Files.readAllLines(destino);
        assertThat(lineas).hasSize(4);
        assertThat(lineas.getFirst()).isNotBlank();
        assertThat(lineas.subList(1, lineas.size()))
                .anySatisfy(linea -> assertThat(linea)
                        .contains("SOL-001", "Cabo Rojas", "ALTA"))
                .anySatisfy(linea -> assertThat(linea)
                        .contains("SOL-002", "Sargento Munoz", "MEDIA"));
    }

    private static GestorSolicitudes gestorConTresSolicitudes() {
        GestorSolicitudes gestor = new GestorSolicitudes();
        gestor.registrar(solicitud("SOL-001", Prioridad.ALTA));
        gestor.registrar(new Solicitud(
                "SOL-002", "Sargento Munoz", "Revisar generador", Prioridad.MEDIA));
        gestor.registrar(new Solicitud(
                "SOL-003", "Cabo Perez", "Restablecer enlace", Prioridad.ALTA));
        return gestor;
    }

    private static Solicitud solicitud(String id, Prioridad prioridad) {
        return new Solicitud(id, "Cabo Rojas", "Reponer radio", prioridad);
    }
}

