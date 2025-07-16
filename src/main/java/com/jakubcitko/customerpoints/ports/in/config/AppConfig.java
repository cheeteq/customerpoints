package com.jakubcitko.customerpoints.ports.in.config;

import com.jakubcitko.customerpoints.domain.event.EventDispatcher;
import com.jakubcitko.customerpoints.domain.event.DomainEventListener;
import com.jakubcitko.customerpoints.ports.out.notification.LowBalanceWarningEvent;
import com.jakubcitko.customerpoints.ports.out.notification.WarningService;
import com.jakubcitko.customerpoints.ports.out.persistance.CustomerRepository;
import com.jakubcitko.customerpoints.application.SystemEngine;
import com.jakubcitko.customerpoints.application.SystemEngineImpl;
import com.jakubcitko.customerpoints.ports.out.persistance.InMemoryCustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }

    @Bean
    public EventDispatcher eventDispatcher() {
        return new EventDispatcher();
    }

    @Bean
    public DomainEventListener<LowBalanceWarningEvent> warningListener() {
        return new WarningService();
    }

    @Bean
    public SystemEngine systemEngine(CustomerRepository customerRepository,
                                     EventDispatcher eventDispatcher,
                                     DomainEventListener<LowBalanceWarningEvent> warningListener) {
        return new SystemEngineImpl(customerRepository, eventDispatcher, warningListener);
    }
}

