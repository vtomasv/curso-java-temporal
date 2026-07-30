# Solucionario Clase 15: Middleware de mensajes, colas y procesamiento asíncrono

Este documento contiene las soluciones paso a paso para los ejercicios de la Clase 15.

## C15-E01 — RabbitMQ local

**Por qué:** Es fundamental contar con un entorno local reproducible para desarrollar y probar integraciones con RabbitMQ. Docker Compose facilita esta tarea aislando la configuración.

```yaml
# docker-compose.yml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: sigeo-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: sigeo
      RABBITMQ_DEFAULT_PASS: sigeo_dev
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

  postgres:
    image: postgres:15
    container_name: sigeo-postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: sigeo
      POSTGRES_USER: sigeo
      POSTGRES_PASSWORD: sigeo_dev
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  rabbitmq_data:
  postgres_data:
```

## C15-E02 — Notificación asíncrona

**Por qué:** Spring AMQP simplifica la publicación y consumo de mensajes. Usar un `correlationId` permite rastrear el flujo del mensaje a través del sistema.

```java
// NotificationPublisher.java
@Component
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(NotificationRequested notification) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            notification,
            message -> {
                message.getMessageProperties().setCorrelationId(notification.correlationId());
                return message;
            }
        );
    }
}

// NotificationConsumer.java
@Component
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(NotificationRequested notification, @Header(AmqpHeaders.CORRELATION_ID) String correlationId) {
        log.info("Received notification request: {} with correlationId: {}", notification, correlationId);
        // Process notification
    }
}
```

## C15-E03 — Mensaje inválido

**Por qué:** Validar los mensajes antes de procesarlos evita errores inesperados y previene que mensajes malformados entren en un ciclo infinito de reintentos (poison messages).

```java
// NotificationConsumer.java (actualizado)
@Component
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(@Valid NotificationRequested notification, @Header(AmqpHeaders.CORRELATION_ID) String correlationId) {
        log.info("Received valid notification request: {} with correlationId: {}", notification, correlationId);
        // Process notification
    }
}

// NotificationRequested.java
public record NotificationRequested(
    @NotBlank String correlationId,
    @NotBlank String recipient,
    @NotBlank String message
) {}
```

## C15-E04 — Duplicado de notificación

**Por qué:** En sistemas distribuidos, la entrega de mensajes suele ser "at-least-once". Un consumidor idempotente garantiza que procesar el mismo mensaje varias veces tenga el mismo efecto que procesarlo una sola vez, utilizando un patrón Inbox.

```java
// IdempotentConsumer.java
@Component
public class IdempotentConsumer {
    private final InboxRepository inboxRepository;
    private final NotificationService notificationService;

    public IdempotentConsumer(InboxRepository inboxRepository, NotificationService notificationService) {
        this.inboxRepository = inboxRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(NotificationRequested notification, @Header(AmqpHeaders.MESSAGE_ID) String messageId) {
        if (inboxRepository.existsById(messageId)) {
            return; // Already processed
        }
        
        notificationService.send(notification);
        
        InboxMessage inboxMessage = new InboxMessage(messageId, LocalDateTime.now());
        inboxRepository.save(inboxMessage);
    }
}
```

## C15-E05 — Poison message

**Por qué:** Configurar una Dead Letter Queue (DLQ) permite apartar los mensajes que fallan repetidamente, evitando que bloqueen el procesamiento de otros mensajes en la cola principal.

```java
// RabbitMQConfig.java
@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notification.queue";
    public static final String EXCHANGE_NAME = "notification.exchange";
    public static final String ROUTING_KEY = "notification.routing.key";
    public static final String DLQ_NAME = "notification.dlq";
    public static final String DLX_NAME = "notification.dlx";

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_NAME).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY)
                .build();
    }
    
    // ... other beans
}
```

## C15-E06 — Publicación confiable

**Por qué:** El patrón Outbox garantiza la consistencia entre la base de datos y el broker de mensajes. Se guarda el evento en la misma transacción que la entidad de negocio, y luego un proceso asíncrono lo publica.

```java
// BusinessService.java
@Service
public class BusinessService {
    private final BusinessEntityRepository entityRepository;
    private final OutboxRepository outboxRepository;

    @Transactional
    public void performAction(BusinessRequest request) {
        BusinessEntity entity = new BusinessEntity(request.getData());
        entityRepository.save(entity);

        OutboxEvent event = new OutboxEvent(
            UUID.randomUUID().toString(),
            "EntityCreated",
            convertToJson(entity)
        );
        outboxRepository.save(event);
    }
}

// OutboxPublisherJob.java
@Component
public class OutboxPublisherJob {
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatus("PENDING");
        for (OutboxEvent event : pendingEvents) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event.getPayload());
            event.setStatus("PUBLISHED");
            outboxRepository.save(event);
        }
    }
}
```

## C15-E07 — Mensaje inicia o señala Workflow

**Por qué:** Integrar mensajería con Temporal requiere cuidado para no duplicar responsabilidades. Un consumidor puede actuar como puente, iniciando o enviando señales a un Workflow utilizando un ID de negocio para garantizar la idempotencia.

```java
// TemporalBridgeConsumer.java
@Component
public class TemporalBridgeConsumer {
    private final WorkflowClient workflowClient;

    public TemporalBridgeConsumer(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @RabbitListener(queues = RabbitMQConfig.WORKFLOW_QUEUE)
    public void handleWorkflowEvent(WorkflowEvent event) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("SIGEO_TASK_QUEUE")
                .setWorkflowId(event.businessId()) // Idempotency key
                .build();

        MyWorkflow workflow = workflowClient.newWorkflowStub(MyWorkflow.class, options);
        
        try {
            // Starts workflow if it doesn't exist, ignores if it does (due to same WorkflowId)
            WorkflowClient.start(workflow::process, event.data());
        } catch (WorkflowExecutionAlreadyStartedException e) {
            // Workflow already running, maybe send a signal instead
            workflow.updateData(event.data());
        }
    }
}
```

## C15-E08 — Evento v1→v2

**Por qué:** Los esquemas de mensajes evolucionan. Es crucial diseñar consumidores tolerantes a versiones, ignorando campos desconocidos y manejando valores por defecto para campos nuevos, asegurando compatibilidad hacia atrás.

```java
// JacksonConfig.java
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}

// NotificationRequestedV2.java
public record NotificationRequestedV2(
    @NotBlank String correlationId,
    @NotBlank String recipient,
    @NotBlank String message,
    String priority // New field, nullable for backwards compatibility
) {
    public String getPriorityOrDefault() {
        return priority != null ? priority : "NORMAL";
    }
}
```
