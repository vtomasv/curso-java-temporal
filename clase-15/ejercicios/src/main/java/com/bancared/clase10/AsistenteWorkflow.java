package com.bancared.clase10;
import io.temporal.workflow.*;
import static com.bancared.clase10.AsistenteModelos.*;
@WorkflowInterface public interface AsistenteWorkflow {@WorkflowMethod Respuesta responder(Consulta consulta);}
