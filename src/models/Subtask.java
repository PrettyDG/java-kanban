package models;

public class Subtask extends DefaultTask implements Task {

    public int epicID;

    public Subtask(String taskName, String description) {
        super(taskName, description);
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
