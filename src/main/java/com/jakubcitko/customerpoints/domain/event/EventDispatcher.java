package com.jakubcitko.customerpoints.domain.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDispatcher {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<Class<? extends DomainEvent>, List<DomainEventListener<? extends DomainEvent>>> listeners = new HashMap<>();

    public <T extends DomainEvent> void addListener(DomainEventListener<T> listener, Class<T> cls) {
        var list = listeners.getOrDefault(cls, new LinkedList<>());
        list.add(listener);
        listeners.put(cls, list);
    }

    public <T extends DomainEvent> void dispatch(T event) {
        var list = listeners.get(event.getClass());
        if (list != null) {
            list.forEach(listener -> {
                executor.submit(() -> {
                    try {
                        ((DomainEventListener<T>) listener).handle(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }

}
