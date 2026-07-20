package com.curso.solucion09;

import io.temporal.spring.boot.WorkflowImpl;

@WorkflowImpl(taskQueues = "HelloWorldTaskQueue")
public class HelloWorldWorkflowImpl implements HelloWorldWorkflow {

    @Override
    public String getGreeting(String name) {
        return "Hola " + name + " desde Temporal!";
    }
}
