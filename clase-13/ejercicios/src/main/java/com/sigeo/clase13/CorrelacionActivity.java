package com.sigeo.clase13;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CorrelacionActivity {
    @ActivityMethod
    String procesarActividad(String input);
}
