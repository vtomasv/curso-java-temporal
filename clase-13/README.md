# Clase 13: Integración de IA en Aplicaciones Java (Parte 1)

## Objetivos de la sesión
* Comprender los conceptos básicos de la integración de Modelos de Lenguaje Grande (LLMs) en aplicaciones Java.
* Configurar y utilizar Spring AI y LangChain4j para interactuar con APIs de IA (ej. OpenAI, Anthropic).
* Diseñar y ejecutar prompts efectivos desde código Java mediante el uso de plantillas.
* Extraer y estructurar respuestas de la IA en formato JSON para su procesamiento automatizado en la aplicación.

## Cronograma propuesto (4 horas)
* **0:00 - 0:45**: Introducción a la IA generativa en Java. Comparativa entre Spring AI y LangChain4j.
* **0:45 - 1:30**: Configuración del entorno, dependencias y claves de API. Primer "Hola Mundo" con IA.
* **1:30 - 1:45**: *Descanso*
* **1:45 - 2:30**: Ingeniería de prompts en código: plantillas (Prompt Templates) y paso de variables.
* **2:30 - 3:15**: Estructuración de salidas: forzando respuestas en JSON y mapeo a objetos Java (Records/POJOs).
* **3:15 - 4:00**: Resolución de ejercicios prácticos y revisión de desafíos.

## Ejercicios prácticos

### Ejercicio 1: Guiado - Tu primer Chatbot con Spring AI
**Descripción:** Crea un endpoint REST simple que reciba una pregunta del usuario y devuelva la respuesta generada por un modelo de IA utilizando Spring AI.

**Pasos:**
1. Crea un nuevo proyecto Spring Boot con la dependencia `spring-ai-openai-spring-boot-starter` (o el proveedor de tu elección).
2. Configura tu API Key en el archivo `application.properties` (`spring.ai.openai.api-key=TU_API_KEY`).
3. Crea un controlador REST `ChatController`.
4. Inyecta el bean `ChatClient` (o `ChatModel` dependiendo de la versión de Spring AI).
5. Crea un endpoint GET `/api/chat` que reciba un parámetro `mensaje` y devuelva la respuesta del modelo llamando a `chatClient.call(mensaje)`.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Soy principiante en Spring AI. ¿Puedes darme el código paso a paso para crear un controlador REST básico en Spring Boot que responda preguntas usando la API de OpenAI?"
* *Claude Code / IDE:* "Agrega la dependencia de Spring AI OpenAI al pom.xml y crea un ChatController con un endpoint GET /api/chat que use ChatClient para responder al parámetro 'mensaje'."

### Ejercicio 2: Semi-guiado - Generador de Recetas con Prompt Templates
**Descripción:** Desarrolla un servicio que genere una receta de cocina basada en una lista de ingredientes proporcionada por el usuario, utilizando plantillas de prompts.

**Pistas:**
* Utiliza `PromptTemplate` de Spring AI o la anotación `@SystemMessage` / `@UserMessage` en LangChain4j.
* Define una plantilla de texto como: "Eres un chef experto. Crea una receta usando los siguientes ingredientes: {ingredientes}. Incluye el tiempo de preparación y los pasos."
* Crea un endpoint POST que reciba una lista de strings (ingredientes) y pase esta lista al template para reemplazar la variable `{ingredientes}` antes de llamar al modelo.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "Tengo una lista de ingredientes en Java. ¿Cómo uso PromptTemplate en Spring AI para inyectar esta lista en un prompt y pedirle a la IA que genere una receta estructurada?"
* *Claude Code / IDE:* "Crea un servicio RecipeService que use un PromptTemplate de Spring AI. El template debe pedir una receta con los {ingredientes} dados. Luego expón esto en un endpoint POST que reciba la lista de ingredientes."

### Ejercicio 3: Desafío - Analizador de Sentimientos con Salida JSON Estructurada
**Descripción:** Construye una API que reciba una reseña de un producto (texto) y devuelva un análisis estructurado en formato JSON, mapeado automáticamente a un `Record` de Java.

**Requisitos:**
* Define un `Record` Java llamado `AnalisisResena` con los campos: `sentimiento` (POSITIVO, NEGATIVO, NEUTRAL), `puntuacion` (1 al 10), y `temasPrincipales` (Lista de strings).
* Configura el modelo de IA para que devuelva estrictamente un JSON que coincida con la estructura del `Record`.
* Utiliza los conversores de salida (`BeanOutputConverter` en Spring AI o Structured Outputs en LangChain4j) para transformar la respuesta de texto de la IA directamente en una instancia de `AnalisisResena`.
* Devuelve el objeto `AnalisisResena` como respuesta JSON en tu endpoint REST.

**Asistencia de IA:**
* *Modo Chat (ChatGPT/Claude):* "¿Cómo puedo usar BeanOutputConverter en Spring AI (o Structured Outputs en LangChain4j) para forzar a la IA a devolver un JSON que se mapee automáticamente a un Record de Java específico?"
* *Claude Code / IDE:* "Implementa un endpoint que reciba una reseña de texto. Usa Spring AI con BeanOutputConverter para que el LLM analice el sentimiento, asigne una puntuación del 1 al 10 y extraiga temas clave, devolviendo todo mapeado al Record AnalisisResena."