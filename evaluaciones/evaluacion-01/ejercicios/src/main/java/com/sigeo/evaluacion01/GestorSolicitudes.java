package com.sigeo.evaluacion01;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Gestiona solicitudes en memoria y permite exportarlas a un archivo de texto. */
public class GestorSolicitudes {

    private final List<Solicitud> solicitudes = new ArrayList<>();

    public void registrar(Solicitud solicitud) {
        // TODO(EV01-E03): Rechazar null e ids repetidos; luego agregar la solicitud.
        throw new UnsupportedOperationException("TODO EV01-E03");
    }

    public Solicitud buscarPorId(String id) {
        // TODO(EV01-E04): Buscar por id o lanzar un error controlado con contexto.
        throw new UnsupportedOperationException("TODO EV01-E04");
    }

    public List<Solicitud> filtrarPorPrioridad(Prioridad prioridad) {
        // TODO(EV01-E05): Filtrar sin exponer la colección interna.
        return List.of();
    }

    public Map<Prioridad, Long> contarPorPrioridad() {
        // TODO(EV01-E06): Agrupar las solicitudes observadas y contarlas.
        return Map.of();
    }

    public void exportarReporte(Path destino) throws IOException {
        // TODO(EV01-E07): Escribir encabezado y una línea por solicitud en UTF-8.
        throw new UnsupportedOperationException("TODO EV01-E07");
    }
}

