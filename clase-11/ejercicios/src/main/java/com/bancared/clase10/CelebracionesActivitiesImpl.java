package com.bancared.clase10;
import java.time.*;
import java.util.*;
public class CelebracionesActivitiesImpl implements CelebracionesActivities {
    private final BankGateway bancos;private final CelebracionesStore store;
    public CelebracionesActivitiesImpl(BankGateway bancos,CelebracionesStore store){this.bancos=bancos;this.store=store;}
    public String fechaChile(){return LocalDate.now(ZoneId.of("America/Santiago")).toString();}
    public int reemplazarSaludos(String fecha){LocalDate dia=LocalDate.parse(fecha);List<String> cuentas=new ArrayList<>();
        // Se consulta a ambos bancos antes de reemplazar las marcas: un banco caído no borra datos a medias.
        for(String banco:List.of("CORDILLERA","PACIFICO"))for(var c:bancos.cuentas(banco))if(PoliticaCumple.corresponde(LocalDate.parse(c.nacimiento()),dia))cuentas.add(c.id());
        return store.reemplazar(dia.toString(),cuentas);
    }
}
