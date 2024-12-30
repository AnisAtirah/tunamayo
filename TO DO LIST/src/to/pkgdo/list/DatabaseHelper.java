package to.pkgdo.list;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tunamayo_db"; // Replace with your database name
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static List<Task> fetchTasksFromDB() {
        List<Task> tasks = new ArrayList<>();

        String query = "SELECT * FROM task WHERE Completion = 0"; // Adjust column names if needed

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Create Task objects from result set
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String dueDate = rs.getString("Due");
                String category = rs.getString("Category");
                String priority = rs.getString("Priority");
                boolean completed = rs.getInt("Completion") == 1; // Assuming 1 = completed

                Task task = new Task(title, description, dueDate, category, priority);
                task.setCompleted(completed);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static List<CompletedTask> fetchCompletedTasksFromDB() {
        List<CompletedTask> completedTasks = new ArrayList<>();
        String query = "SELECT * FROM task WHERE Completion = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CompletedTask task = new CompletedTask(
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Due"),
                        rs.getString("Category"),
                        rs.getString("Priority")
                );
                completedTasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completedTasks;
    }

}
