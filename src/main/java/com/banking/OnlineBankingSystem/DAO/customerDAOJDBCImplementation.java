package com.banking.OnlineBankingSystem.DAO;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.model.Deposit;
import com.banking.OnlineBankingSystem.model.Withdraw;
import com.banking.OnlineBankingSystem.model.serviceRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository-This annotation is specifically designed for classes that handle
// data storage, retrieval, and search operations (like JSON files or SQL database)
//This annotation tells spring that it is a class which works with the database.
@Repository
@Primary //it tells spring that this is the primary way of dealing with data.
public class customerDAOJDBCImplementation implements customerDAO {
    private final databaseConnection dbConnecter;
    private final userDAO user;
    //to create a customer.
    /*
*  {
  "username" : "rekhaJhunjhunwala",
  "password" : "12436",
  "role" : "Customer",
  "customerId" : 2,
  "balance" : 10000.0,
  "serviceRequests" : null
}
* */
    public customerDAOJDBCImplementation(){
        this.dbConnecter=new databaseConnection();
        this.user=new userDAO();
    }
    //CRUD(CREATE,READ,UPDATE,DELETE) METHODS
    //CREATE A CUSTOMER
    @Override
    public Customer createCustomer(Customer newCustomer){
        //query statements
        //query to create a new customer.
        String customer="insert into Customer(userId,balance) values(?,?)";

        try(Connection conn = dbConnecter.createConnection();){
            //CONNECTION

            conn.setAutoCommit(false);
            try {
                //LOGIC TO CREATE A NEW CUSTOMER.
                //to create user and get user_id
                String role = "Customer";
                int user_id = user.createUser(conn, newCustomer.getUsername(), newCustomer.getPassword(), role);
                //INCASE OF FAILURE IN USER CREATION.
                if (user_id == -1) {
                    throw new SQLException("User creation failed");
                }
              try(PreparedStatement ps1 = conn.prepareStatement(customer);) {
                  ps1.setInt(1, user_id);
                  ps1.setDouble(2, newCustomer.getBalance());//starting balance is 0.0 for every customer.
                  ps1.executeUpdate();
              }
              catch (SQLException sqe) {
                  // If ANY query fails, we roll back to prevent half-created accounts
                  System.out.println("Error while creating the customer prepareStatement. rolling back changes");
                  conn.rollback();
                  sqe.printStackTrace();
              }

              //COMMIT THE TRANSACTION.
                conn.commit();

           //CLOSING OF CONNECTION AND PREPARE STATEMENT IS DONE BY JAVA ITSELF AS I AM USING TRY-WITH RESOURCES BLOCK.

                return newCustomer;
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while connecting to database. rolling back");
                conn.rollback();
                conn.setAutoCommit(true);
                sqe.printStackTrace();
            }
            finally{
                conn.setAutoCommit(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //creation of customer failed.
        return null;
    }
    //READ METHODS.
    //TO PRINT ALL AVAILABLE CUSTOMERS IN THE DATABASE.
    @Override
    public void getCustomerRecords(){
        String returnCustomers="select customerId,username,balance from Customer";
        try(Connection conn = dbConnecter.createConnection();) {
            PreparedStatement psReturnCustomers=conn.prepareStatement(returnCustomers);
            ResultSet rsCustomers=psReturnCustomers.executeQuery();

            while(rsCustomers.next()){
                String customerId=rsCustomers.getString("customerId");
                String username=rsCustomers.getString("username");
                String password=rsCustomers.getString("password");
                System.out.println("customerId: "+customerId+" username: "+username+" password: "+password);
            }

        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }

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
                    newCustomer.setCustomerId(customerId);
                    return newCustomer;
                }
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while fetching customerId. Rolling back changes.");
                sqe.printStackTrace();
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //if customer is not present.
        return null;
    }
    @Override
    public boolean isCustomerValid(long customerId) {
        //if customer is already present, true is returned esle false is returend,
        //TO CHECK IF A CUSTOMER ALREADY EXISTS.(IF YES RETURN FALSE.ALREADY PRESENT.)

        //query to check if a customer exists already.(YOU NEED TO CHECK THE USER TABLE.AS
        String checkCustomerExistence = "Select userId from Customer where customerId=?";
        //using try with resources block so that connection closes on its own.
        try (Connection conn = dbConnecter.createConnection();
             PreparedStatement psCheckCustomer = conn.prepareStatement(checkCustomerExistence);) {
            psCheckCustomer.setLong(1, customerId);
            try( ResultSet rsCheckCustomer = psCheckCustomer.executeQuery();){
                //IF CUSTOMER IS ALREADY PRESENT TRUE IS RETURNED.
                if (rsCheckCustomer.next()) {
                    return true;//as the customer already exists.
                }
            } catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error during account creation. Rolling back changes.");
                sqe.printStackTrace();
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //else if customer is not present
        return false;
    }
    //UPDATE METHODS.
    //UPDATE CUSTOMER BALANCE
    @Override
    public Customer updateCustomerBalance(long customerId, Customer newCustomer){
        //we add this amount to the current balance and then put it back.
        //so 1)fetch customerId,balance
        //2)do balance=amount.

        //queries
       // String findCustomerDetails="select c.balance from Customer c where c.customerId=?";
        String setCustomerBalance="update Customer set balance=? where customerId=?";

        try(Connection conn = dbConnecter.createConnection();){
            conn.setAutoCommit(false);

            try{
                //set the balance now.
                PreparedStatement psSetCustomerBalance=conn.prepareStatement(setCustomerBalance);
                psSetCustomerBalance.setLong(2,customerId);
                psSetCustomerBalance.setDouble(1,newCustomer.getBalance());
                psSetCustomerBalance.executeUpdate();

                //now reset the auto commit option.
                conn.commit();
                return getCustomerById(customerId);//the update was successful.
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while fetching details and updating balance. rolling back changes");
                conn.rollback();

                sqe.printStackTrace();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //incase the update failed.
        return null;
    }
    //TO SEE IF CUSTOMER PROVIDED THE CORRECT PASSWORD.SO THAT WE CAN CHANGE IT.
    @Override
    public boolean checkCustomerPassword(long customerId,String currentPassword){
        //return true if this customer provided the right password.else return false;
        String findCustomerPassword="Select u.password from User u join Customer c on u.userId=c.userId where c.customerId=?";
        String password=null;
        try(Connection conn = dbConnecter.createConnection();){
            PreparedStatement psFindPassword=conn.prepareStatement(findCustomerPassword);
            psFindPassword.setLong(1,customerId);
            ResultSet rsFindPassword=psFindPassword.executeQuery();
            if(rsFindPassword.next()){
                password=rsFindPassword.getString("password");
            }
            if(password!=null && password.equals(currentPassword)){
                return true;
            }
            else{
                //so the password given was incorrect.
                System.out.println("The user provided an incorrect password.");
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //password did not match
        return false;
    }
    //TO CHANGE THE CUSTOMER'S PASSWORD.
    @Override
    public boolean updateCustomerPassword(long customerId,String newPassword){
        //writing as a subquery is better than using a join.
        String updateCustomerPassword="update User u set password=? where u.userId=(select c.userId from Customer c where customerId=?)";

        try(Connection conn = dbConnecter.createConnection();){
            conn.setAutoCommit(false);
            try(PreparedStatement psUpdateCustomerPassword=conn.prepareStatement(updateCustomerPassword);) {
                psUpdateCustomerPassword.setString(1, newPassword);
                psUpdateCustomerPassword.setLong(2, customerId);
                int success = psUpdateCustomerPassword.executeUpdate();//it returns number of rows updated incase of success.
                if (success != 0) {
                    System.out.println("The password has been successfully changed");
                    conn.commit();
                    return true;
                }
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while updating the customer's password. rolling back changes");
                conn.rollback();
                sqe.printStackTrace();
            }finally{
                conn.setAutoCommit(true);
            }

        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //in case of failure
        return false;
    }

    //THIS METHOD USES Deposit CLASS OBJECTS AS WELL.(DOES UPDATE TO Deposits,Customer table) and returns a deposit object.
    @Override
    public Deposit depositMoney(Customer customer, double amount){

        //BASIC UPDATE IN Customer table.
        Customer newCustomer=new Customer(customer.getUsername(),customer.getPassword());
        newCustomer.setBalance(customer.getBalance()+amount);
        customer=updateCustomerBalance(customer.getCustomerId(),newCustomer); //now customer table is updated.

        //update RELATED TO  deposits table.
        String deposit="insert into Deposit(customerId,depositAmount) values(?,?)";
        try(Connection conn = dbConnecter.createConnection();) {
            conn.setAutoCommit(false);
            try(PreparedStatement psDeposit = conn.prepareStatement(deposit);){
                psDeposit.setLong(1,customer.getCustomerId());
                psDeposit.setDouble(2,amount);
                int success=psDeposit.executeUpdate();
                if(success!=0){
                    Deposit newDeposit = new Deposit(customer.getCustomerId(), amount);
                    conn.commit();
                    return newDeposit; //now returning the new saved customer deposit data which was saved to deposits table.
                }
            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while adding the deposit. rolling back changes");
                conn.rollback();
                sqe.printStackTrace();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //if nothing happens.
        return null;

    }

    //THIS METHOD USES Withdraw CLASS OBJECTS AS WELL.(DOES UPDATE TO Withdrawals.json and customerDetails.json) and returns a withdraw class object.
    @Override
    public Withdraw withdrawMoney(Customer customer, double amount){

            //BASIC UPDATE IN customer table.
            Customer newCustomer=new Customer(customer.getUsername(),customer.getPassword());

            //ELSE CONTINUE.
            newCustomer.setBalance(customer.getBalance()-amount);
            customer=updateCustomerBalance(customer.getCustomerId(),newCustomer); //now customer table is updated.

            //update RELATED TO  withdraw table.
            String withdraw="insert into Withdraw(customerId,withdrawalAmount) values(?,?)";
            try(Connection conn = dbConnecter.createConnection();) {
                conn.setAutoCommit(false);
                try(PreparedStatement psWithdraw = conn.prepareStatement(withdraw);){
                    psWithdraw.setLong(1,customer.getCustomerId());
                    psWithdraw.setDouble(2,amount);
                    int success=psWithdraw.executeUpdate();
                    if(success!=0){
                        Withdraw newWithdrawal=new Withdraw(customer.getCustomerId(),amount);
                        conn.commit();
                        return newWithdrawal; //now returning the new saved customer data which was saved to Withdraw table.
                    }

                }catch (SQLException sqe) {
                    // If ANY query fails, we roll back to prevent half-created accounts
                    System.out.println("Error while doing the withdrawal. rolling back changes");
                    conn.rollback();

                    sqe.printStackTrace();
                }finally{
                    conn.setAutoCommit(true);
                }
            }catch (SQLException e) {
                // This catches connection failures (e.g., database is offline)
                System.out.println("Database connection error: " + e.getMessage());
            }
        //if nothing happens
        return null;
    }

    // CUSTOMER: Create a loan request and persist it to both the customer table and LoanRequests table which is service requests table.
    @Override
    public serviceRequest createLoanRequest(long customerId, double amount) {
            //query to create a service request.
            String createServiceRequest="insert into serviceRequest(customerId,requestType,requestAmount) values (?,?,?)";

            try(Connection conn = dbConnecter.createConnection();) {
                conn.setAutoCommit(false);
                try(PreparedStatement psCreateLoanRequest = conn.prepareStatement(createServiceRequest);){
                    psCreateLoanRequest.setLong(1,customerId);
                    psCreateLoanRequest.setString(2,"Loan");
                    psCreateLoanRequest.setDouble(3,amount);
                    int success=psCreateLoanRequest.executeUpdate();
                    if(success!=0){
                        serviceRequest req = new serviceRequest(customerId,"Loan", amount);
                        conn.commit();
                        return req; //now returning the new saved customer data which was saved to Withdraw table.
                    }

                }catch (SQLException sqe) {
                    // If ANY query fails, we roll back to prevent half-created accounts
                    System.out.println("Error while adding a loan request. rolling back changes");
                    conn.rollback();

                    sqe.printStackTrace();
                }finally{
                    conn.setAutoCommit(true);
                }
            }catch (SQLException e) {
                // This catches connection failures (e.g., database is offline)
                System.out.println("Database connection error: " + e.getMessage());
            }
          //incase it fails
            return null;
    }

    // CUSTOMER: Create a credit card request and persist it to both the customer table and creditcardRequests table which is service requests table.
    @Override
    public serviceRequest createCreditCardRequest(long customerId, double amount) {
        //query to create a service request.
        String createServiceRequest="insert into serviceRequest(customerId,requestType,requestAmount) values (?,?,?)";

        try(Connection conn = dbConnecter.createConnection();) {
            conn.setAutoCommit(false);
            try(PreparedStatement psCreateCreditCardRequest = conn.prepareStatement(createServiceRequest);){
                psCreateCreditCardRequest.setLong(1,customerId);
                psCreateCreditCardRequest.setString(2,"CreditCard");
                psCreateCreditCardRequest.setDouble(3,amount);
                int success=psCreateCreditCardRequest.executeUpdate();
                if(success!=0){
                    serviceRequest req = new serviceRequest(customerId,"CreditCard", amount);
                    conn.commit();
                    return req; //now returning the new saved customer data which was saved to Withdraw table.
                }

            }catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error while adding a credit card request. rolling back changes");
                conn.rollback();

                sqe.printStackTrace();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }
        //incase it fails
        return null;
    }
    //DELETE METHODS.
    //DELETE A CUSTOMER
    @Override
    public boolean deleteCustomer(long customerId){
        //query statements
        //to find userId
        String findUserId="Select userId from Customer where customerId=?";
        //to remove existing records of a customer from deposit table
        String eraseCustomerFromDeposit="delete from Deposit where customerId=?";
        //to remove existing records of a customer from withdraw table
        String eraseCustomerFromWithdraw="delete from Withdraw where customerId=?";
        //to remove existing records of a customer from service request table
        String eraseCustomerFromServiceRequest="delete from serviceRequest where customerId=?";
        //to remove customer from Customer table
        String eraseCustomerFromCustomer="delete from Customer where userId=?";
        //to remove customer from User table
        String eraseCustomerFromUser="delete from User where userId=?";

        try(Connection conn = dbConnecter.createConnection();
            PreparedStatement psFindUserId=conn.prepareStatement(findUserId);){
            //CONNECTION

            conn.setAutoCommit(false);

            String userId = null;//store the userId here.
            //IF CUSTOMER IS NOT PRESENT.return false.

            //LOGIC TO delete a customer.

            psFindUserId.setLong(1,customerId);
            try(ResultSet rsFindUserId=psFindUserId.executeQuery();) {

                if (rsFindUserId.next()) {
                    userId = rsFindUserId.getString("userId");
                }
            }
            //INCASE THE USER DOES NOT EXIST.
            if(userId==null){
                System.out.println("Customer ID not found.");
                return false;
            }
            //NOW THE LOGIC TO DELETE ALL EXISTING RECORDS OF THE CUSTOMER.
        try(    PreparedStatement psDeleteCustomerFromDeposit=conn.prepareStatement(eraseCustomerFromDeposit);
                PreparedStatement psDeleteCustomerFromWithdraw=conn.prepareStatement(eraseCustomerFromWithdraw);
                PreparedStatement psDeleteCustomerFromServiceRequest=conn.prepareStatement(eraseCustomerFromServiceRequest);
                PreparedStatement psDeleteCustomerFromCustomer = conn.prepareStatement(eraseCustomerFromCustomer);
                PreparedStatement psDeleteCustomerFromUser = conn.prepareStatement(eraseCustomerFromUser);) {

            //USING CUSTOMER ID, DELETE THE EXISTING CUSTOMER RECORDS FROM DEPOSIT,WITHDRAW,SERVICE REQUEST TABLES.
                //for deposit table
                psDeleteCustomerFromDeposit.setLong(1,customerId);
                int depositSuccess=psDeleteCustomerFromDeposit.executeUpdate();

                //for withdraw table
                psDeleteCustomerFromWithdraw.setLong(1,customerId);
                int withdrawSuccess=psDeleteCustomerFromWithdraw.executeUpdate();

                //for serviceRequest table
                psDeleteCustomerFromServiceRequest.setLong(1,customerId);
                int serviceRequestSuccess=psDeleteCustomerFromServiceRequest.executeUpdate();

            //USING USERID DELETE THE CUSTOMER FROM USER AND CUSTOMER TABLES.
                //for customer table.
                psDeleteCustomerFromCustomer.setString(1, userId);
                int customerSuccess=psDeleteCustomerFromCustomer.executeUpdate();

                if (customerSuccess==0) {
                    // This triggers the catch block!.so rollback the changes
                    //customer did not exist in first place.
                    System.out.println("Deletion of customer from Customer table failed");
                    throw new SQLException("Failed: Customer record not found.");
                }
                //for user table.
                psDeleteCustomerFromUser.setString(1, userId);
                int userSuccess=psDeleteCustomerFromUser.executeUpdate();
                if(userSuccess==0){
                    //so rollback the changes.user did not exist in first place.
                    System.out.println("Deletion of customer from User table failed");
                    throw new SQLException("Failed: User credentials not found.");
                }
                //DELETION WAS SUCCESSfUL.
            //IT IS OK IF NO RECORDS ARE DELETED IN DEPOSIT,WITHDRAW,SERVICE REQUEST TABLE AS
            //THEY ARE EMPTY FOR A NEW CUSTOMER WHO JUST CREATED HIS ACCOUNT AND WANTS TO DELETE IT.(WE SHOULD TAKE CARE OF THIS CASE TOO)
                conn.commit();
                return true;
            } catch (SQLException sqe) {
                // If ANY query fails, we roll back to prevent half-created accounts
                System.out.println("Error during account deletion. Rolling back changes.");
                conn.rollback();
                sqe.printStackTrace();
            }finally{
                //reset setAutocommit
                conn.setAutoCommit(true);
            }




        }catch (SQLException e) {
            // This catches connection failures (e.g., database is offline)
            System.out.println("Database connection error: " + e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }
        //deletion failed
        System.out.println("The deletion of customer was unsuccessful");
        return false;

    }
    /// I ADDED MAIN TO PERFORM CHECKS.
    // Add this to the very bottom of your CustomerDAO class
    public static void main(String[] args) {
        System.out.println("--- Starting Database Tests ---");

        // 1. Create an instance of your DAO
        customerDAO dao = new customerDAOJDBCImplementation();
        String testUsername = "test_user_99";

        // 2. Test your read function
        System.out.println("Checking if user exists...");
        boolean exists = dao.isCustomerValid(1);
        System.out.println("Does " + testUsername + " exist? " + exists);

        // 3. Test your insert function
        if (!exists) {
            System.out.println("\nCreating new customer profile...");
            // Use BigDecimal for the balance as discussed earlier
           // dao.createCustomer(testUsername);

            // 4. Verify the insert actually worked
            System.out.println("\nVerifying creation...");
            boolean existsNow = dao.isCustomerValid(1);
            System.out.println("Does " + testUsername + " exist now? " + existsNow);
        } else {
            System.out.println("\nSkipping creation test because the user already exists in the database.");
        }

        System.out.println("--- Tests Complete ---");
    }
}
