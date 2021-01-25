package com.sapient.payments.domain;

import com.sapient.payments.control.ErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PaymentObject extends BaseStateful implements BaseValidatable {

    private String paymentId;
    private Date executionDate;
    private Double amount = 0.0;
    private List<ErrorCode> errors = new ArrayList<>();

    public PaymentObject() {
        //super();
        this.paymentId = UUID.randomUUID().toString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<ErrorCode> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorCode> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "PaymentObject{" +
                "paymentId='" + paymentId + '\'' +
                ", executionDate=" + executionDate +
                ", amount=" + amount +
                ", errors=" + errors +
                "} " + super.toString();
    }
}
