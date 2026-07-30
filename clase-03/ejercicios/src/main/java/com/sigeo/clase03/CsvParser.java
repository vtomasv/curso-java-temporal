package com.sigeo.clase03;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public record ParseResult(List<String> validLines, List<ParseError> errors) {}
    public record ParseError(int lineNumber, String field, String cause) {}

    public ParseResult parse(List<String> lines) {
        // TODO(C03-E01): Implementar el procesamiento de líneas CSV.
        // Cada línea debe tener exactamente 3 campos separados por coma.
        // Si una línea es válida, agregarla a validLines.
        // Si una línea es inválida (ej. menos de 3 campos), atrapar la excepción,
        // crear un ParseError con el número de línea (1-based), el campo problemático (o "formato")
        // y el mensaje de la excepción, y agregarlo a errors.
        // El proceso NO debe detenerse si hay un error.
        throw new UnsupportedOperationException("TODO C03-E01");
    }
}
