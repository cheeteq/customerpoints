package com.jakubcitko.customerpoints.application;

public interface SystemEngine {
    void earn (String customerId, int points);
    void redeem (String customerId, int points);
}
