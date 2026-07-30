package com.sigeo.clase06;

import org.springframework.data.domain.Page;
import java.util.List;

// TODO(C06-E05): Implementar DTO genérico para paginación
public record PageDTO<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean isLast
) {
    public static <T> PageDTO<T> from(Page<T> page) {
        throw new UnsupportedOperationException("TODO C06-E05");
    }
}
