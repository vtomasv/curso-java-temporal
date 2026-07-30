package com.sigeo.clase13;

import io.temporal.testing.WorkflowReplayer;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplayWorkflowTest {
    @Test
    void testReplayHistoriaIncompatible() throws Exception {
        // TODO(C13-E03): Descomentar y verificar que falla con NonDeterministicWorkflowError
        // File historyFile = new File("src/test/resources/historia_incompatible.json");
        // assertThatThrownBy(() -> 
        //     WorkflowReplayer.replayWorkflowExecution(historyFile, ReplayWorkflowImpl.class)
        // ).hasMessageContaining("NonDeterministicWorkflowError");
    }
}
