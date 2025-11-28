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
    String username;// the name of the customer.
    double withdrawalAmount;//the amount the customer wants to withdraw.

    public Withdraw(){}

    public Withdraw(long customerId,String username,double withdrawalAmount) {
        this.customerId=customerId;
        this.username=username;
        this.withdrawalAmount =withdrawalAmount;

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

    public double getWithdrawalAmount(){
        return this.withdrawalAmount;
    }

    public void setWithdrawalAmount(double withdrawalAmount){
        this.withdrawalAmount = withdrawalAmount;
    }
}
