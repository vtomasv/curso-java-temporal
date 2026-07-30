package com.sigeo.clase10.e02;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ServiceActivity {
    @ActivityMethod
    String processRequest(String input);
}
