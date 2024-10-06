package models;

import utils.TaskStage;

import java.time.LocalDateTime;

public interface Task {
    String type = null;

    int getId();

    void setId(int id);

    TaskStage getStage();

    void setStage(TaskStage stage);

    String getType();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();
}
