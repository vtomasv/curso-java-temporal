package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SafeImporterTest {

    @Test
    void shouldCloseResourceAndPreserveSuppressedExceptions() {
        SafeImporter importer = new SafeImporter();
        SafeImporter.FailingResource resource = new SafeImporter.FailingResource();

        Throwable thrown = catchThrowable(() -> importer.importData(resource));

        assertThat(thrown)
            .isNotNull()
            .hasMessageContaining("Error durante el trabajo");
            
        assertThat(thrown.getSuppressed())
            .hasSize(1)
            .first()
            .hasMessageContaining("Error al cerrar el recurso");

        assertThat(resource.isClosed()).isTrue();
    }
}
