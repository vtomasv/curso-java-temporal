package com.sigeo.clase10.e04;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ReservationActivity {
    @ActivityMethod
    String makeReservation(String itemId, String idempotencyKey);
}
