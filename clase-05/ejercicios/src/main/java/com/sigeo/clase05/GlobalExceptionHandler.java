package com.sigeo.clase05;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitudNotFoundException.class)
    public ProblemDetail handleNotFound(SolicitudNotFoundException ex) {
        // TODO(C05-E05): Retornar ProblemDetail con status 404, title "No encontrado" y detail el mensaje de la excepción
        throw new UnsupportedOperationException("TODO C05-E05");
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail handleConflict(EstadoInvalidoException ex) {
        // TODO(C05-E05): Retornar ProblemDetail con status 409, title "Conflicto de estado" y detail el mensaje
        throw new UnsupportedOperationException("TODO C05-E05");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        // TODO(C05-E05): Retornar ProblemDetail con status 400, title "Error de validación"
        // TODO(C05-E05): Agregar los errores de campo como una propiedad adicional ("errores")
        throw new UnsupportedOperationException("TODO C05-E05");
    }
}
