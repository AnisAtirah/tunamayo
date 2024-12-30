package to.pkgdo.list;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;

public class TODOLIST extends Application {

    private ListView<Task> taskListView = new ListView<>();
    private ListView<CompletedTask> completedTasks = new ListView<>();  // List for completed tasks

    @Override
    public void start(Stage primaryStage) {

        //Layout
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10; -fx-spacing: 10;");

        //input field and button
        Label addlbl = new Label("Add Task");
        Button addButton = new Button("+");

        ChoiceBox<String> sort = new ChoiceBox<String>();

        //Retrieving the observable list
        ObservableList<String> sortlist = sort.getItems();
        //Adding items to the list
        sortlist.addAll("Due Date (Ascending)", "Due Date (Descending)", "Priority (High to Low)", "Priority (Low to High)");

        //setting label
        Label setlabel = new Label("SORT");
        Button sortButton = new Button();
        HBox sortgrp = new HBox(5, setlabel, sort, sortButton);

        sortButton.setOnAction(event -> {
            String selectedSort = sort.getValue();
            if (selectedSort == null) {
                return;
            }

            switch (selectedSort) {
                case "Due Date (Ascending)":
                    taskListView.getItems().sort(Comparator.comparing(Task::getDueDate));
                    break;
                case "Due Date (Descending)":
                    taskListView.getItems().sort(Comparator.comparing(Task::getDueDate).reversed());
                    break;
                case "Priority (High to Low)":
                    taskListView.getItems().sort(Comparator.comparing(Task::getPriority));
                    break;
                case "Priority (Low to High)":
                    taskListView.getItems().sort(Comparator.comparing(Task::getPriority).reversed());
                    break;
                default:
                    break;
            }
        });

        // Context menu for task actions
        ContextMenu contextMenu = new ContextMenu();

        taskListView.setCellFactory(lv -> {
            ListCell<Task> cell = new ListCell<>() {

                @Override
                protected void updateItem(Task task, boolean empty) {
                    super.updateItem(task, empty);

                    if (task == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {

                        //display task
                        Label tasklbl = new Label(task.toString());

                        //display cbx and btn 
                        CheckBox complete = new CheckBox();
                        Button delete = new Button("Delete");
                        complete.setSelected(false);

                        // Action for CheckBox
                        complete.setOnAction(event -> {
                            task.setCompleted(complete.isSelected());
                            updateTaskCompletionInDatabase(task); // Update completion in DB

                            if (task.isCompleted()) {
                                // Create a CompletedTask object
                                CompletedTask completedTask = new CompletedTask(task.toTitle(), task.getDescription(), task.getDueDateString(), task.getCategory(), task.getPriority());

                                // Add the completed task to the completedTasks ListView
                                completedTasks.getItems().add(completedTask);

                                //Optionally, remove the task from the main task list
                                taskListView.getItems().remove(task);
                            }
                        });

                        delete.setOnAction(event -> {
                            taskListView.getItems().remove(task); //remove from list
                            deleteTaskFromDatabase(task);  // Remove from database
                        });

                        // Layout for the ListCell content (task label and checkbox)
                        HBox cellLayout = new HBox(10, tasklbl, complete, delete);
                        cellLayout.setStyle("-fx-alignment: center-left;");
                        setGraphic(cellLayout);

                        // Attach the context menu to the cell
                        setContextMenu(contextMenu);
                    }
                }
            };
            return cell;
        });

        completedTasks = new ListView<>();
        Label title = new Label("Completed Task");
        loadCompletedTasksFromDB(); //load previous data

        completedTasks.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CompletedTask task, boolean empty) {
                super.updateItem(task, empty);
                if (task == null || empty) {
                    setText(null);
                } else {
                    Label taskLabel = new Label(task.toString());
                    Button deleteButton = new Button("Delete");

                    // Delete Button Action
                    deleteButton.setOnAction(event -> {
                        completedTasks.getItems().remove(task);
                        deleteCompletedTaskFromDatabase(task); // Remove from database
                    });

                    HBox cellLayout = new HBox(10, taskLabel, deleteButton);
                    setGraphic(cellLayout);
                }
            }
        });

        // Add task action
        addButton.setOnAction(event -> createTask(taskListView));

        // Add components to layout
        HBox inputArea = new HBox(5, addlbl, addButton);
        root.getChildren().addAll(inputArea, taskListView, title, completedTasks, sortgrp);

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        primaryStage.setTitle("TUNA MAYO LIST");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load tasks from the database when the application starts
        loadTasksFromDB();
    }

    private void createTask(ListView<Task> taskListView) {
        Stage createTask = new Stage();
        createTask.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        createTask.setTitle("Pop-Up Window");

        //input field and button
        TextField taskTitle = new TextField();
        taskTitle.setPromptText("Enter a new Task Title");

        //input field and button
        TextField taskDesc = new TextField();
        taskDesc.setPromptText("Enter Description");

        //input field and button
        TextField taskDue = new TextField();
        taskDue.setPromptText("Enter Due Date (YYYYMMDD)");

        //input field and button
        TextField taskCategory = new TextField();
        taskCategory.setPromptText("Enter Category");

        //input field and button
        TextField taskPriority = new TextField();
        taskPriority.setPromptText("Enter Priority(low/medium/high)");

        // Buttons
        Button saveButton = new Button("Save");

        // Save button action
        saveButton.setOnAction(e -> {
            String title = taskTitle.getText().trim();
            String description = taskDesc.getText().trim();
            String dueDate = taskDue.getText().trim();
            String category = taskCategory.getText().trim();
            String priority = taskPriority.getText().trim();

            if (!title.isEmpty()) {
                taskListView.getItems().add(new Task(title, description, dueDate, category, priority));
                addTaskToDatabase(title, description, dueDate, category, priority);

                createTask.close(); // Close the pop-up
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Task Title cannot be empty!", ButtonType.OK);
                alert.showAndWait();
            }

            createTask.close();
        });

        VBox Layout = new VBox(10, taskTitle, taskDesc, taskDue, taskCategory, taskPriority);
        VBox createTaskLayout = new VBox(10, Layout, saveButton);
        createTaskLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene createTaskScene = new Scene(createTaskLayout, 400, 300);
        createTaskScene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        createTask.setScene(createTaskScene);

        // Show the pop-up window
        createTask.showAndWait(); // Wait until this window is closed
    }

    private void loadTasksFromDB() {
        // Fetch tasks from the database
        List<Task> tasks = DatabaseHelper.fetchTasksFromDB();
        System.out.println("Fetched Tasks: " + tasks);

        // Update the ListView
        taskListView.getItems().clear(); // Clear current tasks
        taskListView.getItems().addAll(tasks); // Add tasks from the database
    }

    private void addTaskToDatabase(String title, String description, String dueDate, String category, String priority) {
        String query = "INSERT INTO task (Title, Description, Due, Category, Priority, Completion) VALUES (?, ?, ?, ?, ?, 0)";

        // Importing and registering drivers
        String url1 = "jdbc:mysql://localhost:3306/tunamayo_db"; //insert your database name
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url1, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, dueDate);
            stmt.setString(4, category);
            stmt.setString(5, priority);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTaskFromDatabase(Task task) {
        String query = "DELETE FROM task WHERE Title = ? ";  // Assuming Title and Due Date are unique identifiers

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/tunamayo_db";  // Modify with your actual database URL
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, task.toTitle());  // Using the task's title (assuming the title is unique)
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTaskCompletionInDatabase(Task task) {
        String query = "UPDATE task SET Completion = ? WHERE Title = ?";

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/tunamayo_db";  // Modify with your actual database URL
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the completion value (1 for completed, 0 for not completed)
            stmt.setInt(1, task.isCompleted() ? 1 : 0);  // Use 1 for true, 0 for false
            stmt.setString(2, task.toTitle());  // Use task title to identify which task to update
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCompletedTasksFromDB() {
        List<CompletedTask> tasks = DatabaseHelper.fetchCompletedTasksFromDB();
        completedTasks.getItems().addAll(tasks);
    }

    private void deleteCompletedTaskFromDatabase(CompletedTask task) {
        String query = "DELETE FROM completed_task WHERE Title = ?";
        String url = "jdbc:mysql://localhost:3306/tunamayo_db"; // Replace with your DB details
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, task.toTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

}
