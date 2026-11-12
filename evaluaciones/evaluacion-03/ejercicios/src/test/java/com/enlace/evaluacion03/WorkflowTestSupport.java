package com.enlace.evaluacion03;

import com.enlace.evaluacion03.workflow.DespachoActivities;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

final class WorkflowTestSupport implements AutoCloseable {

    private static final String TASK_QUEUE = "ev03-pruebas";

    final TestWorkflowEnvironment environment = TestWorkflowEnvironment.newInstance();
    final ActivitiesObservables activities = new ActivitiesObservables();

    WorkflowTestSupport() {
        Worker worker = environment.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(
                DespachoWorkflowImpl.class, EntregaUnidadWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        environment.start();
    }

    DespachoWorkflow nuevoWorkflow(String workflowId) {
        return environment.getWorkflowClient().newWorkflowStub(
                DespachoWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(workflowId)
                        .build());
    }

    void despuesDe(Duration demora, Runnable accion) {
        environment.registerDelayedCallback(demora, accion);
    }

    @Override
    public void close() {
        environment.close();
    }

    static final class ActivitiesObservables implements DespachoActivities {

        private final ConcurrentMap<String, ReciboEnvio> entregados = new ConcurrentHashMap<>();
        private final List<String> ordenDeEnvio = new CopyOnWriteArrayList<>();
        private final Set<String> compensadas = ConcurrentHashMap.newKeySet();
        private final List<EstadoDespacho> estados = new CopyOnWriteArrayList<>();

        @Override
        public ReciboEnvio enviar(String ordenId, String unidad, String comandoId) {
            return entregados.computeIfAbsent(ordenId + ":" + unidad, ignorada -> {
                ordenDeEnvio.add(unidad);
                return new ReciboEnvio(ordenId, unidad, "REC-" + unidad);
            });
        }

        @Override
        public void registrarEstado(String ordenId, EstadoDespacho estado) {
            estados.add(estado);
        }

        @Override
        public void compensar(String ordenId, String unidad, String reciboId) {
            ReciboEnvio recibo = entregados.get(ordenId + ":" + unidad);
            if (recibo != null && recibo.reciboId().equals(reciboId)) compensadas.add(unidad);
        }

        List<String> ordenDeEnvio() {
            return List.copyOf(ordenDeEnvio);
        }

        Set<String> unidadesEntregadas() {
            return entregados.values().stream().map(ReciboEnvio::unidad).collect(java.util.stream.Collectors.toSet());
        }

        Set<String> compensadas() {
            return Set.copyOf(compensadas);
        }

        List<EstadoDespacho> estados() {
            return List.copyOf(estados);
        }
    }
}
