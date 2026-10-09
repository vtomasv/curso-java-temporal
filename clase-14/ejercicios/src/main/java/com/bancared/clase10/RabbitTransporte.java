package com.bancared.clase10;
import com.rabbitmq.client.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import tools.jackson.databind.json.JsonMapper;
import static com.bancared.clase10.EventosModelos.*;
/** Confirmación del broker y ack solo después de persistir inbox. */
public class RabbitTransporte implements TransporteMensajes {
    private final ConnectionFactory factory=new ConnectionFactory();private final JsonMapper json=JsonMapper.builder().build();
    private final String cola="bancared.notifications",dlq="bancared.dlq";
    public RabbitTransporte(String uri){try{factory.setUri(uri);factory.setConnectionTimeout(3000);}catch(Exception e){throw new IllegalArgumentException("URI AMQP inválida.");}}
    private Channel canal(Connection c)throws Exception{Channel ch=c.createChannel();ch.queueDeclare(cola,true,false,false,null);ch.queueDeclare(dlq,true,false,false,null);ch.confirmSelect();return ch;}
    private void enviar(Channel ch,String q,byte[] cuerpo,String id)throws Exception{var propiedades=new AMQP.BasicProperties.Builder().deliveryMode(2).contentType("application/json").messageId(id).build();ch.basicPublish("",q,true,propiedades,cuerpo);ch.waitForConfirmsOrDie(3000);}
    public boolean publicar(Evento e){try(var c=factory.newConnection();var ch=canal(c)){enviar(ch,cola,json.writeValueAsBytes(e),e.id());return true;}catch(Exception x){throw new IllegalStateException("RabbitMQ no confirmó la publicación.",x);}}
    public int consumir(int max,InboxStore inbox){try(var c=factory.newConnection();var ch=canal(c)){int total=0;for(int n=0;n<max;n++){var delivery=ch.basicGet(cola,false);if(delivery==null)break;Evento e=null;try{e=json.readValue(delivery.getBody(),Evento.class);}catch(RuntimeException invalido){/* Payload ilegible se enviará a DLQ. */}
        if(e==null||!PoliticaMensaje.valido(e)){enviar(ch,dlq,delivery.getBody(),delivery.getProps().getMessageId());ch.basicAck(delivery.getEnvelope().getDeliveryTag(),false);}
        else{inbox.recibir(e);ch.basicAck(delivery.getEnvelope().getDeliveryTag(),false);}total++;}return total;
    }catch(Exception x){throw new IllegalStateException("Consumo incompleto; el mensaje sin ack vuelve a la cola.",x);}}
    public void veneno(String clave){publicar(new Evento(clave,"TRANSFERENCIA_COMPLETADA","tx-veneno",100,99));}
    public List<Map<String,Object>> vista(){try(var c=factory.newConnection();var ch=canal(c)){return List.of(Map.of("cola",cola,"pendientes",ch.messageCount(cola)),Map.of("cola",dlq,"pendientes",ch.messageCount(dlq)));}catch(Exception x){return List.of(Map.of("estado","BROKER_NO_DISPONIBLE"));}}
    public String modo(){return "RABBITMQ_AMQP";}
}
