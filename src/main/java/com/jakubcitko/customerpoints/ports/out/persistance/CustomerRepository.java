package com.jakubcitko.customerpoints.ports.out.persistance;

public interface CustomerRepository {
    Integer getPoints(String customerId);
    void save(String customerId, int points);
}
