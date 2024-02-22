class DefaultTask {
    private String taskName;
    private String description;
    public TaskStage stage;
    public int id;


    public DefaultTask(String taskName, String description, TaskStage stage) {
        this.taskName = taskName;
        this.description = description;
        this.stage = stage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStage(TaskStage stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return getClass().toString() +
                " taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", stage=" + stage +
                ", id=" + id +
                '}';
    }
}
