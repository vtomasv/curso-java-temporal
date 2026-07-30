package com.sigeo.clase10.e05;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BatchProcessingActivity {
    @ActivityMethod
    int processBatch(int totalRecords);
}
