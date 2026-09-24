package com.sigeo.clase08.config;
import com.sigeo.clase08.model.Solicitud;
import com.sigeo.clase08.repository.SolicitudRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
@Configuration @Profile("lab")
public class LabData {
    @Bean CommandLineRunner seed(SolicitudRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Solicitud("Material de entrenamiento", "user1", "PENDIENTE"));
                repository.save(new Solicitud("Apoyo logístico", "user2", "PENDIENTE"));
            }
        };
    }
}
