package com.jakubcitko.customerpoints.customer;

import com.jakubcitko.customerpoints.application.SystemEngineImpl;
import com.jakubcitko.customerpoints.domain.event.EventDispatcher;
import com.jakubcitko.customerpoints.ports.out.notification.LowBalanceWarningEvent;
import com.jakubcitko.customerpoints.domain.exception.CustomerNotFoundException;
import com.jakubcitko.customerpoints.domain.exception.NegativeBalanceException;
import com.jakubcitko.customerpoints.ports.out.notification.WarningService;
import com.jakubcitko.customerpoints.ports.out.persistance.InMemoryCustomerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SystemEngineImplTest {

    @Test
    void earn_points_non_existing_customer_throws_exception() {
        SystemEngineImpl systemEngine = new SystemEngineImpl(new InMemoryCustomerRepository(), new EventDispatcher(), new WarningService());
        assertThrows(CustomerNotFoundException.class,() -> systemEngine.earn("test", 2));
    }

    @Test
    void earn_points_success() {
        InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
        repository.save("user1", 5);

        SystemEngineImpl systemEngine = new SystemEngineImpl(repository, new EventDispatcher(), new WarningService());
        systemEngine.earn("user1", 3);

        assertEquals(8, repository.getPoints("user1"));
    }

    @Test
    void redeem_points_non_existing_customer_throws_exception() {
        SystemEngineImpl systemEngine = new SystemEngineImpl(new InMemoryCustomerRepository(), new EventDispatcher(), new WarningService());
        assertThrows(CustomerNotFoundException.class,() -> systemEngine.redeem("test", 2));
    }

    @Test
    void redeem_points_negative_balance_throws_exception() {
        InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
        repository.save("user1", 5);

        SystemEngineImpl systemEngine = new SystemEngineImpl(repository, new EventDispatcher(), new WarningService());
        assertThrows(NegativeBalanceException.class,() -> systemEngine.redeem("user1", 10));
    }

    @Test
    void redeem_points_warning_sent() {
        EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
        repository.save("user1", 5);

        SystemEngineImpl systemEngine = new SystemEngineImpl(repository, eventDispatcher, new WarningService());
        systemEngine.redeem("user1", 3);

        verify(eventDispatcher).dispatch(any());
    }

    @Test
    void redeem_points_warning_sent_and_handled() throws InterruptedException {
        WarningService warningService = mock(WarningService.class);
        InMemoryCustomerRepository repository = new InMemoryCustomerRepository();

        repository.save("user1", 5);

        SystemEngineImpl systemEngine = new SystemEngineImpl(repository, new EventDispatcher(), warningService);
        systemEngine.redeem("user1", 3);

        Thread.sleep(50);
        verify(warningService).handle(any(LowBalanceWarningEvent.class));
    }

    @Test
    void redeem_points_success() {
        InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
        repository.save("user1", 5);

        SystemEngineImpl systemEngine = new SystemEngineImpl(repository, new EventDispatcher(), new WarningService());
        systemEngine.redeem("user1", 1);

        assertEquals(4, repository.getPoints("user1"));
    }
}
