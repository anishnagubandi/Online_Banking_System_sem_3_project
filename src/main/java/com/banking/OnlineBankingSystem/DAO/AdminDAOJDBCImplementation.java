package com.banking.OnlineBankingSystem.DAO;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository //IT TELLS SPRING THAT THIS CLASS WORKS WITH DATA.
@Primary //TO TELL SPRING THAT THIS CLASS IS TAKEN AS IMPLEMENTATION FOR ADMINDAO.
public class AdminDAOJDBCImplementation implements AdminDAO{
    private final databaseConnection dbConnecter; //to instantiate a connection
    private final userDAO user; //to use user methods.

    public AdminDAOJDBCImplementation(){
        this.dbConnecter=new databaseConnection();
        this.user=new userDAO();
    }
    @Override
    public Customer getCustomerById(long customerId){
        //if customer is present,custoemrId is returned.else return a null.
        //to get customerId,you need the userId of the customer.
        //QUERY(a join query works better than 2 separate select queries as network does not need to send two separate queries ).
        String findCustomer="select * from Customer c join User u on c.userId=u.userId where c.customerId=?";

        try(Connection conn = dbConnecter.createConnection();){
            conn.setAutoCommit(false);
            try(PreparedStatement psFindCustomer = conn.prepareStatement(findCustomer);) {
                psFindCustomer.setLong(1, customerId);
                ResultSet rsFindCustomer = psFindCustomer.executeQuery();
                if (rsFindCustomer.next()) {
                    Customer newCustomer=new Customer(rsFindCustomer.getString("username"),rsFindCustomer.getString("password"));
                    newCustomer.setBalance(rsFindCustomer.getDouble("balance"));
                    return newCustomer;
                }
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while fetching customerId.");
                sqe.printStackTrace();
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //if customer is not present.
        return null;
    }

    /**
     * Retrieves a list of all registered customers.
     * prints List of Customer objects.
     */
    @Override
    public List<Customer> getAllCustomers() {
        //return all the customer from customers table.
        String printCustomers="Select c.customerId,u.username,c.balance from Customer c join User u on c.userId=u.userId LIMIT=?";
        List<Customer>customers=new ArrayList<>();
        try(Connection conn=dbConnecter.createConnection();){
            try(PreparedStatement psPrintCustomers=conn.prepareStatement(printCustomers)){
                psPrintCustomers.setInt(1,50);
                ResultSet rsPrintCustomers=psPrintCustomers.executeQuery();
                Customer c=new Customer();
                while(rsPrintCustomers.next()){
                    c.setCustomerId(rsPrintCustomers.getLong("customerId"));
                    c.setUsername(rsPrintCustomers.getString("username"));
                    c.setBalance(rsPrintCustomers.getDouble("balance"));
                    customers.add(c);
                    c.printDetails();
                }
                return customers;
            }catch (SQLException sqe) {
                // If ANY query fails, we return
                System.out.println("Error while fetching customer details.");
                sqe.printStackTrace();
            }
        }catch (SQLException sqe) {
            // If ANY query fails,we return
            System.out.println("Error while connecting to database. ");
            sqe.printStackTrace();
        }

        return customers;
    }

    /**
     * Retrieves a complete log of all deposit transactions.
     * prints List of Deposit objects.
     */
    @Override
    public List<Deposit> getAllDeposits() {
        //print all the customer from customers table.
        String printDeposits ="Select * from Deposit";
        List<Deposit>deposits=new ArrayList<>();
        try(Connection conn=dbConnecter.createConnection();){
            try(PreparedStatement psPrintDeposits =conn.prepareStatement(printDeposits)){
                ResultSet rsPrintDeposits = psPrintDeposits.executeQuery();
                Deposit d=new Deposit();
                while(rsPrintDeposits.next()){
                    d.setCustomerId(rsPrintDeposits.getLong("customerId"));
                    d.setDepositAmount(rsPrintDeposits.getDouble("depositAmount"));
                    deposits.add(d);
                    d.printDetails();
                }

                return deposits;
            }catch (SQLException sqe) {
                // If ANY query fails, we return
                System.out.println("Error while fetching customer details.");
                sqe.printStackTrace();
            }
        }catch (SQLException sqe) {
            // If ANY query fails,we return
            System.out.println("Error while connecting to database. ");
            sqe.printStackTrace();
        }

        return deposits;
    }

    /**
     * Retrieves a complete log of all withdrawal transactions.
     * prints List of Withdraw objects.
     */
    @Override
    public List<Withdraw> getAllWithdrawals() {
        //print all the customer from customers table.
        String printWithdrawals ="Select * from Withdraw";
        List<Withdraw>withdrawals=new ArrayList<>();
        try(Connection conn=dbConnecter.createConnection();){
            try(PreparedStatement psPrintWithdrawals =conn.prepareStatement(printWithdrawals)){
                ResultSet rsPrintWithdrawals = psPrintWithdrawals.executeQuery();
                Withdraw w=new Withdraw();
                while(rsPrintWithdrawals.next()){
                    w.setCustomerId(rsPrintWithdrawals.getLong("customerId"));
                    w.setWithdrawalAmount(rsPrintWithdrawals.getDouble("withdrawalAmount"));
                    w.printDetails();
                    withdrawals.add(w);
                }

                return withdrawals;
            }catch (SQLException sqe) {
                // If ANY query fails, we return
                System.out.println("Error while fetching customer details.");
                sqe.printStackTrace();
            }
        }catch (SQLException sqe) {
            // If ANY query fails,we return
            System.out.println("Error while connecting to database. ");
            sqe.printStackTrace();
        }

        return withdrawals;
    }


    // ADMIN: Logic to Approve/Reject a Request .
    @Override
    public boolean processServiceRequest(long customerId,String requestType,long requestId,String status) {
        //THE SERVICE REQUEST IS CHECKED AND UPDATED IN service requests table.
        String updateServiceRequest="update serviceRequest set requestStatus=? where customerId=? and requestType=? and requestId=?";
        try(Connection conn=dbConnecter.createConnection()) {
            conn.setAutoCommit(false);
            //THE SERVICEREQUEST IS UPDATED IN respective table with new status.
            try(PreparedStatement psUpdateServiceRequest=conn.prepareStatement(updateServiceRequest)){
                psUpdateServiceRequest.setString(1,status);
                psUpdateServiceRequest.setLong(2,customerId);
                psUpdateServiceRequest.setString(3,requestType);
                psUpdateServiceRequest.setLong(4,requestId);

                // TRACER BULLET 1: See what we are sending
                System.out.println(">>> SENDING UPDATE: Status=" + status + ", Cust=" + customerId + ", Type=" + requestType + ", Req=" + requestId);

                int success=psUpdateServiceRequest.executeUpdate();

                // TRACER BULLET 2: See what MySQL responded with
                System.out.println(">>> ROWS UPDATED BY MYSQL: " + success);

                if(success == 0){
                    System.out.println(">>> FAILED: No matching rows found in DB!");
                    return false;
                }
                conn.commit();
                return true;
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while updating serviceRequest status. rolling back changes");
                conn.rollback();
                sqe.printStackTrace();
            }finally{
                conn.setAutoCommit(true);
            }

        }catch (SQLException sqe) {
            // If ANY query fails,we return
            System.out.println("Error while connecting to database. ");
            sqe.printStackTrace();
        }
        //if it does not work
        return false;
    }
}
