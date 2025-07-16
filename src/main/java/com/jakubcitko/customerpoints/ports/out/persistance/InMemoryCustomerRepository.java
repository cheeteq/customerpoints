package com.jakubcitko.customerpoints.ports.out.persistance;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCustomerRepository implements CustomerRepository {
    private Map<String, Integer> customers = new HashMap<>();
    @Override
    public Integer getPoints(String customerId) {
        var points = customers.get(customerId);

        return points;
    }
    @Override
    public void save(String customerId, int points) {
        customers.put(customerId, points);
    }
}
