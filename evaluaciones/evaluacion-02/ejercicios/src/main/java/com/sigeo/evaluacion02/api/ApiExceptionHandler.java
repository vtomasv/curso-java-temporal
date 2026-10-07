package com.sigeo.evaluacion02.api;

import com.sigeo.evaluacion02.service.SolicitudNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    ProblemDetail noEncontrada(SolicitudNoEncontradaException exception) {
        // TODO(EV02-E06): Responder 404 con title y detail útiles.
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException exception) {
        // TODO(EV02-E06): Responder 400 sin exponer stack trace.
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    ProblemDetail conflicto(IllegalStateException exception) {
        // TODO(EV02-E06): Responder 409 para una transición inválida.
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
