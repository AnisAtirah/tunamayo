package fetcdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

import java.util.ArrayList;

public class FetcData {
    
    public static void main(String[] args) {
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> describe = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> category = new ArrayList<>();
        ArrayList<String> priority = new ArrayList<>();

        //calling method
        CatchData (title, describe, date, category, priority);
        
         System.out.println(title);
         System.out.println(describe);
         System.out.println(date);
         System.out.println(category);
         System.out.println(priority);
    }
      
    
    public static void CatchData (ArrayList<String> Title, ArrayList<String> Description, ArrayList<String> Due, ArrayList<String> Category, ArrayList<String> Priority){
       
        Connection conn1 = null;
        PreparedStatement p = null;
        ResultSet rs = null; 
     
        try {
        
        // Importing and registering drivers
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        
        //command to select data
        String sql="select * from task";
        p = conn1.prepareStatement(sql);
        rs = p.executeQuery();
        
        //fetch until there is no data
        while (rs.next()) {
            
            //update data to arraylist
            Title.add (rs.getString("Title"));
            Description.add (rs.getString("Description"));
            Due.add (rs.getString("Due"));
            Category.add (rs.getString("Category"));
            Priority.add (rs.getString("Priority"));
        }
        }catch (SQLException e) {
            System.out.println(e);
     
    }}
}
