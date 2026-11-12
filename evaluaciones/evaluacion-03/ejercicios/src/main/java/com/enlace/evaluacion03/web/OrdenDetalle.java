package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.domain.FirmaOrden;

import java.util.List;

public record OrdenDetalle(OrdenResumen resumen, String contenido, String creador, List<FirmaOrden> firmas) {
}
