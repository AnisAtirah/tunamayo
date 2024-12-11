package todoiman;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class RecurringTask extends Task {
    private String recurrenceInterval; //"weekly", "daily", "monthly"
    private LocalDate nextOccurence;
    private static ArrayList<Task> taskList = new ArrayList<>();
    
    public RecurringTask(String title, String description, String recurrenceInterval) {
        super(title, description);
        this.recurrenceInterval = recurrenceInterval;
        this.nextOccurence = LocalDate.now();
    }
    
    public String getRecurrenceInterval(){
        return recurrenceInterval;
    }
    
    public LocalDate getNextOccurence() {
        return nextOccurence;
    }
    
    public void updateNextOccurence(){
        switch (recurrenceInterval.toLowerCase()){
            case "daily":
                nextOccurence = nextOccurence.plusDays(1);
                break;
            case "weekly":
                nextOccurence = nextOccurence.plusWeeks(1);
                break;
            case "monthly":
                nextOccurence = nextOccurence.plusMonths(1);
                break;
            default:
                throw new IllegalArgumentException ("Invalid recurrence interval.");
        }
    }
    
    public static void addRecurringTask(){
        Scanner scanner = new Scanner(System.in);
        String interval;
        
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

        
        RecurringTask recurringTask = new RecurringTask(title, description, interval);
        taskList.add(recurringTask);
        
        System.out.println("\nRecurring Task '"+ title +"' created successfully!\n");
    }
    
    public static void completeTask(int taskId){
        if (taskId < 0 || taskId >= taskList.size()){
            System.out.println("Invalid task ID.\n");
            return;
        }
        
        Task task = taskList.get(taskId);
        
        if (task instanceof RecurringTask) {
            RecurringTask recurringTask = (RecurringTask) task;
            recurringTask.updateNextOccurence();
            System.out.println("\nRecurring Task '" + recurringTask.getTitle() + "' completed! \nNext occurence: " + recurringTask.getNextOccurence() + "\n");
        } else {
            System.out.println("\nTask '" + task.getTitle() +"' completed!\n");
            taskList.remove(taskId); //Non-recurring tasks are removed
        }
    }
    
    public static void displayTask(){
        if (taskList.isEmpty()){
            System.out.println("No tasks available.");
            return;
        }
        
        for (int i = 0; i<taskList.size(); i++){
            Task task = taskList.get(i);
            System.out.println(i + 1 + ". " + task.getTitle());
            System.out.println("    Description: " + task.getDescription());
            
            if (task instanceof RecurringTask){
                RecurringTask recurringTask = (RecurringTask) task;
                System.out.println("    Recurrence: " + recurringTask.getRecurrenceInterval());
                System.out.println("    Next Occurence: " + recurringTask.getNextOccurence());
            }
        }
    }
    
    public static void displayTaskTitle(){
        if (taskList.isEmpty()){
            System.out.println("No tasks available.");
            return;
        }
        
        for (int i = 0; i<taskList.size(); i++){
            Task task = taskList.get(i);
            System.out.println(i + 1 + ". " + task.getTitle());
        }
    }
    
    public static void main(String[] args) {
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
    
  }
   
}
