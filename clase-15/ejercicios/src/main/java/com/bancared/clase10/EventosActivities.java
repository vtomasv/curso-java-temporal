package com.bancared.clase10;
import io.temporal.activity.ActivityInterface;
import java.util.List;
import static com.bancared.clase10.EventosModelos.*;
@ActivityInterface public interface EventosActivities {List<Evento> pendientes();void publicarEvento(Evento e);int consumirMensajes(int max);}
