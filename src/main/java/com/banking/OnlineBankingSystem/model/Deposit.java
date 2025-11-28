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
    String username;// the name of the customer.
    double depositAmount;//the amount the customer wants to deposit.

    public Deposit(){}

    public Deposit(long customerId,String username,double depositAmount) {
        this.customerId=customerId;
        this.username=username;
        this.depositAmount =depositAmount;

    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username=username;
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
