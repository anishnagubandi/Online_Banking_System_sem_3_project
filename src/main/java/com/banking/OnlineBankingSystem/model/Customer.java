package com.banking.OnlineBankingSystem.model;
import com.banking.OnlineBankingSystem.model.serviceRequest;

import java.util.List;

//JSON FORMAT WHILE ADDING.
/*
    {
    "username":"billGates",
    "password":"bill@123",
    "balance":0.0,
    "serviceRequests": []
    }
* */

//JSON format after adding a customer.
/*
*  {
  "username" : "rekhaJhunjhunwala",
  "password" : "12436",
  "role" : "Customer",
  "customerId" : 2,
  "balance" : 10000.0,
  "serviceRequests" : null
}
* */


public class Customer extends User {
    private static long userId; //unique identifier generator for the user.
    private long customerId;//uniqueId of the customer.
    private double balance;
    private List<serviceRequest> Requests;


    public Customer(){
        super();
        this.role="Customer";
        this.customerId = ++userId;
    }

    public Customer(String username, String password) {
        super(username, password, "Customer");
        this.balance = 0.0;
        this.customerId = ++userId;
    }

    public long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<serviceRequest> getServiceRequests(){
        return this.Requests;
    }

    public void setServiceRequests(List<serviceRequest> requests){
        this.Requests = requests;
    }

    public void addServiceRequest(serviceRequest req){
        if(this.Requests == null){
            this.Requests = new java.util.ArrayList<>();
        }
        this.Requests.add(req);
    }


}
