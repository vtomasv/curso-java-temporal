package com.sigeo.clase03;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> listeners = new ArrayList<>();

    public interface EventListener {
        void onEvent(String event);
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    // TODO(C03-E07): Implementar un método para remover listeners y evitar fugas de memoria.
    // public void unregisterListener(EventListener listener) { ... }

    public void fireEvent(String event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }
}
