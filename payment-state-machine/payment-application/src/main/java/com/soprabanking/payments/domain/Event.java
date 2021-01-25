package com.soprabanking.payments.domain;

public class Event {

    private String paymentId;
    private String eventType;

    public Event(String paymentId, String eventType) {
        this.paymentId = paymentId;
        this.eventType = eventType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
