package com.banking.OnlineBankingSystem.model;

//the class is used to send a unique JSON request to store withdrawal messages in Withdrawals.json.

//USED JSON FORMAT.
/*
* {
    "customerId":1,
    "username":"elonMusk",
    "withdrawalAmount":500.0
}
* */

public class Withdraw {
    long customerId; //the uniqueId of the customer.
    double withdrawalAmount;//the amount the customer wants to withdraw.

    public Withdraw(){}

    public Withdraw(long customerId,double withdrawalAmount) {
        this.customerId=customerId;
        this.withdrawalAmount =withdrawalAmount;

    }

    public void printDetails(){
        System.out.println("customerId: "+this.getCustomerId()+", withdrawalAmount: "+ this.getWithdrawalAmount());
    }

    public long getCustomerId(){
        return this.customerId;
    }

    public void setCustomerId(long customerId){
        this.customerId=customerId;
    }

    public double getWithdrawalAmount(){
        return this.withdrawalAmount;
    }

    public void setWithdrawalAmount(double withdrawalAmount){
        this.withdrawalAmount = withdrawalAmount;
    }
}
