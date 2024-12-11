
package tunamayo_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Tunamayo_db {

    public static void insertData (String [] Title, String [] Desc, int [] Due, String [] Category, String [] Priority){
        Connection conn1 = null; 
        
        for (int i = 0; i<Title.length ; i++){ 
            
        try {
            
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        System.out.println("YOU'RE CONNECTED TO DATABASE");
        
        String sqli = "insert into task(Title, Description, Due, Category, Priority) values (?,?,?,?,?)";
        var myStat = conn1.prepareStatement(sqli);
        
        
        myStat.setString(1, Title[i]);
        myStat.setString(2, Desc[i]);
        myStat.setInt(3, Due[i]);
        myStat.setString(4, Category[i]);
        myStat.setString(5, Priority[i]);
 
        
        myStat.executeUpdate();
        System.out.println("DATA INSERTED");
        
        }catch(SQLException ex){
            System.out.println("An error occurred");
            ex.printStackTrace();
        }
        
        
        
        
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }}
        
        }
        
    }
    
    public static void updateTitle(String TitleNew, int No){
                Connection conn1 = null; 
        
        try {
            
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        System.out.println("YOU'RE CONNECTED TO DATABASE");
        
        String sqli = "UPDATE task SET Title=? where No=?";
        var myStat = conn1.prepareStatement(sqli);
        
        myStat.setString(1, TitleNew);
        myStat.setInt(2, No); 
        myStat.executeUpdate();
        System.out.println("DATA UPDATED");
        
        }catch(SQLException ex){
            System.out.println("An error occurred");
            ex.printStackTrace();
        }
        
        finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }}
    }
            
    public static void main(String[] args) {
        
        String [] arrayS = {"anis", "atirah", "aris"};
        int [] arrayN = {21,22,23};
        insertData(arrayS, arrayS, arrayN, arrayS, arrayS);
        
        String titleN = "comel";
        int no = 2;
        
        updateTitle (titleN, no);
        
        
        
    }
    
}
