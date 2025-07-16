package com.jakubcitko.customerpoints.ports.out.notification;

import com.jakubcitko.customerpoints.domain.event.DomainEventListener;

public class WarningService implements DomainEventListener<LowBalanceWarningEvent> {
    @Override
    public void handle(LowBalanceWarningEvent event) {
        var customerId = event.getCustomerWithLowBalance().getCustomerId();
        var points = event.getCustomerWithLowBalance().getPoints();

        System.out.println("Warning: Customer " + customerId + " has a low balance: " + String.valueOf(points) + " points.");
    }
}
