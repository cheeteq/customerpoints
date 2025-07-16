package com.jakubcitko.customerpoints.application;

import com.jakubcitko.customerpoints.domain.exception.CustomerNotFoundException;
import com.jakubcitko.customerpoints.domain.event.EventDispatcher;
import com.jakubcitko.customerpoints.domain.exception.NegativeBalanceException;
import com.jakubcitko.customerpoints.domain.model.Customer;
import com.jakubcitko.customerpoints.domain.event.DomainEventListener;
import com.jakubcitko.customerpoints.ports.out.notification.LowBalanceWarningEvent;
import com.jakubcitko.customerpoints.ports.out.persistance.CustomerRepository;

import java.util.Objects;

public class SystemEngineImpl implements SystemEngine {
    private final int WARNING_THRESHOLD = 10;
    private final CustomerRepository customerRepository;
    private final EventDispatcher eventDispatcher;

    public SystemEngineImpl (CustomerRepository customerRepository, EventDispatcher eventDispatcher, DomainEventListener<LowBalanceWarningEvent> eventListener) {
        this.customerRepository = customerRepository;
        this.eventDispatcher = eventDispatcher;
        eventDispatcher.addListener(eventListener, LowBalanceWarningEvent.class);
    }
    @Override
    public void earn(String customerId, int points) {
        Integer currentPoints = customerRepository.getPoints(customerId);

        if(Objects.isNull(currentPoints))
            currentPoints = 0;

        int newBalance = currentPoints + points;
        customerRepository.save(customerId, newBalance);
    }

    @Override
    public void redeem(String customerId, int points) {
        Integer currentPoints = customerRepository.getPoints(customerId);

        if(Objects.isNull(currentPoints))
            throw new CustomerNotFoundException("Customer " + customerId + " not found");
        if(points > currentPoints)
            throw new NegativeBalanceException("Cannot redeem points. Current balance is too low.");

        int newBalance = currentPoints - points;
        customerRepository.save(customerId, newBalance);

        if(newBalance < WARNING_THRESHOLD)
            eventDispatcher.dispatch(new LowBalanceWarningEvent(new Customer(customerId, newBalance)));
    }
}
