package com.banking.OnlineBankingSystem.model;

//JSON format while adding a customer.
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

//FOR NOW A SINGLE ADMIN IS PRESENT.
public class Admin extends User {
    private static Admin adminObject;
    private static long adminUserId; //unique identifier for the admin.
    private long adminId;//unique id of the admin.
  //  private String department;//admin can do both loan and creditcard departments.

    private Admin(){}

    private Admin(String username, String password, String department) {
        super(username, password, "Admin");
       // this.department = department;
        this.adminId = ++adminUserId;
    }

    public static Admin getAdminObject(String username,String password,String department){
        if(adminObject==null){
            adminObject=new Admin(username,password,department);
        }
        return adminObject;
    }

    public long getAdminId() {
        return this.adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    /*
   public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    */

}
