package com.enlace.evaluacion03.config;

import com.enlace.evaluacion03.service.EstadoOrdenTemporalService;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    public static final String TASK_QUEUE = "SICOR-EV03";

    @Bean
    DespachoActivitiesImpl despachoActivities(EstadoOrdenTemporalService estados) {
        return new DespachoActivitiesImpl(estados::registrar);
    }

    @Bean(destroyMethod = "close")
    MotorTemporal motorTemporal(
            @Value("${sicor.temporal.mode:embedded}") String mode,
            @Value("${sicor.temporal.target:127.0.0.1:7233}") String target,
            DespachoActivitiesImpl activities
    ) {
        return new MotorTemporal(mode, target, activities);
    }

    @Bean
    WorkflowClient workflowClient(MotorTemporal motor) {
        return motor.client;
    }

    public static final class MotorTemporal implements AutoCloseable {

        private final WorkflowClient client;
        private final WorkerFactory factory;
        private final WorkflowServiceStubs service;
        private final TestWorkflowEnvironment embedded;

        MotorTemporal(String mode, String target, DespachoActivitiesImpl activities) {
            if ("embedded".equalsIgnoreCase(mode)) {
                embedded = TestWorkflowEnvironment.newInstance();
                service = null;
                factory = null;
                client = embedded.getWorkflowClient();
                register(embedded.newWorker(TASK_QUEUE), activities);
                embedded.start();
                return;
            }

            if (!"external".equalsIgnoreCase(mode)) {
                throw new IllegalArgumentException(
                        "sicor.temporal.mode debe ser embedded o external");
            }

            embedded = null;
            service = WorkflowServiceStubs.newServiceStubs(
                    WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());
            client = WorkflowClient.newInstance(service);
            factory = WorkerFactory.newInstance(client);
            register(factory.newWorker(TASK_QUEUE), activities);
            factory.start();
        }

        private static void register(Worker worker, DespachoActivitiesImpl activities) {
            worker.registerWorkflowImplementationTypes(
                    DespachoWorkflowImpl.class,
                    EntregaUnidadWorkflowImpl.class
            );
            worker.registerActivitiesImplementations(activities);
        }

        @Override
        public void close() {
            if (embedded != null) {
                embedded.close();
                return;
            }
            factory.shutdown();
            service.shutdown();
        }
    }
}
