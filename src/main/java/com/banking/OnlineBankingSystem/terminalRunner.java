package com.banking.OnlineBankingSystem;
/*
import java.io.IOException;
import java.util.Scanner;

import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.service.customerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component //this annotation tells springboot that this is a part of our project.
public class terminalRunner implements CommandLineRunner {

    @Autowired //it tells springboot.hey,make an instance of customerService.
    private customerService customerService;

    @Override
    public void run(String... args)throws Exception{
        Scanner sc=new Scanner(System.in);
        boolean running=true;

        System.out.println("\n<----ONLINE BANKING SYSTEM BY J1---->\n");

        while(running){
            //printing main menu.
            System.out.println("OPTIONS AVAILABLE:");
            System.out.println("1)Register (If you are a new user)");
            System.out.print("2)Login\n"+"3)Exit\n"+"Enter option number: ");
            int choice;// to take user choice.
            try {
                choice = Integer.parseInt(sc.next());
            } catch(NumberFormatException e){
                System.out.println("Enter a valid option!!!");
                continue; //to skip this iteration and start again.
            }
            //based on option, call the method.

            switch(choice){
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    loginCustomer();
                    break;
                case 3:
                    running=false;
                    System.out.println("EXITING!!!");
                    break;
                default:
                    System.out.println("Enter a valid option!!!");
            }

        }
        System.out.println("EXIT SUCCESSFUL. PLEASE DO COMEBACK");
        System.exit(0);//now the main thread(connecting to tomcat) stops as well.
    }

    public void loginCustomer() throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("<----LOGIN PORTAL---->");
        System.out.print("Enter Username: ");
        String username=sc.nextLine();
        System.out.print("Enter password: ");
        String password=sc.nextLine();

        Customer customer=new Customer(username,password);
        Customer loginCustomer=customerService.customerLogin(customer);

        if(loginCustomer!=null){
            System.out.println("Logged in successfully. Welcome "+username);
            customerMenu(loginCustomer);// now the customer menu gets loaded.
        }else{
            System.out.println("Invalid credentials");
        }

    }

    public void registerCustomer(){
        Scanner sc=new Scanner(System.in);
        System.out.println("<----REGISTER PORTAL---->");
        System.out.print("Enter Username: ");
        String username=sc.nextLine();
        System.out.print("Enter password: ");
        String password=sc.nextLine();

        Customer customer=new Customer(username,password);
        Customer createdCustomer=customerService.createCustomer(customer);

        if(createdCustomer!=null){
            System.out.println("Registration has been successfully done");
        }else{
            System.out.println("Registration was not successful");
        }
    }

    public void depositMoney(Customer customer) throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the amount you want to deposit: ");
        double amount=sc.nextDouble();
        //customer balance is updated.
        customer=customerService.withdrawalOrDeposit(customer,amount,"DEPOSIT");

        if(customer!=null){
            System.out.println("DEPOSIT IS SUCCESSFUL");
        }else{
            System.out.println("DEPOSIT FAILED");
        }
    }

    public void withdrawMoney(Customer customer) throws IOException {
        Scanner sc=new Scanner(System.in);
        double newBalance;
        do {
            System.out.print("Enter the amount you want to withdraw: ");
            double amount = sc.nextDouble();
            newBalance = customer.getBalance() - amount;
            if (newBalance < 0)
                System.out.println("BALANCE LIMIT EXCEEDED. CURRENT BALANCE: " + customer.getBalance() + "PLEASE INPUT BASED ON THAT");
        }while(newBalance<0);
        //customer balance is updated.
        customer=customerService.withdrawalOrDeposit(customer,newBalance,"WITHDRAW");

        if(customer!=null){
            System.out.println("WITHDRAWAL IS SUCCESSFUL");
        }else{
            System.out.println("WITHDRAWAL FAILED");
        }
    }

    public void customerMenu(Customer customer) throws IOException {
        Scanner sc=new Scanner(System.in);
        boolean loggedIn=true;
        while(loggedIn){
            System.out.println("<----CUSTOMER MENU---->");
            System.out.println("1)Check balance");
            System.out.println("2)Deposit funds");
            System.out.println("3)Withdraw funds");
            System.out.println("4)Exit");
            int choice;
            try {
                choice = Integer.parseInt(sc.next());
            } catch(NumberFormatException e){
                System.out.println("Enter a valid option!!!");
                continue; //to skip this iteration and start again.
            }

            switch(choice){
                case 1:
                    customer=customerService.getCustomerById(customer.getCustomerId());
                    System.out.println("CURRENT BALANCE: "+customer.getBalance());
                    break;
                case 2:
                    depositMoney(customer);
                    break;
                case 3:
                    withdrawMoney(customer);
                case 4:
                    loggedIn=false;
                    System.out.println("LOGGING OUT!!!");
                    break;
                default:
                    System.out.println("Enter a valid option!!!");
            }
        }
    }
}


 */