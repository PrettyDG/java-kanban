package models;

import controllers.FileBackedTaskManager;
import utils.TaskStage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends DefaultTask implements Task {
    private final ArrayList<Integer> subtasksID = new ArrayList<>();
    private LocalDateTime endTime = LocalDateTime.now();

    public Epic(String taskName, String description) {
        super(taskName, description);
        super.type = "EPIC";
        super.setStage(TaskStage.NEW);
    }

    public Epic(String taskName, String description, LocalDateTime startTime, Duration duration) {
        super(taskName, description);
        super.type = "EPIC";
        super.setStage(TaskStage.NEW);
        super.setStartTime(startTime);
        super.setDuration(duration);
    }

    public Epic(int id, String taskName, String description, TaskStage stage) {
        super(id, taskName, description, stage);
        super.type = "EPIC";
        super.setStage(TaskStage.NEW);
    }

    @Override
    public String toString() {
        if (getEndTime() == null) {
            return getId() + "," + getType() + "," + getTaskName() + "," + getStage() + "," + getDescription();
        }
        return super.toString();
    }


    public LocalDateTime getEndTime(FileBackedTaskManager fileBackedTaskManager) {
        updateTime(fileBackedTaskManager);
        return endTime;
    }

    public void updateTime(FileBackedTaskManager fileBackedTaskManager) {
        LocalDateTime minTime = LocalDateTime.of(2570, 10, 10, 10, 10);
        LocalDateTime maxTime = LocalDateTime.of(1970, 10, 10, 10, 10);
        Duration duration = Duration.ofMinutes(0);
        try {
            minTime = fileBackedTaskManager.getAllSubtasksByEpicID(id).stream()
                    .map(Subtask::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(minTime);

            maxTime = fileBackedTaskManager.getAllSubtasksByEpicID(id).stream()
                    .map(Subtask::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(maxTime);

            duration = fileBackedTaskManager.getAllSubtasksByEpicID(id).stream()
                    .map(Subtask::getDuration)
                    .reduce(Duration.ofMinutes(0), Duration::plus);

            setStartTime(minTime);
            setDuration(duration);
            setEndTime(maxTime);
        } catch (NullPointerException e) {
            System.out.println("Subtask == null");
        }
    }

    public void setEndTime(LocalDateTime localDateTime) {
        endTime = localDateTime;
    }

    public void setSubtasksID(int id) {
        subtasksID.add(id);
    }

    public ArrayList<Integer> getAllSubtasksID() {
        return subtasksID;
    }

    public void deleteSubtaskID(int id) {
        for (int i = 0; i < subtasksID.size(); i++) {
            if (subtasksID.get(i) == id) {
                subtasksID.remove(i);
                return;
            }
        }
    }

    public void clearSubtasks() {
        subtasksID.clear();
    }
}
