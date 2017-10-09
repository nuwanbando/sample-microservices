package com.acme.service.payment;

/**
 * Created by nuwanbando on 3/28/17.
 */
public class PaymentStatus {

    private String status;
    private Payment payment;

    public PaymentStatus(String status, Payment payment) {
        this.status = status;
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

}