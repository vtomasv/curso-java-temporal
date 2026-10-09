package com.bancared.clase10;
import io.temporal.workflow.*;
import static com.bancared.clase10.LiquidacionModelos.*;
@WorkflowInterface public interface LiquidacionWorkflow {@WorkflowMethod Lote liquidar(String id);}
