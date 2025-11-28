package com.banking.OnlineBankingSystem.model;

public abstract class User {
    protected String username;//name of the account holder.
    protected String password;//password of the user.
    protected String role; //can be a customer or a admin.

    public User(){}

    public User(String username,String password,String role){
        this.username=username;
        this.password=password;
        this.role=role;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getRole(){
        return this.role;
    }

    public void setRole(String role){
        this.role=role;
    }

}


