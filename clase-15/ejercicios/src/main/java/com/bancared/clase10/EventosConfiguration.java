package com.bancared.clase10;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.bancared.clase10.EventosModelos.*;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class EventosConfiguration {
    @Bean OutboxStore outboxStore(JdbcTemplate j){return new OutboxStore(j);}
    @Bean InboxStore inboxStore(JdbcTemplate j){return new InboxStore(j);}
    @Bean TransporteMensajes transporte(JdbcTemplate j,@Value("${banking.broker.mode:local}")String mode,@Value("${banking.broker.uri:amqp://curso:laboratorio@127.0.0.1:5672/bancared}")String uri){if("local".equals(mode))return new ColaSql(j);if("rabbit".equals(mode))return new RabbitTransporte(uri);throw new IllegalArgumentException("Modo de broker desconocido.");}
    @Bean ExtraWorkerModule eventosModule(OutboxStore o,InboxStore i,TransporteMensajes t){return w->{w.registerWorkflowImplementationTypes(EventosWorkflowImpl.class);w.registerActivitiesImplementations(new EventosActivities(){public java.util.List<Evento> pendientes(){return o.pendientes();}public void publicarEvento(Evento e){o.confirmar(e.id(),t.publicar(e));}public int consumirMensajes(int max){return t.consumir(max,i);}});};}
}
