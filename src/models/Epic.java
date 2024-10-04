package models;

import controllers.FileBackedTaskManager;
import utils.TaskStage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends DefaultTask implements Task {
    private ArrayList<Integer> subtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
        super.type = "EPIC";
        super.setStage(TaskStage.NEW);
    }

    public Epic(int id, String taskName, String description, TaskStage stage) {
        super(id, taskName, description, stage);
        super.type = "EPIC";
        super.setStage(TaskStage.NEW);
    }

    @Override
    public String toString() {
        if(getEndTime()==null){
            return getId() + "," + getType() + "," + getTaskName() + "," + getStage() + "," + getDescription();
        }
        return super.toString();
    }


    public LocalDateTime getEndTime(FileBackedTaskManager fileBackedTaskManager) {
        updateTime(fileBackedTaskManager);
        return getEndTime();
    }

    public void updateTime(FileBackedTaskManager fileBackedTaskManager) {
        LocalDateTime minTime = LocalDateTime.of(2570, 10, 10, 10, 10);
        LocalDateTime maxTime = LocalDateTime.of(1970, 10, 10, 10, 10);

        try {
            for (Integer i : subtasksID) {
                if (fileBackedTaskManager.getSubtaskByID(i).getStartTime().isBefore(minTime)) {
                    minTime = fileBackedTaskManager.getSubtaskByID(i).getStartTime();
                }
                if (fileBackedTaskManager.getSubtaskByID(i).getEndTime().isAfter(maxTime)) {
                    maxTime = fileBackedTaskManager.getSubtaskByID(i).getEndTime();
                }
            }
            setStartTime(minTime);
            setDuration(Duration.between(minTime, maxTime));
        } catch (NullPointerException e) {
            System.out.println("Subtask == null");
        }
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
