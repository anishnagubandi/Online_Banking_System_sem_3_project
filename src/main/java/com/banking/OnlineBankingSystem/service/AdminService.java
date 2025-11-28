package com.banking.OnlineBankingSystem.service;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File customerFile;
    private File depositFile;
    private File withdrawalFile;
    private  File serviceRequestsWriter;//only for service request handling.
    private final customerService customerService;

    public AdminService(customerService customerService){
        this.customerService=customerService;
    }
    /**
     * Initializes file references upon bean creation.
     * Ensures the service points to the correct data sources.
     */
    @PostConstruct
    public void init() {
        customerFile = new File("customerDetails.json");
        depositFile = new File("Deposits.json");
        withdrawalFile = new File("Withdrawals.json");
    }

    /**
     * Retrieves a list of all registered customers.
     * @return List of Customer objects.
     */
    public List<Customer> getAllCustomers() {
        return loadData(customerFile, new TypeReference<List<Customer>>() {});
    }

    /**
     * Retrieves a complete log of all deposit transactions.
     * @return List of Deposit objects.
     */
    public List<Deposit> getAllDeposits() {
        return loadData(depositFile, new TypeReference<List<Deposit>>() {});
    }

    /**
     * Retrieves a complete log of all withdrawal transactions.
     * @return List of Withdraw objects.
     */
    public List<Withdraw> getAllWithdrawals() {
        return loadData(withdrawalFile, new TypeReference<List<Withdraw>>() {});
    }

    /**
     * Generic helper method to safely load and parse JSON data.
     * Handles IOExceptions internally to prevent application crashes.
     *
     * @param file The file to read from.
     * @param typeReference Jackson TypeReference for the expected list type.
     * @param <T> The type of object in the list.
     * @return A list of objects of type T, or an empty list if the file is missing/empty.
     */
    private <T> List<T> loadData(File file, TypeReference<List<T>> typeReference) {
        try {
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            byte[] jsonData = Files.readAllBytes(file.toPath());
            return objectMapper.readValue(jsonData, typeReference);
        } catch (IOException e) {
            // Log the error (could use SLF4J in a production environment)
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ADMIN: Logic to Approve/Reject a Request .
    public boolean processServiceRequest(long customerId,String requestType,long requestId,String status) {
        try {
            //THE SERVICE REQUEST IS CHECKED AND UPDATED IN CustomerDetails.json.
            List<Customer> customers = customerService.loadCustomers(customerFile);
            boolean customerUpdated = false;

            for (Customer c : customers) {
                // Check if customer matches AND has requests
                if (c.getCustomerId() == customerId && c.getServiceRequests() != null) {
                    for (serviceRequest req : c.getServiceRequests()) {
                        if (req.getRequestId() == requestId) {
                            req.setStatus(status); // Set Approved/Rejected
                            customerUpdated = true;
                        }
                    }
                }
            }

            if (customerUpdated) {
                customerService.saveCustomers(customers, customerFile);
            }
            //THE SERVICEREQUEST IS UPDATED IN respective file with new status.
            boolean requestUpdated=false;
            List<serviceRequest>serviceRequests=null;//to hold the service requests.

            if(requestType.equals("Loan")){
                serviceRequestsWriter=new File("LoanRequests.json");
                serviceRequests=customerService.loadLoanRequests(serviceRequestsWriter);

                for(serviceRequest loanRequest:serviceRequests){
                    if(loanRequest.getCustomerId()==customerId && loanRequest.getRequestId()==requestId){
                        loanRequest.setStatus(status);
                        requestUpdated=true;
                    }
                }

                //once request is updated, save the file details back into the file.

                if (requestUpdated) {
                    customerService.saveLoanRequests(serviceRequests,serviceRequestsWriter);
                }
            }
            else if(requestType.equals("CreditCard")){
                serviceRequestsWriter=new File("CreditCardRequests.json");
                serviceRequests=customerService.loadCreditCardRequests(serviceRequestsWriter);

                for(serviceRequest creditCardRequest:serviceRequests){
                    if(creditCardRequest.getCustomerId()==customerId && creditCardRequest.getRequestId()==requestId){
                        creditCardRequest.setStatus(status);
                        requestUpdated=true;
                    }
                }

                //once request is updated, save the file details back into the file.

                if (requestUpdated) {
                    customerService.saveCreditCardRequests(serviceRequests,serviceRequestsWriter);
                }
            }



            return (customerUpdated && requestUpdated);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}