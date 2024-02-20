public class DefaultTask {
    String taskName;
    String description;
    TaskStage stage;


    public DefaultTask(String taskName, String description, TaskStage stage) {
        this.taskName = taskName;
        this.description = description;
        this.stage = stage;
    }
}
