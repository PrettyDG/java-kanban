package Models;

public class Subtask extends DefaultTask {
    public int epicID;

    public Subtask(String taskName, String description) {
        super(taskName, description);
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
