# Clase 14: Integración de IA en Aplicaciones Java (Parte 2)

## Objetivos de la sesión
- Comprender los conceptos fundamentales de RAG (Retrieval-Augmented Generation) y su importancia en aplicaciones de IA.
- Aprender a generar y utilizar embeddings para representar texto vectorialmente.
- Integrar y consultar bases de datos vectoriales (ej. ChromaDB, Pinecone o PgVector) desde una aplicación Spring Boot.
- Implementar Function Calling para permitir que los modelos de lenguaje interactúen con herramientas y APIs externas.
- Construir un flujo completo de RAG utilizando Spring AI.

## Cronograma propuesto (4 horas)
- **0:00 - 0:45**: Introducción a Embeddings y Bases de Datos Vectoriales. Conceptos teóricos y configuración inicial.
- **0:45 - 1:30**: Implementación de RAG (Retrieval-Augmented Generation) con Spring AI.
- **1:30 - 1:45**: *Descanso*.
- **1:45 - 2:45**: Function Calling: Teoría, configuración y casos de uso prácticos.
- **2:45 - 3:45**: Desarrollo del proyecto integrador (Ejercicios prácticos).
- **3:45 - 4:00**: Revisión de soluciones, dudas y cierre de la sesión.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Generación de Embeddings y Almacenamiento Vectorial
**Descripción:** En este ejercicio, configuraremos una base de datos vectorial en memoria (como `SimpleVectorStore` de Spring AI) y crearemos un endpoint que reciba un texto, genere su embedding y lo almacene.

**Pasos:**
1. Agrega las dependencias de Spring AI para el modelo de embeddings (ej. OpenAI) y el Vector Store.
2. Configura las credenciales (API Key) en el archivo `application.properties`.
3. Crea un servicio `DocumentService` que inyecte `VectorStore`.
4. Implementa un método que convierta un `String` en un objeto `Document` y lo guarde usando `vectorStore.add()`.
5. Crea un controlador REST con un endpoint POST `/api/documents` para probar la funcionalidad.

**Asistencia de IA:**
- *Modo Chat:* "Actúa como un experto en Spring AI. ¿Cómo configuro un `SimpleVectorStore` en una aplicación Spring Boot 3 paso a paso?"
- *Claude Code / Codex:* "Genera un servicio en Java llamado `DocumentService` que utilice `VectorStore` de Spring AI para guardar una lista de documentos de texto."

### Ejercicio 2: Semi-guiado - Implementación de un flujo RAG básico
**Descripción:** Construye un endpoint de chat que utilice RAG. El sistema debe buscar documentos relevantes en la base vectorial antes de enviar la pregunta al LLM, enriqueciendo así el contexto de la respuesta.

**Pistas:**
- Necesitarás inyectar tanto el `ChatClient` (o `ChatModel`) como el `VectorStore`.
- Utiliza `vectorStore.similaritySearch(query)` para obtener los documentos relacionados con la pregunta del usuario.
- Concatena el contenido de los documentos recuperados y úsalos como contexto en el `SystemPrompt` o en el mensaje del usuario antes de llamar al modelo.
- Devuelve la respuesta generada por el LLM.

**Asistencia de IA:**
- *Modo Chat:* "Tengo un `VectorStore` y un `ChatClient` en Spring AI. ¿Me puedes dar un ejemplo de cómo hacer una búsqueda de similitud y usar los resultados como contexto para el LLM?"
- *Claude Code / Codex:* "Completa el método `askWithContext(String question)`: realiza una búsqueda en `vectorStore`, extrae el texto de los documentos, crea un prompt que incluya este contexto y la pregunta, y retorna la respuesta del `ChatClient`."

### Ejercicio 3: Desafío - Asistente Inteligente con Function Calling y RAG
**Descripción:** Crea un asistente virtual para una tienda que combine RAG y Function Calling. El asistente debe ser capaz de responder preguntas sobre las políticas de la tienda (usando RAG con documentos previamente cargados) y consultar el estado de un pedido en tiempo real (usando Function Calling).

**Requisitos:**
- Carga un documento de texto con políticas de devolución en el Vector Store al iniciar la aplicación.
- Define una función (ej. `@Bean` de tipo `Function<OrderRequest, OrderStatus>`) que simule la búsqueda de un pedido por su ID.
- Configura el `ChatClient` para que tenga acceso a esta función.
- El endpoint principal debe recibir la pregunta del usuario, buscar contexto en el Vector Store, y enviar todo al LLM habilitando la llamada a funciones.
- Prueba preguntando: "¿Cuál es la política de devoluciones?" y "Quiero saber el estado de mi pedido 12345".

**Asistencia de IA:**
- *Modo Chat:* "Quiero implementar Function Calling en Spring AI. ¿Cómo registro un `@Bean` de tipo `Function` y cómo le indico al `ChatClient` que puede usar esa función durante una conversación?"
- *Claude Code / Codex:* "Refactoriza este servicio de chat para incluir opciones de llamada a función. Añade la función `getOrderStatus` a las opciones del prompt (prompt options) al hacer la petición al modelo de OpenAI."