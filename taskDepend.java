import java.util.Scanner;
import java.util.ArrayList;

//this class is to used in method to detect cycle of dependencies
class ListNode {
    int val;
    ListNode next;

    // Constructor to create a new node
    ListNode(int val) {
        this.val = val;
        this.next = null;
    }
}

public class taskDepend {
    public static void main(String[] args) {
        
        //samples of user input of task
        ArrayList<String> taskTitle = new ArrayList<String>();
        taskTitle.add("COOK");
        taskTitle.add("EAT");
        taskTitle.add("WASH DISH");
        taskTitle.add("SLEEP");
        ArrayList<Integer> taskNumber = new ArrayList<Integer>();
        taskNumber.add(1);
        taskNumber.add(2);
        taskNumber.add(3);
        taskNumber.add(4);
        ArrayList<Integer> taskStatus = new ArrayList<Integer>();
        taskStatus.add(0);
        taskStatus.add(0);
        taskStatus.add(0);
        taskStatus.add(0); //0-tak siap , 1-complete
        
        //Variables needed for coding of task dependencies
            Scanner tm = new Scanner(System.in);
            int [] taskDependent = new int[taskNumber.size()+1];
            int [] taskPreceding = new int[taskNumber.size()+1];
            int [] dependent = new int[taskNumber.size()+1];
            int count_Depend = 0;
            boolean depend = false;
            int i=0;

            // Create an array of ListNode to store the nodes
            ListNode[] nodes = new ListNode[taskDependent.length]; // Using 1-based index for convenience

            // Input: Create nodes for each value
            for (int j = 0; j <taskDependent.length; j++) {
                nodes[j] = new ListNode(j);
            }
        
        System.out.print("1. View All Tasks \n2. Task Completion \n3. Edit Task \n4. Exit \nEnter no.: ");
        int menu = tm.nextInt();
        
        while (menu!=4){
            switch(menu){
                case 1:
                    break;
                case 2:
                    taskCompletion(taskStatus, taskNumber, taskTitle, taskDependent, dependent, taskPreceding, depend, count_Depend, tm);
                    break;
                case 3:
                    System.out.print("\nWhat to EDIT? \n1. Set Task Dependency \n2. Home \nEnter no.: ");
                    int edit = tm.nextInt();

                    while (edit!=2){
                        switch (edit){
                            case 1:
                                taskDependency(i, taskDependent, nodes, dependent, taskPreceding, taskTitle, tm);
                                i++;
                                break;
                            case 2:
                                break;
                        }
                    System.out.print("\nWhat to EDIT? \n1. Set Task Dependency \n2. Home \nEnter no.: ");
                    edit = tm.nextInt();
                    }
            }
            System.out.print("\n1. View All Tasks \n2. Task Completion \n3. Edit Task \n4. Exit \nEnter no.: ");
            menu = tm.nextInt();
        } 
    }
    
    public static void taskCompletion(ArrayList<Integer> taskStatus, ArrayList<Integer> taskNumber, ArrayList<String> taskTitle, int [] taskDependent, int [] dependent, int [] taskPreceding, boolean depend, int count_Depend, Scanner tm){
        System.out.println("\n=== Mark Task as Complete ===");
        System.out.print("Enter the task number you want to mark as complete: ");
        int taskDONE = tm.nextInt(); //check task dh siap lum
        count_Depend = 0;
        switch (taskStatus.get(taskDONE-1)){  //taskStatus[1]
            case 0:
                for (int j=0;j<taskDependent.length;j++) { //check whether the task is an dependency task or not 
                    if (taskNumber.get(taskDONE-1)==dependent[j]){  //taskNumber[1]==taskDependent[1]  , 2==2 , count=1
                        depend = true;
                        break;
                    } else {
                        depend = false;
                    }
                    count_Depend++;
                }
                if (depend == false){
                    System.out.println("\nTask \"" + taskTitle.get(taskDONE-1) + "\" marked as complete!");
                    taskStatus.set(taskDONE-1, 1);
                    break;
                } else {
                    for (int j=count_Depend;j<taskDependent.length;j++) {
                        if (taskStatus.get(taskPreceding[j]-1)==0){ //taskStatus[0]==0
                            System.out.println("\nWarning: Task \"" + taskTitle.get(taskDONE-1) + "\" cannot be marked as complete because it depends on \"" + taskTitle.get(taskPreceding[j]-1)+ "\".");
                            System.out.println("Please complete \"" + taskTitle.get(taskPreceding[j]-1) + "\" first."); 
                            break;
                        }else{
                            System.out.println("\nTask \"" + taskTitle.get(taskDONE-1) + "\" marked as complete!");
                            taskStatus.set(taskDONE-1, 1);
                            break;
                        }
                    }
                    break;
                }
            case 1:
                System.out.println("\nTask \"" + taskTitle.get(taskDONE-1) + "\" already marked as complete!");
                break;
            default:
                System.out.println("\nNo data for Task " + taskDONE);
        }
    }
    
    public static void taskDependency(int i, int [] taskDependent, ListNode[] nodes, int [] dependent, int [] taskPreceding, ArrayList<String> taskTitle, Scanner tm){
        System.out.println("\n=== Set Task Dependency ===");
        System.out.print("\nEnter task number that depends on another task: ");
        dependent[i] = tm.nextInt();
        System.out.print("Enter the task number it depends on: ");
        taskPreceding[i] = tm.nextInt();

        // Add dependency
        (nodes[taskPreceding[i]]).next = nodes[dependent[i]];

        // Detect cycle of dependency
        for (int k=0; k < taskDependent.length; k++) {
            taskDepend cycleDetector = new taskDepend();
            if (cycleDetector.hasCycle(nodes[taskPreceding[k]])) {
                System.out.println("\nCycle detected! Dependency not added.");
                (nodes[taskPreceding[i]]).next = null;  // Remove the last added dependency
                dependent[i] = 0;  // Reset the last added dependency in tracking arrays
                taskPreceding[i] = 0;
                break;
            } else {
                System.out.println("\nTask \"" + taskTitle.get(dependent[i]-1) + "\" now depends on \"" +taskTitle.get(taskPreceding[i]-1) + "\".");
                break;
            }
        }
    }
    
    // Method to detect cycle using the slow and fast pointer approach
    public static boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        // Traverse the linked list using fast and slow pointers
        while (fast != null && fast.next != null) {
            fast = fast.next.next; // Move fast pointer two steps
            slow = slow.next; // Move slow pointer one step

            // If fast and slow pointers meet, a cycle is detected
            if (fast == slow) {
                return true;
            }
        }
        // No cycle detected
        return false;
    }
}
