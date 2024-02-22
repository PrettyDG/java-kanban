import java.util.ArrayList;

public class Epic extends DefaultTask {
    private ArrayList<Integer> SubtasksID = new ArrayList<>();

    public Epic(String taskName, String description, TaskStage stage) {
        super(taskName, description, stage);
    }

    public void setSubtasksID(int id) {
        SubtasksID.add(id);
    }
}
