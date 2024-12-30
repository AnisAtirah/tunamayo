package to.pkgdo.list;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// Task class
public class Task {

    private final String title;
    private final String description;
    private final String dueDate;
    private final String category;
    private final String priority;
    private final BooleanProperty completed = new SimpleBooleanProperty(false);

    public Task(String title, String description, String dueDate, String category, String priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        
        
    }

    public int getDueDate() {
        int DueDate = Integer.parseInt(dueDate);
        return DueDate;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDateString() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public int geCompletion() {
        if (isCompleted()) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return title + " (" + dueDate + ") - " + category + " [" + priority + "]";
    }

    public String toTitle() {
        return title;
    }

}
