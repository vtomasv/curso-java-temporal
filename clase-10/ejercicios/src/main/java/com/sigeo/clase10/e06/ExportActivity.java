package com.sigeo.clase10.e06;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ExportActivity {
    @ActivityMethod
    void exportData();
}
