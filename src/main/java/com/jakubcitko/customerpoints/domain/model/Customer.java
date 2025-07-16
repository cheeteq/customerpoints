package com.jakubcitko.customerpoints.domain.model;

public class Customer {
    private String customerId;
    private int points;

    public Customer(String customerId, int points) {
        this.customerId = customerId;
        this.points = points;
    }
    public String getCustomerId() {
        return customerId;
    }
    public int getPoints() {
        return points;
    }
}
