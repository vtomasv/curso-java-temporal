package com.sigeo.clase17;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalWorkerConfig {

    public static final String TASK_QUEUE = "SIGEO_TASK_QUEUE";

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    // TODO(C17-E01): Configurar el Worker para registrar los Workflows y Activities
    // @Bean
    // public Worker worker(WorkerFactory workerFactory, SigeoActivities sigeoActivities) {
    //     Worker worker = workerFactory.newWorker(TASK_QUEUE);
    //     // Registrar implementaciones
    //     workerFactory.start();
    //     return worker;
    // }
}
