package com.sigeo.clase14;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WorkerMain {

    public static void main(String[] args) {
        SpringApplication.run(WorkerMain.class, args);
    }

    @Bean
    public CommandLineRunner startWorker(AnalisisAiActivityImpl analisisAiActivity) {
        return args -> {
            // Configuración básica del worker de Temporal
            WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
            WorkflowClient client = WorkflowClient.newInstance(service);
            WorkerFactory factory = WorkerFactory.newInstance(client);

            Worker worker = factory.newWorker("AnalisisTaskQueue");
            worker.registerWorkflowImplementationTypes(AnalisisWorkflowImpl.class);
            worker.registerActivitiesImplementations(analisisAiActivity);

            factory.start();
            System.out.println("Worker iniciado en AnalisisTaskQueue...");
        };
    }
}
