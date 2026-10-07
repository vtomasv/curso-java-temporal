package com.sigeo.clase10.e07;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingActivityImpl implements LoggingActivity {

    private static final Logger log = LoggerFactory.getLogger(LoggingActivityImpl.class);

    @Override
    public void doWork(String sensitiveData) {
        ActivityInfo info = Activity.getExecutionContext().getInfo();
        log.info(
                "Executing activity. WorkflowId: {}, ActivityId: {}, Attempt: {}",
                info.getWorkflowId(),
                info.getActivityId(),
                info.getAttempt()
        );

        if (info.getAttempt() < 2) {
            throw new RuntimeException("Simulated transient error");
        }
    }
}
