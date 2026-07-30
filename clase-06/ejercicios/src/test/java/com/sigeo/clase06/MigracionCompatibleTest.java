package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MigracionCompatibleTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void migracionesDebenHaberAgregadoCampoDepartamento() {
        // Verificar que la columna existe en la base de datos
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'solicitud' AND column_name = 'departamento'",
                Integer.class);
                
        // Si las migraciones V3 y V4 están correctas, la columna debe existir
        // assertThat(count).isEqualTo(1);
        
        // Este test fallará hasta que se implementen las migraciones
        throw new UnsupportedOperationException("TODO C06-E08: Implementar migraciones V3 y V4");
    }
}
