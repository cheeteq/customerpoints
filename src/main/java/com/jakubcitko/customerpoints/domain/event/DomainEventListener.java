package com.jakubcitko.customerpoints.domain.event;

public interface DomainEventListener<T extends DomainEvent> {
    void handle(T event);
}

