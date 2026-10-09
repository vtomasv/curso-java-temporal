package com.bancared.clase10;
import io.temporal.client.schedules.*;
import java.util.List;
public final class AgendaCumple {
    private AgendaCumple() {}
    public static ScheduleSpec especificacion(){
        // TODO(C11-E03): diario a las 00:05, zona America/Santiago. Usar ScheduleCalendarSpec y ScheduleRange.
        return ScheduleSpec.newBuilder().setTimeZoneName("UTC").build();
    }
}
