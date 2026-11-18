package com.enlace.evaluacion03.config;

import com.enlace.evaluacion03.service.OrdenTemporalService;
import com.enlace.evaluacion03.workflow.AccesoWorkflowImpl;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(
        prefix = "sicor.temporal",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class TemporalConfig {

    public static final String TASK_QUEUE = "SICOR-EV03";

    @Bean
    DespachoActivitiesImpl despachoActivities(OrdenTemporalService ordenes) {
        return new DespachoActivitiesImpl(new DespachoActivitiesImpl.OrdenLifecycle() {
            @Override
            public com.enlace.evaluacion03.workflow.FirmaProgreso consultarFirmas(String ordenId) {
                return ordenes.consultarFirmas(ordenId);
            }

            @Override
            public com.enlace.evaluacion03.workflow.FirmaProgreso registrarFirma(
                    String ordenId, String firmante, String rol) {
                return ordenes.registrarFirma(ordenId, firmante, rol);
            }

            @Override
            public void iniciarDespacho(String ordenId) {
                ordenes.iniciarDespacho(ordenId);
            }

            @Override
            public void registrarEstado(String ordenId,
                                        com.enlace.evaluacion03.workflow.EstadoDespacho estado) {
                ordenes.registrarEstado(ordenId, estado);
            }
        });
    }

    @Bean(destroyMethod = "close")
    MotorTemporal motorTemporal(
            @Value("${sicor.temporal.target:127.0.0.1:7233}") String target,
            @Value("${sicor.temporal.connect-timeout:5s}") Duration connectTimeout,
            DespachoActivitiesImpl activities
    ) {
        return new MotorTemporal(target, connectTimeout, activities);
    }

    @Bean
    WorkflowClient workflowClient(MotorTemporal motor) {
        return motor.client;
    }

    public static final class MotorTemporal implements AutoCloseable {

        private final WorkflowClient client;
        private final WorkerFactory factory;
        private final WorkflowServiceStubs service;

        MotorTemporal(
                String target,
                Duration connectTimeout,
                DespachoActivitiesImpl activities
        ) {
            WorkflowServiceStubs connectedService = null;
            WorkerFactory startedFactory = null;
            try {
                var options = WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(target)
                        .build();
                connectedService = WorkflowServiceStubs.newConnectedServiceStubs(
                        options, connectTimeout);
                WorkflowClient connectedClient = WorkflowClient.newInstance(connectedService);
                startedFactory = WorkerFactory.newInstance(connectedClient);
                register(startedFactory.newWorker(TASK_QUEUE), activities);
                startedFactory.start();

                service = connectedService;
                client = connectedClient;
                factory = startedFactory;
            } catch (RuntimeException failure) {
                if (startedFactory != null) {
                    startedFactory.shutdown();
                }
                if (connectedService != null) {
                    connectedService.shutdown();
                }
                throw new IllegalStateException(
                        "No se pudo conectar a Temporal en " + target + ". "
                                + "Inicie el servidor de desarrollo antes de SICOR. "
                                + "El puerto de 'temporal server start-dev --port' debe coincidir "
                                + "con el de SICOR_TEMPORAL_TARGET (7233 por defecto).",
                        failure
                );
            }
        }

        private static void register(Worker worker, DespachoActivitiesImpl activities) {
            worker.registerWorkflowImplementationTypes(
                    AccesoWorkflowImpl.class,
                    DespachoWorkflowImpl.class,
                    EntregaUnidadWorkflowImpl.class
            );
            worker.registerActivitiesImplementations(activities);
        }

        @Override
        public void close() {
            factory.shutdown();
            service.shutdown();
        }
    }
}
