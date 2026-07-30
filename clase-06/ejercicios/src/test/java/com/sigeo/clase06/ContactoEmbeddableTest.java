package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContactoEmbeddableTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void debePersistirContactoComoEmbeddable() {
        Solicitud solicitud = new Solicitud("Test", "PENDIENTE", 1);
        Contacto contacto = new Contacto("test@test.com", "123456789");
        solicitud.setContacto(contacto);
        
        Solicitud guardada = entityManager.persistAndFlush(solicitud);
        entityManager.clear();
        
        Solicitud recuperada = entityManager.find(Solicitud.class, guardada.getId());
        assertThat(recuperada.getContacto()).isNotNull();
        assertThat(recuperada.getContacto().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void contactoDebeTenerIgualdadPorValor() {
        Contacto c1 = new Contacto("a@a.com", "123");
        Contacto c2 = new Contacto("a@a.com", "123");
        
        assertThat(c1).isEqualTo(c2);
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }
}
