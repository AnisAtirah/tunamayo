package partnajah;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

public class partnajah {
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int i = 0;
        
        //Creating arraylist to store task information
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> describe = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> category = new ArrayList<>();
        ArrayList<String> priority = new ArrayList<>();

        //User select which procedure
        while (true) {
            System.out.print("What do you want to do today ");
            System.out.println("\n1) Create a task");
            System.out.println("2) Manage task");
            System.out.println("3) Delete task");
            System.out.println("4) View all tasks");
            System.out.println("5) Edit task");
            System.out.println("6) Exit program");
            System.out.print("Enter number: ");
            int option = input.nextInt();
            input.nextLine();
        
            switch (option){
                case 1:
                    i = createTask(i, title, describe, date, category, priority, input);
                    break;
                case 2:
                    manageTask(i, title, describe, date, category, priority, input);
                    break;
                case 3:
                    deleteTaskArr(title, describe, date, category, priority, input);
                    break;
                case 4:
                    viewTask();
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("Bye for Now!");
                    return;
                default:
                    System.out.println("Invalid number please enter again");
                    System.out.println("");
                    break;  
            }
        } 
    }
    
    public static int createTask(int i, ArrayList<String> title, ArrayList<String> describe, ArrayList<String> date, ArrayList<String> category, ArrayList<String> priority, Scanner input){
        
        while (true) {
            System.out.println("");
            System.out.println("========== Add a New task ==========");
            System.out.print("Enter task title: ");
            title.add(input.nextLine());
            System.out.print("Enter task description: ");
            describe.add(input.nextLine());
            System.out.print("Enter due date (YYYY-MM-DD): ");
            date.add(input.nextLine());
            System.out.print("Enter task category (Homework / Personal / Work): ");
            category.add(input.nextLine());
            System.out.print("Enter priority level (High / Medium / Low): ");
            priority.add(input.nextLine());   
            
            String title1 = title.get(i);
            String describe1 = describe.get(i);
            String  date1 = date.get(i);
            String category1 = category.get(i);
            String priority1 = priority.get(i);
                    
            System.out.println("Task \"" + title.get(i) + "\" added succesfully!");
            insertData(i, title1, describe1, date1, category1, priority1);
            i++; 
            System.out.print("Add more task? (Enter Yes / No): ");
            String choice = input.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                System.out.println("");
                return i;
            }
        }
    }
    
    public static void insertData(int i, String title, String describe , String date, String category, String priority){
        Connection conn1 = null; 
            
        try {
            
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        if (conn1 != null) {
            System.out.println("YOU'RE CONNECTED TO DATABASE");

            String sqli = "insert into task(numTask, Title, Description, Due, Category, Priority) values (?,?,?,?,?,?)";
            var myStat = conn1.prepareStatement(sqli);

            myStat.setInt(1, i);
            myStat.setString(2, title);
            myStat.setString(3, describe);
            myStat.setString(4, date);
            myStat.setString(5, category);
            myStat.setString(6, priority);

            myStat.executeUpdate();
            System.out.println("DATA INSERTED");
           
        }
        } catch(SQLException ex){
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
            }
        }
         
    }
    
    public static void deleteTaskArr(ArrayList<String> title, ArrayList<String> describe, ArrayList<String> date, ArrayList<String> category, ArrayList<String> priority, Scanner input){
        while (true){
            System.out.println("");
            System.out.println("========== Delete Task =========="); 
            System.out.print("Enter task number: ");
            int taskNum = input.nextInt()- 1;
            input.nextLine();
            
            //String deleteT = title.get(taskNum);
            deleteTask(taskNum);
            //remove every info berkaitan dengan task
            title.remove(taskNum);
            describe.remove(taskNum);
            date.remove(taskNum);
            category.remove(taskNum);
            priority.remove(taskNum);
            
            System.out.println("Task removed");
            System.out.print("Delete more task? (Enter Yes / No): ");
            String choice = input.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                System.out.println("");
                return;
            }
        }
    }
    
    public static void deleteTask(int taskNum) {
        Connection conn1 = null; 
            
        try {
            
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        
        String sqlo = "DELETE FROM task WHERE numTask=?";
        var myStat = conn1.prepareStatement(sqlo);
        
        myStat.setInt(1, taskNum);
        myStat.execute();
        
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
            }
        }
    }

    public static void manageTask(int i, ArrayList<String> title, ArrayList<String> describe, ArrayList<String> date, ArrayList<String> category, ArrayList<String> priority, Scanner input){
        while (true){
            System.out.println("");
            System.out.println("========== Task Management ==========");
            System.out.println("1) Mark Task Complete");
            System.out.println("2) Mark Task Incomplete");
            //System.out.println("3) Update Task Detail");
            System.out.print("Enter number: ");
            int option = input.nextInt();
            input.nextLine();
       
            switch (option) {
                case 1:
                    System.out.println("");
                    System.out.println("========== Mark Task as Complete ==========");
                    System.out.print("Enter task number: ");
                    i = input.nextInt() - 1;
                    input.nextLine();
                    if (i <= 0 && i < title.size()) {
                        System.out.print("Task \"" + title.get(i) + "\" marked as complete");
                    } else {
                        System.out.println("Invalid task number");
                    }
                    break;
                
                case 2:
                    System.out.println("");
                    System.out.println("========== Mark Task as Incomplete ==========");
                    System.out.print("Enter task number: ");
                    int j = input.nextInt() - 1;
                    input.nextLine();
                    if (j <= 0 && j < title.size()) {
                        System.out.print("Task \"" + title.get(j) + "\" marked as incomplete");
                    } else {
                        System.out.print("Invalid task number");
                    }
                    break;
                
                /*case 3:
                    System.out.println("");
                    System.out.println("========== Update Task Detail ==========");
                    System.out.println("1) Update title");
                    System.out.println("2) Update description");
                    System.out.println("3) Update due date");
                    System.out.println("4) Update category");
                    System.out.println("5) Update priority level");
                    System.out.print("Enter task number: ");
                    int taskNum = input.nextInt() - 1;
                    System.out.println("Update task \"" + title.get(taskNum) + "\"");
                    System.out.print("Enter the number of details you want to update: ");
                    int update = input.nextInt();
                    input.nextLine();
                
                    /*switch (update) {
                        case 1:
                            System.out.print("New task title: ");
                            title.set(taskNum, input.nextLine());
                            System.out.println("Task title updated!");
                            break;
                        case 2:
                            System.out.print("New task description: ");
                            describe.set(taskNum, input.nextLine());
                            System.out.println("Task description updated!");
                            break;
                        case 3:
                            System.out.print("New task due date: ");
                            date.set(taskNum, input.nextLine());
                            System.out.println("Task due updated!");
                            break;
                        case 4:
                            System.out.print("New task category: ");
                            category.set(taskNum, input.nextLine());
                            System.out.println("Task category updated");
                            break;
                        case 5:
                            System.out.print("New task priority level: ");
                            priority.set(taskNum, input.nextLine());
                            System.out.println("Task priority updated!");
                            break;
                        default:
                            System.out.println("Invalid number entered");
                            break;
                   }*/
            }
            System.out.print("Update more task? (Enter Yes / No): ");
            String choice = input.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                System.out.println("");
                return;
            }         
        }
    }
    
    public static void viewTask() {
        Connection conn1 = null; 
            
        try {
            
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        //connect with your database
        conn1 = DriverManager.getConnection(url1, user, password);
        if (conn1 != null) {
            System.out.println("\n=== View All Tasks ===");
            String sql1 = "SELECT * FROM task where numTask>=0";
            var check = conn1.prepareStatement(sql1);

            var results = check.executeQuery();
            while (results.next()){
                System.out.println(results.getInt(1)+ ". [" + results.getNString(7) +  "] " + results.getNString(2) + " - Due: " + results.getDate(4));
            } 
            System.out.println("");
        }
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
            }
        }
    }


}
