package com.sigeo.clase16.config;

import com.sigeo.clase16.workflow.SolicitudActivitiesImpl;
import com.sigeo.clase16.workflow.SolicitudWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    public static final String TASK_QUEUE = "SIGEO_TASK_QUEUE";

    @Value("${temporal.target:localhost:7233}")
    private String target;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(target)
                        .build()
        );
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs,
                WorkflowClientOptions.newBuilder().build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient, SolicitudActivitiesImpl activities) {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(TASK_QUEUE);
        
        worker.registerWorkflowImplementationTypes(SolicitudWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        
        factory.start();
        return factory;
    }
}
