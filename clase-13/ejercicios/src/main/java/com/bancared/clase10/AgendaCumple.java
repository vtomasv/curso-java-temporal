package com.bancared.clase10;
import io.temporal.client.schedules.*;
import java.util.List;
public final class AgendaCumple {
    private AgendaCumple() {}
    public static ScheduleSpec especificacion(){
        return ScheduleSpec.newBuilder().setTimeZoneName("America/Santiago").setCalendars(List.of(
            ScheduleCalendarSpec.newBuilder().setHour(List.of(new ScheduleRange(0))).setMinutes(List.of(new ScheduleRange(5))).build()
        )).build();
    }
}
