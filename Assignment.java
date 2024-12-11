/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment;

/**
 *
 * @author Allya
 */

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Assignment {
    
  //  public static void main (String[] args) {
        
    //task searching - linear search using contains method
    //Scanner a = new Scanner(System.in);
    /*
    System.out.println("Enter your full sentence");
    String sentence = a.nextLine();
    
    System.out.print("Enter words you want to find : ");
    String keyword = a.nextLine();
    
    if (sentence.toLowerCase().contains(keyword.toLowerCase())) {
        System.out.println("Match found : \n" +sentence); 
        } else {
        System.out.println("No matches found.");}
        }
    */
    
    
    //task sorting - switch case,
    
// Bubble Sort for due date (ascending or descending)
    public static void bubbleSortByDueDate(List<Task> tasks, boolean ascending) {
        int n = tasks.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean condition = ascending
                        ? tasks.get(j).getDueDate().isAfter(tasks.get(j + 1).getDueDate())
                        : tasks.get(j).getDueDate().isBefore(tasks.get(j + 1).getDueDate());
                
                if (condition) {
                    // Swap the tasks
                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        // Sample tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task 1", LocalDate.of(2024, 12, 5), 3));
        tasks.add(new Task("Task 2", LocalDate.of(2024, 12, 10), 1));
        tasks.add(new Task("Task 3", LocalDate.of(2024, 12, 8), 2));

        // Display tasks before sorting
        System.out.println("Tasks before sorting:");
        tasks.forEach(System.out::println);

        // Sorting example (ascending due date)
        bubbleSortByDueDate(tasks, true);  // true for ascending
        System.out.println("\nTasks after sorting by due date (ascending):");
        tasks.forEach(System.out::println);

        // Sorting example (descending due date)
        bubbleSortByDueDate(tasks, false);  // false for descending
        System.out.println("\nTasks after sorting by due date (descending):");
        tasks.forEach(System.out::println);
    }
}
    
    
    
