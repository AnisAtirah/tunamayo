package recurringtask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class RecurringTask extends Task {
    private static String recurrenceInterval; //"weekly", "daily", "monthly"
    private static LocalDate nextOccurence;
    private static LocalDate due;
    private static java.sql.Date sqlDue;
    private static java.sql.Date sqlnextOcc;
    private static ArrayList<Task> taskList = new ArrayList<>();
    private static int i = 1;
    private static int complete;
    
    public RecurringTask(String title, String description, String recurrenceInterval, LocalDate nextOccurence, LocalDate due, int complete) {
    super(title, description);
    this.recurrenceInterval = recurrenceInterval;
    this.nextOccurence = LocalDate.now();
    this.due = calculateDueDate(nextOccurence, recurrenceInterval);
    this.complete = complete;
    }
        public static void main(String[] args) {
        loadRecurTasksFromDatabase();
        scheduleRecurringTaskCheck();
        Scanner scanner = new Scanner(System.in);
        int menu;
        int numTask;
        do {
            System.out.println("1. Add recurring tasks");
            System.out.println("2. Task completion");
            System.out.println("3. Display task");
            System.out.println("4. Exit");
            System.out.print("Enter number: \n");
            menu = scanner.nextInt();

            switch(menu) {
                case 1:
                    System.out.println("\n=== Add a Recurring Task ===");
                   addRecurringTask();
                   
                    break;
                case 2:
                    displayTaskTitle();
                    if (taskList.isEmpty()){
                        break;
                    }
                    System.out.print("Which task do you want to mark as completed? (Insert number): \n");
                    numTask = scanner.nextInt();
                    completeTask(numTask-1);
                    
                    break;
                    case 3:
                    displayTask();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Incorrent input.");

            }
    } while (menu != 4);
            System.out.println("Thank you");
  }
   
    private static void scheduleRecurringTaskCheck() {
        // Create a ScheduledExecutorService that runs the update task periodically
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the update task every 1 minute (you can change this to any interval)
        scheduler.scheduleAtFixedRate(() -> {
            // Call the method that checks and updates task completion
            selectTaskCompletionIfDue();
            System.out.println("Task checked!");
        }, 0, 1, TimeUnit.MINUTES);  // Initial delay of 0, repeat every 1 minute
        
    }
        
    private LocalDate calculateDueDate(LocalDate nextOccurence, String recurrenceInterval) {
    switch (recurrenceInterval.toLowerCase()) {
        case "daily":
            return nextOccurence.plusDays(1);
        case "weekly":
            return nextOccurence.plusWeeks(1);
        case "monthly":
            return nextOccurence.plusMonths(1);
        default:
            throw new IllegalArgumentException("Invalid recurrence interval: " + recurrenceInterval);
    }
}

    public String getRecurrenceInterval(){
        return recurrenceInterval;
    }
    
    public LocalDate getNextOccurence() {
        return nextOccurence;
    }
    
    public LocalDate getDue() {
        return due;
    }
    
    public int getCompletion() {
        return complete;
    }
    

    public static void updateNextOccurence(int taskId){
        //System.out.println("CURRENT NEXT OCCURENCE BASED ON TASK: "+nextOccurence);
        Task task = taskList.get(taskId);
        nextOccurence=LocalDate.now();
        RecurringTask recurringTask = (RecurringTask) task;
        switch (recurringTask.getRecurrenceInterval().toLowerCase()){
            case "daily":
                nextOccurence = nextOccurence.plusDays(1);
                due = nextOccurence.plusDays(1);
                break;
            case "weekly":
                nextOccurence = nextOccurence.plusWeeks(1);
                due = nextOccurence.plusWeeks(1);

                break;
            case "monthly":
                nextOccurence = nextOccurence.plusMonths(1);
                due = nextOccurence.plusMonths(1);
                break;
            default:
                throw new IllegalArgumentException ("Invalid recurrence interval.");
        }
        
        sqlnextOcc = java.sql.Date.valueOf(nextOccurence);
        sqlDue = java.sql.Date.valueOf(due);

        //System.out.println("AFTER UPDATE NEXT OCCURENCE BASED ON TASK: "+sqlnextOcc);
        //System.out.println("AFTER UPDATE DUEBASED ON TASK: "+sqlDue);
        String url = "jdbc:mysql://localhost:3306/tunamayo_db";
        String user = "root";
        String password = "";

        // SQL query template for updating
        String sql = "UPDATE recurtask SET NextOccurence = ?, Due = ? WHERE numTask = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement
            preparedStatement = connection.prepareStatement(sql);

            // Set the parameter values
            preparedStatement.setDate(1, sqlnextOcc);
            preparedStatement.setDate(2, sqlDue); 
            preparedStatement.setInt(3, taskId); 


            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void addRecurringTask(){
        Scanner scanner = new Scanner(System.in);
        String interval;
        int taskcomplete = 0;
        
        System.out.println("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.println("Enter task description: ");
        String description = scanner.nextLine();
        
        inputInterval: 
            while (true){
            System.out.println("Enter task recurrence interval (daily, weekly, monthly): ");
            interval = scanner.next();
            if (interval.equalsIgnoreCase("daily")||interval.equalsIgnoreCase("weekly")||interval.equalsIgnoreCase("monthly"))
                break inputInterval;
            else {
               System.out.println("Invalid interval.");
               continue inputInterval;
                }
        }
        
        
       nextOccurence = LocalDate.now();
       
        switch (interval.toLowerCase()){
            case "daily":
                due = nextOccurence.plusDays(1);
                break;
            case "weekly":
                due = nextOccurence.plusWeeks(1);
                break;
            case "monthly":
                due = nextOccurence.plusMonths(1);
                break;
            default:
                throw new IllegalArgumentException ("Invalid recurrence interval.");
        }
        
        sqlnextOcc = java.sql.Date.valueOf(nextOccurence);
        sqlDue = java.sql.Date.valueOf(due);
        
        RecurringTask recurringTask = new RecurringTask(title, description, interval, nextOccurence, due, taskcomplete);
        taskList.add(recurringTask);
        insertData(title, description, interval, taskcomplete);
        i++;
        System.out.println("\nRecurring Task '"+ title +"' created successfully!\n");
    }
    
    public static void insertData(String title, String description, String interval, int taskcomplete){
        nextOccurence = LocalDate.now();
        
        switch (interval.toLowerCase()){
            case "daily":
                due = nextOccurence.plusDays(1);
                break;
            case "weekly":
                due = nextOccurence.plusWeeks(1);
                break;
            case "monthly":
                due = nextOccurence.plusMonths(1);
                break;
            default:
                throw new IllegalArgumentException ("Invalid recurrence interval.");
        }
        
        sqlDue = java.sql.Date.valueOf(due);
        sqlnextOcc = java.sql.Date.valueOf(nextOccurence);
        
        Connection conn1 = null; 
        try {
            String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
            String user = "root";
            String password = "";
            
            //connect with your database
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                String sql1 = "INSERT into recurtask(numTask, Title, Description, RecurrenceInterval, NextOccurence, Due, Completion) values (?,?,?,?,?,?,?)";
                var myStat = conn1.prepareStatement(sql1);

                        myStat.setInt(1, i);
                        myStat.setString(2, title);
                        myStat.setString(3, description);
                        myStat.setString(4, interval);
                        myStat.setDate(5, sqlnextOcc);
                        myStat.setDate(6, sqlDue);
                        myStat.setInt(7, taskcomplete);
                        
                        myStat.executeUpdate();
            }
        } catch (SQLException ex){
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
    
    public static void completeTask(int taskId){
        if (taskId < 0 || taskId >= taskList.size()){
            System.out.println("Invalid task ID.\n");
            return;
        }
        
        Task task = taskList.get(taskId);
        
        
        if (task instanceof RecurringTask) {
            RecurringTask recurringTask = (RecurringTask) task;
            complete = 1;
            updateNextOccurence(taskId);
            System.out.println("\nRecurring Task '" + recurringTask.getTitle() + "' completed!\n");
        } else {
            System.out.println("\nTask '" + task.getTitle() +"' completed!\n");
            taskList.remove(taskId); //Non-recurring tasks are removed
        }
        
        //nk buat update to table
        String url = "jdbc:mysql://localhost:3306/tunamayo_db";
        String user = "root";
        String password = "";

        // SQL query template for updating
        String sql = "UPDATE recurtask SET Completion = ? WHERE numTask = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement
            preparedStatement = connection.prepareStatement(sql);

            // Set the parameter values
            preparedStatement.setInt(1, complete);
            preparedStatement.setInt(2, taskId+1); 

            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void selectTaskCompletionIfDue() {
        LocalDate today = LocalDate.now();

        Connection conn = null; 
        try {
            String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
            String user = "root";
            String password = "";
            
            //connect with your database
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                String sql = "SELECT numTask, NextOccurence, Completion FROM recurtask";
                var myStat = conn.prepareStatement(sql);
                 ResultSet rs = myStat.executeQuery();

                while (rs.next()) {
                    int numTask = rs.getInt("numTask");
                    java.sql.Date nextocc = rs.getDate("NextOccurence");
                    int isCompleted = rs.getInt("Completion");

                    LocalDate nextocc2 = nextocc.toLocalDate();

                    if (nextocc2.isEqual(today) && isCompleted == 1){
                        isCompleted = 0;
                        updateTaskCompletionIfDue(numTask, isCompleted);
                    }
                }
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (conn != null) 
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            
        }
        }
    }
    
        private static void updateTaskCompletionIfDue(int numTask, int isCompleted){

        String url = "jdbc:mysql://localhost:3306/tunamayo_db";
        String user = "root";
        String password = "";

        // SQL query template for updating
        String sql = "UPDATE recurtask SET Completion = ? WHERE numTask = ?";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement
            preparedStatement = connection.prepareStatement(sql);
            
                
            // Set the parameter values
            preparedStatement.setInt(1, isCompleted);
            preparedStatement.setInt(2, numTask); 

            // Execute the update
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            
        }
        }
    }
    public static void displayTask(){
        if (taskList.isEmpty()){
            System.out.println("No tasks available.");
            return;
        }
        
        String url = "jdbc:mysql://localhost:3306/tunamayo_db"; // Update with your DB name
        String user = "root"; // Update with your DB username
        String password = ""; // Update with your DB password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT numTask, Title, Description, RecurrenceInterval, NextOccurence, Due, Completion FROM recurtask"; // Adjust table/column names
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int numTask = rs.getInt("numTask");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String recurrence = rs.getString("RecurrenceInterval");
                    java.sql.Date nextocc = rs.getDate("NextOccurence");
                    java.sql.Date duedate = rs.getDate("Due");
                    int isCompleted = rs.getInt("completion");

                    LocalDate nextocc2 = nextocc.toLocalDate();
                    LocalDate duedate2 = duedate.toLocalDate();
                    
                    System.out.println("");
                    System.out.println(numTask + ". " + title);
                    System.out.println("    Description: " + description);
                    System.out.println("    Recurrence: " + recurrence);
                    System.out.println("    Next Occurence: " + nextocc2);
                    System.out.println("    Due Date: " + duedate2);
                    System.out.println("    Completion: " + isCompleted);

                }
                
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading tasks from the database.");
        }
    
    }
    
    
    public static void displayTaskTitle(){
        if (taskList.isEmpty()){
            System.out.println("No tasks available.");
            return;
        }
        
        String url = "jdbc:mysql://localhost:3306/tunamayo_db"; // Update with your DB name
        String user = "root"; // Update with your DB username
        String password = ""; // Update with your DB password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT numTask, Title FROM recurtask"; // Adjust table/column names
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int numTask = rs.getInt("numTask");
                    String title = rs.getString("title");
                    
                    System.out.println("");
                    System.out.println(numTask + ". " + title);

                }
                
            }

            System.out.println("Tasks loaded from database successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading tasks from the database.");
        }
    
    }
    
    public static void loadRecurTasksFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/tunamayo_db"; // Update with your DB name
        String user = "root"; // Update with your DB username
        String password = ""; // Update with your DB password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT numTask, Title, Description, RecurrenceInterval, NextOccurence, Due, Completion FROM recurtask"; // Adjust table/column names
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int numTask = rs.getInt("numTask");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String recurrence = rs.getString("RecurrenceInterval");
                    java.sql.Date nextocc = rs.getDate("NextOccurence");
                    java.sql.Date duedate = rs.getDate("Due");
                    int isCompleted = rs.getInt("completion");

                    LocalDate nextocc2 = nextocc.toLocalDate();
                    LocalDate duedate2 = duedate.toLocalDate();
                    
                    RecurringTask task = new RecurringTask(title, description, recurrence, nextocc2, duedate2, isCompleted); // Assuming Task has this constructor
                    taskList.add(task); 
                    
                    i = Math.max(i, numTask + 1);
                }
                
            }

            System.out.println("Tasks loaded from database successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading tasks from the database.");
        }
    }
}


