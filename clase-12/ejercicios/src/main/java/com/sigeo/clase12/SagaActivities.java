package com.sigeo.clase12;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SagaActivities {

    @ActivityMethod
    void reserveResource(String reservationId);

    @ActivityMethod
    void cancelResource(String reservationId);

    @ActivityMethod
    void allocateBudget(String reservationId, boolean fail);

    @ActivityMethod
    void releaseBudget(String reservationId);

    @ActivityMethod
    void scheduleAgenda(String reservationId, boolean fail);

    @ActivityMethod
    void cancelAgenda(String reservationId);

    @ActivityMethod
    void sendNotification(String reservationId, boolean fail);
}
