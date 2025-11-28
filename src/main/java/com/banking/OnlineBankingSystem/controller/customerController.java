package com.banking.OnlineBankingSystem.controller;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import com.banking.OnlineBankingSystem.service.customerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class customerController {

    private final customerService customerService; //to use the customerService class.

    public customerController(customerService customerService){
        this.customerService=customerService;
    }

    //CREATING A CUSTOMER.
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer createdCustomer=customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    //DELETE METHODS.

    //DELETING A CUSTOMER ALONG WITH CUSTOMER HISTORY.
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long customerId){
        boolean deleted=customerService.deleteCustomer(customerId);

        //now check if deletion is successful.
        if(!deleted){
            return ResponseEntity.notFound().build(); //we get a (error 404).
        }
        return ResponseEntity.noContent().build(); // (204 no content) is returned on success.
    }


    //LOAD RELATED METHODS.
    //we get all the students.
    @GetMapping("/customerDetails")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());// all data is retrieved and a (200 ok) is given.
    }

    //getting a customer using a customerId.
    @GetMapping("/id/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long customerId){
        Customer customer=customerService.getCustomerById(customerId);
        if(customer==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    //UPDATE related methods.
    //we use this method in case of an update in customer details.
    @PutMapping("/id/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long customerId,@RequestBody Customer customer){
        Customer updatedCustomer=customerService.updateCustomer(customerId,customer);
        if(updatedCustomer==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    //we use this when a customer wants to deposit money into his account based on customerId.
    @PutMapping("/deposit/{customerId}")
    public ResponseEntity<Deposit> DepositByCustomer(@PathVariable long customerId,@RequestBody Deposit newDeposit) throws IOException {
        Customer requiredCustomer=customerService.getCustomerById(customerId); //we get the required customer based on id.
        Deposit customerDeposit=customerService.depositMoney(requiredCustomer,newDeposit.getDepositAmount());//now we deposit money in user's account.
        if(customerDeposit==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerDeposit);
    }

    //we use this when a customer wants to withdraw money from his account based on customerId.
    @PutMapping("/withdraw/{customerId}")
    public ResponseEntity<Withdraw> WithdrawalByCustomer(@PathVariable long customerId, @RequestBody Withdraw newWithdrawal) throws IOException {
        Customer requiredCustomer=customerService.getCustomerById(customerId); //we get the required customer based on id.
        Withdraw customerWithdrawal=customerService.withdrawMoney(requiredCustomer,newWithdrawal.getWithdrawalAmount());//now we deposit money in user's account.
        if(customerWithdrawal==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerWithdrawal);
    }

    //customer uses this method to create a loan request
    @PostMapping("/request/loan/{customerId}")
    public ResponseEntity<serviceRequest> createLoanRequest(@PathVariable long customerId, @RequestBody serviceRequest request) {
        serviceRequest created = customerService.createLoanRequest(customerId, request.getAmount());
        if (created == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //customer uses this method to create a credit card request
    @PostMapping("/request/creditcard/{customerId}")
    public ResponseEntity<serviceRequest> createCreditCardRequest(@PathVariable long customerId, @RequestBody serviceRequest request) {
        serviceRequest created = customerService.createCreditCardRequest(customerId, request.getAmount());
        if (created == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
