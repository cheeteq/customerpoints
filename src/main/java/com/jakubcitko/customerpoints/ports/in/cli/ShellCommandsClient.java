package com.jakubcitko.customerpoints.ports.in.cli;

import com.jakubcitko.customerpoints.application.SystemEngine;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommandsClient {
    private final SystemEngine systemEngine;
    public ShellCommandsClient(SystemEngine systemEngine) {
        this.systemEngine = systemEngine;
    }
    @ShellMethod(key = "earn",value = "Adds points to customer")
    public void earn(String customerId, int points) {
        systemEngine.earn(customerId, points);
        System.out.printf("Customer %s earned %d points.%n", customerId, points);
    }

    @ShellMethod(key = "redeem",value = "Subtracts points from customer")
    public void redeem(String customerId, int points) {
        systemEngine.redeem(customerId, points);
        System.out.printf("Customer %s redeemed %d points.%n", customerId, points);
    }
}