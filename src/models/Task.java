package models;

import utils.TaskStage;

import java.time.LocalDateTime;

public interface Task {
    int getId();

    void setStage(TaskStage stage);

    TaskStage getStage();

    void setId(int id);

    String type = null;

    String getType();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();
}
