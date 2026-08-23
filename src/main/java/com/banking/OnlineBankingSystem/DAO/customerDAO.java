package com.banking.OnlineBankingSystem.DAO;

//we will use the below one later.
//import java.math.BigDecimal;//to prevent 0 rounding errors with money.

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;

public interface customerDAO{
    //include method signatures which will be implemented in the implementation.
    void getCustomerRecords(); //DONE---IN BOTH
    Customer getCustomerById(long customerId); //DONE ---IN BOTH
    boolean isCustomerValid(long customerId);//we use customerId as it is unique. DONE ----IN BOTH
    Customer createCustomer(Customer newCustomer);//DONE---IN BOTH
    Customer updateCustomerBalance(long customerId,Customer newCustomer); //DONE ---IN BOTH
    Deposit depositMoney(Customer customer, double amount);//DONE--- IN BOTH
    Withdraw withdrawMoney(Customer customer, double amount);//DONE ---IN BOTH
    boolean checkCustomerPassword(long customerId,String currentPassword);//DONE ---IN BOTH
    boolean updateCustomerPassword(long customerId,String newPassword);//DONE ---IN BOTH
    serviceRequest createLoanRequest(long customerId, double amount);
    serviceRequest createCreditCardRequest(long customerId, double amount);
    boolean deleteCustomer(long customerId);//DONE--IN BOTH

}

