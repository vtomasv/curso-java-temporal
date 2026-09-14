package com.sigeo.clase06;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final SolicitudService solicitudService;

    public DatabaseSeeder(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("--- Sembrando datos de prueba en la base de datos ---");

        String[] departamentos = {"IT", "Recursos Humanos", "Finanzas", "Legal", "Operaciones"};
        String[] estados = {"PENDIENTE", "APROBADO", "RECHAZADO"};
        String[] responsibles = {"Ana Garcia", "Luis Perez", "Maria Lopez", "Carlos Ruiz", "Elena Sanz"};

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            String desc = "Solicitud de servicio #" + i + " - " + (random.nextBoolean() ? "Urgente" : "Normal");
            String estado = estados[random.nextInt(estados.length)];
            Integer prioridad = random.nextInt(5) + 1;
            String dept = departamentos[random.nextInt(departamentos.length)];

            Contacto contacto = new Contacto(
                "usuario" + i + "@empresa.com",
                "+34" + (100000000 + random.nextInt(900000000))
            );

            Solicitud solicitud = solicitudService.crearSolicitud(desc, estado, prioridad, contacto, dept);

            // Agregar aprobaciones aleatorias (0 a 3)
            int numAprobaciones = random.nextInt(4);
            for (int j = 0; j < numAprobaciones; j++) {
                String resp = responsibles[random.nextInt(responsibles.length)];
                String comentario = "Aprobación " + (j + 1) + " para la solicitud " + i;
                solicitudService.agregarAprobacion(solicitud.getId(), resp, comentario);
            }
        }

        System.out.println("--- Sembrado completado: 100 solicitudes creadas ---");
    }
}
