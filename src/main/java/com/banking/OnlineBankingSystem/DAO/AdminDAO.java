package com.banking.OnlineBankingSystem.DAO;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;

import java.util.List;

public interface AdminDAO {
    Customer getCustomerById(long customerId);
    List<Customer> getAllCustomers();
    List<Deposit> getAllDeposits();
    List<Withdraw> getAllWithdrawals();
    boolean processServiceRequest(long customerId,String requestType,long requestId,String status);
}
