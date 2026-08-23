package com.banking.OnlineBankingSystem.service;

import com.banking.OnlineBankingSystem.DAO.customerDAO;
import com.banking.OnlineBankingSystem.DAO.customerDAOJDBCImplementation;
import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import org.springframework.stereotype.Service; //to import service annotation from spring.
import com.fasterxml.jackson.core.type.TypeReference; //it helps JACKSON UNDERSTAND GENERIC TYPES LIKE List<ServiceRequests>
import com.fasterxml.jackson.databind.ObjectMapper; //to convert between java objects and JSON.

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service //now spring creates and manages an instance of this class.
public class customerService {
    private customerDAO customerDAO;

    public customerService(customerDAO customerDAO){
        this.customerDAO=customerDAO;
    }

    //CREATE
    public Customer createCustomer(Customer newCustomer){
        //check if customer exists.then create a new customer.
        if(customerDAO.isCustomerValid(newCustomer.getCustomerId())){
            return null;//we return null if customer exists already.
        }
        //NOW CREATE A NEW CUSTOMER WITH GIVEN DETAILS.
        newCustomer.setBalance(0.0);
        return customerDAO.createCustomer(newCustomer);
    }

    //to get a customer by ID.
    public Customer getCustomerById(long customerId){
        Customer customer=customerDAO.getCustomerById(customerId);
        if(customer==null){
            System.out.println("Customer with this customerId does not exist");
            return null;
        }

        return customer;
    }

    //UPDATE Customer's balance.
    public Customer updateCustomerBalance(long customerId,Customer newCustomer){
        //check if customer exists.
        if(!customerDAO.isCustomerValid(customerId)){
            return null;
        }
        //now as customer is valid.we call the necessary method.
        return customerDAO.updateCustomerBalance(customerId,newCustomer);
    }

    //to do a deposit for a customer
    public Deposit depositMoney(Customer customer,double amount){
        //to check if customer exists.
        if(!customerDAO.isCustomerValid(customer.getCustomerId())){
            return null;//if customer does not exist.
        }
        if(amount<=0){
            System.out.println("Invalid deposit amount has been typed.please try again");
            return null;
        }
        //else.now you can deposit money.
        return customerDAO.depositMoney(customer,amount);
    }
    //to do a withdrawal for a customer.
    public Withdraw withdrawMoney(Customer customer, double amount){
        //to check if customer exists.
        if(!customerDAO.isCustomerValid(customer.getCustomerId())){
            return null;//if customer does not exist.
        }
        //A CHECK IF WITHDRAWAL IS POSSIBLE USING CURRENT BALANCE.
        if(customer.getBalance()-amount<0.0){
            System.out.println("CURRENT BALANCE IS: "+customer.getBalance()+".SO WITHDRAWAL IS NOT POSSIBLE");
            return null; //as withdrawal is not possible.
        }
        //else.now you can withdraw money.
        return customerDAO.withdrawMoney(customer,amount);
    }

    //TO CHECK AND CHANGE PASSWORD OF A CUSTOMER
    public boolean updateCustomerPassword(long customerId,String currentPassword,String newPassword){
        //check if customer exists.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer does not exist in the database");
            return false;//if customer does not exist.
        }
        //change if current password is supplied correctly.
        if(!customerDAO.checkCustomerPassword(customerId,currentPassword)){
            System.out.println("Invalid password has been provided.Please try again");
            return false;
        }
        //now you can change the password;
        return customerDAO.updateCustomerPassword(customerId,newPassword);

    }
    //to create a loan request for the customer.
    public serviceRequest createLoanRequest(long customerId, double amount){
        //check if customer exists.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer does not exist in the database");
            return null;//if customer does not exist.
        }

        return customerDAO.createLoanRequest(customerId,amount);
    }
    //to create a credit card request for the customer.
    public serviceRequest createCreditCardRequest(long customerId, double amount){
        //check if customer exists.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer does not exist in the database");
            return null;//if customer does not exist.
        }

        return customerDAO.createCreditCardRequest(customerId,amount);
    }
    //DELETE
    public boolean deleteCustomer(long customerId){
        //check if customer exists.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer does not exist in the database");
            return false;//if customer does not exist.
        }

        //delete the customer.else if failed return false.
        return customerDAO.deleteCustomer(customerId);
    }
}
