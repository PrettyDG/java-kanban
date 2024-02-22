public class Subtask extends DefaultTask {
    Epic epic;
    int epicID;

    public Subtask(String taskName, String description, TaskStage stage, Epic epic) {
        super(taskName, description, stage);
        this.epic = epic;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
