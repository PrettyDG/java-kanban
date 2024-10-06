package models;

import utils.TaskStage;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends DefaultTask implements Task {

    public int epicID;


    public Subtask(String taskName, String description) {
        super(taskName, description);
        super.type = "SUBTASK";
    }

    public Subtask(int id, String taskName, String description, TaskStage stage, int epicID, LocalDateTime startTime, Duration duration) {
        super(id, taskName, description, stage, startTime, duration);
        this.epicID = epicID;
        super.type = "SUBTASK";
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
