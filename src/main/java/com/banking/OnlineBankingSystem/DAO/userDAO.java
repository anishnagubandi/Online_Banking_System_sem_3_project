package com.banking.OnlineBankingSystem.DAO;

import java.sql.*;

public class userDAO{

    public int createUser(Connection conn, String username, String password, String role)throws SQLException {

        //query statement to create a new user
        String user="insert into User(username,password,role) values(?,?,?)";

        //to make sure that the user id is returned after creation for further usage,we use RETURN_GENERATED_KEYS.
        PreparedStatement ps=conn.prepareStatement(user, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,username);
        ps.setString(2,password);
        ps.setString(3,role);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();//to get the user_id
        int user_id=-1;
        if(rs.next()){
            user_id=rs.getInt(1);
        }
        return user_id;//we return newly generated user_id.
    }
}
