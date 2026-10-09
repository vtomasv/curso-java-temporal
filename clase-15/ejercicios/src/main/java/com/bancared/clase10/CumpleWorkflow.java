package com.bancared.clase10;
import io.temporal.workflow.*;
@WorkflowInterface public interface CumpleWorkflow { @WorkflowMethod String ejecutar(String fecha); }
