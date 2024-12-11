package todoiman;

public class Task {

    public String title;
    public String description;
    
    public Task(String title,String description){
        this.title = title;
        this.description = description;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getDescription(){
        return description;
    }
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
