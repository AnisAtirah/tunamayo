package to.pkgdo.list;

public class CompletedTask {

    private final String title;
    private final String description;
    private final String dueDate;
    private final String category;
    private final String priority;

    public CompletedTask(String title, String description, String dueDate, String category, String priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
    }

    // Add getters if needed
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return title + " [" + dueDate + "] - [" + category + "] - [" + priority + "]";
    }

    public String toTitle() {
        return title;
    }
}
