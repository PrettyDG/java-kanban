package Controllers;

import Utils.*;
import Models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    private static int idNumberForTasks = 0;
    private final HistoryManager history = Managers.getDefaultHistory();


    @Override
    public DefaultTask createDefaultTask(DefaultTask currentTask) {
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
    public DefaultTask deleteDefaultTaskByID(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        defaultTasksHash.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllDefaultTasks() {
        defaultTasksHash.clear();
    }

    @Override
    public ArrayList<DefaultTask> getAllDefaultTasks() {
        return new ArrayList<>(defaultTasksHash.values());
    }

    @Override
    public DefaultTask getDefaultTaskByID(int id) {
        history.add(defaultTasksHash.get(id));
        return defaultTasksHash.get(id);
    }


    @Override
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
    public Epic deleteEpicTask(int id) {
        Epic currentTask = epicTasksHash.get(id);
        ArrayList<Integer> subtasksIDs = currentTask.getAllSubtasksID();

        for (int i = 0; i < currentTask.getAllSubtasksID().size(); i++) {
            deleteSubtaskByID(subtasksIDs.get(i));
        }
        deleteSubtaskByID(subtasksIDs.get(0));

        epicTasksHash.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllEpics() {
        epicTasksHash.clear();
        subtaskHash.clear();
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasksHash.values());
    }

    @Override
    public Epic getEpicTaskByID(int id) {
        history.add(epicTasksHash.get(id));
        return epicTasksHash.get(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpicID(int id) {
        Epic currentEpic = epicTasksHash.get(id);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int IDs : currentEpic.getAllSubtasksID()) {
            subtasks.add(subtaskHash.get(IDs));
        }
        return new ArrayList<>(subtasks);
    }


    @Override
    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
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
    public Subtask deleteSubtaskByID(int id) {
        Subtask beforeSubtask = subtaskHash.get(id);
        Epic currentEpic = epicTasksHash.get(beforeSubtask.epicID);
        subtaskHash.remove(id);
        currentEpic.deleteSubtaskID(id);
        updateEpicTask(beforeSubtask.epicID);
        return beforeSubtask;
    }

    @Override
    public void deleteAllSubtasksForEpic(Epic currentEpic) {
        for (int i : currentEpic.getAllSubtasksID()) {
            subtaskHash.remove(i);
        }
        currentEpic.clearSubtasks();
        updateEpicTask(currentEpic.id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskHash.values());
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        history.add(subtaskHash.get(id));
        return subtaskHash.get(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }
}