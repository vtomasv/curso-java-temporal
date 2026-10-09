package com.bancared.clase10;
import io.temporal.activity.ActivityInterface;
import static com.bancared.clase10.AsistenteModelos.*;
@ActivityInterface public interface AsistenteActivities {Propuesta obtenerPropuesta(Consulta consulta);Propuesta validarPropuesta(String pregunta,Propuesta propuesta);Respuesta guardarRespuesta(Consulta consulta,Propuesta propuesta);}
