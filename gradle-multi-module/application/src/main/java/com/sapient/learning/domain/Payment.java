package com.sapient.learning.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Payment implements Serializable {

    private String paymentId;

    private Long amount;

    private String sender;

    private String receiver;

    private Date executionDate;

    public Payment() {
    }

    public Payment(Long amount, String sender, String receiver) {
        this.paymentId = UUID.randomUUID().toString();
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.executionDate = new Date();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Payment initialize() {
        this.paymentId = UUID.randomUUID().toString();
        this.executionDate = new Date();
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", executionDate=" + executionDate +
                '}';
    }

}
