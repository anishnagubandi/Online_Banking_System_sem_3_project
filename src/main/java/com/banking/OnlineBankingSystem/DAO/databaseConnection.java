package com.banking.OnlineBankingSystem.DAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class databaseConnection{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL =  "YOUR_DATABASE_URL";
    static final String USER = "YOUR_USERNAME";
    static final String PASSWORD = "YOUR_PASSWORD";
    public Connection createConnection(){
        try {
            //to load and register the driver.
            Class.forName(JDBC_DRIVER);
            //to create a connection
            //ESTABLISH CONNECTION and return it.
            System.out.println("Establishing connection...");
            return DriverManager.getConnection(DB_URL,USER,PASSWORD);

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}