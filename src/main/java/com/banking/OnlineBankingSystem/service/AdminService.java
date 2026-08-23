package com.banking.OnlineBankingSystem.service;

import com.banking.OnlineBankingSystem.DAO.customerDAO;
import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import com.banking.OnlineBankingSystem.DAO.AdminDAO;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling Admin-related operations.
 * Responsible for retrieving data from persistent storage (JSON files).
 */
@Service
public class AdminService {
    private final customerDAO customerDAO;
    private final AdminDAO adminDAO;

    public AdminService(customerDAO customerDAO,AdminDAO adminDAO){
        this.customerDAO=customerDAO;
        this.adminDAO=adminDAO;
    }

    public Customer getCustomerById(long customerId){

        //check if customer exists or not.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer is not valid");
            return null;
        }
        return adminDAO.getCustomerById(customerId);
    }
    public List<Customer> getAllCustomers(){
        return adminDAO.getAllCustomers();
    }
    public List<Deposit> getAllDeposits(){
        return adminDAO.getAllDeposits();
    }
    public List<Withdraw> getAllWithdrawals(){
        return adminDAO.getAllWithdrawals();
    }


    // ADMIN: Logic to Approve/Reject a Request .
    public boolean processServiceRequest(long customerId,String requestType,long requestId,String status) {
        //check if customer exists or not.
        if(!customerDAO.isCustomerValid(customerId)){
            System.out.println("The customer is not valid");
            return false;
        }
        // Check if a valid requestType is provided.
        if(!("Loan".equalsIgnoreCase(requestType) || "CreditCard".equalsIgnoreCase(requestType))) {
            System.out.println("Invalid requestType has been given");
            return false;
        }
        return adminDAO.processServiceRequest(customerId,requestType,requestId,status);
    }
}