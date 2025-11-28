package com.banking.OnlineBankingSystem.model;

public class serviceRequest {
    private static long requestNumber;
    private long customerId; //to map to the customer.
    private long requestId; //to parse through the files.
    private String type;
    private String status; //pending,accepted or rejected.
    private double amount;

    //default constructor is made so that jackson can run.
    public serviceRequest(){
        this.requestId = ++requestNumber;
        this.status = "Pending";
    }

    public serviceRequest(long customerId,String type, double amount) {
        this.customerId=customerId;
        this.requestId = ++requestNumber;
        this.type = type;
        this.status = "Pending";
        this.amount = amount;
    }

    public long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
