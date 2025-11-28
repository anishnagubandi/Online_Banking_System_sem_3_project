package com.banking.OnlineBankingSystem.service;

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
    private final ObjectMapper objectMapper= new ObjectMapper();
    private File dataFile; //only for customer details.
    private File moneyWriter; //only for deposits or withdrawals.
    @PostConstruct //the method is automatically executed after the service is created by spring.
    public void init(){
        //the below one is a file object pointing to required json file.
        dataFile=new File("customerDetails.json");

        //if file does not exist, make a new empty file.
        if(!dataFile.exists()){
            try{
                saveCustomers(new ArrayList<>(),dataFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //LOADING FROM FILE AND SAVING INTO FILE METHODS

    //to load data from the JSON file.
    public synchronized List<Customer> loadCustomers(File inputFile) throws IOException {
        if (!inputFile.exists() || inputFile.length() == 0) {
            return new ArrayList<>();
        }

        byte[] jsonData = Files.readAllBytes(inputFile.toPath());
        return objectMapper.readValue(jsonData, new TypeReference<List<Customer>>() {});
    }
    //SPECIFICALLY FOR deposits.json.
    private synchronized List<Deposit> loadDeposits(File inputFile) throws IOException {
        if (!inputFile.exists() || inputFile.length() == 0) {
            return new ArrayList<>();
        }

        byte[] jsonData = Files.readAllBytes(inputFile.toPath());
        return objectMapper.readValue(jsonData, new TypeReference<List<Deposit>>() {});
    }

    //SPECIFICALLY FOR Withdrawals.json.
    private synchronized List<Withdraw> loadWithdrawals(File inputFile) throws IOException {
        if (!inputFile.exists() || inputFile.length() == 0) {
            return new ArrayList<>();
        }

        byte[] jsonData = Files.readAllBytes(inputFile.toPath());
        return objectMapper.readValue(jsonData, new TypeReference<List<Withdraw>>() {});
    }

    //SPECIFICALLY FOR LoanRequests.json.
    synchronized List<serviceRequest> loadLoanRequests(File inputFile) throws IOException {
        if (!inputFile.exists() || inputFile.length() == 0) {
            return new ArrayList<>();
        }

        byte[] jsonData = Files.readAllBytes(inputFile.toPath());
        return objectMapper.readValue(jsonData, new TypeReference<List<serviceRequest>>() {});
    }

    //SPECIFICALLY FOR CreditCardRequests.json.
    synchronized List<serviceRequest> loadCreditCardRequests(File inputFile) throws IOException {
        if (!inputFile.exists() || inputFile.length() == 0) {
            return new ArrayList<>();
        }

        byte[] jsonData = Files.readAllBytes(inputFile.toPath());
        return objectMapper.readValue(jsonData, new TypeReference<List<serviceRequest>>() {});
    }

    //this saves the list of students to the JSON file.
    public synchronized void saveCustomers(List<Customer> customers, File outputFile) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, customers);
    }
    //SPECIFICALLY TO SAVE TO Deposits.json.
    private synchronized void saveDeposits(List<Deposit> deposits,File outputFile) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, deposits);
    }

    //SPECIFICALLY TO SAVE TO Withdrawals.json.
    private synchronized void saveWithdrawals(List<Withdraw> withdrawals,File outputFile) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, withdrawals);
    }

    //SPECIFICALLY TO SAVE LoanRequests.json
    synchronized void saveLoanRequests(List<serviceRequest> requests, File outputFile) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, requests);
    }

    //SPECIFICALLY TO SAVE CreditCardRequests.json
    synchronized void saveCreditCardRequests(List<serviceRequest> requests, File outputFile) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, requests);
    }

    //CRUD(CREATE,READ,UPDATE,DELETE) METHODS.

    //CREATE METHODS.
    //checks if customer with similar id already exists and a new id is given if present.
    public Customer createCustomer(Customer customer){
        try{
            List<Customer>customers= loadCustomers(dataFile);

            //if customer id exists already or not.
            if(customers.stream().anyMatch(c->c.getCustomerId() == customer.getCustomerId())){
                long maxCustomerId = customers.stream().mapToLong(Customer::getCustomerId).max().orElse(0);
                customer.setCustomerId(maxCustomerId+1);
            }

            customers.add(customer);
            saveCustomers(customers,dataFile);
            return customer;
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    //DELETE METHODS.
    //if the customer wants to delete his account.(USER'S HISTORY WITH THE BANK GETS DELETED AS WELL(withdrawals,deposits))
    public boolean deleteCustomer(long customerId){
        try{
            //to remove customer from customerDetails.json.
            List<Customer>customers=loadCustomers(dataFile);
            boolean detailsRemoved= customers.removeIf( c->c.getCustomerId()==customerId);
            if(detailsRemoved){
                saveCustomers(customers,dataFile);
            }

            //to remove customer from deposits.json.
            moneyWriter=new File("Deposits.json");
            //if file does not exist, make a new empty file.
            if(!moneyWriter.exists()){
                try{
                    saveDeposits(new ArrayList<>(),moneyWriter);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            List<Deposit>deposits=loadDeposits(moneyWriter);
            boolean depositRemoved= deposits.removeIf( d->d.getCustomerId()==customerId);
            if(depositRemoved){
                saveDeposits(deposits,moneyWriter);
            }

            //to remove customer from withdrawals.json.
            moneyWriter=new File("Withdrawals.json");
            //if file does not exist, make a new empty file.
            if(!moneyWriter.exists()){
                try{
                    saveWithdrawals(new ArrayList<>(),moneyWriter);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            List<Withdraw>withdrawals=loadWithdrawals(moneyWriter);
            boolean withdrawalRemoved= withdrawals.removeIf( w->w.getCustomerId()==customerId);
            if(withdrawalRemoved){
                saveWithdrawals(withdrawals,moneyWriter);
            }

            return (detailsRemoved);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    //LOAD METHODS.
    //to get details of a customer.
    public Customer getCustomerById(long customerId){
        try{
            List<Customer>customers=loadCustomers(dataFile);
            Optional<Customer> customerOpt= customers.stream()
                    .filter(c->c.getCustomerId()==customerId)
                    .findFirst(); //we get the first customer which meets the criteria.
            return customerOpt.orElse(null);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //to get details of all the customers.
    public List<Customer> getAllCustomers(){
        try{
            return loadCustomers(dataFile);
        } catch(IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //UPDATE METHODS.
    //updates the details of the customer.
    public Customer updateCustomer(long customerId,Customer newcustomer){
        try{

            List<Customer>customers=loadCustomers(dataFile);

            boolean found=false; //to check if customer is present.

            for(int i=0;i<customers.size();i++){
                if(customers.get(i).getCustomerId()==customerId){
                    customers.get(i).setBalance(newcustomer.getBalance());
                    found=true;
                    break;
                }
            }

            //if customer is not found.
            if(!found){
                return null;
            }

            saveCustomers(customers,dataFile); //saving it to the file for persistence.
            return getCustomerById(customerId); //now returning the new saved customer data.

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //THIS METHOD USES Deposit CLASS OBJECTS AS WELL.(DOES UPDATE TO Deposits.json and customerDetails.json) and returns a deposit object.
    public Deposit depositMoney(Customer customer,double amount) throws IOException {
        try{
            moneyWriter=new File("Deposits.json");

            //if file does not exist, make a new empty file.
            if(!moneyWriter.exists()){
                try{
                    saveDeposits(new ArrayList<>(),moneyWriter);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            //BASIC UPDATE IN customerDetails.json.
            Customer newCustomer=new Customer(customer.getUsername(),customer.getPassword());
            newCustomer.setBalance(customer.getBalance()+amount);
            customer=updateCustomer(customer.getCustomerId(),newCustomer); //now customerDetails.json is updated.

            //update RELATED TO  deposits.json.
            List<Deposit>deposits=loadDeposits(moneyWriter);
            Deposit newDeposit=new Deposit(customer.getCustomerId(),customer.getUsername(),amount);
            deposits.add(newDeposit); //need to update JSON format.

            saveDeposits(deposits,moneyWriter); //saving it to the deposits.json file for persistence.
            return newDeposit; //now returning the new saved customer data which was saved to deposits.json.

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    //THIS METHOD USES Withdraw CLASS OBJECTS AS WELL.(DOES UPDATE TO Withdrawals.json and customerDetails.json) and returns a withdraw class object.
    public Withdraw withdrawMoney(Customer customer, double amount) throws IOException {
        try{
            moneyWriter=new File("Withdrawals.json");

            //if file does not exist, make a new empty file.
            if(!moneyWriter.exists()){
                try{
                    saveDeposits(new ArrayList<>(),moneyWriter);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            //BASIC UPDATE IN customerDetails.json.
            Customer newCustomer=new Customer(customer.getUsername(),customer.getPassword());
            //A CHECK IF WITHDRAWAL IS POSSIBLE USING CURRENT BALANCE.
            if(customer.getBalance()-amount<0.0){
                System.out.println("CURRENT BALANCE IS: "+customer.getBalance()+".SO WITHDRAWAL IS NOT POSSIBLE");
                return null; //as withdrawal is not possible.
            }
            //ELSE CONTINUE.
            newCustomer.setBalance(customer.getBalance()-amount);
            customer=updateCustomer(customer.getCustomerId(),newCustomer); //now customerDetails.json is updated.

            //update RELATED TO  withdrawals.json.
            List<Withdraw>withdrawals=loadWithdrawals(moneyWriter);
            Withdraw newWithdrawal=new Withdraw(customer.getCustomerId(),customer.getUsername(),amount);
            withdrawals.add(newWithdrawal); //need to update JSON format.

            saveWithdrawals(withdrawals,moneyWriter); //saving it to the Withdrawals.json file for persistence.
            return newWithdrawal; //now returning the new saved customer data which was saved to Withdrawals.json.

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }
    //LOGIN METHOD FOR A CUSTOMER.
    public Customer customerLogin(Customer customer){
        try{
            List<Customer>customers=loadCustomers(dataFile);

            for(int i=0;i<customers.size();i++){
                if(Objects.equals(customers.get(i).getUsername(), customer.getUsername()) && Objects.equals(customers.get(i).getPassword(), customer.getPassword())){
                    //customer exists.
                    return customers.get(i);
                }
            }

            return null;//no such customer was found/

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    // CUSTOMER: Create a loan request and persist it to both the customer's record and LoanRequests.json
    public serviceRequest createLoanRequest(long customerId, double amount) {
        try {
            List<Customer> customers = loadCustomers(dataFile);

            Optional<Customer> customerOpt = customers.stream()
                    .filter(c -> c.getCustomerId() == customerId)
                    .findFirst();

            if (!customerOpt.isPresent()) {
                return null; // customer not found
            }

            Customer cust = customerOpt.get();

            // create the request
            serviceRequest req = new serviceRequest(customerId,"Loan", amount);

            // add to customer's in-memory requests
            cust.addServiceRequest(req);

            // save updated customers to customerDetails.json
            saveCustomers(customers, dataFile);

            // also append to global LoanRequests.json
            File loanFile = new File("LoanRequests.json");
            if (!loanFile.exists()) {
                saveLoanRequests(new ArrayList<>(), loanFile);
            }
            List<serviceRequest> loans = loadLoanRequests(loanFile);
            loans.add(req);
            saveLoanRequests(loans, loanFile);

            return req;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // CUSTOMER: Create a credit card request and persist it to both the customer's record and CreditCardRequests.json
    public serviceRequest createCreditCardRequest(long customerId, double amount) {
        try {
            List<Customer> customers = loadCustomers(dataFile);

            Optional<Customer> customerOpt = customers.stream()
                    .filter(c -> c.getCustomerId() == customerId)
                    .findFirst();

            if (!customerOpt.isPresent()) {
                return null; // customer not found
            }

            Customer cust = customerOpt.get();

            // create the request
            serviceRequest req = new serviceRequest(customerId,"CreditCard", amount);

            // add to customer's in-memory requests
            cust.addServiceRequest(req);

            // save updated customers to customerDetails.json
            saveCustomers(customers, dataFile);

            // also append to global CreditCardRequests.json
            File ccFile = new File("CreditCardRequests.json");
            if (!ccFile.exists()) {
                saveCreditCardRequests(new ArrayList<>(), ccFile);
            }
            List<serviceRequest> cards = loadCreditCardRequests(ccFile);
            cards.add(req);
            saveCreditCardRequests(cards, ccFile);

            return req;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
