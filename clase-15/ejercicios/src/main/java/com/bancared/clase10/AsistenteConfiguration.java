package com.bancared.clase10;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.bancared.clase10.AsistenteModelos.*;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class AsistenteConfiguration {
    @Bean AsistenteStore asistenteStore(JdbcTemplate j){return new AsistenteStore(j);}
    @Bean ProveedorAsistente proveedor(@Value("${banking.ai.mode:mock}")String mode,@Value("${banking.ai.url:}")String url,@Value("${banking.ai.key:}")String key,@Value("${banking.ai.model:}")String model){return new ProveedorAsistente(mode,url,key,model);}
    @Bean ExtraWorkerModule asistenteModule(AsistenteStore s,ProveedorAsistente p){return w->{w.registerWorkflowImplementationTypes(AsistenteWorkflowImpl.class);w.registerActivitiesImplementations(new AsistenteActivities(){
        public Propuesta obtenerPropuesta(Consulta c){var docs=BaseConocimiento.recuperar(c.pregunta());try{return p.consultar(c,docs);}catch(RuntimeException e){return RespuestaSegura.fallback(docs);}}
        public Propuesta validarPropuesta(String q,Propuesta r){return RespuestaSegura.validar(r,BaseConocimiento.recuperar(q));}
        public Respuesta guardarRespuesta(Consulta c,Propuesta r){return s.guardar(c,r);}
    });};}
}
