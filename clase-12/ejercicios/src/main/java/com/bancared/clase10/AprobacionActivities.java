package com.bancared.clase10;
import io.temporal.activity.ActivityInterface;
import static com.bancared.clase10.Modelos.*;
@ActivityInterface public interface AprobacionActivities { ResultadoWorkflow estadoSolicitud(String clave,String estado,String detalle); Transferencia prepararTransferencia(SolicitudTransferencia solicitud); }
