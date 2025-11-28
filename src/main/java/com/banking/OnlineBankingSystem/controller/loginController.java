package com.banking.OnlineBankingSystem.controller;

import com.banking.OnlineBankingSystem.model.Admin;
import com.banking.OnlineBankingSystem.model.Customer;
import com.banking.OnlineBankingSystem.service.customerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class loginController {
    private File dataFile;
    private final customerService customerService; //to use the customerService class.

    public loginController(customerService customerService){
        this.customerService=customerService;
    }

    @PostMapping
    public String userLogin(@RequestParam String role,@RequestParam String username,@RequestParam String password) throws IOException {
        if(role.equalsIgnoreCase("Admin")) {
            Admin admin = Admin.getAdminObject("root","123456","loanAndCreditcard");
            if(admin.getUsername().equals(username) && admin.getPassword().equals(password)){
                return "Login was successful.welcome admin";
            }else{
                return "Invalid login credentials";
            }
        }
        else if(role.equalsIgnoreCase("Customer")){
            dataFile=new File("CustomerDetails.json");

            //if file does not exist, make a new empty file.
            if(!dataFile.exists()){
                try{
                    customerService.saveCustomers(new ArrayList<>(),dataFile);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            List<Customer>customers=customerService.loadCustomers(dataFile);
            for(int i=0;i<customers.size();i++){
                if(customers.get(i).getUsername().equals(username) && customers.get(i).getPassword().equals(password)){
                    return "Login was successful. welcome "+username;
                }
            }
            return "Invalid login credentials";
        }
        return "Invalid role has been used";
    }
}
