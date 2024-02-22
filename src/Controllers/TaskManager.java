package Controllers;

import Utils.*;
import Models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    private HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    private ArrayList<Subtask> subtaskList = new ArrayList<>();
    private ArrayList<Epic> epicList = new ArrayList<>();
    private ArrayList<DefaultTask> defaultTaskList = new ArrayList<>();
    private static int idNumberForTasks = 0;


    public DefaultTask createDefaultTask(DefaultTask currentTask) {
        defaultTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        defaultTaskList.add(currentTask);
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
        defaultTaskList.remove(currentTask);
        return currentTask;
    }

    public void deleteAllDefaultTasks() {
        defaultTasksHash.clear();
        defaultTaskList.clear();
    }

    public ArrayList getAllDefaultTasks() {
        return defaultTaskList;
    }

    public DefaultTask getDefaultTaskByID(int id) {
        return defaultTasksHash.get(id);
    }


    public Epic createEpicTask(Epic currentTask) {
        epicTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        epicList.add(currentTask);
        idNumberForTasks++;
        return currentTask;
    }

    private Epic updateEpicTask(int id) {
        Epic currentTask = epicTasksHash.get(id);
        boolean isDone = true;
        boolean isNew = true;

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
        for (int subtaskID : currentTask.getAllSubtasksID()) {
            deleteSubtaskByID(subtaskID);
        }
        epicTasksHash.remove(id);
        epicList.remove(currentTask);
        return currentTask;
    }

    public void deleteAllEpics() {
        epicTasksHash.clear();
        subtaskHash.clear();
        epicList.clear();
    }

    public ArrayList<Epic> getEpicTasks() {
        return epicList;
    }

    public void getEpicTaskByID(int id) {
        System.out.println(epicTasksHash.get(id));
    }

    public void getAllSubtasksByEpicID(int id) {
        Epic currentEpic = epicTasksHash.get(id);

    }


    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
        subtaskHash.put(idNumberForTasks, currentSubtask);
        currentSubtask.setId(idNumberForTasks);
        currentSubtask.setEpicID(currentEpic.id);
        currentEpic.setSubtasksID(idNumberForTasks);
        idNumberForTasks++;
        subtaskList.add(currentSubtask);
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
        updateEpicTask(beforeSubtask.epicID);
        subtaskList.remove(beforeSubtask);
        return beforeSubtask;
    }

    public void deleteAllSubtasksForEpic(Epic currentEpic) {
        for (int i : currentEpic.getAllSubtasksID()) {
            deleteSubtaskByID(i);
        }
        //updateEpicTask(currentEpic.id);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return subtaskList;
    }

    public Subtask getSubtaskByID(int id) {
        return subtaskHash.get(id);
    }
}