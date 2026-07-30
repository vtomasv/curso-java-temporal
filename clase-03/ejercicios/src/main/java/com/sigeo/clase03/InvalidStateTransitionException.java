package com.sigeo.clase03;

// TODO(C03-E05): Implementar esta excepción personalizada.
// Debe extender RuntimeException.
// Debe tener un constructor que acepte un mensaje y una causa (Throwable).
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
