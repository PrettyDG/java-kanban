package Models;

import Utils.TaskStage;

public interface Task {
    int getId();
    void setStage(TaskStage stage);
    TaskStage getStage();
    void setId(int id);

}
