package controllers;

import exceptions.ManagerSaveException;
import utils.*;
import models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    private static int idNumberForTasks = 0;
    private final HistoryManager history = Managers.getDefaultHistory();


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(defaultTasksHash.values());
        allTasks.addAll(epicTasksHash.values());
        allTasks.addAll(subtaskHash.values());
        return allTasks;
    }

    public void clearEverything() {
        defaultTasksHash.clear();
        epicTasksHash.clear();
        subtaskHash.clear();
        idNumberForTasks = 0;
    }

    @Override
    public DefaultTask createDefaultTask(DefaultTask currentTask) throws ManagerSaveException {
        defaultTasksHash.put(idNumberForTasks, currentTask);
        currentTask.setId(idNumberForTasks);
        idNumberForTasks++;
        return currentTask;
    }

    @Override
    public DefaultTask updateDefaultTask(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.put(idNumberForTasks, currentTask);
        return currentTask;
    }

    @Override
    public DefaultTask deleteDefaultTaskByID(int id) throws ManagerSaveException {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.remove(id);
        history.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllDefaultTasks() throws ManagerSaveException {
        defaultTasksHash.keySet().stream()
                .forEach(history::remove);
        defaultTasksHash.clear();
    }

    @Override
    public ArrayList<DefaultTask> getAllDefaultTasks() {
        return new ArrayList<>(defaultTasksHash.values());
    }

    @Override
    public DefaultTask getDefaultTaskByID(int id) {
        return defaultTasksHash.get(id);
    }

    public Task getTaskforUserByID(int id) {
        for (Task task : getAllTasks()){
            if (task.getId() == id) {
                history.add(task);
                return task;
            }
        }
        return null;
    }


    @Override
    public Epic createEpicTask(Epic currentTask) throws ManagerSaveException {
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
            Subtask subtask = subtaskHash.get(subtaskIDs);
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

    @Override
    public Epic deleteEpicTask(int id) throws ManagerSaveException {
        Epic currentTask = epicTasksHash.get(id);
        ArrayList<Integer> subtasksIDs = currentTask.getAllSubtasksID();

        currentTask.getAllSubtasksID().stream()
                .forEach(this::deleteSubtaskByID);

        deleteSubtaskByID(subtasksIDs.get(0));

        history.remove(id);
        epicTasksHash.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {

        epicTasksHash.keySet().stream()
                .forEach(history::remove);

        subtaskHash.keySet().stream()
                .forEach(history::remove);

        epicTasksHash.clear();
        subtaskHash.clear();
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasksHash.values());
    }

    @Override
    public Epic getEpicTaskByID(int id) {
        return epicTasksHash.get(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpicID(int id) {
        Epic currentEpic = epicTasksHash.get(id);
        ArrayList<Subtask> subtasks = new ArrayList<>();

        currentEpic.getAllSubtasksID().stream()
                .map(subtaskHash::get)
                .forEach(subtasks::add);

        return new ArrayList<>(subtasks);
    }


    @Override
    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) throws ManagerSaveException {
        subtaskHash.put(idNumberForTasks, currentSubtask);
        currentSubtask.setId(idNumberForTasks);
        currentSubtask.setEpicID(currentEpic.id);
        currentEpic.setSubtasksID(idNumberForTasks);
        idNumberForTasks++;
        updateEpicTask(currentEpic.id);
        return currentSubtask;
    }

    @Override
    public Subtask updateSubtask(int id, Subtask newSubTask, Epic currentEpic) {
        Subtask beforeSubtask = subtaskHash.get(id);
        subtaskHash.remove(id, beforeSubtask);
        newSubTask.setEpicID(currentEpic.id);
        subtaskHash.put(id, newSubTask);
        updateEpicTask(currentEpic.id);
        return newSubTask;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) throws ManagerSaveException {
        Subtask beforeSubtask = subtaskHash.get(id);
        Epic currentEpic = epicTasksHash.get(beforeSubtask.epicID);
        history.remove(id);
        subtaskHash.remove(id);
        currentEpic.deleteSubtaskID(id);
        updateEpicTask(beforeSubtask.epicID);
        return beforeSubtask;
    }

    @Override
    public void deleteAllSubtasksForEpic(Epic currentEpic) throws ManagerSaveException {
        currentEpic.getAllSubtasksID().stream()
                .forEach(id -> {
                    history.remove(id);
                    subtaskHash.remove(id);
                });

        currentEpic.clearSubtasks();
        updateEpicTask(currentEpic.id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskHash.values());
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        return subtaskHash.get(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }
}