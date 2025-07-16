package com.jakubcitko.customerpoints.ports.out.notification;

import com.jakubcitko.customerpoints.domain.event.DomainEvent;
import com.jakubcitko.customerpoints.domain.model.Customer;

public class LowBalanceWarningEvent implements DomainEvent {
    private Customer customer;
    public LowBalanceWarningEvent(Customer customer) {
        this.customer = customer;
    }
    public Customer getCustomerWithLowBalance() {
        return customer;
    }
}
