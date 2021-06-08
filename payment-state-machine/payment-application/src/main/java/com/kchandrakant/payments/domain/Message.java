package com.kchandrakant.payments.domain;

public class Message {

    private PaymentObject paymentObject;
    private Transition<PaymentObject> transition;

    public Message(PaymentObject paymentObject, Transition<PaymentObject> transition) {
        this.paymentObject = paymentObject;
        this.transition = transition;
    }

    public PaymentObject getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(PaymentObject paymentObject) {
        this.paymentObject = paymentObject;
    }

    public Transition<PaymentObject> getTransition() {
        return transition;
    }

    public void setTransition(Transition<PaymentObject> transition) {
        this.transition = transition;
    }

    @Override
    public String toString() {
        return "Event{" +
                "paymentObject=" + paymentObject +
                ", transition=" + transition +
                '}';
    }
}

