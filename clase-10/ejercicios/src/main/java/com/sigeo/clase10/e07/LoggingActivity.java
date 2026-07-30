package com.sigeo.clase10.e07;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface LoggingActivity {
    @ActivityMethod
    void doWork(String sensitiveData);
}
