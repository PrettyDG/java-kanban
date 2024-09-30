package models;

import utils.TaskStage;

public class Subtask extends DefaultTask implements Task {

    public int epicID;


    public Subtask(String taskName, String description) {
        super(taskName, description);
        super.type = "SUBTASK";
    }

    public Subtask(int id, String taskName, String description, TaskStage stage, int epicID) {
        super(id, taskName, description, stage);
        this.epicID = epicID;
        super.type = "SUBTASK";
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }
}
