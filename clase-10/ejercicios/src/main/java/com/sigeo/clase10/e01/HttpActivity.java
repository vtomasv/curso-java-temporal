package com.sigeo.clase10.e01;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface HttpActivity {
    @ActivityMethod
    String callExternalService(int latencySeconds);
}
