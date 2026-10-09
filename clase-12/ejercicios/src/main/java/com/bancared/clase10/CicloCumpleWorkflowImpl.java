package com.bancared.clase10;
import io.temporal.workflow.*;
import java.time.*;
public class CicloCumpleWorkflowImpl implements CicloCumpleWorkflow {
    private int avance;
    public int avance(){return avance;}
    public String simular(String fecha,int noches,int periodoSegundos,int procesadas){
        if(noches<1||noches>365||periodoSegundos<1||periodoSegundos>86400)throw new IllegalArgumentException("Parámetros del ciclo fuera de rango.");
        avance=procesadas;LocalDate dia=LocalDate.parse(fecha);
        for(int n=0;n<noches;n++){
            var hijo=Workflow.newChildWorkflowStub(CumpleWorkflow.class,ChildWorkflowOptions.newBuilder().setWorkflowId(Workflow.getInfo().getWorkflowId()+"-dia-"+avance).build());
            hijo.ejecutar(dia.toString());avance++;dia=dia.plusDays(1);
            if(n+1<noches){Workflow.sleep(Duration.ofSeconds(periodoSegundos));
                if((n+1)%30==0){Workflow.newContinueAsNewStub(CicloCumpleWorkflow.class).simular(dia.toString(),noches-n-1,periodoSegundos,avance);}
            }
        }
        return "Noches procesadas: "+avance;
    }
}
