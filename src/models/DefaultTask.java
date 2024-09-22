package models;

import utils.TaskStage;

public class DefaultTask implements Task {
    private String taskName;
    private String description;
    private TaskStage stage;
    public int id;


    public DefaultTask(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStage(TaskStage stage) {
        this.stage = stage;
    }

    public TaskStage getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return getClass().getName() +
                " taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", stage=" + stage +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }
}
