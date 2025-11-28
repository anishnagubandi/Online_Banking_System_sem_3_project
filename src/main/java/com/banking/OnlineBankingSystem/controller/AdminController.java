package com.banking.OnlineBankingSystem.controller;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.service.AdminService;
import com.banking.OnlineBankingSystem.service.customerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Admin operations.
 * Provides endpoints for viewing system records and managing service requests.
 * Base URL: /admin
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final customerService customerService;

    /**
     * Constructor Injection for required services.
     * @param adminService Service for retrieving admin-viewable data.
     * @param customerService Service for processing customer-related logic.
     */
    public AdminController(AdminService adminService, customerService customerService) {
        this.adminService = adminService;
        this.customerService = customerService;
    }

    // --- DATA RETRIEVAL ENDPOINTS ---

    /**
     * Endpoint to retrieve all registered customer details.
     * URL: GET /admin/customerDetails
     * @return List of Customers or 204 No Content.
     */
    @GetMapping("/customerDetails")
    public ResponseEntity<List<Customer>> viewAllCustomers() {
        List<Customer> customers = adminService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    /**
     * Endpoint to retrieve the global history of all deposits.
     * URL: GET /admin/deposits
     * @return List of Deposits or 204 No Content.
     */
    @GetMapping("/deposits")
    public ResponseEntity<List<Deposit>> viewAllDeposits() {
        List<Deposit> deposits = adminService.getAllDeposits();
        if (deposits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(deposits);
    }

    /**
     * Endpoint to retrieve the global history of all withdrawals.
     * URL: GET /admin/withdrawals
     * @return List of Withdrawals or 204 No Content.
     */
    @GetMapping("/withdrawals")
    public ResponseEntity<List<Withdraw>> viewAllWithdrawals() {
        List<Withdraw> withdrawals = adminService.getAllWithdrawals();
        if (withdrawals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(withdrawals);
    }

    // --- APPROVAL WORKFLOW ENDPOINTS ---

    /**
     * Approves a specific service request (Loan/Credit Card).
     * URL: PUT /admin/approve/{customerId}/{requestId}
     * @param customerId The ID of the customer who made the request.
     * @param requestId The ID of the specific request.
     * @return Success message or Error if not found.
     */
    @PutMapping("/approve/{customerId}/{requestType}/{requestId}")
    public ResponseEntity<String> approveRequest(@PathVariable long customerId,@PathVariable String requestType, @PathVariable long requestId) {
        boolean success = adminService.processServiceRequest(customerId,requestType,requestId, "APPROVED");
        if (success) {
            return ResponseEntity.ok("Request Approved Successfully.");
        }
        return ResponseEntity.badRequest().body("Request or Customer not found.");
    }

    /**
     * Rejects a specific service request.
     * URL: PUT /admin/reject/{customerId}/{requestId}
     * @param customerId The ID of the customer who made the request.
     * @param requestId The ID of the specific request.
     * @return Success message or Error if not found.
     */
    @PutMapping("/reject/{customerId}/{requestType}/{requestId}")
    public ResponseEntity<String> rejectRequest(@PathVariable long customerId,@PathVariable String requestType,@PathVariable long requestId) {
        boolean success = adminService.processServiceRequest(customerId,requestType,requestId,"REJECTED");
        if (success) {
            return ResponseEntity.ok("Request Rejected Successfully.");
        }
        return ResponseEntity.badRequest().body("Request or Customer not found.");
    }
}