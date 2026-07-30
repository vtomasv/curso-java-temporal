package com.sigeo.clase17;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SigeoActivities {

    @ActivityMethod
    void approveRequest(String requestId);

    @ActivityMethod
    void notifyUser(String requestId);
    
    @ActivityMethod
    void compensateRequest(String requestId);
}
