package com.banking.OnlineBankingSystem;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import com.banking.OnlineBankingSystem.service.customerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class CustomerServiceTests {

    @AfterEach
    void cleanup() {
        // remove any files that tests may have created so repeated test runs are clean
        new File("customerDetails.json").delete();
        new File("loanrequests.json").delete();
        new File("creditcardrequests.json").delete();
    }

    @Test
    void createLoanAndCreditCardRequestsForCustomer() {
        customerService svc = new customerService();
        svc.init();

        Customer c = new Customer("testUser", "pass");
        Customer created = svc.createCustomer(c);
        Assertions.assertNotNull(created);

        serviceRequest loan = svc.createLoanRequest(created.getCustomerId(), 5000.0);
        Assertions.assertNotNull(loan);
        Assertions.assertEquals("Loan", loan.getType());

        serviceRequest card = svc.createCreditCardRequest(created.getCustomerId(), 2000.0);
        Assertions.assertNotNull(card);
        Assertions.assertEquals("CreditCard", card.getType());
    }
}
