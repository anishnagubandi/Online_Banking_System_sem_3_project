package com.banking.OnlineBankingSystem.model;

//the class is used to send a unique JSON request to store deposits messages in Deposits.json.

//USED JSON FORMAT.
/*
* {
    "customerId":1,
    "username":"elonMusk",
    "depositAmount":500.0
}
* */


public class Deposit {
    long customerId; //the uniqueId of the customer.
    double depositAmount;//the amount the customer wants to deposit.

    public Deposit(){}

    public Deposit(long customerId,double depositAmount) {
        this.customerId=customerId;
        this.depositAmount =depositAmount;

    }
    public void printDetails(){
        System.out.println("customerId: "+this.getCustomerId()+", depositAmount: "+ this.getDepositAmount());
    }

    public long getCustomerId(){
        return this.customerId;
    }

    public void setCustomerId(long customerId){
        this.customerId=customerId;
    }

    public double getDepositAmount(){
        return this.depositAmount;
    }

    public void setDepositAmount(double depositAmount){
        this.depositAmount =depositAmount;
    }
}
