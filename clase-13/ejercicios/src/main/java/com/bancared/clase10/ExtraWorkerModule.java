package com.bancared.clase10;
import io.temporal.worker.Worker;
/** Cada clase registra sus Workflows y Activities sin mezclar E/S con orquestación. */
public interface ExtraWorkerModule { void registrar(Worker worker); }
