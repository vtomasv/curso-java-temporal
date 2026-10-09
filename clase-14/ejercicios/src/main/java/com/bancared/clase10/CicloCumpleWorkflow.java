package com.bancared.clase10;
import io.temporal.workflow.*;
@WorkflowInterface public interface CicloCumpleWorkflow { @WorkflowMethod String simular(String fecha,int noches,int periodoSegundos,int procesadas); @QueryMethod int avance(); }
