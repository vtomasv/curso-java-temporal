package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.service.AccesoDenegadoException;
import com.enlace.evaluacion03.service.OrdenNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(OrdenNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String noEncontrada(RuntimeException error, Model model) {
        model.addAttribute("titulo", "Orden no encontrada");
        model.addAttribute("detalle", error.getMessage());
        return "error";
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String denegada(RuntimeException error, Model model) {
        model.addAttribute("titulo", "Acceso denegado");
        model.addAttribute("detalle", error.getMessage());
        return "error";
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    String conflicto(RuntimeException error, Model model) {
        model.addAttribute("titulo", "Operación no permitida");
        model.addAttribute("detalle", error.getMessage());
        return "error";
    }
}
