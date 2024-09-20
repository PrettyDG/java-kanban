package models;

import utils.TaskStage;

public interface Task {
    int getId();
    void setStage(TaskStage stage);
    TaskStage getStage();
    void setId(int id);

}
