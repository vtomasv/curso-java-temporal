package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EventManagerTest {

    @Test
    void shouldNotLeakMemoryWhenListenersAreUnregistered() {
        EventManager manager = new EventManager();
        EventManager.EventListener listener = event -> {};
        
        manager.registerListener(listener);
        assertThat(manager.getListenerCount()).isEqualTo(1);
        
        // TODO(C03-E07): Descomentar cuando se implemente unregisterListener
        // manager.unregisterListener(listener);
        // assertThat(manager.getListenerCount()).isEqualTo(0);
    }
}
