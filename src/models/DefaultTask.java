package models;

import utils.TaskStage;

import javax.swing.text.DateFormatter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultTask implements Task {
    public Integer id;
    public String type = "TASK";
    private final String taskName;
    private final String description;
    private TaskStage stage;
    private Duration duration;
    private LocalDateTime startTime;

    public DefaultTask(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public DefaultTask(int id, String taskName, String description, TaskStage stage, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.stage = stage;
        this.startTime = startTime;
        this.duration = duration;
    }

    public DefaultTask(int id, String taskName, String description, TaskStage stage) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.stage = stage;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public TaskStage getStage() {
        return stage;
    }

    public void setStage(TaskStage stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        return id + "," + type + "," + taskName + "," + stage + "," + description + ","
                + startTime.format(dateTimeFormatter) + "," + duration.toMinutes();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }
}
