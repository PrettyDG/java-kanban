package controllers;

import models.DefaultTask;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.TaskStage;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idNumberForTasks = 0;
    private final HashMap<Integer, DefaultTask> defaultTasksHash = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasksHash = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHash = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();
    protected final Comparator<Task> timeComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getStartTime().compareTo(o2.getStartTime().minusSeconds(1));
        }
    };
    protected final Set<Task> prioritizedTasks = new TreeSet<>(timeComparator);

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isTasksCrossing(Task task) {
        if (getPrioritizedTasks().isEmpty()) return false;
        return getPrioritizedTasks().stream().anyMatch(task1 -> isCrossing(task1, task));
    }

    public boolean isCrossing(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime());
    }


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(defaultTasksHash.values());
        allTasks.addAll(epicTasksHash.values());
        allTasks.addAll(subtaskHash.values());
        return allTasks;
    }

    @Override
    public DefaultTask createDefaultTask(DefaultTask currentTask) {
        if (!isTasksCrossing(currentTask)) {
            defaultTasksHash.put(idNumberForTasks, currentTask);
            currentTask.setId(idNumberForTasks);
            idNumberForTasks++;
            prioritizedTasks.add(currentTask);
            return currentTask;
        } else {
            System.out.println("Задача " + currentTask.toString() + " пересекается по времени с другой. Не была создана");
            return null;
        }
    }

    @Override
    public DefaultTask updateDefaultTask(int id, DefaultTask newDefaultTask) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        if (!isTasksCrossing(newDefaultTask)) {
            defaultTasksHash.remove(id, currentTask);
            defaultTasksHash.put(id, newDefaultTask);
            return newDefaultTask;
        } else {
            return null;
        }
    }

    @Override
    public DefaultTask deleteDefaultTaskByID(int id) {
        DefaultTask currentTask = defaultTasksHash.get(id);
        prioritizedTasks.remove(currentTask);
        defaultTasksHash.remove(id);
        history.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllDefaultTasks() {
        defaultTasksHash.keySet()
                .forEach(taskID -> {
                    prioritizedTasks.remove(getDefaultTaskByID(taskID));
                    history.remove(taskID);
                });
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
        for (Task task : getAllTasks()) {
            if (task.getId() == id) {
                history.add(task);
                return task;
            }
        }
        return null;
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

        currentTask.getAllSubtasksID().stream()
                .forEach(subID -> {
                    prioritizedTasks.remove(getSubtaskByID(subID));
                    history.remove(subID);
                });

        deleteSubtaskByID(subtasksIDs.get(0));

        history.remove(id);
        epicTasksHash.remove(id);
        return currentTask;
    }

    @Override
    public void deleteAllEpics() {

        epicTasksHash.keySet().stream()
                .forEach(epicID -> {
                    prioritizedTasks.remove(getEpicTaskByID(epicID));
                    history.remove(epicID);
                });

        subtaskHash.keySet().stream()
                .forEach(subID -> {
                    prioritizedTasks.remove(getSubtaskByID(subID));
                    history.remove(subID);
                });

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
    public Subtask createSubtask(Subtask currentSubtask, Epic currentEpic) {
        if (!isTasksCrossing(currentSubtask)) {
            subtaskHash.put(idNumberForTasks, currentSubtask);
            currentSubtask.setId(idNumberForTasks);
            currentSubtask.setEpicID(currentEpic.id);
            currentEpic.setSubtasksID(currentSubtask.getId());
            idNumberForTasks++;
            prioritizedTasks.add(currentSubtask);
            updateEpicTask(currentEpic.id);
            return currentSubtask;
        } else {
            System.out.println("Задача " + currentSubtask.toString() + " пересекается по времени с другой. Не была создана");
            return null;
        }
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
        prioritizedTasks.remove(beforeSubtask);
        Epic currentEpic = epicTasksHash.get(beforeSubtask.epicID);
        history.remove(id);
        subtaskHash.remove(id);
        currentEpic.deleteSubtaskID(id);
        updateEpicTask(beforeSubtask.epicID);
        return beforeSubtask;
    }

    @Override
    public void deleteAllSubtasksForEpic(Epic currentEpic) {
        currentEpic.getAllSubtasksID().stream()
                .forEach(id -> {
                    prioritizedTasks.remove(getSubtaskByID(id));
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