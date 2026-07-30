package com.sigeo.clase10.e07;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingActivityImpl implements LoggingActivity {

    private static final Logger log = LoggerFactory.getLogger(LoggingActivityImpl.class);
    private int attempt = 0;

    @Override
    public void doWork(String sensitiveData) {
        attempt++;
        
        // TODO(C10-E07): Obtener ActivityInfo desde Activity.getExecutionContext().getInfo()
        // y loguear el workflowId, activityId y el intento actual (attempt).
        // NO loguear 'sensitiveData'.
        
        if (attempt < 2) {
            throw new RuntimeException("Simulated transient error");
        }
    }
}
