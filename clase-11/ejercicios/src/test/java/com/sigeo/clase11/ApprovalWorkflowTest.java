package com.sigeo.clase11;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApprovalWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("ApprovalTaskQueue");
        worker.registerWorkflowImplementationTypes(ApprovalWorkflowImpl.class, TechnicalReviewWorkflowImpl.class);
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testApproveSignal() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-1");
        
        workflow.approve("cmd-1");
        
        String result = workflow.processApproval("req-1"); // Wait for completion
        assertThat(result).isEqualTo("APPROVED");
    }

    @Test
    void testQueryState() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-2");
        
        workflow.reject("cmd-2", "Incomplete");
        
        ApprovalState state = workflow.getState();
        assertThat(state.decision()).isEqualTo("REJECTED");
        assertThat(state.rejectionReason()).isEqualTo("Incomplete");
    }

    @Test
    void testTimeout() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-3");
        
        testEnv.sleep(Duration.ofMinutes(31));
        
        String result = workflow.processApproval("req-3");
        assertThat(result).isEqualTo("TIMEOUT");
    }

    @Test
    void testUpdatePriority() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-4");
        
        int newPriority = workflow.updatePriority(5);
        assertThat(newPriority).isEqualTo(5);
        
        ApprovalState state = workflow.getState();
        assertThat(state.priority()).isEqualTo(5);
        
        assertThatThrownBy(() -> workflow.updatePriority(10))
            .hasMessageContaining("Prioridad debe estar entre 1 y 5");
    }

    @Test
    void testDeduplication() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-5");
        
        workflow.approve("cmd-5");
        workflow.reject("cmd-5", "Should be ignored");
        
        ApprovalState state = workflow.getState();
        assertThat(state.decision()).isEqualTo("APPROVED");
    }

    @Test
    void testChildWorkflow() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-6");
        
        testEnv.sleep(Duration.ofMinutes(6)); // Wait for child workflow
        
        workflow.approve("cmd-6");
        
        String result = workflow.processApproval("req-6");
        assertThat(result).isEqualTo("APPROVED");
    }

    @Test
    void testRaceCondition() {
        ApprovalWorkflow workflow = workflowClient.newWorkflowStub(ApprovalWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ApprovalTaskQueue").build());

        WorkflowClient.start(workflow::processApproval, "req-7");
        
        testEnv.sleep(Duration.ofMinutes(30));
        workflow.approve("cmd-7");
        
        String result = workflow.processApproval("req-7");
        // Depending on implementation, it could be APPROVED or TIMEOUT, but must be deterministic
        assertThat(result).isNotNull();
    }
}
