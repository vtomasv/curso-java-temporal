package com.sigeo.clase11;

import io.temporal.workflow.Workflow;

import java.util.LinkedList;
import java.util.Queue;

public class LongRunningWorkflowImpl implements LongRunningWorkflow {

    private final Queue<String> eventQueue = new LinkedList<>();
    private int processedCount = 0;

    @Override
    public void processEvents(int initialCount) {
        this.processedCount = initialCount;
        
        // TODO(C11-E07): Procesar hasta 50 eventos y luego usar ContinueAsNew
        // Esperar eventos con Workflow.await
        // Incrementar processedCount
        // Llamar a Workflow.continueAsNew(this.processedCount)
        
        throw new UnsupportedOperationException("TODO C11-E07");
    }

    @Override
    public void addEvent(String event) {
        eventQueue.add(event);
    }
}
