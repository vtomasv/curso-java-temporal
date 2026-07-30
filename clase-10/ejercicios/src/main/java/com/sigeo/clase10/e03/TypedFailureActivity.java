package com.sigeo.clase10.e03;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TypedFailureActivity {
    @ActivityMethod
    void validateData(String data);
}
