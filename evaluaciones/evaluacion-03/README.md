# Evaluación 3 · SICOR

La guía del examen está disponible en dos formatos con las mismas instrucciones, reglas, puntajes y entrega.

- [Enunciado en Word](01_Prueba_Practica_SICOR_Estudiantes.docx)
- [Enunciado en Markdown e instrucciones de ejecución](ejercicios/README.md)
- [Pistas autorizadas](solucion/SOLUCION_GUIADA.md)

Distribuya esta carpeta completa. El alumno entrega un único ZIP con el proyecto fuente de `ejercicios/`, sus recursos, Maven Wrapper y sus pruebas. El corrector docente y cualquier pauta interna se distribuyen por separado.

Desde `ejercicios/`, `./mvnw clean verify` comprueba la plantilla inicial con Java 25. Para corregir una entrega, ejecute `./mvnw clean verify -Pev03-all` y pruebe el recorrido de las dos órdenes descrito en el enunciado.
