package models;

import utils.TaskStage;

public class DefaultTask implements Task {
    private String taskName;
    private String description;
    private TaskStage stage;
    public Integer id;
    public String type = "TASK";

    public DefaultTask(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public DefaultTask(int id, String taskName, String description, TaskStage stage) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.stage = stage;
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

//    @Override
//    public String toString() {
//        return getClass().getName() +
//                " taskName='" + taskName + '\'' +
//                ", description='" + description + '\'' +
//                ", stage=" + stage +
//                ", id=" + id +
//                '}';
//    }


    @Override
    public String toString() {
        return id + "," + type + "," + taskName + "," + stage + "," + description;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
