package Controllers;

import Utils.*;
import Models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    private HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    private static int idNumberForTasks = 0;


    public DefaultTask createDefaultTask(DefaultTask currentTask) {
        defaultTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        idNumberForTasks++;
        return currentTask;
    }

    public DefaultTask updateDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.put(idNumberForTasks, currentTask);
        return currentTask;
    }

    public DefaultTask deleteDefaultTaskByID(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.remove(id);
        return currentTask;
    }

    public void deleteAllDefaultTasks() {
        defaultTasksHash.clear();
    }

    public ArrayList<DefaultTask> getAllDefaultTasks() {
        return new ArrayList<>(defaultTasksHash.values());
    }

    public DefaultTask getDefaultTaskByID(int id) {
        return defaultTasksHash.get(id);
    }


    public Epic createEpicTask(Epic currentTask) {
        epicTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        idNumberForTasks++;
        return currentTask;
    }

    private Epic updateEpicTask(int id) {
        Epic currentTask = epicTasksHash.get(id);
        boolean isDone = true;
        boolean isNew = true;

        if (currentTask.getAllSubtasksID().isEmpty()) {
            currentTask.setStage(TaskStage.NEW);
            return currentTask;
        }

        for (int subtaskIDs : currentTask.getAllSubtasksID()) {
            Subtask subtask = getSubtaskByID(subtaskIDs);
            if (subtask != null) {
                if (subtask.getStage() == TaskStage.NEW) {
                    isDone = false;
                } else if (subtask.getStage() == TaskStage.DONE) {
                    isNew = false;
                } else if (subtask.getStage() == TaskStage.IN_PROGRESS) {
                    isDone = false;
                    isNew = false;
                    break;
                }
            }
        }
        if (isDone) {
            currentTask.setStage(TaskStage.DONE);
        } else if (isNew) {
            currentTask.setStage(TaskStage.NEW);
        } else {
            currentTask.setStage(TaskStage.IN_PROGRESS);
        }


        return currentTask;
    }

    public Epic deleteEpicTask(int id) {
        Epic currentTask = epicTasksHash.get(id);
        ArrayList<Integer> subtasksIDs = currentTask.getAllSubtasksID();
        for (int i = 0; i < currentTask.getAllSubtasksID().size(); i++) {
            deleteSubtaskByID(subtasksIDs.get(i));
        }
        epicTasksHash.remove(id);
        return currentTask;
    }

    public void deleteAllEpics() {
        epicTasksHash.clear();
        subtaskHash.clear();
    }

    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasksHash.values());
    }

    public Epic getEpicTaskByID(int id) {
        return epicTasksHash.get(id);
    }

    public ArrayList<Subtask> getAllSubtasksByEpicID(int id) {
        Epic currentEpic = epicTasksHash.get(id);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int IDs : currentEpic.getAllSubtasksID()) {
            subtasks.add(getSubtaskByID(IDs));
        }
        return new ArrayList<>(subtasks);
    }


    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
        subtaskHash.put(idNumberForTasks, currentSubtask);
        currentSubtask.setId(idNumberForTasks);
        currentSubtask.setEpicID(currentEpic.id);
        currentEpic.setSubtasksID(idNumberForTasks);
        idNumberForTasks++;
        updateEpicTask(currentEpic.id);
        return currentSubtask;
    }

    public Subtask updateSubtask(int id, Subtask newSubTask, Epic currentEpic) {
        Subtask beforeSubtask = subtaskHash.get(id);
        subtaskHash.remove(id, beforeSubtask);
        newSubTask.setEpicID(currentEpic.id);
        subtaskHash.put(id, newSubTask);
        updateEpicTask(currentEpic.id);
        return newSubTask;
    }

    public Subtask deleteSubtaskByID(int id) {
        Subtask beforeSubtask = subtaskHash.get(id);
        subtaskHash.remove(id);
        Epic currentEpic = getEpicTaskByID(beforeSubtask.epicID);
        currentEpic.deleteSubtaskID(id);
        updateEpicTask(beforeSubtask.epicID);
        return beforeSubtask;
    }

    public void deleteAllSubtasksForEpic(Epic currentEpic) {
        for (int i : currentEpic.getAllSubtasksID()) {
            subtaskHash.remove(i);
        }
        currentEpic.clearSubtasks();
        updateEpicTask(currentEpic.id);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskHash.values());
    }

    public Subtask getSubtaskByID(int id) {
        return subtaskHash.get(id);
    }
}