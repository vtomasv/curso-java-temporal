# Guía docente · clase 12

No distribuir referencia durante el lab. Los alumnos reciben las pantallas y completan tres TODO. Generar una carpeta nueva desde la raíz del repositorio:

```bash
python3 clase-12/solucion/preparar.py ../bancared-c12-resuelta
cd ../bancared-c12-resuelta
./mvnw -Plaboratorios clean verify
python3 scripts/laboratorio.py --reset
```

Iniciar Temporal externo antes del launcher en otra terminal. `--hasta e01` entrega base para E02; `--hasta e02` para E03. La base de clase 13 ya contiene resueltos estos incrementos; no depende de entregas. No sobrescribir una carpeta del alumno.

Orden de los archivos: PoliticaAprobacion.java, AprobacionWorkflowImpl.java, CambiosAprobacion.java. Los pasos de cada escenario están en ../ejercicios/LABORATORIOS.md; repetir con datos iniciales y claves nuevas. Si se eligió modo embebido/broker RabbitMQ/proveedor remoto, mantener sus flags tras recompilar.

Las vistas de PPT se generan con HTTP real y se identifican como renderizaciones. Capturar con navegador y consola Temporal en el equipo docente para registrar evidencia externa. Los controles de Schedule requieren el dev server externo.
